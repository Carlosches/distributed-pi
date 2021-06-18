package main.java.co.edu.icesi.swarch.distributedpi;

import main.java.co.edu.icesi.swarch.distributedpi.Generator;
import java.rmi.RemoteException;

public class Wrap implements Runnable{

    private int seed;
    private long blockSize; 
    private Generator generator;
    private long pointsResult;
    
    public Wrap(Generator generator, int seed, long blockSize){
        this.seed = seed;
        this.blockSize = blockSize;
        this.generator = generator;
    }
    @Override
    public void run() {
        try{
            pointsResult=generator.generatePoints(blockSize, seed);
          //  System.out.println("Wrap points Result: " + pointsResult);
        }catch(RemoteException e){
            System.out.println("no se generaron los puntos");
        }
    }   
    
    public long getPointsResult(){
        return this.pointsResult;
    }
}
