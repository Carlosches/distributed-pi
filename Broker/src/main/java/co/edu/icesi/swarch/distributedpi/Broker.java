package main.java.co.edu.icesi.swarch.distributedpi;

import org.osoa.sca.annotations.*;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.math.*;

import java.util.concurrent.*;

//@Service(Runnable.class)
public class Broker extends UnicastRemoteObject implements Client_Broker_Service, BrokerNotifier{

  private Client_notifier cNotifier;
  private static Semaphore semaphore = new Semaphore(1);
  private LinkedList<Generator> availableGenerators;
  private LinkedList<Integer> states;
  private long totalPoints;
  private long pointsInCircle;
  private double min;
  private double max;
  private double regionSize;
  private long blockSize;
  private int seed;
  @Reference(name = "providerr")
  private Provider provider;

  public Broker() throws RemoteException {
    availableGenerators = new LinkedList<Generator>();
    states = new LinkedList<Integer>();
  }

  // @Reference
  public final void setProvider(Provider provider) {
    this.provider = provider;
  }

  @Override
  public void generatePoints(long points, int seed, int nodes, long blSize) throws RemoteException {
    //availableGenerators = new LinkedList<Generator>();
    //states = new LinkedList<Integer>();
    this.totalPoints = points;
    this.blockSize = blSize;
    this.seed = seed;
    int threads = (int) Math.ceil(points/ (blockSize*nodes*1.0));
    regionSize = (1.0/(nodes*threads*1.0));
    min = 0.0;
    max = regionSize;

    for(int i=0; i<nodes ;i++){
       Generator gen = provider.getGenerator();
       gen.setBrokerNotifier(this);
       callGenerator(gen, blockSize);
       min = max;
       max+=regionSize;
    }
   
    
   
    //ArrayList<Wrap> wraps = new ArrayList<Wrap>();
    //ExecutorService executor = Executors.newFixedThreadPool(threads);


   /* while(threads-->0){
      
      for(int i=0;i<nodes;i++){
        Generator gen = availableGenerators.poll();
        availableGenerators.add(gen);
        Wrap w = new Wrap(gen, points, seed, blockSize, min,max);
        try {
          semaphore.acquire();
          wraps.add(w);
          semaphore.release();
        } catch (Exception e) {
          System.out.println("Error with semaphore");
        }
        
        executor.execute(w);
        min=max;
        max+=regionSize;
      }
      
    }
    executor.shutdown();
    while(!executor.isTerminated());

    System.out.println("tamaño de wrapper: " + wraps.size());

    for(Wrap wr: wraps){
      totalPoints+=wr.getPointsResult();
    }*/
  }
  public void callGenerator(Generator generator, long points){
    Wrap w = new Wrap(generator, seed, points, min,max);
    new Thread(w).start(); 
    
  }

  @Override
  public void setNotifier(String uri) throws RemoteException{
    System.out.println("set notifier called");
    try {
      cNotifier = (Client_notifier) Naming.lookup(uri);
      if(cNotifier==null)
        System.out.println("Client notifier es null");
    } catch (Exception e) {
      System.out.println("ERROR IN BROKER SETNOTIFIER");
    }  
  }

  @Override
  public synchronized void notify(Generator generator) throws RemoteException {
    try{
        semaphore.acquire();
        totalPoints-=generator.getTotalPoints();
        pointsInCircle+=generator.getPointsInCircle();
        if(totalPoints>0){
          min = max;
          max+=regionSize;
          callGenerator(generator, this.blockSize);
          
        }
      else{
        cNotifier.notifyClient(this.pointsInCircle);
      }
      semaphore.release();
    }catch(InterruptedException e){
      e.printStackTrace();
    }
  }
}
