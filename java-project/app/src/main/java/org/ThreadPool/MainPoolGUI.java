package org.ThreadPool;

import org.NBodySimulator;
import org.SimulationView;
public class MainPoolGUI {
     private static final int NUM_BODIES = 20;
    private static final int MAX_STEPS = 5000;
    private static final double DT = 0.01;
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors() + 1;
    public static void main(String[] args) {
        NBodySimulator simulation = new PoolSimulation();
        SimulationView view = new SimulationView(800, 800, simulation);
        simulation.setView(view);
        simulation.initRandom(NUM_BODIES);
        simulation.execute(NUM_THREADS, MAX_STEPS, DT);
        view.shutdown();
    }
    private static void log(String message) {
        System.out.println(message);
    }
}
