package main.java.co.edu.icesi.swarch.distributedpi;

import org.osoa.sca.annotations.Service;

@Service
public interface Provider {
    
    Generator getGenerator();
    int generatorCount();

}
