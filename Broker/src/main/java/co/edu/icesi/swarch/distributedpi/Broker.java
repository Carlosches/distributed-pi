package main.java.co.edu.icesi.swarch.distributedpi;

import org.osoa.sca.annotations.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

//@Service(Runnable.class)
public class Broker extends UnicastRemoteObject implements Client_Broker_Service{

    @Reference(name="providerr")
    private Provider provider;

    //@Reference
    public final void setProvider(Provider provider)
    {
      this.provider = provider;
    }

    public Broker() throws RemoteException{

    }

    @Override
    public long generatePoints(long points, int seed) throws RemoteException {
      
      Generator g = provider.getGenerator();
      
      return g.generatePoints(points, seed);
    }
}
