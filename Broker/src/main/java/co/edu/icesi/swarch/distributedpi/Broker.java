package main.java.co.edu.icesi.swarch.distributedpi;

import org.osoa.sca.annotations.*;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

//@Service(Runnable.class)
public class Broker extends UnicastRemoteObject implements Client_Broker_Service {

  private Client_notifier cNotifier;

  @Reference(name = "providerr")
  private Provider provider;

  public Broker() throws RemoteException {

  }

  // @Reference
  public final void setProvider(Provider provider) {
    this.provider = provider;
  }

  @Override
  public long generatePoints(long points, int seed) throws RemoteException {

    // IMPLEMENTAR LOGICA DE HILOS

    Generator g = provider.getGenerator();

    return g.generatePoints(points, seed);
  }

  @Override
  public void setNotifier(String uri) throws RemoteException{
    
    try {
      cNotifier = (Client_notifier) Naming.lookup(uri);
    } catch (Exception e) {
      System.out.println("ERROR IN BROKER SETNOTIFIER");
    }  
  }
}
