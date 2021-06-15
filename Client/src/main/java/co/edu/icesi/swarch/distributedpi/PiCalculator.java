package main.java.co.edu.icesi.swarch.distributedpi;

import java.rmi.RemoteException;
import java.util.Scanner;

import org.osoa.sca.annotations.*;

@Service(Runnable.class)
public class PiCalculator implements Client_notifier, Runnable {

  @Property
  private String myUri;

  private long blockSize;

  @Reference(name = "client_generator")
  private Client_Broker_Service cb_Service;

  // @Reference
  public final void setGenerator(Client_Broker_Service cb_Service) {
    this.cb_Service = cb_Service;
  }

  public PiCalculator() {
    System.out.println("CLIENT created");
    blockSize = 10000000;
  }

  @Override
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
    int cont = sc.nextInt();
    while(cont != 0){

      long points = sc.nextLong();
      int seed = sc.nextInt();
      int nodes = sc.nextInt();

      long p = 0;
      if(points<100000000){
        blockSize = 100000;
      }

      long antes = 0;
      long despues = 0;

      try {
        antes = System.currentTimeMillis();
        p = cb_Service.generatePoints(points, seed, nodes, blockSize);
        despues = System.currentTimeMillis();
        // cb_Service.getPointsInCircle();
      } catch (RemoteException e) {
        System.out.println("Lastimosament falló");
      }
      long totaltime = (despues-antes)/1000;
      double pi = 4 * ((double) p / points);
      System.out.println("Points in circle: " + p);
      System.out.println("Pi " + pi);
      System.out.println("time: " + totaltime);
      cont = sc.nextInt();
    }

    System.exit(0);
  }

  @Override
  public void notifyClient(long points) throws RemoteException {
    
  }

}
