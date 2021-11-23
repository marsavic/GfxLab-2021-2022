package xyz.marsavic.gfxlab.graphics3d;


import java.util.Collection;


public interface Collider {
	
	/**
	 * Returns the first intersection with a positive time of the ray r with a set of bodies.
	 * Returned collision time is "ray-time", that is, it depends on the speed of the ray (r.d).
	 * If there is no collision, null is returned.
	 */
	Collision collide(Ray r);
	
	
	
	public static record Collision (
			Hit hit, Body body
	) {}
	
	
	
	
	public static class BruteForce implements Collider {
		
		private static final double EPSILON = 1e-12;
		
		private final Body[] bodies; // Using an array for efficiency.
		
		
		public BruteForce(Collection<Body> bodies) {
			this.bodies = bodies.toArray(new Body[0]);
		}
		
		
		@Override
		public Collision collide(Ray r) {
			Hit minHit = null;
			double minHitT = Double.POSITIVE_INFINITY;
			Body minBody = null;
			
			for (Body body : bodies) {
				Hit hit = body.solid().firstHit(r, EPSILON);
				if ((hit != null) && (hit.t() < minHitT)) {
					minHitT = hit.t();
					minHit = hit;
					minBody = body;
				}
			}
			
			return minBody == null ? null : new Collision(minHit, minBody);
		}
	}
	
}
