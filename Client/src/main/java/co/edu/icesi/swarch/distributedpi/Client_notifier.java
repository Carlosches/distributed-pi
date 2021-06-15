package main.java.co.edu.icesi.swarch.distributedpi;

import java.rmi.*;
import java.rmi.RemoteException;

import org.osoa.sca.annotations.Service;
@Service
public interface Client_notifier extends Remote {
    
    void notifyClient(long points) throws RemoteException;
    String getUri() throws RemoteException; 

}
