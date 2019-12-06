package ucbusca.model;

import RMISERVER.ServerLibrary;
import RMISERVER.User;

import java.io.InputStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class ManageUsersModel {
	private ServerLibrary ucBusca;
	private Properties prop = new Properties();
	private Map<String, Object> session;

	public ManageUsersModel(Map<String, Object> session) {
		this.session = session;
		String propFileName = "RMISERVER/config.properties";
		InputStream inputStream = ManageUsersModel.class.getClassLoader().getResourceAsStream(propFileName);
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

	
	public ArrayList<String> getManageusers() throws InterruptedException, RemoteException, NotBoundException {
		HashMap<String,String> protocol;
		ArrayList<String> anwser = new ArrayList<>();
		protocol = retry(0);
		int arraySize = Integer.parseInt((String)protocol.get("user_count"));

		System.out.println(arraySize);
		if (arraySize == 0)
			anwser.add("Empty");
		for(int i = arraySize ;i>0;i--){
			System.out.println((String)protocol.get("user_"+i));
			anwser.add((String)protocol.get("user_"+i));
		}
		return anwser;
	}



	private HashMap<String,String> retry(int replyCounter) throws RemoteException, InterruptedException, NotBoundException {
		HashMap<String,String> myDic;
		try {
			this.ucBusca=(ServerLibrary) LocateRegistry.getRegistry(prop.getProperty("REGISTRYIP"), Integer.parseInt(prop.getProperty("REGISTRYPORT"))).lookup(prop.getProperty("LOOKUP"));
			myDic = this.ucBusca.getAllUsers();
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
