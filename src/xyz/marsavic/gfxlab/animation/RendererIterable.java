package xyz.marsavic.gfxlab.animation;

import xyz.marsavic.gfxlab.gui.UtilsGL;
import xyz.marsavic.time.Profiler;


public abstract class RendererIterable<F> extends Renderer<F> {
	
	protected abstract void iterate();
	protected abstract boolean finished();

	
	private final Profiler profilerIteration = UtilsGL.profiler(this, "iteration");
	
	private int nIterations = 0;
	
	private boolean pauseRequested = false;
	private boolean stopRequested = false;
	
	private final Object pauseLock = new Object();
	
	
	/** Call at the end of the constructor of the implementing class. */
	protected void start() {
		Runnable iterations = () -> {
			while (!finished.get()) {
				synchronized (pauseLock) {
					while (pauseRequested && !stopRequested) {
						rPaused.set(true);
						try {
							pauseLock.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if (stopRequested) {
						break;
					}
					rPaused.set(false);
				}
				profilerIteration.enter();
				iterate();
				profilerIteration.exit();
				
				nIterations++;
			}
			rPaused.set(true);
			rFinished.set(true);
		};
		
		Thread thread = new Thread(iterations);
		thread.setDaemon(true);
		thread.start();
	}
	
	
	public final int nIterations() {
		return nIterations;
	}
	
	
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
	
}
