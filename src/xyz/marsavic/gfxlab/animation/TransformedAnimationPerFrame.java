package xyz.marsavic.gfxlab.animation;

import xyz.marsavic.functions.interfaces.Action1;
import xyz.marsavic.functions.interfaces.Function1;
import xyz.marsavic.time.Profiler;
import xyz.marsavic.time.ProfilerPool;


public class TransformedAnimationPerFrame<F, S> extends Animation<F> {
	
	private final Animation<S> source;
	private final Function1<F, S> f;
	
	private final Action1<EventAnimationUpdate> actionSourceUpdated = this::sourceUpdated;
	
	private final Profiler profiler = ProfilerPool.get(this.getClass().getSimpleName());
	
	
	
	public TransformedAnimationPerFrame(Animation<S> source, Function1<F, S> f) {
		this.source = source;
		this.f = f;
		
		source.onAnimationUpdate().add(actionSourceUpdated);
	}
	
	
	public void dispose() {
		source.onAnimationUpdate().remove(actionSourceUpdated);
	}
	
	
	public Animation<S> source() {
		return source;
	}
	
	
	public Function1<F, S> f() {
		return f;
	}
	
	
	@Override
	public int nFrames() {
		return source.nFrames();
	}
	
	
	@Override
	public F frame(int iFrame) {
		S input = source.frame(iFrame);
		profiler.enter();
		F output = f.at(input);
		profiler.exit();
		return output;
	}
	
	
	public void sourceUpdated(EventAnimationUpdate e) {
		dispatcherAnimationUpdate.fire(e);
	}
	
}
