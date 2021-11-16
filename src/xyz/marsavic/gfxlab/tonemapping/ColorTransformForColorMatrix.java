package xyz.marsavic.gfxlab.tonemapping;

import xyz.marsavic.functions.interfaces.Function1;
import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.Matrix;
import xyz.marsavic.objectinstruments.annotations.GadgetDouble;


public interface ColorTransformForColorMatrix extends Function1<ColorTransform, Matrix<Color>> {
	
	// TODO "auto-whatever" color filters here.
	
	
	
	
	class Multiply implements ColorTransformForColorMatrix {
		@GadgetDouble(p = 0, q = 100)
		public double multiplier = 1.0;
		
		public Multiply(double multiplier) {
			this.multiplier = multiplier;
		}
		
		@Override
		public ColorTransform at(Matrix<Color> matrixColor) {
			double multiplierCopy = multiplier;
			return c -> c.mul(multiplierCopy);
		}
	}
	
}
