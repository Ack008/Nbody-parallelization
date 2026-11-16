package org.Thread;

import org.NBodySimulator;
import org.SimulationView;

public class SimulationThreadWithGUI {
    static final int NUM_BODIES = 200;
    static final int NUM_THREADS = Runtime.getRuntime().availableProcessors() + 1;
    static final int MAX_STEPS = 5000;
    static final double DT = 0.01;
    public static void main(String[] args) {
        NBodySimulator simulation = new ThreadNBodySimulation();
        SimulationView view = new SimulationView(800,800, simulation);
        simulation.setView(view);

        simulation.initRandom(NUM_BODIES);
        simulation.execute(NUM_THREADS, MAX_STEPS, 0.01);
    }
    
    private static void log(String msg) {
        System.out.println("[" + System.currentTimeMillis() + "] " + msg);
    }
}
