package org;
import java.util.List;
public class Body {
    private static final double G_CONST_SCALED = 0.001;
    private P2d pos;
    private V2d vel;
    private double mass;
    private double radius;
    private int id;

    public Body(int id, P2d pos, V2d vel, double radius, double mass) {
        this.id = id;
        this.pos = pos;
        this.vel = vel;
        this.radius = radius;
        this.mass = mass;
    }

    public synchronized double getMass() {
        return mass;
    }

    public synchronized P2d getPos() {
        return pos;
    }

    public synchronized V2d getVel() {
        return vel;
    }

    public synchronized int getId() {
        return id;
    }

    public synchronized double getRadius() {
        return radius;
    }

    public synchronized boolean equals(Object b) {
        return ((Body) b).id == id;
    }
    public synchronized void calculateTotalForce(List<Body> bodies) throws InfiniteForceException {
        V2d totalForce = new V2d(0, 0);
        for (Body b : bodies) {
            if (!b.equals(this)) {
                totalForce.sum(computeGForceBy(b));
            }
        }
        // Do something with the total force, e.g., update the body's velocity
        updateVelocity(totalForce, 1.0);
    }
    public synchronized void update(double dt) {
        updatePos(dt);
    }
    /**
     * Update the position, given the instant velocity
     * 
     * @param dt time elapsed
     */
    private void updatePos(double dt) {
        pos.sum(new V2d(vel).scalarMul(dt));
    }

    private void updateVelocity(V2d acc, double dt) {
        vel.sum(new V2d(acc).scalarMul(dt));
    }

    private double getDistanceFrom(Body b) {
        double dx = pos.getX() - b.getPos().getX();
        double dy = pos.getY() - b.getPos().getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    private V2d computeGForceBy(Body b) throws InfiniteForceException {
        double dist = getDistanceFrom(b);
        if (dist > 0) {
            try {
                return new V2d(pos, b.getPos())
                        .normalize()
                        .scalarMul(b.getMass() * G_CONST_SCALED / (dist * dist));
            } catch (Exception ex) {
                throw new InfiniteForceException();
            }
        } else {
            throw new InfiniteForceException();
        }
    }
}
