package xyz.marsavic.gfxlab.tonemapping;

import xyz.marsavic.gfxlab.*;
import xyz.marsavic.gfxlab.RawImage;
import xyz.marsavic.gfxlab.ToneMappingFunction;
import xyz.marsavic.utils.Utils;


public class ToneMappingFunctionSimple implements ToneMappingFunction {
	
	private ColorTransformForColorMatrix colorTransformForColorMatrix = new ColorTransformForColorMatrix.Multiply();
	
	
	@Override
	public void apply(Matrix<Color> input, RawImage output) {
		ColorTransform f = colorTransformForColorMatrix.at(input);
		int w = output.width();
		
		Utils.parallelY(input.size(), y -> {
			int o = y * w;
			for (int x = 0; x < w; x++) {
				output.pixels()[o + x] = f.at(input.get(x, y)).codeClamp();
			}
		});
	}
	
	public ColorTransformForColorMatrix colorTransformFactory() {
		return colorTransformForColorMatrix;
	}
	
}
