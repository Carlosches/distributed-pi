package main.java.co.edu.icesi.swarch.distributedpi;

import java.rmi.*;

import org.osoa.sca.annotations.Service;
@Service
public interface Client_Broker_Service extends Remote{

    void generatePoints(long points, int seed, int nodes, long blockSize)throws RemoteException;
    void setNotifier(String uri) throws RemoteException;

}