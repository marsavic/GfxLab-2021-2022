package xyz.marsavic.gfxlab.graphics3d;

import xyz.marsavic.gfxlab.Vec3;


/** Interaction of a ray with a solid.*/
public interface Hit {
	
	/** The time of the hit */
	double t();
	
	/** The normal at the point of the hit */
	Vec3 n();
	
	
	static double t(Hit hit) {
		return hit == null ? Double.POSITIVE_INFINITY : hit.t();
	}
	
	
	// =====================================================================================================
	
	abstract class HitRayT implements Hit {
		private final Ray ray;
		private final double t;
		
		protected HitRayT(Ray ray, double t) {
			this.ray = ray;
			this.t = t;
		}
		
		public Ray ray() {
			return ray;
		}
		
		@Override
		public double t() {
			return t;
		}
	}
	
}
