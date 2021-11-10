package xyz.marsavic.gfxlab.animation;

import xyz.marsavic.functions.interfaces.Action1;
import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.Matrix;
import xyz.marsavic.utils.Utils;


public class RendererAggregate extends RendererIterable<Matrix<Color>> {
	
	private final AnimationColorSampling source;
	
	private final int nSamplesPerPixel;

	private final AnimationData<Matrix<Color>> aggregate;

	private final Vector sizeFrame;
	private final int[] iterations;
	private final boolean[] waitingForAdvance;
	
	private final Action1<EventAnimationUpdate> actionSourceUpdated = this::sourceUpdated;
	
	
	
	public RendererAggregate(AnimationColorSampling source, int nSamplesPerPixel) {
		this.source = source;
		this.nSamplesPerPixel = nSamplesPerPixel;
		
		sizeFrame = source.sizeFrame();
		aggregate = new AnimationData<>(source.size(), Matrix::createBlack);
		iterations = new int[source.nFrames()];
		waitingForAdvance = new boolean[source.nFrames()];
		
		source.onAnimationUpdate().add(actionSourceUpdated);
	}
	
	
	public RendererAggregate(AnimationColorSampling source) {
		this(source, Integer.MAX_VALUE);
	}
	

	@Override
	public void dispose() {
		source.onAnimationUpdate().remove(actionSourceUpdated);
	}
	
	
	@Override
	public int nFrames() {
		return aggregate.nFrames();
	}
	
	
	@Override
	public Matrix<Color> frame(int iFrame) {
		Matrix<Color> frameAggregate = aggregate.frame(iFrame);
		Matrix<Color> frame = new Matrix<>(sizeFrame);
		
		int n = iterations[iFrame]; // TODO thread safety
		
		Utils.parallelY(sizeFrame, y -> {
			int sizeFrameX = sizeFrame.xInt();
			for (int x = 0; x < sizeFrameX; x++) {
				frame.set(x, y, frameAggregate.get(x, y).div(n));
			}
		});
		
		return frame;
	}
	
	
	private void sourceFrameUpdated(int iFrame) {
		Matrix<Color> frameSource = source.frame(iFrame);
		Matrix<Color> frameAggregate = aggregate.frame(iFrame);
		Matrix<Color> frameAggregateNew = new Matrix<>(sizeFrame);
		
		Utils.parallelY(sizeFrame, y -> {             // TODO parallel(sizeFrame, (y, maxX) -> {...})
			int sizeFrameX = sizeFrame.xInt();
			for (int x = 0; x < sizeFrameX; x++) {
				Color cPrev = frameAggregate.get(x, y);
				Color cAdd = frameSource.get(x, y);
				frameAggregateNew.set(x, y, cPrev.add(cAdd));
			}
		});

		aggregate.setFrame(iFrame, frameAggregateNew); // TODO thread safety
		iterations[iFrame]++;
		waitingForAdvance[iFrame] = false;
		dispatcherAnimationUpdate.fire(new EventAnimationUpdate(iFrame));
	}
	
	
	private void sourceUpdated(EventAnimationUpdate e) {
		for (int iFrame = e.iFrameFrom(); iFrame < e.iFrameTo(); iFrame++) {
			sourceFrameUpdated(iFrame);
		}
	}

	
	private void iterateFrame(int iFrame) {
		if (!waitingForAdvance[iFrame]) {
			waitingForAdvance[iFrame] = true;
			source.advance(iFrame);
		}
	}

	
	@Override
	public boolean finished() {
		return nIterations() >= nSamplesPerPixel;
	}
	
	
	@Override
	public void iterate() {
		for (int iFrame = 0; iFrame < nFrames(); iFrame++) {
			iterateFrame(iFrame);
		}
	}
	
}
