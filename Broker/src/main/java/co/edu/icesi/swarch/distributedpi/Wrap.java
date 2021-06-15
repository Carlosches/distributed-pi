package main.java.co.edu.icesi.swarch.distributedpi;

import main.java.co.edu.icesi.swarch.distributedpi.Generator;
import java.rmi.RemoteException;

public class Wrap implements Runnable{
    private int seed;
    private long blockSize;
    private double min;
    private double max; 
    private Generator generator;
    
    public Wrap(Generator generator, int seed, long blockSize, double min, double max){
        this.seed = seed;
        this.blockSize = blockSize;
        this.min = min;
        this.max = max;
        this.generator = generator;
    }
    @Override
    public void run() {
        try{
            generator.generatePoints(blockSize, seed, min,max);
            //System.out.println("termine en wrap");
          //  System.out.println("Wrap points Result: " + pointsResult);
        }catch(RemoteException e){
            System.out.println("no se generaron los puntos");
        }
    }   
   
}
