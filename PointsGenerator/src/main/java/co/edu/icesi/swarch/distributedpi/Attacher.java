package main.java.co.edu.icesi.swarch.distributedpi;

//import org.osoa.sca.annotations.Service;

//@Service
public interface Attacher{

    void attachGenerator(String uri);
    void detachGenerator(String uri);

}