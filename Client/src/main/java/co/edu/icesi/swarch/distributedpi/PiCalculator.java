package main.java.co.edu.icesi.swarch.distributedpi;

import java.rmi.RemoteException;
import java.util.Scanner;

import org.osoa.sca.annotations.*;

@Service(Runnable.class)
public class PiCalculator implements Client_notifier, Runnable {

  @Property
  private String myUri;

  private long blockSize;
  private static long totalPoints;
  private int seed;
  private int nodes;

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

      totalPoints = sc.nextLong();
      seed = sc.nextInt();
      nodes = sc.nextInt();

      long p = 0;
      if(totalPoints<100000000){
        blockSize = 100000;
      }
     
        new Thread(new Runnable(){
          public void run(){
            try{
              cb_Service.generatePoints(totalPoints, seed, nodes, blockSize);
            }catch(RemoteException e){
              System.out.println("Lastimosament falló");
            }
          }
        }).start();
        System.out.println("El hilo se mando del cliente al broker");
        // cb_Service.getPointsInCircle();
     
      cont = sc.nextInt();
    }

    System.exit(0);
  }

  @Override
  public void notifyClient(long pointsInCircle) throws RemoteException {
     double pi = 4 * ((double) pointsInCircle / totalPoints);
     System.out.println("Pi " + pi);
  }

  @Override
  public String getUri() throws RemoteException{
    return myUri;
  }

}
