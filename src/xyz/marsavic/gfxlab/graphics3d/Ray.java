package xyz.marsavic.gfxlab.graphics3d;

import xyz.marsavic.gfxlab.Vec3;


public record Ray(Vec3 p, Vec3 d) {
	
	
	public static Ray pd(Vec3 p, Vec3 d) {
		return new Ray(p, d);
	}
	
	
	public static Ray pq(Vec3 p, Vec3 q) {
		return pd(p, q.sub(p));
	}
	
	
	public Vec3 at(double t) {
		return p.add(d.mul(t));
	}
	
	
	public Ray moveTo(double t) {
		return Ray.pd(at(t), d());
	}
	
	
	public Ray normalized_() {
		return Ray.pd(p(), d().normalized_());
	}
	
}