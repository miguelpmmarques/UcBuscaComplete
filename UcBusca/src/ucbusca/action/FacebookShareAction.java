package ucbusca.action;

import RMISERVER.SearchRMIClient;
import RMISERVER.ServerLibrary;
import RMISERVER.User;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import ucbusca.model.HistoryModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;


import com.github.scribejava.apis.FacebookApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.model.Verifier;
import com.github.scribejava.core.oauth.OAuthService;

import uc.sd.apis.FacebookApi2;

public class FacebookShareAction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 5590830L;
    private Map<String, Object> session;
    private String username;
    private String password;
    private ServerLibrary ucBusca;
    private Properties prop = new Properties();

    private static final String NETWORK_NAME = "Facebook";
    private static final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/me";
    private static final Token EMPTY_TOKEN = null;
    private String jsonString;
    private String facebookSession;

    public String getFacebookSession(){
        String apiKey = "3327462673961598";
        String apiSecret = "994cd6e13ace400e63e368cd349a98fd";
        return "https://www.facebook.com/dialog/feed?app_id="+apiKey+"&display=popup&href="+"https%3A%2F%2Flocalhost%3A8443%2F&link=https%3A%2F%2Ffacebook.com/ustudyapp/?eid=ARAXsuh29D9MdjnRn67L78hXg2BphnL7PMIHVq8ijfo-AjokFQe1DDFYSnVxX7EhUNXJhPHQZDF89_pO%2F&redirect_uri=https://localhost:8443/UCBUSCA/closePopUp.action";

    }

    @Override
    public String execute() throws Exception {
        String propFileName = "RMISERVER/config.properties";
        InputStream inputStream = LoginAction.class.getClassLoader().getResourceAsStream(propFileName);
        try {
            this.prop.load(inputStream);
        } catch (Exception e){
            System.out.println(e);
            System.out.println("Cannot read properties File");
            return ERROR;
        }

        try {
            this.ucBusca = (ServerLibrary) LocateRegistry.getRegistry(prop.getProperty("REGISTRYIP"), Integer.parseInt(prop.getProperty("REGISTRYPORT") )).lookup(prop.getProperty("LOOKUP") );
            System.out.println("Connected to UcBusca");

        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Connecting...");
            try {
                Thread.sleep(2000);
            }catch (InterruptedException es){
                System.out.println("Sleep interrupted");
            }
        }
        HashMap<String,String> protocol;

        SearchRMIClient client = new SearchRMIClient(ucBusca,prop);


        facebookSession = getFacebookSession();
        System.out.println("FB_SESSION==="+ facebookSession);
        return SUCCESS;

    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }




    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
    public Map<String, Object> getSession() {
        return this.session;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
