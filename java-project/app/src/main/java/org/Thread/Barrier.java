package org.Thread;
import java.util.List;
import java.util.Optional;

import org.Body;
import org.SimulationView;
public class Barrier {
    private final double dt;
    private final int numThreads;
    private int currentStep;
    private final int maxSteps;
    private int forcesCalculated;
    private int updatesCompleted;
    private final Optional<SimulationView> view;

    private final List<Body> bodies;


    public Barrier(List<Body> bodies, int numThreads, int maxSteps, double dt) {
        this(bodies, numThreads, maxSteps, dt, Optional.empty());
    }
    public Barrier(List<Body> bodies, int numThreads, int maxSteps, double dt, Optional<SimulationView> view) {
        this.numThreads = numThreads;
        this.maxSteps = maxSteps;
        this.currentStep = 0;
        this.dt = dt;
        this.updatesCompleted = 0;
        this.forcesCalculated = 0;
        this.view = view;
        this.bodies = bodies;
    }

    public synchronized double getDt() {
        return dt;
    }

    public synchronized int getCurrentStep() {
        return currentStep;
    }

    public synchronized int getMaxSteps() {
        return maxSteps;
    }
    public synchronized void waitAllUpdating(){
        updatesCompleted++;
        if (updatesCompleted == numThreads) {
            currentStep++;
            forcesCalculated = 0;
            if (view.isPresent()) {
                view.get().display(bodies, dt, currentStep);
            }
            notifyAll();
        } else {
            while (updatesCompleted < numThreads) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public synchronized void waitAllForceCalculations() {
        forcesCalculated++;
        if (forcesCalculated == numThreads) {
            updatesCompleted = 0;
            notifyAll();
        } else {
            while (forcesCalculated < numThreads) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
