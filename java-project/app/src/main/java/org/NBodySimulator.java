package org;

/**
 * A simulator for the N-body problem.
 */
public interface NBodySimulator {
    /**
     * Initializes the simulation with a given number of bodies, randomly positioned and with random velocities.
     * @param numBodies The number of bodies to initialize in the simulation.
     */

    public void initRandom(int numBodies);
    /**
     * Initializes the simulation with a fixed number of bodies, positioned and configured in a predefined manner.
     * @param numBodies The number of bodies to initialize in the simulation.
     */
    public void initFixed(int numBodies);
    /**
     * Executes the simulation for a specified number of threads, maximum steps, and time step (dt).
     * @param numThreads The number of threads to use for the simulation.
     * @param maxSteps The maximum number of steps to simulate.
     * @param dt The time step for each simulation update.
     */
    public void execute(int numThreads, int maxSteps, double dt);

}
