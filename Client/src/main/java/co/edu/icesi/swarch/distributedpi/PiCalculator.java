package main.java.co.edu.icesi.swarch.distributedpi;

import java.rmi.RemoteException;
import java.util.Scanner;
import org.osoa.sca.annotations.*;
import javax.swing.JFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.annotation.processing.FilerException;
import javax.swing.JFileChooser;
import java.io.*;    

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
  private static File in;
  private static FileWriter out;
  private static Scenario scenario;
  private static BufferedReader br;
  @Reference(name = "client_generator")
  private Client_Broker_Service cb_Service;

  // @Reference
  public final void setGenerator(Client_Broker_Service cb_Service) {
    this.cb_Service = cb_Service;
  }

  public PiCalculator() {
    System.out.println("CLIENT created");
    blockSize = 10000000;
    try{
      out= new FileWriter("out.txt", true);
    }catch(IOException e){
        System.out.println("fallo creando el fileWriter");
    }
  }

  @Override
  public final void run() {

    System.out.println("Setting observer...");
    clientInterface = new ClientInterface();
    clientInterface.setLocationRelativeTo(null);
    clientInterface.setVisible(true);

    try {

      cb_Service.setNotifier(myUri);
      Thread.sleep(500);

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
        if(!clientInterface.getPointsTF().getText().isEmpty())
        totalPoints = Long.parseLong(clientInterface.getPointsTF().getText());
        if(!clientInterface.getSeedTF().getText().isEmpty())
        seed = Integer.parseInt(clientInterface.getSeedTF().getText());
        if(!clientInterface.getNodesTf().getText().isEmpty())
        nodes = Integer.parseInt(clientInterface.getNodesTf().getText());

        if (totalPoints < 100000000) {
          blockSize = 100000;
        }
        if(in!=null){
          readFile(in);
        }else{
          new Thread(new Runnable() {
            public void run() {
              try {
                scenario = new Scenario("Scenario",totalPoints,seed,nodes);
                beforeExecution = System.currentTimeMillis();
                cb_Service.generatePoints(totalPoints, seed, nodes, blockSize);
              } catch (RemoteException e) {
                System.out.println("Falló el llamado cliente-broker");
              }
            }
          }).start();
        }
       }
    });

    clientInterface.getFileBtn().addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
          JFileChooser fileChooser = new JFileChooser();
          fileChooser.setCurrentDirectory(new File("distributed-pi/Client"));
          int result = fileChooser.showOpenDialog(clientInterface.getContentPane().getParent());
          if (result == JFileChooser.APPROVE_OPTION) {
            try{
            in = fileChooser.getSelectedFile();
            br = new BufferedReader(new FileReader(in));
            }catch(IOException ee){
              System.out.println("fallo creando el buffer");
            }
            clientInterface.getFileLabel().setText("Selected file: " + in.getAbsolutePath());
            //readFile(in);
          }
        }
    });
  }
  private void readFile(File file){
      String st;
      try{
          if((st = br.readLine())!=null){
            
            String configName = st;
            String po = br.readLine();
            if(po!=null)totalPoints = Long.parseLong(po);
            if((po = br.readLine())!=null)seed = Integer.parseInt(po);
            if((po = br.readLine())!=null)nodes= Integer.parseInt(po);
            System.out.println("cofigName: " + configName);
            scenario = new Scenario(configName,totalPoints,seed,nodes);
            if (totalPoints < 100000000) {
              blockSize = 100000;
            }
    
            new Thread(new Runnable() {
              public void run() {
                try {
                  beforeExecution = System.currentTimeMillis();
                  cb_Service.generatePoints(totalPoints, seed, nodes, blockSize);
                } catch (RemoteException e) {
                  System.out.println("Falló el llamado cliente-broker");
                }
              }
            }).start();
          }else{
            out.close();
          }
       }catch(IOException e){
         System.out.println("falló leyendo el archivo");
       }catch(NullPointerException ee){

       }
      
  }

  @Override
  public void notifyClient(long pointsInCircle) throws RemoteException {
    afterExecution = System.currentTimeMillis();
    double pi = 4 * ((double) pointsInCircle / totalPoints);
    clientInterface.getPiTF().setText(""+pi);
    long diff = (afterExecution-beforeExecution)/1000;
    clientInterface.getTimeTF().setText(""+diff);
    try{
  //  out= new FileWriter("out.txt");
    if(scenario==null){
      System.out.println("El scenario es null");
    }
    out.write("Scenario name: " + scenario.getName()+"\n");
    out.write("Points sent to generate : " + totalPoints+"\n");
    out.write("Points in circle : " + pointsInCircle+"\n");
    out.write("Pi calculated : " + pi+"\n");
    out.write("Execution time (seconds): " +diff+"\n");
    out.write("=======================================================\n"+"\n");
    
   
    }catch(IOException e){
      System.out.println("Falló escribiendo el archivo");
    }
    if(in!=null)readFile(in);
  }

  @Override
  public String getUri() throws RemoteException {
    return myUri;
  }

}
