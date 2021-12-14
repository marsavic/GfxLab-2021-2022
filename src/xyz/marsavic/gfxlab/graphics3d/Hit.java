package xyz.marsavic.gfxlab.graphics3d;

import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.Vec3;



/** Interaction of a ray with a solid.*/
public interface Hit {
	
	/** The time of the hit */
	double t();
	
	/** The normal at the point of the hit */
	Vec3 n();
	
	/** The normalized normal at the point of the hit */
	default Vec3 n_() {
		return n().normalized_();
	}
	
	default Vector uv() {
		return Vector.ZERO;
	}
	
	// =====================================================================================================
	
	abstract class RayT implements Hit {
		private final Ray ray;
		private final double t;
		
		protected RayT(Ray ray, double t) {
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
	
	
	record Data(double t, Vec3 n) implements Hit {
		public static Data tn(double t, Vec3 n) {
			return new Data(t, n);
		}
		
		public static Data from(Hit hit) {
			return Data.tn(hit.t(), hit.n());
		}
	}
	
}
