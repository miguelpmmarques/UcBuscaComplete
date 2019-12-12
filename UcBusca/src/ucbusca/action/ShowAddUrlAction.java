
package ucbusca.action;

import RMISERVER.SearchRMIClient;
import RMISERVER.ServerLibrary;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.io.InputStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ShowAddUrlAction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 5590830L;
    private Map<String, Object> session;
    private String url;
    private ServerLibrary ucBusca;
    private Properties prop = new Properties();


    @Override
    public String execute() throws Exception {
        if (!session.containsKey("admin") || !(boolean) session.get("admin")){
            return "notAdmin";
        }
        return SUCCESS;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
    public Map<String, Object> getSession() {
        return this.session;
    }

}
