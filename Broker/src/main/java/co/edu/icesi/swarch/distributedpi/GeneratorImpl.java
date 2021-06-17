package main.java.co.edu.icesi.swarch.distributedpi;

import java.net.URI;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

import org.osoa.sca.annotations.*;

@Scope("CONVERSATION")
public class GeneratorImpl extends UnicastRemoteObject implements Generator, Runnable {

    private long pointsInCircle;

    @Property
    private String myUri;

    private Attacher attacher;
    private BrokerNotifier brokerNotifier;

    private long totalPoints;

    @Reference(name = "attacher")
    public void setSubject(Attacher att) {
        this.attacher = att;
    }

    @Reference(name = "broker_notifier")
    public void setBrokerNotifier1(BrokerNotifier bn) {
        this.brokerNotifier = bn;
    }

    public GeneratorImpl() throws RemoteException {
        pointsInCircle = 0;
        System.out.println("server created");
    }

    public void generatePoints(long points, int seed) throws RemoteException {
        System.out.println("I was called");
        this.totalPoints = points;
        Random random = new Random(seed);
        this.pointsInCircle = 0;
        for (long i = 0; i < points; i++) {
            double x = random.nextDouble();
            double y = random.nextDouble();
            if (x * x + y * y <= 1) {
                this.pointsInCircle++;
            }

        }

        brokerNotifier.notify(this);

    }

    @Override
    public void run() {
        System.out.println("Conecting generator");
        attacher.attachGenerator(myUri);
        System.out.println("Conected generator");
    }

    @Override
    public void setBrokerNotifier(BrokerNotifier brokerNotifier) {
        this.brokerNotifier = brokerNotifier;
    }

    @Override
    public long getPointsInCircle() throws RemoteException {
        return this.pointsInCircle;
    }

    @Override
    public long getTotalPoints() throws RemoteException {
        return this.totalPoints;
    }

    @Override
    public String getUri() throws RemoteException {
        return myUri;
    }
}
