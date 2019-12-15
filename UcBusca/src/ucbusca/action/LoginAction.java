
package ucbusca.action;

import RMISERVER.SearchRMIClient;
import RMISERVER.*;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.io.InputStream;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Handle the log in and log out of our app
 */
public class LoginAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 5590830L;
	private Map<String, Object> session;
	private String username;
	private String password;
	private ServerLibrary ucBusca;
	private Properties prop = new Properties();


	/**
	 * POST METHOD
	 * @return Return ERROR in case the password dont match the username and blank fields
	 * */
	@Override
	public String execute() {
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
		SearchRMIClient client = null;
		try {
			client = new SearchRMIClient(ucBusca, prop);
		}catch (Exception e){
			System.out.println("Error in LoginAction.java in line 57");
		}

		System.out.println(this.username);
		System.out.println(this.password);
		if (this.username.equals("")){
			addActionError("Please fill the username field");
			return ERROR;
		}
		if (this.username.equals("") || this.password.equals("")){
			addActionError("Please fill the password field");
			return ERROR;
		}
		User thisUser = new User(this.username,this.password,client);
		protocol =  retry(thisUser,0);
		if(protocol.get("status").equals("logged on")){
			System.out.println("LOGIN UTILIZADOR");
			session.put("user",thisUser);
			session.put("username", username);
			session.put("loggedin", true);
			session.put("admin", false);
			if(protocol.get("Fb").equals("true")){
				session.put("facebookAssociation", true);
			}else{
				session.put("facebookAssociation", false);
			}

		} else if(protocol.get("status").equals("logged admin")){
			addActionMessage("LOG ON AS AN ADMINISTRATOR");
			session.put("user",thisUser);
			session.put("username", username);
			session.put("loggedin", true);
			session.put("admin", true);
			if(protocol.get("Fb").equals("true")){
				session.put("facebookAssociation", true);
			}else{
				session.put("facebookAssociation", false);
			}
		}
		else {
			System.out.println("INVALID LOGIN");
			session.put("loggedin", false);
			addActionError("INVALID LOGIN");
			return ERROR;
		}


		return SUCCESS;
	}
	/**
	 * Communication with the RMI SERVER
	 * @param parameter USER TO LOGIN
	 * @param replyCounter Retrys
	 * @return
	 */
	private HashMap<String,String> retry(Object parameter,int replyCounter) {
		HashMap<String,String> myDic;
		try {
			this.ucBusca=(ServerLibrary) LocateRegistry.getRegistry(prop.getProperty("REGISTRYIP"), Integer.parseInt(prop.getProperty("REGISTRYPORT"))).lookup(prop.getProperty("LOOKUP"));
			myDic = this.ucBusca.userLogin((User)parameter, false);
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

	/**
	 * Logout the current user
	 * @return Always Sucess
	 */
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
