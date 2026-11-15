package org.Thread;

import java.util.List;

import org.Body;
import org.InfiniteForceException;

public class Worker extends Thread {
    private final List<Body> allBodies;
    private final List<Body> assignedBodies;
    private final Barrier barrier;

    public Worker(Barrier barrier, List<Body> assignedBodies, List<Body> allBodies) {
        this.allBodies = allBodies;
        this.assignedBodies = assignedBodies;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        double dt = barrier.getDt();
        int maxSteps = barrier.getMaxSteps();
        int currentStep = barrier.getCurrentStep();
        while (currentStep < maxSteps) {
            for (Body body : assignedBodies) {
                try {
                    body.calculateTotalForce(allBodies, dt);
                } catch (InfiniteForceException e) {
                    e.printStackTrace();
                }
            }
            barrier.waitAllForceCalculations();
            //log("Worker " + Thread.currentThread().threadId() + " completed force calculations for step " + currentStep);
            for (Body body : assignedBodies) {
                body.update(dt);
            }
            barrier.waitAllUpdating();
            currentStep = barrier.getCurrentStep();
        }
    }

    private synchronized void log(String msg) {
        System.out.println("[" + System.currentTimeMillis() + "] " + msg);
    }

}
