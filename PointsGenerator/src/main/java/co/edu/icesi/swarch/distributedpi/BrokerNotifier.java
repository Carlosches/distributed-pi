package main.java.co.edu.icesi.swarch.distributedpi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BrokerNotifier extends Remote {

    public void notify(Generator generator) throws RemoteException;
    
}
