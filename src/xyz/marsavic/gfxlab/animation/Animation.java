package xyz.marsavic.gfxlab.animation;


import xyz.marsavic.gfxlab.gui.UtilsGL;
import xyz.marsavic.reactions.Dispatcher;
import xyz.marsavic.reactions.Reactions;
import xyz.marsavic.time.Profiler;


/**
 * Thread-safe (implementations should be, too)
 */
public abstract class Animation<F> {
	
	public abstract int nFrames();
	
	public abstract F frame(int iFrame);
	
	public void dispose() {}
	

	protected final Dispatcher<EventAnimationUpdate> dispatcherAnimationUpdate = new Dispatcher<>();
	
	/** If you now ask me for these frames, my answer will be different from the last one. */
	public final Reactions<EventAnimationUpdate> onAnimationUpdate() {
		return dispatcherAnimationUpdate.reactions();
	}
	
}
