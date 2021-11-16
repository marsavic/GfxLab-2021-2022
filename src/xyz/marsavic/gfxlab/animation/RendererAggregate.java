package xyz.marsavic.gfxlab.animation;

import xyz.marsavic.functions.interfaces.Action1;
import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.Matrix;
import xyz.marsavic.gfxlab.gui.UtilsGL;


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
		start();
	}
	
	
	public RendererAggregate(AnimationColorSampling source) {
		this(source, Integer.MAX_VALUE);
	}
	
	
	public AnimationColorSampling source() {
		return source;
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
		
		UtilsGL.parallelY(sizeFrame, y -> {
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
		// TODO Why am I making frameAggregateNew and not writing directly to frameAggregate?
		//  I remember introducing this variable for purpose...
		//  (Better?) alternatives:
		//  - Locking
		//  - Buffering
		//  - Nothing, write to the same matrix (is anything really needed?)
		
		UtilsGL.parallelY(sizeFrame, y -> {
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
		// TODO this is a short-circuit in RendererIterable.
		for (int iFrame = 0; iFrame < nFrames(); iFrame++) {
			iterateFrame(iFrame);
		}
	}
	
}
