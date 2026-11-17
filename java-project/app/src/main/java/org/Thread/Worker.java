package org.Thread;

import java.util.List;

import org.Body;
import org.InfiniteForceException;

public class Worker extends Thread {
    private final List<Body> allBodies;
    private final List<Body> assignedBodies;
    private final Monitor monitor;

    public Worker(Monitor monitor, List<Body> assignedBodies, List<Body> allBodies) {
        this.allBodies = allBodies;
        this.assignedBodies = assignedBodies;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        double dt = monitor.getDt();
        int maxSteps = monitor.getMaxSteps();
        int currentStep = monitor.getCurrentStep();
        while (currentStep < maxSteps) {
            if (monitor.isPaused()) {
                continue; // Skip the rest of the loop and check the isPaused flag again
            }
            for (Body body : assignedBodies) {
                try {
                    body.calculateTotalForce(allBodies, dt);
                } catch (InfiniteForceException e) {
                    e.printStackTrace();
                }
            }
            monitor.waitAllForceCalculations();
            //log("Worker " + Thread.currentThread().threadId() + " completed force calculations for step " + currentStep);
            for (Body body : assignedBodies) {
                body.update(dt);
            }
            monitor.waitAllUpdating();
            
            currentStep = monitor.getCurrentStep();
        }
    }

    private synchronized void log(String msg) {
        System.out.println("[" + System.currentTimeMillis() + "] " + msg);
    }

}
