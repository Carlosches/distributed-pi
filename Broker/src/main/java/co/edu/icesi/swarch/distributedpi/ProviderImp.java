package main.java.co.edu.icesi.swarch.distributedpi;

import java.rmi.*;
import java.util.ArrayList;

public class ProviderImp implements Attacher, Provider {

    private static ArrayList<Generator> availableGenerators = new ArrayList<>();
    private static ArrayList<Generator> unAvailableGenerators = new ArrayList<>();

    @Override
    public synchronized void attachGenerator(String uri) {
        
        try{
            
            Generator generator = (Generator)Naming.lookup(uri);
            System.out.println("New generator connected : " + uri);
            availableGenerators.add(generator);

        }catch(Exception e){
            System.out.println("error al hacer binding: "+uri);
            e.printStackTrace();
        }
        
    }

    @Override
    public synchronized void detachGenerator(String uri) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Generator getGenerator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int generatorCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    
    
}
