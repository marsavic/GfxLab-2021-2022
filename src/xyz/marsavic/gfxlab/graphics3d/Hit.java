package xyz.marsavic.gfxlab.graphics3d;

import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.Vec3;


/** Interaction of a ray with a solid.*/
public interface Hit {
	
	/** The time of the hit */
	double t();
	
	/** The normal at the point of the hit */
	Vec3 n();
	
	
	
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
	
	
	Hit NEGATIVE_INFINITY = new Hit() {
		@Override public double t () { return Double.NEGATIVE_INFINITY; }
		@Override public Vec3   n () { return Vec3.ZERO; }
	};
	
	
	Hit POSITIVE_INFINITY = new Hit() {
		@Override public double t () { return Double.POSITIVE_INFINITY; }
		@Override public Vec3   n () { return Vec3.ZERO; }
	};
	
}
