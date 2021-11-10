package xyz.marsavic.gfxlab.graphics3d;

public interface Solid {
	
	/**
	 * Returns the first hit of the ray into the surface of the solid, occurring after the given time.
	 * If there is no hit, Hit.POSITIVE_INFINITY is returned.
	 */
	Hit firstHit(Ray ray, double afterTime);
	
}
