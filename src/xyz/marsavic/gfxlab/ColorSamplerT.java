package xyz.marsavic.gfxlab;

import xyz.marsavic.geometry.Vector;

/**
 * Implementations should override at least one of the methods sample(Vec3) and sample(Vector).
 */
public interface ColorSamplerT extends ColorSampler {
	
	/**	p is a point in (x, y) space. */
	Color sample(double t, Vector p);
	
	/**	p is a point in (t, x, y) space. */
	@Override
	default Color sample(Vec3 p) {
		return sample(p.x(), p.p12());
	}
	
}
