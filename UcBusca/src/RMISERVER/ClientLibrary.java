package RMISERVER;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientLibrary extends Remote {
    public void notification(String sms) throws RemoteException;
}
