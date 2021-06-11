package main.java.co.edu.icesi.swarch.distributedpi;

import org.osoa.sca.annotations.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

//@Service(Runnable.class)
public class Broker extends UnicastRemoteObject implements Client_Broker_Service{

    private Provider provider;

    public Broker() throws RemoteException{

    }

    @Override
    public long generatePoints(long points, int seed) throws RemoteException {
        return 0;
    }
}
