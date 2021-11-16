package xyz.marsavic.gfxlab.tonemapping;

import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.Matrix;
import xyz.marsavic.gfxlab.RawImage;
import xyz.marsavic.gfxlab.ToneMappingFunction;
import xyz.marsavic.gfxlab.gui.UtilsGL;


public class ToneMappingFunctionSimple implements ToneMappingFunction {
	
	private final ColorTransformForColorMatrix colorTransformForColorMatrix;
	
	
	public ToneMappingFunctionSimple(ColorTransformForColorMatrix colorTransformForColorMatrix) {
		this.colorTransformForColorMatrix = colorTransformForColorMatrix;
	}
	
	
	public ColorTransformForColorMatrix colorTransformFactory() {
		return colorTransformForColorMatrix;
	}
	
	
	@Override
	public void apply(Matrix<Color> input, RawImage output) {
		ColorTransform f = colorTransformForColorMatrix.at(input);
		int w = output.width();
		
		UtilsGL.parallelY(input.size(), y -> {
			int o = y * w;
			for (int x = 0; x < w; x++) {
				output.pixels()[o + x] = f.at(input.get(x, y)).codeClamp();
			}
		});
	}
	
	
}
