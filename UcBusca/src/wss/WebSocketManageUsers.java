package wss;

import RMISERVER.ServerLibrary;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import ucbusca.model.HistoryModel;

import javax.websocket.OnClose;
import javax.websocket.server.ServerEndpoint;
import java.io.InputStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;


import javax.websocket.OnOpen;
import javax.websocket.OnMessage;
import javax.websocket.Session;

@ServerEndpoint(value = "/wss")
public class WebSocketManageUsers {
    private static final Set<WebSocketManageUsers> users = new CopyOnWriteArraySet<>();
    private static final AtomicInteger sequence = new AtomicInteger(1);
    private final String username;
    private Properties prop = new Properties();
    private Session session;

    public WebSocketManageUsers() {
        if (sequence.get()==1){
            String propFileName = "RMISERVER/config.properties";
            InputStream inputStream = HistoryModel.class.getClassLoader().getResourceAsStream(propFileName);
            try {
                this.prop.load(inputStream);
            } catch (Exception e){
                System.out.println(e);
                System.out.println("Cannot read properties File");

            }
            System.out.println(prop.getProperty("REGISTRYIP"));
            System.out.println(prop.getProperty("REGISTRYPORT"));
            System.out.println(prop.getProperty("LOOKUP"));
            CheckChanges c = new CheckChanges(this,this.prop);
        }
        username = "User" + sequence.getAndIncrement();
        System.out.println("CONSTRUTOR");
    }

    @OnOpen
    public void start(Session session) {
        users.add(this);
        this.session = session;
        System.out.println("---------------------SESSAO---------------------");
        System.out.println(session);
        System.out.println("START");
        for (WebSocketManageUsers user:this.users) {
            System.out.println("ACTIVEUSER"+user.username);
        }
        sendMessage("CONNECTED");

    }

    @OnClose
    public void end() {
        users.remove(this);
    }


    @OnMessage
    public void receiveMessage(String message) {
    	sendMessage(message);
        System.out.println("RECEBEU"+message);
    }

    void sendMessage(String text) {
        System.out.println("ENVIOU "+text);
        for (WebSocketManageUsers user:this.users) {
            System.out.println(user);
            try {
                user.session.getBasicRemote().sendText(text);
            } catch (Exception e) {
                // clean up once the WebSocket connection is closed
                System.out.println("DEIXOU DE ESTAR ON " + e);
            }
        }
    }
}
class CheckChanges extends Thread{
    Properties prop;
    ServerLibrary ucBusca;
    HashMap<String,String> systemInfo;
    HashMap<String,String> systemInfoAct;
    WebSocketManageUsers notify;
    public CheckChanges(WebSocketManageUsers notify,Properties prop) {

        this.prop = prop;
        this.notify = notify;
        System.out.println("Aqui");

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
        try {
            systemInfo = retry(0);
            systemInfo.remove("id");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

        this.start();
    }
    public void run() {
        while(true){
            try {
                Thread.sleep(2000);
            } catch(InterruptedException e2) {
                System.out.println("Interrupted");
            }
            try {
                systemInfoAct = retry(0);
                systemInfoAct.remove("id");
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
            System.out.println("-- PREVIOUS --");
            System.out.println(systemInfo);
            System.out.println("-- ACT --");
            System.out.println(systemInfoAct);
            ArrayList<String> one = new ArrayList<>( systemInfo.values());
            ArrayList<String> two = new ArrayList<>( systemInfoAct.values());
            Collections.sort(one);
            Collections.sort(two);
            if (!one.equals(two)){
                systemInfo = systemInfoAct;
                notify.sendMessage("CHANGED");
                System.out.println(".......................CHANGEDDDDDDDDDD.......................");
            }
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e2) {
                System.out.println("Interrupted");
            }
        }

    }
    private HashMap<String,String> retry(int replyCounter) throws RemoteException, InterruptedException, NotBoundException {
        HashMap<String,String> myDic;
        try {
            this.ucBusca = (ServerLibrary) LocateRegistry.getRegistry(prop.getProperty("REGISTRYIP"), Integer.parseInt(prop.getProperty("REGISTRYPORT") )).lookup(prop.getProperty("LOOKUP") );

            //this.ucBusca=(ServerLibrary) LocateRegistry.getRegistry(prop.getProperty("REGISTRYIP"), Integer.parseInt(prop.getProperty("REGISTRYPORT"))).lookup(prop.getProperty("LOOKUP"));
            myDic = this.ucBusca.sendSystemInfo();
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
