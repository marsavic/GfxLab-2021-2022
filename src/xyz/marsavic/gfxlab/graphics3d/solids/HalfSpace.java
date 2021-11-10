package xyz.marsavic.gfxlab.graphics3d.solids;

import xyz.marsavic.gfxlab.graphics3d.GeometryUtils;
import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.gfxlab.graphics3d.Hit;
import xyz.marsavic.gfxlab.graphics3d.Ray;
import xyz.marsavic.gfxlab.graphics3d.Solid;


public class HalfSpace implements Solid {
	
	private final Vec3 p; // A point on the boundary plane
	private final Vec3 e; // A vector parallel to the boundary plane.
	private final Vec3 f; // A vector parallel to the boundary plane, not parallel to e.
	
	// transient
	private final Vec3 n; // A normal vector to the boundary plane
	private final double e_f, f_e, eLSqr, fLSqr, sinSqr;
	
	
	
	private HalfSpace(Vec3 p, Vec3 e, Vec3 f) {
		this.p = p;
		this.e = e;
		this.f = f;
		this.n = e.cross(f);

		eLSqr = e.lengthSquared();
		fLSqr = f.lengthSquared();
		double ef = e.dot(f);
		e_f = ef / fLSqr;
		f_e = ef / eLSqr;
		sinSqr = 1 - e_f * f_e;
	}
	
	
	public static HalfSpace pef(Vec3 p, Vec3 e, Vec3 f) {
		return new HalfSpace(p, e, f);
	}
	
	
	public static HalfSpace pqr(Vec3 p, Vec3 q, Vec3 r) {
		return pef(p, q.sub(p), r.sub(p));
	}
	
	
	public static HalfSpace pn(Vec3 p, Vec3 n) {
		double nl = n.length();
		Vec3 e = GeometryUtils.normal(n).normalizedTo(nl);
		Vec3 f = n.cross(e).normalizedTo(nl);
		return new HalfSpace(p, e, f);
	}
	
	
	public Vec3 p() {
		return p;
	}
	
	
	public Vec3 e() {
		return e;
	}
	
	
	public Vec3 f() {
		return f;
	}
	
	
	public Vec3 n() {
		return n;
	}
	
	
	@Override
	public Hit firstHit(Ray ray, double afterTime) {
		double o = n().dot(ray.d());
		if (o == 0) {
			return Hit.POSITIVE_INFINITY;
		} else {
			double t = n().dot(p().sub(ray.p())) / o;
			if (t > afterTime) {
				return new HitHalfSpace(ray, t);
			} else {
				return Hit.POSITIVE_INFINITY;
			}
		}
	}
	
	
	@Override
	public String toString() {
		return "HalfSpace{" +
				"p=" + p +
				", e=" + e +
				", f=" + f +
				", n=" + n +
				'}';
	}



//	@Override
//	public Solid complement() {
//		return HalfSpace.pef(p(), f(), e());
//	}
	
	
	class HitHalfSpace extends Hit.HitRayT {
		
		protected HitHalfSpace(Ray ray, double t) {
			super(ray, t);
		}
		
		@Override
		public Vec3 n() {
			return n;
		}
		
	}
	
}
