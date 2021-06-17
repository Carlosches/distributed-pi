package main.java.co.edu.icesi.swarch.distributedpi;

import java.rmi.RemoteException;

public class Wrap implements Runnable{
    private int seed;
    private long blockSize;
    private Generator generator;
    
    public Wrap(Generator generator, int seed, long blockSize){
        this.seed = seed;
        this.blockSize = blockSize;
        this.generator = generator;
    }
    @Override
    public void run() {
        try{
            generator.generatePoints(blockSize, seed);
        }catch(RemoteException e){
            System.out.println("no se generaron los puntos");
        }
    }   
   
}
