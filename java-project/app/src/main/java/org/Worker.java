package org;
import java.util.List;
public class Worker extends Thread {
    private Body body;
    private List<Body> bodies;
    private double dt;

    public Worker(Body body, List<Body> bodies, double dt) {
        this.body = body;
        this.bodies = bodies;
        this.dt = dt;
    }

    @Override
    public void run() {
        try {
            body.calculateTotalForce(bodies);
            body.update(dt);
        } catch (InfiniteForceException e) {
            e.printStackTrace();
        }
    }
    
}
