package main.java.co.edu.icesi.swarch.distributedpi;

import java.rmi.RemoteException;
import java.util.Scanner;

import org.osoa.sca.annotations.*;
import javax.swing.JFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Service(Runnable.class)
public class PiCalculator implements Client_notifier, Runnable {

  @Property
  private String myUri;

  private long blockSize;
  private static long totalPoints;
  private int seed;
  private int nodes;
  private static ClientInterface clientInterface;
  private static long beforeExecution;
  private static long afterExecution;

  @Reference(name = "client_generator")
  private Client_Broker_Service cb_Service;

  // @Reference
  public final void setGenerator(Client_Broker_Service cb_Service) {
    this.cb_Service = cb_Service;
  }

  public PiCalculator() {
    System.out.println("CLIENT created");
    blockSize = 10000000;
  }

  @Override
  public final void run() {

    System.out.println("Setting observer...");
    clientInterface = new ClientInterface();
    clientInterface.setLocationRelativeTo(null);
    clientInterface.setVisible(true);
    try {
    
      cb_Service.setNotifier(myUri);
      Thread.sleep(1000);

    } catch (Exception e) {
      
      System.out.println("Error while setting observer!");

    }

    System.out.println("Observer ready!");
    System.out.println("Started");

    events();     

   
  }
  public void events() {
    clientInterface.getCalculateBtn().addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        totalPoints = Long.parseLong(clientInterface.getPointsTF().getText());
        seed = Integer.parseInt(clientInterface.getSeedTF().getText());
        nodes = Integer.parseInt(clientInterface.getNodesTf().getText());

        if (totalPoints < 100000000) {
          blockSize = 100000;
        }
        new Thread(new Runnable() {
          public void run() {
            try {
              beforeExecution = System.currentTimeMillis();
              long p = cb_Service.generatePoints(totalPoints, seed, nodes, blockSize);
              calculatePi(p);
            } catch (RemoteException e) {
              System.out.println("Falló el llamado cliente-broker");
            }
          }
        }).start();
      }
    });
  }

  public void calculatePi(long pointsInCircle){
    afterExecution = System.currentTimeMillis();
    double pi = 4 * ((double) pointsInCircle / totalPoints);
    clientInterface.getPiTF().setText(""+pi);
    long diff = (afterExecution-beforeExecution)/1000;
    clientInterface.getTimeTF().setText(""+diff);
  }

  @Override
  public void notifyClient(long points) throws RemoteException {
    
  }

}