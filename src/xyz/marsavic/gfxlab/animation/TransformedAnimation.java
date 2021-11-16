package xyz.marsavic.gfxlab.animation;

import xyz.marsavic.functions.interfaces.Action1;
import xyz.marsavic.functions.interfaces.Function1;
import xyz.marsavic.time.Profiler;
import xyz.marsavic.time.ProfilerPool;


public class TransformedAnimation<F, S> extends Animation<F> {
	
	private final Animation<S> source;
	private final Function1<Animation<F>, Animation<S>> f;
	
	private final Action1<EventAnimationUpdate> actionSourceUpdated = this::sourceUpdated;
	
	
	
	public TransformedAnimation(Animation<S> source, Function1<Animation<F>, Animation<S>> f) {
		this.source = source;
		this.f = f;
		source.onAnimationUpdate().add(actionSourceUpdated);
	}
	
	
	@Override
	public void dispose() {
		source.onAnimationUpdate().remove(actionSourceUpdated);
	}
	
	
	
	@Override
	public int nFrames() {
		return f.at(source).nFrames();
	}
	
	
	@Override
	public F frame(int iFrame) {
		return f.at(source).frame(iFrame);
	}
	
	
	public void sourceUpdated(EventAnimationUpdate e) {
		dispatcherAnimationUpdate.fire(new EventAnimationUpdate(this));
	}
	
}
