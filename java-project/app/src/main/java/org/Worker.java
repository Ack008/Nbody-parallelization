package org;
import java.util.List;
public class Worker extends Thread {
    private final List<Body> allBodies;
    private final List<Body> assignedBodies;
    private final Barrier barrier;
    public Worker(Barrier barrier, List<Body> assignedBodies ,List<Body> allBodies) {
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
            barrier.waitAllDone();
            for (Body body : assignedBodies) {
                body.update(dt);
            }
        }
    }

}
