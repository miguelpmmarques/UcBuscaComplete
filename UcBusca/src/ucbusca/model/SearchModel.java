package ucbusca.model;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import RMISERVER.ServerLibrary;


public class SearchModel {



	private String seachWords;
	private ServerLibrary ucBusca;
	private Properties prop = new Properties();



	public SearchModel() {
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
		this.seachWords = "Anonymous "+this.seachWords;
		String[] searchWordsSplited = this.seachWords.split("\\s+");


		for(int count = 0; count < searchWordsSplited.length; count++)
			System.out.println(searchWordsSplited[count]);
			//anwser.add(searchWordsSplited[count]);
		protocol =  retry(searchWordsSplited,0);

		ArrayList<HashMap<String,String>> searchHash;
		for (String value : protocol.values()) {
			HashMap<String,String> urlInfo  = new HashMap();
			if (value.startsWith("http")){
				try {
					Document document = Jsoup.connect(value).get();
					urlInfo.put("title", document.title());
					urlInfo.put("description", document.select("meta[name=description]").get(0)
							.attr("content"));
					urlInfo.put("url", value);
					urlInfo.put("found", "true");
				} catch (IOException | IndexOutOfBoundsException e){
					urlInfo.put("found", "false");
					urlInfo.put("info", "--- Cannot reach page info ---");
					urlInfo.put("url", value);
				}
				anwser.add(urlInfo);
			}
		}

		return anwser;
	}


	private HashMap<String,String> retry(Object parameter,int replyCounter) throws RemoteException, InterruptedException, NotBoundException {
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
			retry(parameter,++replyCounter);
		}
		return new HashMap<String,String>();
	}
}
