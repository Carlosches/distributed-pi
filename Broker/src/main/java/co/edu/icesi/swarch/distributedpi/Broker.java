package main.java.co.edu.icesi.swarch.distributedpi;

import java.rmi.RemoteException;

//import org.osoa.sca.annotations.*;

//@Service(Runnable.class)
public class Broker implements Generator{
    
    private Provider provider;

    @Override
    public long generatePoints(long points, int seed) throws RemoteException {
   
        return 0;
    }

    @Override
    public long getPointsInCircle() throws RemoteException {
        
        return 0;
    }

  

}
