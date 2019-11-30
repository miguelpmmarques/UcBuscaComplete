
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

public class LoginAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 5590830L;
	private Map<String, Object> session;
	private String username;
	private String password;
	private ServerLibrary ucBusca;
	private Properties prop = new Properties();


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
		System.out.println(this.username);
		System.out.println(this.password);
		protocol =  retry(new User(this.username,this.password,client),0);
		if(protocol.get("status").equals("logged on")){
			System.out.println("LOGIN UTILIZADOR");
			session.put("username", username);
			session.put("loggedin", true);
			session.put("admin", false);

		} else if(protocol.get("status").equals("logged admin")){
			System.out.println("LOGIN UTILIZADOR");
			session.put("username", username);
			session.put("loggedin", true);
			session.put("admin", true);
		}
		else {
			System.out.println("INVALID LOGIN");
			session.put("loggedin", false);
		}


		return SUCCESS;
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
