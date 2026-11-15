package org.ThreadPool;


import org.Body;
import org.InfiniteForceException;
import org.NBodySimulator;
import org.P2d;
import org.SimulationView;
import org.V2d;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.List;
public class PoolSimulation implements NBodySimulator {
    private Optional<SimulationView> view;

	/* bodies in the field */
	List<Body> bodies;

	/* virtual time */
	private double vt;

	/* virtual time step */
	double dt;

    public PoolSimulation(SimulationView view) {
        this.view = Optional.of(view);
    }
    public PoolSimulation() {
        this.view = Optional.empty();
    }
    @Override
	public void initRandom(int nBodies) {
		Random rand = new Random(System.currentTimeMillis());
		bodies = new ArrayList<Body>();
		for (int i = 0; i < nBodies; i++) {
			double x = -1.0 + 2.0*rand.nextDouble();
			double y = -1.0 + 2.0*rand.nextDouble();
			double vx =0;
			double vy =0;
			double m = 1 + rand.nextDouble()*9;
			Body b = new Body(i, new P2d(x, y), new V2d(vx, vy), 0.01, m);
			bodies.add(b);
		}
	}
    @Override
    public void initFixed(int nBodies) {
        bodies = new ArrayList<Body>();
        for (int i = 0; i < nBodies; i++) {
            double x = -1.0 + 2.0 * i / nBodies;
            double y = -1.0 + 2.0 * i / nBodies;
            double vx = 0.0;
            double vy = 0.0;
            double m = 1 + i % 10;
            Body b = new Body(i, new P2d(x, y), new V2d(vx, vy), 0.01, m);
            bodies.add(b);
        }
	}
    @Override
    public void execute(int numThreads, int maxSteps, double dt) {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        PoolBarrier barrier = new PoolBarrier(bodies);
        List<Future<?>> futures = new ArrayList<>();
        int currentStep = 0;
        if (view.isPresent()) {
            view.get().init();
        }
        try{
            while (currentStep < maxSteps){
                calculateForces(dt, executor, barrier);
                updateBodies(dt, executor);
                if (view.isPresent()) {
                    view.get().display(bodies,dt, currentStep);
                }
                currentStep++;
            }
        } catch (Exception e) {
            System.err.println("Error occurred during simulation: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }
    private void updateBodies(double dt, ExecutorService executor) {
        List<Future<?>> futures = new ArrayList<>();
        for(Body b : bodies){
            futures.add(executor.submit(() -> {
                b.update(dt);
            }));
        }
        for(Future<?> future : futures){
            try {
                future.get();
            } catch (Exception e) {
                System.err.println("Error occurred while waiting for task completion: " + e.getMessage());
            }
        }
        futures.clear();
    }
    private void calculateForces(double dt, ExecutorService executor, PoolBarrier barrier) {
        List<Future<?>> futures = new ArrayList<>();
        for(Body b : bodies){
            futures.add(executor.submit(() -> {
                try {
                    b.calculateTotalForce(bodies, dt);
                    barrier.notifyForcesCalculated();
                } catch (InfiniteForceException e) {
                    System.err.println("Infinite force encountered for body " + b.getId());
                }
            }));
        }
        for(Future<?> future : futures){
            try {
                future.get();
            } catch (Exception e) {
                System.err.println("Error occurred while waiting for task completion: " + e.getMessage());
            }
        }
        futures.clear();
    }
}
