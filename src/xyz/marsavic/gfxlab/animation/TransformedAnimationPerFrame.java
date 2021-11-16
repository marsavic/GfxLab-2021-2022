package xyz.marsavic.gfxlab.animation;

import xyz.marsavic.functions.interfaces.Action1;
import xyz.marsavic.functions.interfaces.Function1;
import xyz.marsavic.gfxlab.gui.UtilsGL;
import xyz.marsavic.time.Profiler;


public class TransformedAnimationPerFrame<F, S> extends Animation<F> {
	
	private final Animation<S> source;
	private final Function1<F, S> transformation;  // TODO should this be reactive?
	private final boolean cacheLastFrame;
	private final Action1<EventAnimationUpdate> actionSourceUpdated = this::sourceUpdated;
//	private final Action1<EventValueChange<Function1<F, S>>> actionTransformationChanged = this::transformationChanged;
	
	private final Profiler profiler = UtilsGL.profiler(this, "frame compute");
	
	
	private static record IndexedFrame<F> (
			int index,
			F frame
	) {}
	
	private final IndexedFrame<F> invalidCache = new IndexedFrame<>(-1, null);
	
	private IndexedFrame<F> cached = invalidCache;
	

	
	public TransformedAnimationPerFrame(Animation<S> source, Function1<F, S> transformation, boolean cacheLastFrame) {
		this.source = source;
		this.transformation = transformation;
		this.cacheLastFrame = cacheLastFrame;
		
		source.onAnimationUpdate().add(actionSourceUpdated);
//		transformation.onChange().add(actionTransformationChanged);
	}
	
	//** cacheLastFrame = false */
	public TransformedAnimationPerFrame(Animation<S> source, Function1<F, S> transformation) {
		this(source, transformation, false);
	}
	
	//** transformation = toIdentity, cacheLastFrame = false */
	public static <F> TransformedAnimationPerFrame<F, F> cachedLastFrame(Animation<F> source) {
		return new TransformedAnimationPerFrame<>(source, f -> f);
	}
	
	public Animation<S> source() {
		return source;
	}
	
	public Function1<F, S> transformation() {
		return transformation;
	}

	public boolean cacheLastFrame() {
		return cacheLastFrame;
	}
	
	
	@Override
	public void dispose() {
		source.onAnimationUpdate().remove(actionSourceUpdated);
//		transformation.onChange().remove(actionTransformationChanged);
	}
	
	
	@Override
	public int nFrames() {
		return source.nFrames();
	}
	
	private final Object lock = new Object();
	
	@Override
	public F frame(int iFrame) {
		if (cacheLastFrame) {
			IndexedFrame<F> toShow = null;
			
			synchronized (lock) {
				if (cached.index() == iFrame) {
					toShow = cached;
				}
			}
			if (toShow == null) {
				profiler.enter();
				toShow = new IndexedFrame<>(iFrame, transformation.at(source.frame(iFrame)));
				profiler.exit();
				cached = toShow;
			}
			return toShow.frame();
		} else {
			profiler.enter();
			F frame = transformation.at(source.frame(iFrame));
			profiler.exit();
			return frame;
		}
	}
	
	// TODO the event will come immediately after we invoke frame above, making caching useless!
	private void sourceUpdated(EventAnimationUpdate e) {
		synchronized (lock) {
			if (e.includesFrame(cached.index())) {
				cached = invalidCache;
			}
		}
		dispatcherAnimationUpdate.fire(e);
	}
	
	
//	private void transformationChanged(EventValueChange<Function1<F, S>> event) {
//	}
	
}
