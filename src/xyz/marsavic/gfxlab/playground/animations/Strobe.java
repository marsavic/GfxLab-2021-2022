package xyz.marsavic.gfxlab.playground.animations;

import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.Matrix;
import xyz.marsavic.gfxlab.animation.EventAnimationUpdate;
import xyz.marsavic.gfxlab.animation.RendererIterable;


public class Strobe extends RendererIterable<Matrix<Color>> {
	
	private final Vector size;
	
	
	public Strobe(Vector size) {
		this.size = size;
		iterate();
	}
	
	
	@Override
	public int nFrames() {
		return 1;
	}
	
	
	@Override
	protected boolean finished() {
		return false;
	}
	
	
	private Matrix<Color> readyFrame;
	
	
	@Override
	protected void iterate() {
		readyFrame = new Matrix<>(size, (nIterations() & 1) == 1 ? Color.BLACK : Color.WHITE);
		dispatcherAnimationUpdate.fire(new EventAnimationUpdate(0));
	}
	
	
	@Override
	public Matrix<Color> frame(int iFrame) {
		return readyFrame;
	}
	
}
