package xyz.marsavic.gfxlab.animation;


import xyz.marsavic.reactions.values.ReactiveValue;
import xyz.marsavic.reactions.values.ReactiveVar;


public abstract class Renderer<F> extends Animation<F> {

	/** It should be assumed that the renderer is paused until resume method is called. Until then, running method
	 * should return false. Implementations must make sure that immediately after returning from init method resumo
	 * method can be called */
	protected void init() {}
	
	/** Pause and resume should be overridden if the renderer is a perpetual renderer (vs reactive-only renderer).
	 * Both methods must return immediately */
	public void pause() {
		rRunning.set(false);
	}
	
	/** Pause and resume should be overridden if the renderer is a perpetual renderer (vs reactive-only renderer).
	 * Both methods must return immediately */
	public void resume() {
		rRunning.set(true);
	}
	
	
	@Override
	public void dispose() {
		stop();
	}
	
	
	// TODO: Put a new abstract class, RendererProcess between Renderer and RendererIterable

	
	protected final ReactiveVar<Boolean> rInitialized = new ReactiveVar<>(false);
	protected final ReactiveVar<Boolean> rRunning     = new ReactiveVar<>(false);
	protected final ReactiveVar<Boolean> rFinished    = new ReactiveVar<>(false);
	
	public final ReactiveValue<Boolean> initialized = rInitialized.asReadOnly();
	public final ReactiveValue<Boolean> running     = rRunning    .asReadOnly();
	public final ReactiveValue<Boolean> finished    = rFinished   .asReadOnly();
	
	
	
	
	public final void start(boolean runImmediately) {
		init();
		rInitialized.set(true);
		if (runImmediately) {
			resume();
		}
	}
	
	
	public final void start() {
		start(true);
	}
	

	public void stop() {
		rFinished.set(true);
	}
	
}
