package org.ThreadPool;
import org.NBodySimulator;
public class JpfSim {
    private static final int NUM_BODIES = 4;
    private static final int MAX_STEPS = 5000;
    private static final double DT = 0.01;
    private static final int NUM_THREADS = 2;
    public static void main(String[] args) {
        NBodySimulator simulation = new PoolSimulation();
        simulation.initFixed(NUM_BODIES);
        simulation.execute(NUM_THREADS, MAX_STEPS, DT);
    }
    private static void log(String message) {
        System.out.println(message);
    }
}
