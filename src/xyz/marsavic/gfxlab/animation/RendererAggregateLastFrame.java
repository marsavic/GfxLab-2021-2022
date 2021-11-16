package xyz.marsavic.gfxlab.animation;

import xyz.marsavic.functions.interfaces.Action1;
import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.Matrix;


public class RendererAggregateLastFrame extends RendererIterable<Matrix<Color>> {
	
	private static record Aggregate(
			Matrix<Color> frame,
			int iFrame,
			int count
	) {}
	

	private final AnimationColorSampling source;
	private final Action1<EventAnimationUpdate> actionSourceUpdated = this::sourceUpdated;
	
	private Aggregate aggregate = new Aggregate(null, -1, 0);
	
	
	
	public RendererAggregateLastFrame(AnimationColorSampling source) {
		this.source = source;
		source.onAnimationUpdate().add(actionSourceUpdated);
		start();
	}
	
	
	public AnimationColorSampling source() {
		return source;
	}
	
	
	@Override
	public void dispose() {
		source.onAnimationUpdate().remove(actionSourceUpdated);
		super.dispose();
	}
	
	
	@Override
	public int nFrames() {
		return source.nFrames();
	}
	
	
	private final Object monitorWaitForAdvanceRequest = new Object();
	private boolean waitingForAdvanceResult = false;
	private int iFrameLastAdvanceRequest = -1;
	private int iFrameNextAdvanceRequest = -1;
	
	private final Object monitorWaitForAdvanceResult = new Object();
	
	
	// -1 if requesting should stop
	private void setAdvanceRequest(int iFrame) {
		synchronized (monitorWaitForAdvanceRequest) {
			if (iFrame != iFrameLastAdvanceRequest) {
				iFrameNextAdvanceRequest = iFrame;
			
				if (iFrameNextAdvanceRequest != -1) {
					monitorWaitForAdvanceRequest.notifyAll();
				}
			}
		}
	}
	
	
	@Override
	public Matrix<Color> frame(int iFrame) {
		Aggregate aggregate_ = aggregate;
		
		if (iFrame != aggregate_.iFrame()) {
			aggregate_ = new Aggregate(source.frame(iFrame), iFrame, 1);
			aggregate = aggregate_;
			setAdvanceRequest(iFrame);
			return aggregate_.frame();
		} else {
			return (aggregate_.count() == 1) ?
					aggregate_.frame() :
					Matrix.mul(aggregate_.frame(), 1.0 / aggregate_.count());
		}
	}
	

	private void sourceFrameUpdated(int iFrame) {
		synchronized (monitorWaitForAdvanceResult) {
			if (iFrameLastAdvanceRequest == iFrame) {
				waitingForAdvanceResult = false;
				monitorWaitForAdvanceResult.notifyAll();
			}
		}
		
		Aggregate aggregate_ = aggregate;
		if (iFrame == aggregate_.iFrame()) {
			Matrix<Color> frameSource = source.frame(iFrame);
			Matrix<Color> frameSum = Matrix.add(aggregate_.frame(), frameSource);
			aggregate = new Aggregate(frameSum, iFrame, aggregate_.count() + 1);
		}
		
		dispatcherAnimationUpdate.fire(new EventAnimationUpdate(iFrame));
	}
	
	
	private void sourceUpdated(EventAnimationUpdate e) {
		for (int iFrame = e.iFrameFrom(); iFrame < e.iFrameTo(); iFrame++) {
			sourceFrameUpdated(iFrame);
		}
	}

	
	@Override
	public boolean finished() {
		return false;
	}
	
	
	@Override
	public void iterate() {
		
		// TODO looks like every time the frame index changes, the first frame is not aggregated.
		
		// TODO what happened: I changed the frame, and the result froze to the first frame (didn't aggregate at all)
		
		// TODO busy loop the moment after changing frames (doesn't wait)
		
		int iFrameToAdvance;
		
		synchronized (monitorWaitForAdvanceRequest) {
			if (iFrameNextAdvanceRequest == -1) {
				try {
					monitorWaitForAdvanceRequest.wait(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			iFrameToAdvance = iFrameLastAdvanceRequest = iFrameNextAdvanceRequest;
			waitingForAdvanceResult = true;
		}
		
		if (iFrameToAdvance != -1) {
			source.advance(iFrameToAdvance);
		}
		
		synchronized (monitorWaitForAdvanceResult) {
			if (waitingForAdvanceResult) {
				try {
					monitorWaitForAdvanceResult.wait(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
}
