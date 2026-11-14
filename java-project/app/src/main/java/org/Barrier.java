package org;

public class Barrier {
    private final double dt;
    private final int numThreads;
    private int currentStep;
    private final int maxSteps;
    private int forcesCalculated;
    private int updatesCompleted;

    public Barrier(int numThreads, int maxSteps, double dt) {
        this.numThreads = numThreads;
        this.maxSteps = maxSteps;
        this.currentStep = 0;
        this.dt = dt;
        this.updatesCompleted = 0;
        this.forcesCalculated = 0;
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
