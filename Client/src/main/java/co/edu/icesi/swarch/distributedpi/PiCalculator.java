package main.java.co.edu.icesi.swarch.distributedpi;

import java.rmi.RemoteException;
import java.util.Scanner;

import org.osoa.sca.annotations.*;

@Service(Runnable.class)
public class PiCalculator implements Client_notifier, Runnable {

  @Property
  private String myUri;

  @Reference(name = "client_generator")
  private Client_Broker_Service cb_Service;

  // @Reference
  public final void setGenerator(Client_Broker_Service cb_Service) {
    this.cb_Service = cb_Service;
  }

  public PiCalculator() {
    System.out.println("CLIENT created");
  }

  public final void run() {

    System.out.println("Setting observer...");
    
    try {
    
      cb_Service.setNotifier(myUri);
      Thread.sleep(1000);

    } catch (Exception e) {
      
      System.out.println("Error while setting observer!");

    }

    System.out.println("Observer ready!");
    System.out.println("Started");

    Scanner sc = new Scanner(System.in);
    long points = sc.nextLong();
    int seed = sc.nextInt();
    long p = 0;
    
    try {
      p = cb_Service.generatePoints(points, seed);
      // cb_Service.getPointsInCircle();
    } catch (RemoteException e) {
      System.out.println("Lastimosament falló");
    }

    double pi = 4 * ((double) p / points);
    System.out.println("Points in circle: " + p);
    System.out.println("Pi " + pi);

  }

  @Override
  public void notifyClient(long points) throws RemoteException{

  }

}
