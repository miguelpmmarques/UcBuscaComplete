
package ucbusca.action;

import RMISERVER.SearchRMIClient;
import RMISERVER.ServerLibrary;
import RMISERVER.User;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.io.InputStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
/**
 * Action that includes the GET and POST of the ADD URL VIEW
 */
public class AddUrlAction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 5590830L;
    private Map<String, Object> session;
    private String url;
    private ServerLibrary ucBusca;
    private Properties prop = new Properties();

    /**
     * POST METHOD
     * @return Success or notAdmin in case the user dont have permission to acess the page
     */
    @Override
    public String execute(){
        boolean flag = (boolean) session.get("admin");
        if (!flag){
            return "notAdmin";
        }
        String propFileName = "RMISERVER/config.properties";
        InputStream inputStream = AddUrlAction.class.getClassLoader().getResourceAsStream(propFileName);
        try {
            this.prop.load(inputStream);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Cannot read properties File");
            return ERROR;
        }
        try {
            this.ucBusca = (ServerLibrary) LocateRegistry.getRegistry(prop.getProperty("REGISTRYIP"), Integer.parseInt(prop.getProperty("REGISTRYPORT"))).lookup(prop.getProperty("LOOKUP"));
            System.out.println("Connected to UcBusca");

        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Connecting...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException es) {
                System.out.println("Sleep interrupted");
            }
        }
        HashMap<String, String> protocol;
        System.out.println("-----" + this.url + "-----");

        if (this.url.trim().length() == 0 || !this.url.contains(".") || (!this.url.startsWith("http://") && !this.url.startsWith("https://"))) {
			addActionError("Please write a valid URL");
            return ERROR;
        }

        retry(this.url, 0);
        addActionMessage("URL add with success");
        return SUCCESS;
    }
    /**
     * Communication with the RMI SERVER
     * @param parameter URL ADDED
     * @param replyCounter Retrys
     * @return
     */
    private HashMap<String, String> retry(Object parameter, int replyCounter) {
        HashMap<String, String> myDic;
        try {
            this.ucBusca = (ServerLibrary) LocateRegistry.getRegistry(prop.getProperty("REGISTRYIP"), Integer.parseInt(prop.getProperty("REGISTRYPORT"))).lookup(prop.getProperty("LOOKUP"));
            myDic = this.ucBusca.addURLbyADMIN((String) parameter);
            System.out.println(myDic);
            return myDic;

        } catch (Exception e) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e2) {
                System.out.println("Interrupted");
            }
            if (replyCounter > 16) {
                System.out.println("Please, try no reconnect to the UcBusca");
                System.exit(0);
            }
            System.out.println(e);
            System.out.println("Retransmiting... " + replyCounter);
            retry(parameter, ++replyCounter);
        }
        return new HashMap<String, String>();
    }


    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public Map<String, Object> getSession() {
        return this.session;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
