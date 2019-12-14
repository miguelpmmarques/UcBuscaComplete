package ucbusca.model;

import RMISERVER.ServerLibrary;

import java.io.InputStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * Handles the Bean the shows the system Statistcs in the System info view
 */
public class SystemInfoModel {
	private ServerLibrary ucBusca;
	private Properties prop = new Properties();
	private Map<String, Object> session;
	HashMap<String,String> protocol;

	public SystemInfoModel(Map<String, Object> session) {
		this.session = session;
		String propFileName = "RMISERVER/config.properties";
		InputStream inputStream = SystemInfoModel.class.getClassLoader().getResourceAsStream(propFileName);
		try {
			this.prop.load(inputStream);
		} catch (Exception e){
			System.out.println(e);
			System.out.println("Cannot read properties File");
			return;
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

	}

	/**
	 * @return List of active multicasts
	 */
	public ArrayList<String> getActivemulticasts() {
		this.protocol = retry(0);
		ArrayList<String> anwser = new ArrayList<>();
		int arraySize = Integer.parseInt((String)this.protocol.get("activeMulticast_count"));

		System.out.println(arraySize);
		if (arraySize == 0)
			anwser.add("Empty");
		for(int i = 1 ;i<arraySize+1;i++){
			System.out.println((String)this.protocol.get("activeMulticast_"+i));
			anwser.add((String)this.protocol.get("activeMulticast_"+i));
		}
		return anwser;
	}

	/**
	 * @return List of 10 most referenced web pages
	 */
	public ArrayList<String> getToppages(){
		this.protocol = retry(0);
		ArrayList<String> anwser = new ArrayList<>();
		int arraySize = Integer.parseInt((String)this.protocol.get("important_pages_count"));

		System.out.println(arraySize);
		if (arraySize == 0)
			anwser.add("Empty");
		for(int i = 1 ;i<arraySize+1;i++){
			System.out.println((String)this.protocol.get("important_pages_"+i));
			anwser.add((String)this.protocol.get("important_pages_"+i));
		}
		return anwser;
	}

	/**
	 * @return List of most popular searches
	 */
	public ArrayList<String> getTopsearches(){
		this.protocol = retry(0);
		ArrayList<String> anwser = new ArrayList<>();

		int arraySize = Integer.parseInt((String)this.protocol.get("top_search_count"));

		System.out.println(arraySize);
		if (arraySize == 0)
			anwser.add("Empty");
		for(int i = 1 ;i<arraySize+1;i++){
			System.out.println((String)this.protocol.get("itop_search_"+i));
			anwser.add((String)this.protocol.get("top_search_"+i));
		}
		return anwser;
	}

	/**
	 * Communication with the RMI SERVER
	 * @param replyCounter Retrys
	 * @return
	 */
	private HashMap<String,String> retry(int replyCounter){
		HashMap<String,String> myDic;
		try {
			this.ucBusca=(ServerLibrary) LocateRegistry.getRegistry(prop.getProperty("REGISTRYIP"), Integer.parseInt(prop.getProperty("REGISTRYPORT"))).lookup(prop.getProperty("LOOKUP"));
			myDic = this.ucBusca.sendSystemInfo();
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
			retry(++replyCounter);
		}
		return new HashMap<String,String>();
	}
}
