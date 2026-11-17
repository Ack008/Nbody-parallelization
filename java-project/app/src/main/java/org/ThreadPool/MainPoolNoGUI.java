package org.ThreadPool;
import org.NBodySimulator;
import org.SimulationView;
public class MainPoolNoGUI {
    private static final int NUM_BODIES = 500;
    private static final int MAX_STEPS = 5000;
    private static final double DT = 0.01;
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors() + 1;
    public static void main(String[] args) {
        NBodySimulator simulation = new PoolSimulation();
        simulation.initRandom(NUM_BODIES);
        log("Starting simulation with " + NUM_BODIES + " bodies "  + NUM_THREADS + " threads and " + MAX_STEPS + " steps");
        long startTime = System.currentTimeMillis();
        simulation.execute(NUM_THREADS, MAX_STEPS, DT);
        long endTime = System.currentTimeMillis();
        log("Simulation completed in " + (endTime - startTime) + " ms");
    }
    private static void log(String message) {
        System.out.println(message);
    }
}
