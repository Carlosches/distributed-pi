package main.java.co.edu.icesi.swarch.distributedpi;

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

    @Reference(name="attacher")
	public void setSubject(Attacher att) {
		this.attacher = att;
	}

    public GeneratorImpl() throws RemoteException {
        pointsInCircle=0;
        state = FREE;
        System.out.println("server created");
    }
    
    @Override
    public long generatePoints(long points, int seed){
        System.out.println("I was called");
        Random random = new Random(seed);
        long pointsInCircle2 = 0;           // LOOK AT THIS LATER
        for(long i=0; i<points; i++){
            double x = random.nextDouble();
            double y = random.nextDouble();
            //System.out.println("x: " + x + "  y: "+ y);
            if(x*x+y*y<=1){
                pointsInCircle2++;
            }
            
        }
       // System.out.println("points in circle: "+pointsInCircle2);
        return pointsInCircle2;
    }
    public long getPointsInCircle(){
        return pointsInCircle;
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
    
}
