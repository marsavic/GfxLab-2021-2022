package xyz.marsavic.gfxlab.animation;


import xyz.marsavic.functions.interfaces.Action1;

/**
 * Thread safe
 */
public class CacheLastFrameLazy<F> extends Animation<F> {
	
	private final Animation<F> source;
	private final Action1<EventAnimationUpdate> actionSourceUpdated = this::sourceUpdated;
	
	
	static record IndexedFrame<F> (
			int index,
			F frame
	) {}
	
	private IndexedFrame<F> cached;
	
	
	public Animation<F> source() {
		return source;
	}
	
	
	public CacheLastFrameLazy(Animation<F> source) {
		this.source = source;
		source.onAnimationUpdate().add(actionSourceUpdated);
	}
	
	
	@Override
	public void dispose() {
		source.onAnimationUpdate().remove(actionSourceUpdated);
	}
	
	
	@Override
	public int nFrames() {
		return source.nFrames();
	}
	
	
	@Override
	public F frame(int iFrame) {
		if ((cached == null) || (cached.index() != iFrame)) {
			cached = new IndexedFrame<>(iFrame, source.frame(iFrame));
		}
		return cached.frame();    // Should "cached" be volatile? Or synchronize this somehow.
	}
	

	private void sourceUpdated(EventAnimationUpdate e) {
		dispatcherAnimationUpdate.fire(e);
	}
	
}
