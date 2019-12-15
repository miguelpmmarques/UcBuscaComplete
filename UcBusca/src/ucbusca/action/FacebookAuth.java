package ucbusca.action;

import RMISERVER.SearchRMIClient;
import RMISERVER.ServerLibrary;
import RMISERVER.User;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.struts2.interceptor.SessionAware;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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

import com.google.gson.Gson;
import com.google.common.reflect.TypeToken;

import java.lang.reflect.Type;


public class FacebookAuth extends ActionSupport implements SessionAware {
    private Logger logger = Logger.getLogger(String.valueOf(FacebookAuth.class));
    private static final long serialVersionUID = 5590830L;
    private Map<String, Object> session;
    private String username;
    private String password;
    private ServerLibrary ucBusca;
    private Properties prop = new Properties();
    String code;

    private static final String NETWORK_NAME = "Facebook";
    private static final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/me";
    private static final Token EMPTY_TOKEN = null;

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public Map<String, Object> getSession() {
        return this.session;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String execute() throws Exception{
        //necessary variables for facebook api login and rmi client setup
        String apiKey = "3327462673961598";
        String apiSecret = "994cd6e13ace400e63e368cd349a98fd";
        String propFileName = "RMISERVER/config.properties";
        HashMap<String, String> protocol = null;
        //reading from config file
        InputStream inputStream = LoginAction.class.getClassLoader().getResourceAsStream(propFileName);
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
        SearchRMIClient client = null;
        try {
            client = new SearchRMIClient(ucBusca, prop);
        } catch (java.rmi.RemoteException e) {
            e.printStackTrace();
        }

        //getting the facebook login code from the get request's query params
        this.code = getCode();
        System.out.println("CODE=============>>>>>>>>>>>>>>>>>>>>>>>>" + this.code);

        //initializing new OAuthService for validating the code
        // might have to change the callback url later
        OAuthService service = new ServiceBuilder()
                .provider(FacebookApi2.class)
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .callback("https://raul.deus:8443/UcBusca/facebookAuth.action") // Do not change this.                .scope("public_profile")
                .build();
        ;

        // Getting the access token and validating it with facebook
        Verifier verifier = new Verifier(this.code);
        //Token accessToken = null;
        System.out.println("here===" + service);

        Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);

        System.out.println("Access token ==" + accessToken);
        OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL, service);
        service.signRequest(accessToken, request);
        Response response = request.send();

        // parsing facebook's response - first converting the strings to utf, and then parsing the json to a hashMap
        String text = StringEscapeUtils.unescapeJava(response.getBody());
        Type mapType = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> user_map = new Gson().fromJson(text, mapType);
        System.out.println("map==="+ user_map);
        this.username = user_map.get("name");
        this.password = user_map.get("id");
        System.out.printf("username=="+ this.username);
        System.out.println("password=="+ this.password);

        //creating the user instance, to attempt the login into our application
        User thisUser = new User(this.username, this.password, true, client);

        if (!login(thisUser, protocol)) {
            if (!register(thisUser, protocol)) {
                return ERROR;
            }
        }
        return SUCCESS;
    }

    private Boolean register(User thisUser,HashMap<String, String> protocol) {

        try{
            protocol =  retryRegister(thisUser,0);
        }catch(RemoteException | InterruptedException | NotBoundException e){
            e.printStackTrace();
        }

        if(protocol.get("status").equals("Success")){
            session.put("user",thisUser);
            session.put("username", username);
            session.put("loggedin", true);
            session.put("admin", false);
            session.put("facebookAssociation", true);
            setSession(session);
            return true;

        } else if(protocol.get("status").equals("Admin")){
            System.out.println("Regist admin");
            session.put("user",thisUser);
            session.put("username", username);
            session.put("loggedin", true);
            session.put("admin", true);
            session.put("facebookAssociation", true);
            setSession(session);
            return true;
        }
        else {
            System.out.println("INVALID REGISTER");
            session.put("loggedin", false);
            return false;
        }
    }

    private boolean login(User thisUser, HashMap<String, String> protocol) {
        try {
            protocol = retryLogin(thisUser, 0);

        } catch (RemoteException | InterruptedException | NotBoundException e) {
            e.printStackTrace();
            return false;
        }
        //successful login for a common user
        if (protocol.get("status").equals("logged on")) {
            System.out.println("LOGIN UTILIZADOR");
            session.put("user", thisUser);
            session.put("username", username);
            session.put("loggedin", true);
            session.put("admin", false);
            session.put("facebookAssociation", true);
            setSession(session);
            //successful login for admin
        } else if (protocol.get("status").equals("logged admin")) {
            System.out.println("LOGIN UTILIZADOR");
            session.put("user", thisUser);
            session.put("username", username);
            session.put("loggedin", true);
            session.put("admin", true);
            session.put("facebookAssociation", true);
            setSession(session);
        }
        //unsuccessful login, registering the user
        else {
            System.out.println("Atempting Register");
            return false;
        }
        return true;
    }


    private HashMap<String, String> retryLogin(Object parameter, int replyCounter) throws RemoteException, InterruptedException, NotBoundException {
        HashMap<String, String> myDic;
        try {
            this.ucBusca = (ServerLibrary) LocateRegistry.getRegistry(prop.getProperty("REGISTRYIP"), Integer.parseInt(prop.getProperty("REGISTRYPORT"))).lookup(prop.getProperty("LOOKUP"));
            myDic = this.ucBusca.userLogin((User) parameter, true);
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
            retryLogin(parameter, ++replyCounter);
        }
        return new HashMap<String, String>();
    }
    private HashMap<String,String> retryRegister(Object parameter,int replyCounter) throws RemoteException, InterruptedException, NotBoundException {
        HashMap<String,String> myDic;
        try {
            this.ucBusca=(ServerLibrary) LocateRegistry.getRegistry(prop.getProperty("REGISTRYIP"), Integer.parseInt(prop.getProperty("REGISTRYPORT"))).lookup(prop.getProperty("LOOKUP"));
            myDic = this.ucBusca.userRegistration((User)parameter,true);
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
            retryRegister(parameter,++replyCounter);
        }
        return new HashMap<String,String>();
    }
}



