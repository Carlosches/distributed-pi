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

public class Broker extends UnicastRemoteObject implements Client_Broker_Service, BrokerNotifier {

  private static Client_notifier cNotifier;
  private static Semaphore semaphore = new Semaphore(1);
  private LinkedList<Generator> availableGenerators;
  private static long totalPoints;
  private static long pointsInCircle;
  private double regionSize;
  private static long blockSize;
  private static Random seedGenerator;

  private static int state;
  public final static int WORKING = 1;
  public final static int FREE = 0;

  @Property
  private String brokerUri;

  @Reference(name = "providerr")
  private Provider provider;

  public Broker() throws RemoteException {
    availableGenerators = new LinkedList<Generator>();
  }

  public final void setProvider(Provider provider) {
    this.provider = provider;
  }

  @Override
  public void generatePoints(long points, int seed, int nodes, long blSize) throws RemoteException {

    totalPoints = points;
    pointsInCircle = 0;
    blockSize = blSize;

    seedGenerator = new Random(seed);

    int calls = (int) Math.ceil(points / (blockSize * nodes * 1.0));
    System.out.println("calls: " + calls);

    state = WORKING;

    for (int i = 0; i < nodes; i++) {
      Generator gen = provider.getGenerator();
      //gen.setBrokerNotifier(this);
      callGenerator(gen, blockSize, seedGenerator.nextInt());
    }

  }

  public void callGenerator(Generator generator, long points, int genSeed) {

    Wrap w = new Wrap(generator, genSeed, points);
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
  public void notify(Generator generator) throws RemoteException {   
    if (totalPoints > 0) {
      try {
        semaphore.acquire();
        totalPoints -= generator.getTotalPoints();
        pointsInCircle += generator.getPointsInCircle();
      
        if (totalPoints > 0) {
          callGenerator(generator, blockSize, seedGenerator.nextInt());
        
        } else {
          cNotifier.notifyClient(pointsInCircle);
          state = FREE;
        }
        semaphore.release();
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Error while notifying");
      }

    }
  }

  public String getBrokerUri(){
    return this.brokerUri;
  }

  @Override
  public int getState() throws RemoteException {
    return state;
  }

}
