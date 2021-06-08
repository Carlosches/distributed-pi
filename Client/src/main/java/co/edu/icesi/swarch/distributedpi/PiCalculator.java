package main.java.co.edu.icesi.swarch.distributedpi;
import java.rmi.RemoteException;
import java.util.Scanner;
import main.java.co.edu.icesi.swarch.distributedpi.Generator;

import org.osoa.sca.annotations.Reference;

public class PiCalculator implements Runnable{
    
    private Generator generator;

    @Reference
    public final void setGenerator(Generator generator)
    {
      this.generator = generator;
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
            p=generator.generatePoints(points, seed);
            //generator.getPointsInCircle();
        }catch (RemoteException e){
            System.out.println("Lastimosament falló");
        }
        double pi = 4*( (double) p/points); 
        System.out.println("Points in circle: " + p);
        System.out.println("Pi " + pi);
        
  }

    
}
