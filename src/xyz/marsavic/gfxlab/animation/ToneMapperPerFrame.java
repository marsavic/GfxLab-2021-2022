package xyz.marsavic.gfxlab.animation;

import xyz.marsavic.functions.interfaces.Function1;
import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.Matrix;
import xyz.marsavic.gfxlab.RawImage;


public class ToneMapperPerFrame extends TransformedAnimationPerFrame<RawImage, Matrix<Color>> {
	
	public ToneMapperPerFrame(Animation<Matrix<Color>> source, Function1<RawImage, Matrix<Color>> transformation, boolean cacheLastFrame) {
		super(source, transformation, cacheLastFrame);
	}
	
	public ToneMapperPerFrame(Animation<Matrix<Color>> source, Function1<RawImage, Matrix<Color>> toneMappingFunction) {
		super(source, toneMappingFunction);
	}
	
}
