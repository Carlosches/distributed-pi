package main.java.co.edu.icesi.swarch.distributedpi;

import java.net.URI;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

import org.osoa.sca.annotations.*;

@Scope("CONVERSATION")
public class GeneratorImpl extends UnicastRemoteObject implements Generator, Runnable{

    public final static int FREE=0;
    public final static int WORKING=1;

	private int state;

    private long pointsInCircle;
    
    @Property
    private String myUri;
    
    private Attacher attacher;

    private BrokerNotifier brokerNotifier;
    private long totalPoints;

    @Reference(name="attacher")
	public void setSubject(Attacher att) {
		this.attacher = att;
	}

    public GeneratorImpl() throws RemoteException {
        pointsInCircle=0;
        state = FREE;
        System.out.println("server created");
    }
    
    public void generatePoints(long points, int seed, double min, double max) throws RemoteException{
       // System.out.println("min: "+min +" max: "+ max);
        this.totalPoints=points;
        Random random = new Random(seed);
        this.pointsInCircle=0;
        for(long i=0; i<points; i++){
            double x = min + ( max - min ) * random.nextDouble();
            double y = random.nextDouble();
            //System.out.println("x: " + x + "  y: "+ y);
            if(x*x+y*y<=1){
                this.pointsInCircle++;
            }
            
        }
       
       brokerNotifier.notify(this);
       //System.out.println("Free");
    }
    

    @Override
    public void run(){
        System.out.println("Conecting generator");
		attacher.attachGenerator(myUri);
		System.out.println("Conected generator");
    }

    @Override
    public int getState() throws RemoteException{
        return state;
    }

    @Override
    public void setState(int state) throws RemoteException {
        this.state = state;
    }

    @Override
    public void setBrokerNotifier(BrokerNotifier brokerNotifier){
        this.brokerNotifier = brokerNotifier;
    }
    @Override
    public long getPointsInCircle() throws RemoteException{
        return this.pointsInCircle;
    }
    @Override
    public long getTotalPoints() throws RemoteException{
        return this.totalPoints;
    } 

    @Override
    public String getUri() throws RemoteException{
        return myUri;
    }
}
