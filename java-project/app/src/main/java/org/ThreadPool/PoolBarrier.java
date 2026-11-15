package org.ThreadPool;
import java.util.List;
import java.util.Optional;
import org.*;

public class PoolBarrier {
    private int forcesCalculated;
    private int updatesCompleted;
    private final List<Body> bodies;
    public PoolBarrier(List<Body> bodies) {
        this.bodies = bodies;  
    }

    public synchronized void notifyUpdating(){
        updatesCompleted++;
        if (updatesCompleted == bodies.size()) {
            forcesCalculated = 0;
            notifyAll();
        }
    }

    public synchronized void notifyForcesCalculated() {
        forcesCalculated++;
        if (forcesCalculated == bodies.size()) {
            updatesCompleted = 0;
            notifyAll();
        }
    }

    public synchronized void waitAllUpdating(){
        while (updatesCompleted < bodies.size()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
    public synchronized void waitAllForcesCalculated() {
        while (forcesCalculated < bodies.size()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
