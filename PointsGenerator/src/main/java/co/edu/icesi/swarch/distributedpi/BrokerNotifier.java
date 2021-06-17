package main.java.co.edu.icesi.swarch.distributedpi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import org.osoa.sca.annotations.Service;

@Service
public interface BrokerNotifier extends Remote {

    public void notify(Generator generator) throws RemoteException;
    public int getState() throws RemoteException;
}
