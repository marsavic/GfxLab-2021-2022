package xyz.marsavic.gfxlab.graphics3d;

import xyz.marsavic.gfxlab.Color;

import java.util.Collection;


public interface Scene {
	
	Collection<Body> bodies();
	Collection<Light> lights();
	
	
	default Color backgroundColor() {
		return Color.BLACK;
	}
	
}
