package xyz.marsavic.gfxlab.animation;

import xyz.marsavic.reactions.Event;


public record EventAnimationUpdate(
		int iFrameFrom, // inclusive
		int iFrameTo    // exclusive
) implements Event {
	
	public EventAnimationUpdate(Animation<?> animation) {
		this(0, animation.nFrames());
	}
	
	public EventAnimationUpdate(int iFrame) {
		this(iFrame, iFrame + 1);
	}
	
	public boolean includesFrame(int i) {
		return !(i < iFrameFrom) && (i < iFrameTo);
	}
	
}
