package xyz.marsavic.gfxlab;


import xyz.marsavic.gfxlab.graphics3d.Affine;
import xyz.marsavic.gfxlab.graphics3d.Ray;


public interface Transformation {
	
	Vec3 applyTo(Vec3 p);
	
	/** The default implementation might not be meaningful with non-affine transformations. */
	default Ray applyTo(Ray r) {
		return Ray.pq(applyTo(r.p()), applyTo(r.p().add(r.d())));
	}
	
	
	// --------------------
	
	
	Transformation identity = p -> p;

	
	@SuppressWarnings("unused")
	static Transformation toIdentity(Vec3 size) {
		return identity;
	}
	
	
	static Transformation toUnitBox(Vec3 size) {
		return p -> p.div(size);
	}
	
	
	static Transformation toGeometric(Vec3 size) {
		Vec3 s = Vec3.xyz( 1,  2, -2);
		Vec3 t = Vec3.xyz( 0, -1,  1);
		return p -> p.div(size).mul(s).add(t);
	}
	
	
	/** Returns a linear transformation which transforms unit vectors the same way as this transform. */
	default Affine linearize() {
		return Affine.unitVectors(
			applyTo(Vec3.EX),
			applyTo(Vec3.EY),
			applyTo(Vec3.EZ)
		);
	}
	
}
