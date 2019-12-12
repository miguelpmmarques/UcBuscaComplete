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

public class FacebookLoginAction extends ActionSupport implements SessionAware {
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

        OAuthService service = new ServiceBuilder()
                .provider(FacebookApi2.class)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .callback("https://localhost:8443/UcBusca/facebookAuth.action") // Do not change this.
                .scope("public_profile")
                .build();

        // Obtain the Authorization URL
        String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
        System.out.println("ole==="+authorizationUrl);
        //session.put("service", service);
        //System.out.println("session=="+ session.get("service"));
        //String code = getAccessCode(authorizationUrl);

        // Trade the Request Token and Verfier for the Access Token
        /*.out.println("Trading the Request Token for an Access Token...");
        Verifier verifier = new Verifier(code);
        Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
        // Now let's go and ask for a protected resource!*/
        return authorizationUrl;
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
        System.out.println(prop.getProperty("REGISTRYIP"));
        System.out.println(prop.getProperty("REGISTRYPORT"));
        System.out.println(prop.getProperty("LOOKUP"));


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
        /*
        User thisUser = new User(this.username,this.password,client);
        protocol =  retry(thisUser,0);
        if(protocol.get("status").equals("logged on")){
            System.out.println("LOGIN UTILIZADOR");
            session.put("user",thisUser);
            session.put("username", username);
            session.put("loggedin", true);
            session.put("admin", false);

        } else if(protocol.get("status").equals("logged admin")){
            System.out.println("LOGIN UTILIZADOR");
            session.put("user",thisUser);
            session.put("username", username);
            session.put("loggedin", true);
            session.put("admin", true);
        }
        else {
            System.out.println("INVALID LOGIN");
            session.put("loggedin", false);
            addActionError("INVALID LOGIN");
            return ERROR;
        }*/



    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }





    private HashMap<String,String> retry(Object parameter,int replyCounter) throws RemoteException, InterruptedException, NotBoundException {
        HashMap<String,String> myDic;
        try {
            this.ucBusca=(ServerLibrary) LocateRegistry.getRegistry(prop.getProperty("REGISTRYIP"), Integer.parseInt(prop.getProperty("REGISTRYPORT"))).lookup(prop.getProperty("LOOKUP"));
            myDic = this.ucBusca.userLogin((User)parameter);
            System.out.println(myDic);
            return myDic;

        }catch (Exception e) {
            try {
                Thread.sleep(2000);
            } catch(InterruptedException e2) {
                System.out.println("Interrupted");
            }
            if (replyCounter>16){
                System.out.println("Please, try no reconnect to the UcBusca");
                System.exit(0);
            }
            System.out.println(e);
            System.out.println("Retransmiting... "+replyCounter);
            retry(parameter,++replyCounter);
        }
        return new HashMap<String,String>();
    }

    public String logout() {
        // remove userName from the session
        if (this.session.containsKey("username")) {
            this.session.remove("username");
        }
        if (this.session.containsKey("loggedin")) {
            this.session.remove("loggedin");
        }
        if (this.session.containsKey("admin")) {
            this.session.remove("admin");
        }

        return SUCCESS;
    }

    public String getAccessCode(String url){
        try {
            System.out.println("URLLLLLLLLLLLLLL-----------------------------------------------------------------_>"+url);


            Connection conn;
            Document doc;
            try {
                conn = (Connection) Jsoup.connect(url);
                conn.timeout(5000);
                doc = conn.get();
            } catch (IllegalArgumentException e) {
                System.out.println("URL not found by Jsoup");
                return "";
            }
            System.out.println("-------------------------------------------------------------------");
            System.out.println(doc);
            System.out.println("--------------------------------------------------------------------");

            return "";

        } catch (org.jsoup.HttpStatusException | UnknownHostException | IllegalArgumentException e) {
            System.out.println(
                    "------------------------ Did not search for website ----------------------");
            return "";
        } catch (IOException e) {
            System.out.println(
                    "------------------------ Did not search for website -------------------------");
            // e.printStackTrace();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
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
