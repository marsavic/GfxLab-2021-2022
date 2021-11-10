package xyz.marsavic.gfxlab.tonemapping;

import xyz.marsavic.functions.interfaces.Function1;
import xyz.marsavic.gfxlab.Color;


public interface ColorTransform extends Function1<Color, Color> {

	ColorTransform IDENTITY = c -> c;
	
}

