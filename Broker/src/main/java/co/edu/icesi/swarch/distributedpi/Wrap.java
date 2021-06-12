package main.java.co.edu.icesi.swarch.distributedpi;

import main.java.co.edu.icesi.swarch.distributedpi.Generator;
import java.rmi.RemoteException;

public class Wrap implements Runnable{

    private long points;
    private int seed;
    private long blockSize;
    private double min;
    private double max; 
    private Generator generator;
    private long pointsResult;
    
    public Wrap(Generator generator, long points, int seed, long blockSize, double min, double max){
        this.points=points;
        this.seed = seed;
        this.blockSize = blockSize;
        this.min = min;
        this.max = max;
        this.generator = generator;
    }
    @Override
    public void run() {
        try{
            pointsResult=generator.generatePoints(blockSize, seed, min,max);
          //  System.out.println("Wrap points Result: " + pointsResult);
        }catch(RemoteException e){
            System.out.println("no se generaron los puntos");
        }
    }   
    
    public long getPointsResult(){
        return this.pointsResult;
    }
}
