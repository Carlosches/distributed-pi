package main.java.co.edu.icesi.swarch.distributedpi;

import java.rmi.*;

import org.osoa.sca.annotations.Service;
@Service
public interface Generator extends Remote{

    public long generatePoints(long points, int seed, double min, double max)throws RemoteException;
    public long getPointsInCircle()throws RemoteException;
    int getState() throws RemoteException;
    void setState(int state) throws RemoteException;

}