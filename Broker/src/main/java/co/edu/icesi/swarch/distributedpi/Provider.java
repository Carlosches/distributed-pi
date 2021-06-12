package main.java.co.edu.icesi.swarch.distributedpi;

import org.osoa.sca.annotations.Service;
import java.util.LinkedList;
@Service
public interface Provider {
    
    public Generator getGenerator();
    public int generatorCount();
    public LinkedList<Generator> getAvailableGenerators();

}
