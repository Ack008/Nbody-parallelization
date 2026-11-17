package org.Thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.Body;
import org.NBodySimulator;
import org.P2d;
import org.SimulationView;
import org.V2d;

import java.util.Optional;
public class ThreadNBodySimulation implements NBodySimulator {
    private Optional<SimulationView> view;

	/* bodies in the field */
	List<Body> bodies;

	/* virtual time */
	private double vt;

	/* virtual time step */
	double dt;
	private int numThreads;
	private int maxSteps;
	private Monitor monitor;

	public ThreadNBodySimulation(SimulationView view) {
		this.view = Optional.of(view);
	}
	
	public ThreadNBodySimulation() {
		this.view = Optional.empty();
	}
	
	public void init(ArrayList<Body> bodies) {
		this.bodies = bodies;
	}

	public void initRandom(int nBodies) {
		Random rand = new Random(System.currentTimeMillis());
		bodies = new ArrayList<Body>();
		for (int i = 0; i < nBodies; i++) {
			double x = -1.0 + 2.0*rand.nextDouble();
			double y = -1.0 + 2.0*rand.nextDouble();
			double vx = -0.1 + 0.2*rand.nextDouble();
			double vy = -0.1 + 0.2*rand.nextDouble();
			double m = 1 + rand.nextDouble()*9;
			Body b = new Body(i, new P2d(x, y), new V2d(vx, vy), 0.01, m);
			bodies.add(b);
		}
	}
    private List<Worker> getWorkers(int numThreads, Monitor barrier) {
        List<Worker> workers = new ArrayList<>();
        int bodiesPerThread = bodies.size() / numThreads;
        for (int i = 0; i < numThreads; i++) {
            int startIdx = i * bodiesPerThread;
            int endIdx = (i == numThreads - 1) ? bodies.size() : startIdx + bodiesPerThread;
            List<Body> assignedBodies = bodies.subList(startIdx, endIdx);
            Worker worker = new Worker(barrier, assignedBodies, bodies);
            workers.add(worker);
        }
        return workers;
    }
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
    public void execute(int numThreads, int maxSteps, double dt) {
		if (view.isPresent()) {
			view.get().init();
		}
		monitor = new Monitor(bodies, numThreads, maxSteps, dt, view);
		monitor.setPaused(false); // Start the simulation in a running stateq
        List<Worker> workers = getWorkers(numThreads, monitor);
        workers.forEach(i -> i.start());
        for (Worker worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
	public void setView(SimulationView view) {
		this.view = Optional.of(view);
	}
	public void start() {
		monitor.setPaused(false);
    }
    public void stop() {
        monitor.setPaused(true);
    }
    public void nextStep() {
		monitor.oneStep();

    }

}
