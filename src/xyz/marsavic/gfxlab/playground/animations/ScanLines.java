package xyz.marsavic.gfxlab.playground.animations;

import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.Matrix;
import xyz.marsavic.gfxlab.animation.Animation;


public class ScanLines extends Animation<Matrix<Color>> {
	
	private final int d;
	
	
	
	public ScanLines(int d) {
		this.d = d;
	}
	
	
	@Override
	public int nFrames() {
		return d;
	}
	
	
	@Override
	public Matrix<Color> frame(int iFrame) {
		Matrix<Color> frame = Matrix.createBlack(Vector.xy(d, d));
		for (int i = 0; i < d; i++) {
			frame.set(iFrame, i, Color.WHITE);
			frame.set(i, iFrame, Color.WHITE);
		}
		return frame;
	}
}
