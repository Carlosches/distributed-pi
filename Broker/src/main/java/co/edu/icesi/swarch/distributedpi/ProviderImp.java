package main.java.co.edu.icesi.swarch.distributedpi;

import java.rmi.*;
import java.util.LinkedList;

public class ProviderImp implements Attacher, Provider {

    private static LinkedList<Generator> availableGenerators = new LinkedList<>();
    private static LinkedList<Generator> unAvailableGenerators = new LinkedList<>();

    private static LinkedList<String> availableUris = new LinkedList<>();

    @Override
    public synchronized void attachGenerator(String uri) {
        
        try{
            
            Generator generator = (Generator)Naming.lookup(uri);
            System.out.println("New generator connected : " + uri);
            availableGenerators.add(generator);
            availableUris.add(uri);

        }catch(Exception e){
            System.out.println("error al hacer binding: "+uri);
            e.printStackTrace();
        }
        
    }

    @Override
    public synchronized void detachGenerator(String uri) {
        int index=availableUris.indexOf(uri);
		String uriRemoved=availableUris.remove(index);
		availableGenerators.remove(index);
		assert(uri.equals(uriRemoved));	
        
    }

    @Override
    public Generator getGenerator() {
        int size=availableGenerators.size();
        if(size>0){
            Generator ret=availableGenerators.poll();
            availableUris.add(availableUris.poll());
            availableGenerators.add(ret);
            return ret;
        }
        return null;
    }

    @Override
    public int generatorCount() {
        return availableGenerators.size();
    }

    
    
}
