package main.java.co.edu.icesi.swarch.distributedpi;

import org.osoa.sca.annotations.*;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.math.*;

import java.util.concurrent.*;

//@Service(Runnable.class)
public class Broker extends UnicastRemoteObject implements Client_Broker_Service {

  private Client_notifier cNotifier;
  private static Semaphore semaphore = new Semaphore(1);
  private LinkedList<Generator> availableGenerators;

  @Reference(name = "providerr")
  private Provider provider;

  public Broker() throws RemoteException {
    availableGenerators = new LinkedList<Generator>();
  }

  // @Reference
  public final void setProvider(Provider provider) {
    this.provider = provider;
  }

  @Override
  public long generatePoints(long points, int seed, int nodes, long blockSize) throws RemoteException {
    availableGenerators = new LinkedList<Generator>();
    for(int i=0; i<nodes ;i++){
       availableGenerators.add(provider.getGenerator());  
    }
   // System.out.println("cantidad de generators: " + availableGenerators.size());
    int threads = (int) Math.ceil(points/ (blockSize*nodes*1.0));
    long totalPoints=0;
    double regionSize = (1.0/(nodes*1.0));
   // System.out.println("threads: " + threads);
    ArrayList<Wrap> wraps = new ArrayList<Wrap>();
    ExecutorService executor = Executors.newFixedThreadPool(threads);
    while(threads-->0){
      double min = 0.0;
      double max = regionSize;

      for(int i=0;i<nodes;i++){
        Generator gen = availableGenerators.poll();
        availableGenerators.add(gen);
        Wrap w = new Wrap(gen, points,seed, blockSize, min,max);
        wraps.add(w);
        executor.execute(w);
       /* try{
          // Thread.sleep(1000);
           semaphore.acquire();
           totalPoints+=w.getPointsResult();
           System.out.println("total points: " + totalPoints);
           semaphore.release();
        }catch(Exception e){

        }*/
        min=max;
        max+=regionSize;
      }
      
    }
    executor.shutdown();
    while(!executor.isTerminated());
    for(Wrap wr: wraps){
      totalPoints+=wr.getPointsResult();
    }
    return totalPoints;  // retorna el total de puntos
  }

  @Override
  public void setNotifier(String uri) throws RemoteException{
    
    try {
      cNotifier = (Client_notifier) Naming.lookup(uri);
    } catch (Exception e) {
      System.out.println("ERROR IN BROKER SETNOTIFIER");
    }  
  }
}
