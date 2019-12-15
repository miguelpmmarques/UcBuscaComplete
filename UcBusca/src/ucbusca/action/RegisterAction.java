
package ucbusca.action;

import RMISERVER.SearchRMIClient;
import RMISERVER.*;
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
 * Handle the register of our app
 */
public class RegisterAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 5590830L;
	private Map<String, Object> session;
	private String username;
	private String password;
	private String password1;
	private ServerLibrary ucBusca;
	private Properties prop = new Properties();

	/**
	 * POST METHOD
	 * @return Return ERROR in case the passwords dont match, blank fields and username already taken
	 */
	@Override
	public String execute(){
		String propFileName = "RMISERVER/config.properties";
		InputStream inputStream = RegisterAction.class.getClassLoader().getResourceAsStream(propFileName);
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
		if (this.password1.equals("") || this.password.equals("")){
			addActionError("Please fill the passwords field");
			return ERROR;
		}
		if (!this.password1.equals( this.password)){
			addActionError("Passwords dont match");
			return ERROR;
		}
		User thisUser = new User(this.username,this.password,client);
		protocol =  retry(thisUser,0);


		if(protocol.get("status").equals("Success")){
			session.put("user",thisUser);
			session.put("username", username);
			session.put("loggedin", true);
			session.put("admin", false);
			session.put("facebookAssociation", false);

		} else if(protocol.get("status").equals("Admin")){
			System.out.println("Regist admin");
			session.put("user",thisUser);
			session.put("username", username);
			session.put("loggedin", true);
			session.put("admin", true);
			session.put("facebookAssociation", false);
			addActionMessage("LOG ON AS AN ADMINISTRATOR");
		}
		else {
			addActionError("Username already in use");
			session.put("loggedin", false);
			return ERROR;
		}


		return SUCCESS;
	}
	/**
	 * Communication with the RMI SERVER
	 * @param parameter USER TO REGIST
	 * @param replyCounter Retrys
	 * @return
	 */
	private HashMap<String,String> retry(Object parameter,int replyCounter) {
		HashMap<String,String> myDic;
		try {
			this.ucBusca=(ServerLibrary) LocateRegistry.getRegistry(prop.getProperty("REGISTRYIP"), Integer.parseInt(prop.getProperty("REGISTRYPORT"))).lookup(prop.getProperty("LOOKUP"));
			myDic = this.ucBusca.userRegistration((User)parameter,false);
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

	public String getPassword1() {
		return password1;
	}

	public void setPassword1(String password1) {
		this.password1 = password1;
	}
}
