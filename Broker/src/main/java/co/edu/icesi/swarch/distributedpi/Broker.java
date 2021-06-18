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
public class Broker extends UnicastRemoteObject implements Client_Broker_Service{

  private Client_notifier cNotifier;
  private static Semaphore semaphore = new Semaphore(1);
  private LinkedList<Generator> availableGenerators;
  private LinkedList<Integer> states;

  @Reference(name = "providerr")
  private static Provider provider;

  public Broker() throws RemoteException {
    availableGenerators = new LinkedList<Generator>();
    states = new LinkedList<Integer>();
  }

  // @Reference
  public final void setProvider(Provider provider) {
    this.provider = provider;
  }

  @Override
  public long generatePoints(long points, int seed, int nodes, long blockSize) throws RemoteException {
    availableGenerators = new LinkedList<Generator>();
    states = new LinkedList<Integer>();

    for(int i=0; i<nodes ;i++){
       availableGenerators.add(provider.getGenerator());
       states.add(GeneratorImpl.FREE);
    }
   
    int threads = (int) Math.ceil(points/ (blockSize*nodes*1.0));
    
    long totalPoints=0;
    
   
    ArrayList<Wrap> wraps = new ArrayList<Wrap>();
    ExecutorService executor = Executors.newFixedThreadPool(threads);

    Random r = new Random(seed);

    while(threads-->0){
      
      for(int i=0;i<nodes;i++){
        //Generator gen = availableGenerators.poll();
        Generator gen = provider.getGenerator();
        availableGenerators.add(gen);
        Wrap w = new Wrap(gen, r.nextInt(), blockSize);
        try {
          semaphore.acquire();
          wraps.add(w);
          semaphore.release();
        } catch (Exception e) {
          System.out.println("Error with semaphore");
        }
        
        executor.execute(w);
       
      }
      
    }
    executor.shutdown();
    while(!executor.isTerminated());

    System.out.println("tamaño de wrapper: " + wraps.size());

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
