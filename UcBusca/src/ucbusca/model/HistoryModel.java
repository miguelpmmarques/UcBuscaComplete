package ucbusca.model;

import RMISERVER.ServerLibrary;
import RMISERVER.User;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class HistoryModel {
	private ServerLibrary ucBusca;
	private Properties prop = new Properties();
	private Map<String, Object> session;

	public HistoryModel(Map<String, Object> session) {
		this.session = session;
		String propFileName = "RMISERVER/config.properties";
		InputStream inputStream = HistoryModel.class.getClassLoader().getResourceAsStream(propFileName);
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

				//this.ucBusca = (ServerLibrary) Naming.lookup(prop.getProperty("LOOKUP"));

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

	
	public ArrayList<String> getHistory() throws InterruptedException, RemoteException, NotBoundException {
		HashMap<String,String> protocol;
		ArrayList<String> anwser = new ArrayList<>();


		User thisUser = (User)session.get("user");
		protocol =  retry(thisUser,0);
		int arraySize = Integer.parseInt((String)protocol.get("word_count"));
		System.out.println(arraySize);
		if (arraySize == 0)
			anwser.add("Empty");
		for(int i =arraySize ;i>0;i--){
			System.out.println((String)protocol.get("word_"+i));
			anwser.add((String)protocol.get("word_"+i));
		}
		return anwser;
	}



	private HashMap<String,String> retry(Object parameter,int replyCounter) throws RemoteException, InterruptedException, NotBoundException {
		HashMap<String,String> myDic;
		try {
			this.ucBusca=(ServerLibrary) LocateRegistry.getRegistry(prop.getProperty("REGISTRYIP"), Integer.parseInt(prop.getProperty("REGISTRYPORT"))).lookup(prop.getProperty("LOOKUP"));
			myDic = this.ucBusca.getHistory((User)parameter);
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
}
