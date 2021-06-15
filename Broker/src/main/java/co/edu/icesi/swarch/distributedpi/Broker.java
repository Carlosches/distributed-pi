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
public class Broker extends UnicastRemoteObject implements Client_Broker_Service, BrokerNotifier {

  private static Client_notifier cNotifier;
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

    this.totalPoints = points;
    System.out.println("total points: " + totalPoints);
    this.blockSize = blSize;
    this.seed = seed;
    int threads = (int) Math.ceil(points / (blockSize * nodes * 1.0));
    System.out.println("threads: " + threads); 
    regionSize = (1.0 / (nodes * threads * 1.0));
    System.out.println("region: " + regionSize);
    min = 0.0;
    max = regionSize;

    for (int i = 0; i < nodes; i++) {
      Generator gen = provider.getGenerator();
      gen.setBrokerNotifier(this);
      callGenerator(gen, blockSize);
      min = max;
      max += regionSize;
      System.out.println("max: " + max);
    }

  }

  public void callGenerator(Generator generator, long points) {
    Wrap w = new Wrap(generator, seed, points, min, max);
    new Thread(w).start();

  }

  @Override
  public void setNotifier(String uri) throws RemoteException {
    System.out.println("set notifier called");
    try {
      cNotifier = (Client_notifier) Naming.lookup(uri);
    } catch (Exception e) {
      System.out.println("ERROR IN BROKER SETNOTIFIER");
    }

  }

  @Override
  public synchronized void notify(Generator generator) throws RemoteException {
    if (totalPoints > 0) { // macheteeeee XD

      System.out.println(generator.getUri());
      try {
        semaphore.acquire();
        totalPoints -= generator.getTotalPoints();
        System.out.println(totalPoints);
        pointsInCircle += generator.getPointsInCircle();
        System.out.println(pointsInCircle);
        System.out.println("max: " + max);
        if (totalPoints > 0) {
          callGenerator(generator, this.blockSize);
          min = max;
          max += regionSize;
          
        } else {
          cNotifier.notifyClient(this.pointsInCircle);
        }
        semaphore.release();
      } catch (Exception e) {
        System.out.println("Errorrrrrrr");
      }
      
      System.out.println();
    }
  }
}
