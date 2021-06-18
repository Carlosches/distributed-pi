package main.java.co.edu.icesi.swarch.distributedpi;
public class Scenario {
    
    private String name;
    private long totalPoints;
    private int seed;
    private int nodes;

    public Scenario(String name, long totalPoints, int seed, int nodes){
        this.name = name;
        this.totalPoints = totalPoints;
        this.seed = seed;
        this.nodes = nodes;
    }

    public String getName(){
        return this.name;
    }
    public long getTotalPoints(){
        return this.totalPoints;
    }
    public long getSeed(){
        return this.seed;
    }
    public long getNodes(){
        return this.nodes;
    }

}
