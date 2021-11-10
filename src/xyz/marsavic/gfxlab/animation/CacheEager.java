package xyz.marsavic.gfxlab.animation;


import xyz.marsavic.functions.interfaces.Action1;

/**
 * Thread safe
 */
public class CacheEager<F> extends Animation<F> {
	
	private final Animation<F> source;
	private final AnimationData<F> data;
	
	private final Action1<EventAnimationUpdate> actionSourceUpdated = this::sourceUpdated;
	
	
	
	public CacheEager(Animation<F> source) {
		this.source = source;
		data = new AnimationData<>(source.nFrames());
		
		source.onAnimationUpdate().add(actionSourceUpdated);
	}
	
	
	@Override
	public void dispose() {
		source.onAnimationUpdate().remove(actionSourceUpdated);
	}
	
	
	private void frameUpdated(int iFrame) {
		F frame = source.frame(iFrame);
		data.setFrame(iFrame, frame);
		dispatcherAnimationUpdate.fire(new EventAnimationUpdate(iFrame));
	}
	
	
	private void sourceUpdated(EventAnimationUpdate e) {
		for (int iFrame = e.iFrameFrom(); iFrame < e.iFrameTo(); iFrame++) {
			frameUpdated(iFrame);
		}
	}
	
	
	@Override
	public int nFrames() {
		return data.nFrames();
	}
	
	
	@Override
	public F frame(int iFrame) {
		return data.frame(iFrame);
	}
	
}
