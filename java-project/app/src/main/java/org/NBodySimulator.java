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
    /**
     * Sets the view for the simulation, allowing for visualization of the simulation's state.
     * @param view The SimulationView instance to be used for displaying the simulation.
     */
    public void setView(SimulationView view);
    /**
     * Starts the simulation, allowing it to run continuously until stopped.
     */
    public void start();
    /**
     * Stops the simulation, halting any ongoing updates and calculations.
     */
    public void stop();
    /**
     * Advances the simulation by one step.
     */
    public void nextStep();
    /**
     * Suspends the simulation, pausing any ongoing updates and calculations.
     */
    public void suspend();

}
