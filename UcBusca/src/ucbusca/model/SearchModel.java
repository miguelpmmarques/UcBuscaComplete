package ucbusca.model;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import RMISERVER.*;


public class SearchModel {
	private String seachWords;
	private ServerLibrary ucBusca;
	private Properties prop = new Properties();
	private Map<String, Object> session;

	public SearchModel(Map<String, Object> session) {
		this.session = session;
		String propFileName = "RMISERVER/config.properties";
		InputStream inputStream = SearchModel.class.getClassLoader().getResourceAsStream(propFileName);
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

	public String getSeachWords() {
		return this.seachWords;
	}

	public void setSeachWords(String seachWords) {
		this.seachWords = seachWords;
	}

	public ArrayList<HashMap<String,String>> getResearch() throws InterruptedException, RemoteException, NotBoundException {
		HashMap<String,String> protocol;
		ArrayList<HashMap<String,String>> anwser = new ArrayList<>();
		System.out.println(this.seachWords);
		String thisSearchWords;

		if (this.seachWords.startsWith("http://") ||  this.seachWords.startsWith("https://")){
			protocol =  findURL(this.seachWords,0);

		}else{

			if (this.session.containsKey("loggedin") && this.session.get("loggedin").equals(true) ){
				thisSearchWords = this.session.get("username")+" "+this.seachWords;
			}else {
				thisSearchWords = "Anonymous "+this.seachWords;
			}
			String[] searchWordsSplited = thisSearchWords.split("\\s+");
			protocol =  findWord(searchWordsSplited,0);

		}
		for (String value : protocol.values()) {
			HashMap<String,String> urlInfo  = new HashMap();

			if (value.startsWith("http")){
				String[] split_info = value.split("\\*oo#&");
				System.out.println(value);
				String url = split_info[0];
				String title = "NO TITLE AVAILABLE";
				String description ="NO DESCRIPTION AVAILABE";
				try {
					title = split_info[1];
					System.out.println("Title - "+title);
				} catch (IndexOutOfBoundsException e){
					title = "NO TITLE AVAILABLE";
				}
				try {
					description = split_info[2];
					System.out.println("Description - "+description);
				} catch (IndexOutOfBoundsException e){
					description ="NO DESCRIPTION AVAILABE";
				}
				System.out.println("[INSERT] Description - "+description);
				urlInfo.put("title", title);
				urlInfo.put("description",description);
				urlInfo.put("url", url);
				urlInfo.put("found", "true");
				anwser.add(urlInfo);
			}
		}



		return anwser;
	}

	private HashMap<String,String> findURL(Object parameter,int replyCounter) throws RemoteException, InterruptedException, NotBoundException {
		HashMap<String,String> myDic;
		try {
			this.ucBusca=(ServerLibrary) LocateRegistry.getRegistry(prop.getProperty("REGISTRYIP"), Integer.parseInt(prop.getProperty("REGISTRYPORT"))).lookup(prop.getProperty("LOOKUP"));
			myDic = this.ucBusca.getReferencePages((String) parameter);
			System.out.println(" --- Resultados de pesquisa ---\n\n");
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
			findURL(parameter,++replyCounter);
		}
		return new HashMap<String,String>();
	}

	private HashMap<String,String> findWord(Object parameter,int replyCounter) throws RemoteException, InterruptedException, NotBoundException {
		HashMap<String,String> myDic;
		try {
			this.ucBusca=(ServerLibrary) LocateRegistry.getRegistry(prop.getProperty("REGISTRYIP"), Integer.parseInt(prop.getProperty("REGISTRYPORT"))).lookup(prop.getProperty("LOOKUP"));
			myDic = this.ucBusca.searchWords((String[]) parameter);
			System.out.println(" --- Resultados de pesquisa ---\n\n");
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
			findWord(parameter,++replyCounter);
		}
		return new HashMap<String,String>();
	}
}
