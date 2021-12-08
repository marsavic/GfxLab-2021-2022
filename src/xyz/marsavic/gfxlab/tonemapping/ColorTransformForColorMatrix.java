package xyz.marsavic.gfxlab.tonemapping;

import xyz.marsavic.functions.interfaces.Function1;
import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.Matrix;
import xyz.marsavic.gfxlab.gui.UtilsGL;
import xyz.marsavic.objectinstruments.annotations.GadgetDoubleExponential;


public interface ColorTransformForColorMatrix extends Function1<ColorTransform, Matrix<Color>> {
	
	// TODO "auto-whatever" color filters here.
	
	
	
	
	public static class Multiply implements ColorTransformForColorMatrix {
		@GadgetDoubleExponential(p = 0x1p-16, q = 0x1p+16)
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
	
	
	public static class AutoSoft implements ColorTransformForColorMatrix {
		
		@GadgetDoubleExponential(p = 0x1p-16, q = 0x1p+16)
		public double preFactor = 0x1p-3;
		
		@GadgetDoubleExponential(p = 0x1p-4, q = 0x1p+4)
		public double power = 1.0;
		
		
		
		public AutoSoft() {
		}
		
		
		public AutoSoft(double preFactor, double power) {
			this.preFactor = preFactor;
			this.power = power;
		}
		
		
		private double lFactor(double lSrc) {
			double lPre = lSrc * preFactor;
			double lDst = 1 - 1 / (1 + Math.pow(lPre, power));
			
			double f = lDst / lSrc;
			if (Double.isNaN(f)) {
				f = 0;
			}
			return f;
		}
		
		
		@Override
		public ColorTransform at(Matrix<Color> colorMatrix) {
			Vector size = colorMatrix.size();
			
			double[] maxY = new double[size.xInt()];
			
			UtilsGL.parallelY(size, y -> {
				maxY[y] = Double.NEGATIVE_INFINITY;
				for (int x = 0; x < size.xInt(); x++) {
					Color c = colorMatrix.get(x, y);
					Color result = c.mul(lFactor(c.luminance()));
					maxY[y] = Math.max(maxY[y], result.max());
				}
			});
			
			// TODO Replace with fork-join task.
			
			double max = Double.NEGATIVE_INFINITY;
			for (int y = 0; y < size.yInt(); y++) {
				max = Math.max(max, maxY[y]);
			}
			
			double max_ = max;

			// TODO fix tearing because direct references to non-final class fields.
			return c -> c.mul(lFactor(c.luminance()) / max_);
		}
	}
}
