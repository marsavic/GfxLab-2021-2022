package xyz.marsavic.gfxlab.animation;


import xyz.marsavic.functions.interfaces.Action1;

import java.util.Arrays;

/**
 * Thread safe
 */
public class CacheLazy<F> extends Animation<F> {
	
	private final Animation<F> source;
	private final AnimationData<F> data;
	
	private final boolean[] dirtyFrames;

	private final Action1<EventAnimationUpdate> actionSourceUpdated = this::sourceUpdated;
	
	
	
	public CacheLazy(Animation<F> source) {
		this.source = source;
		data = new AnimationData<>(source.nFrames());

		dirtyFrames = new boolean[nFrames()];
		Arrays.fill(dirtyFrames, true);
		
		source.onAnimationUpdate().add(actionSourceUpdated);
	}
	
	
	@Override
	public void dispose() {
		source.onAnimationUpdate().remove(actionSourceUpdated);
	}
	
	
	@Override
	public int nFrames() {
		return data.nFrames();
	}
	
	
	@Override
	public F frame(int iFrame) {
		if (dirtyFrames[iFrame]) {
			F frame = source.frame(iFrame);
			data.setFrame(iFrame, frame);
			dirtyFrames[iFrame] = false;
		}
		return data.frame(iFrame);
	}
	
	
	private void frameUpdated(int iFrame) {
		dirtyFrames[iFrame] = true;
	}
	
	
	private void sourceUpdated(EventAnimationUpdate e) {
		for (int iFrame = e.iFrameFrom(); iFrame < e.iFrameTo(); iFrame++) {
			frameUpdated(iFrame);
		}
		dispatcherAnimationUpdate.fire(e);
	}
	
}
