package xyz.marsavic.gfxlab.animation;


import xyz.marsavic.reactions.values.ReactiveValue;
import xyz.marsavic.reactions.values.ReactiveVar;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;


public abstract class Renderer<F> extends Animation<F> {

	/** Pause and resume should be overridden if the renderer is a perpetual renderer (vs reactive-only renderer).
	 * Both methods must return immediately */
	public void pause() {
		rPaused.set(true);
	}
	
	/** Pause and resume should be overridden if the renderer is a perpetual renderer (vs reactive-only renderer).
	 * Both methods must return immediately */
	public void resume() {
		rPaused.set(false);
	}

	
	/** Stops the renderer. After the stop method is called, resuming will have no effect. */
	public void stop() {
		rFinished.set(true);
	}
	
	
	@Override
	public void dispose() {
		stop();
	}
	
	
	// TODO: Put a new abstract class, RendererProcess between Renderer and RendererIterable

	
	protected final ReactiveVar<Boolean> rPaused      = new ReactiveVar<>(false);
	protected final ReactiveVar<Boolean> rFinished    = new ReactiveVar<>(false);
	
	public final ReactiveValue<Boolean> paused      = rPaused  .asReadOnly();
	public final ReactiveValue<Boolean> finished    = rFinished.asReadOnly();
	
}
