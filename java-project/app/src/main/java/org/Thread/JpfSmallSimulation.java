package org.Thread;

public class JpfSmallSimulation {
    public static void main(String[] args) {
        ThreadNBodySimulation simulation = new ThreadNBodySimulation();
        simulation.initFixed(4);
        simulation.execute(3, 2, 0.01);
    }
}