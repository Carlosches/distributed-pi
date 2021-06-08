package main.java.co.edu.icesi.swarch.distributedpi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class GeneratorImpl implements Generator{

    private long pointsInCircle;

    public GeneratorImpl() throws RemoteException {
        System.out.println("server created");
        pointsInCircle=0;
    }
    public long generatePoints(long points, int seed){
        Random random = new Random(seed);
        for(long i=0; i<points; i++){
            double x = random.nextDouble();
            double y = random.nextDouble();
            //System.out.println("x: " + x + "  y: "+ y);
            if(x*x+y*y<=1){
                pointsInCircle++;
            }
        }
        return pointsInCircle;
    }
    public long getPointsInCircle(){
        return pointsInCircle;
    }
    
}
