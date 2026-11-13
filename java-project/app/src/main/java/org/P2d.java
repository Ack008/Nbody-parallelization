package org;

/**
 * 
 * A point in a 2D space
 * 
 */
public class P2d {

    private double x, y;

    public P2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public synchronized P2d sum(V2d v) {
        x += v.getX();
        y += v.getY();
        return this;
    }

    public synchronized void change(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public synchronized double getX() {
        return x;
    }

    public synchronized double getY() {
        return y;
    }

    public synchronized String toString() {
        return "(x: " + x + ", y: " + y + ")";
    }
}
