package xyz.marsavic.gfxlab.graphics3d;

public interface Solid {
	
	/**
	 * Returns the first hit of the ray into the surface of the solid, occurring after the given time.
	 * If there is no hit, returns null.
	 */
	Hit firstHit(Ray ray, double afterTime);
	
}
