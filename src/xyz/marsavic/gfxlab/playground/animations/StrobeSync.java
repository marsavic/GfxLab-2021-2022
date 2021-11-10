package xyz.marsavic.gfxlab.playground.animations;

import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.Matrix;
import xyz.marsavic.gfxlab.animation.Animation;

import java.util.ArrayList;
import java.util.List;


public class StrobeSync extends Animation<Matrix<Color>> {
	
	private final List<Matrix<Color>> frames = new ArrayList<>(2);
	
	
	
	public StrobeSync(Vector size) {
		frames.add(new Matrix<>(size, Color.BLACK));
		frames.add(new Matrix<>(size, Color.WHITE));
	}
	
	
	@Override
	public int nFrames() {
		return 2;
	}

	
	@Override
	public Matrix<Color> frame(int iFrame) {
		return frames.get(iFrame);
	}
	
}
