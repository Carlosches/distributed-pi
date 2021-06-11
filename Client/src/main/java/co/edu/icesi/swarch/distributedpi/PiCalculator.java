package main.java.co.edu.icesi.swarch.distributedpi;

import java.rmi.RemoteException;
import java.util.Scanner;

import org.osoa.sca.annotations.*;

@Service(Runnable.class)
public class PiCalculator implements Runnable{
    
    @Reference(name = "client_generator")
    private Client_Broker_Service cb_Service;

    //@Reference
    public final void setGenerator(Client_Broker_Service cb_Service)
    {
      this.cb_Service = cb_Service;
    }

    
  public PiCalculator()
  {
    System.out.println("CLIENT created");
  }

  public final void run() 
  {
        Scanner sc = new Scanner(System.in);
        long points = sc.nextLong();
        int seed = sc.nextInt();
        long p = 0;
        try{
            p=cb_Service.generatePoints(points, seed);
            //cb_Service.getPointsInCircle();
        }catch (RemoteException e){
            System.out.println("Lastimosament falló");
        }
        double pi = 4*( (double) p/points); 
        System.out.println("Points in circle: " + p);
        System.out.println("Pi " + pi);
        
  }

    
}
