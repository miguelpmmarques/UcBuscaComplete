
package ucbusca.action;

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
public class AddAdminAction extends ActionSupport implements SessionAware {
private static final long serialVersionUID = 5590830L;
private Map<String, Object> session;
private String username;
private ServerLibrary ucBusca;
private Properties prop = new Properties();

/**
 * POST METHOD
 * @return Success or notAdmin in case the user dont have permission to acess the page
 */
@Override
public String execute(){
								if (!session.containsKey("admin") || !(boolean) session.get("admin")) {
																return "notAdmin";
								}
								String propFileName = "RMISERVER/config.properties";
								InputStream inputStream = AddAdminAction.class.getClassLoader().getResourceAsStream(propFileName);
								try {
																this.prop.load(inputStream);
								} catch (Exception e) {
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
																}catch (InterruptedException es) {
																								System.out.println("Sleep interrupted");
																}
								}

								retry(this.username,0);

								return SUCCESS;
}

/**
 * Communication with the RMI SERVER
 * @param parameter USERNAME FROM NEW ADMIN
 * @param replyCounter Retrys
 * @return
 */
private HashMap<String,String> retry(Object parameter,int replyCounter) {
								HashMap<String,String> myDic;
								try {
																this.ucBusca=(ServerLibrary) LocateRegistry.getRegistry(prop.getProperty("REGISTRYIP"), Integer.parseInt(prop.getProperty("REGISTRYPORT"))).lookup(prop.getProperty("LOOKUP"));
																myDic = this.ucBusca.changeUserPrivileges((String) parameter);
																System.out.println(myDic);
																return myDic;

								}catch (Exception e) {
																try {
																								Thread.sleep(2000);
																} catch(InterruptedException e2) {
																								System.out.println("Interrupted");
																}
																if (replyCounter>16) {
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


}
