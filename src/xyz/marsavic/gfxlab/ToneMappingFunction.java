package xyz.marsavic.gfxlab;

import xyz.marsavic.functions.interfaces.Function1;


public interface ToneMappingFunction extends Function1<RawImage, Matrix<Color>> {
	
	void apply(Matrix<Color> input, RawImage output);
	
	
	@Override
	default RawImage at(Matrix<Color> input) {
		RawImage output = new RawImage(input.size());
		apply(input, output);
		return output;
	}
	
}
