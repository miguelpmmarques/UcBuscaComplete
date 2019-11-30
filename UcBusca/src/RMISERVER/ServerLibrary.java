package RMISERVER;

import java.net.UnknownHostException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface ServerLibrary extends Remote {
    public String connected(ClientLibrary newUser) throws RemoteException;
    public HashMap<String,String> userRegistration(User newUser) throws RemoteException, UnknownHostException;
    public HashMap<String,String> userLogin(User newUser) throws RemoteException, InterruptedException;
    public HashMap<String,String> searchWords(String[] words) throws RemoteException;
    public int checkMe() throws RemoteException;
    public HashMap<String,String> sendSystemInfo() throws RemoteException;
    public HashMap<String,String> addURLbyADMIN(String url) throws RemoteException;
    public HashMap<String,String> getAllUsers() throws RemoteException;
    public HashMap<String,String> getReferencePages(String url) throws RemoteException;
    public HashMap<String,String> getHistory(User thisUser) throws RemoteException;
    public HashMap<String,String> changeUserPrivileges(String username) throws RemoteException;
    }

