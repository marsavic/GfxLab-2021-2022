package xyz.marsavic.gfxlab.animation;

import xyz.marsavic.gfxlab.playground.GfxLab;
import xyz.marsavic.time.Profiler;
import xyz.marsavic.time.ProfilerPool;


public abstract class RendererIterable<F> extends Renderer<F> {
	
	private final Profiler profiler = ProfilerPool.get(this.getClass().getSimpleName());
	
	
	protected abstract void iterate();
	
	protected abstract boolean finished();
	
	
	private int nIterations = 0;
	
	protected final int nIterations() {
		return nIterations;
	}
	
	
	private boolean pauseRequested = true;
	private boolean stopRequested = false;
	private final Object pauseLock = new Object();
	
	
	@Override
	public final void pause() {
		synchronized (pauseLock) {
			pauseRequested = true;
		}
	}
	
	
	@Override
	public final void resume() {
		synchronized (pauseLock) {
			pauseRequested = false;
			pauseLock.notifyAll();
		}
	}
	
	
	@Override
	public void stop() {
		synchronized (pauseLock) {
			stopRequested = true;
			pauseLock.notifyAll();
		}
	}
	
	
	@Override
	protected void init() {
		Runnable iterations = () -> {
			while (!finished.get()) {
				
				synchronized (pauseLock) {
					while (pauseRequested && !stopRequested) {
						rRunning.set(false);
						try {
							pauseLock.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if (stopRequested) {
						break;
					}
					rRunning.set(true);
				}
				
				profiler.enter();
				iterate();
				profiler.exit();
				
				nIterations++;
			}
			rRunning.set(false);
			rFinished.set(true);
		};
		
		GfxLab.pool.submit(iterations);
	}
	
	
	@Override
	public void dispose() {
		stop();
	}
}
