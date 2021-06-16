package main.java.co.edu.icesi.swarch.distributedpi;

import java.rmi.*;

import org.osoa.sca.annotations.Service;
@Service
public interface Client_notifier extends Remote {
    
    void notifyClient(long points) throws RemoteException;
}
