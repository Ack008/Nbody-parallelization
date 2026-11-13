package org;

public class Barrier {
    private double dt;
    private int numThreads;
    private int currentStep;
    private int maxSteps;
    private int threadsDone;
    public Barrier(int numThreads, int maxSteps, double dt) {
        this.numThreads = numThreads;
        this.maxSteps = maxSteps;
        this.currentStep = 0;
        this.dt = dt;
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
    
    public synchronized void waitAllDone() {
        threadsDone++;
        if (threadsDone == numThreads) {
            threadsDone = 0;
            currentStep++;
            notifyAll();
        } else {
            while (threadsDone < numThreads) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();    
                }
            }
        }
    }

}
