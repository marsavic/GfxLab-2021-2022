package xyz.marsavic.gfxlab;

import xyz.marsavic.geometry.Vector;


public interface ColorSampler {
	
	/**	p is a point in (t, x, y) space. */
	Color sample(Vec3 p);
	
	
	default Color sample(double t, Vector p) {
		return sample(Vec3.xp(t, p));
	}
	
}
