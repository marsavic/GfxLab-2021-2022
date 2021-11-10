package xyz.marsavic.gfxlab.tonemapping;

import xyz.marsavic.functions.interfaces.Function1;
import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.Matrix;


public interface ColorTransformForColorMatrix extends Function1<ColorTransform, Matrix<Color>> {
	
	// TODO "auto-whatever" color filters here.
	
	
	
	
	class Multiply implements ColorTransformForColorMatrix {
		public double multiplier = 1.0;
		
		@Override
		public ColorTransform at(Matrix<Color> matrixColor) {
			double multiplierCopy = multiplier;
			return c -> c.mul(multiplierCopy);
		}
	}
	
}
