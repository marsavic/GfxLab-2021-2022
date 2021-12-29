package xyz.marsavic.gfxlab.graphics3d;


import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.random.sampling.Sampler;
import xyz.marsavic.utils.Numeric;


public class GeometryUtils {
	
	/** Returns a vector normal to v. */
	public static Vec3 normal(Vec3 v) {
		if (v.x() != 0 || v.y() != 0) {
			return Vec3.xyz(-v.y(), v.x(), 0);
		} else {
			return Vec3.EX;
		}
	}
	
	
	public static Vec3 reflected(Vec3 n, Vec3 d) {
		return n.mul(2 * d.dot(n) / n.lengthSquared()).sub(d);
	}

	
	public static Vec3 reflectedN(Vec3 n_, Vec3 d) {
		return n_.mul(2 * d.dot(n_)).sub(d);
	}
	
	
	public static Vec3 refractedN(double refractiveIndex, Vec3 n_, Vec3 i) {
		double ri = refractiveIndex;
		double k = 1;
		double lI = i.length();
		
		double c1 = i.dot(n_) / lI;
		if (c1 < 0) { 		                              // We are exiting the object
			ri = 1.0 / ri;
			k = -1;
		}
		double c2Sqr = 1 - (1 - c1 * c1) / (ri * ri);
		
		Vec3 f;
		if (c2Sqr > 0) {
			double c2 = Math.sqrt(c2Sqr);
			f = n_.mul(c1/ri - k * c2).sub(i.div(ri*lI)); // refraction
		} else {
			f = reflectedN(n_, i);                        // total reflection
		}
		return f;
	}
	
	
	/** The length of the result equals the length of n. */
	public static Vec3 sampleHemisphereUniform(Sampler sampler, Vec3 n) {
		double phi = sampler.uniform();
		double x   = sampler.uniform();
		
		double r = Math.sqrt(1 - x*x);
		double y = Numeric.cosT(phi) * r;
		double z = Numeric.sinT(phi) * r;
		
		return Affine.asEX(n).applyWithoutTranslationTo(Vec3.xyz(x, y, z));
	}
	
	
	/** The length of the result equals the length of n. */
	public static Vec3 sampleHemisphereCosineDistributed(Sampler sampler, Vec3 n) {
		double phi  = sampler.uniform();
		double xSqr = sampler.uniform();
		double x = Math.sqrt(xSqr);
		
		double r = Math.sqrt(1 - xSqr);
		double y = Numeric.cosT(phi) * r;
		double z = Numeric.sinT(phi) * r;
		
		return Affine.asEX(n).applyWithoutTranslationTo(Vec3.xyz(x, y, z));
	}
	
	
	
	/** The length of the result equals the length of n. */
	public static Vec3 sampleHemisphereCosineDistributedN_(Sampler sampler, Vec3 n_) {
		double phi  = sampler.uniform();
		double xSqr = sampler.uniform();
		double x = Math.sqrt(xSqr);
		
		double r = Math.sqrt(1 - xSqr);
		double y = Numeric.cosT(phi) * r;
		double z = Numeric.sinT(phi) * r;
		
		return Affine.asEXN(n_).applyWithoutTranslationTo(Vec3.xyz(x, y, z));
	}
	
	
	public static Vec3 sampleHemisphereCosineDistributedRejection(Sampler sampler, Vec3 n) {
		// Sample the sphere with radius 1, add n_
		double x, y, z, lVSqr;
		
		do {
			x = 2 * sampler.uniform() - 1;
			y = 2 * sampler.uniform() - 1;
			z = 2 * sampler.uniform() - 1;
			lVSqr = x*x + y*y + z*z;
		} while (lVSqr > 1);
		
		double c = Math.sqrt(n.lengthSquared() / lVSqr);
		return Vec3.xyz(x * c, y * c, z * c).add(n);
	}
	
	
	public static Vec3 sampleHemisphereCosineDistributedRejectionN(Sampler sampler, Vec3 n_) {
		// Sample the sphere with radius 1, add n_
		double x, y, z, lVSqr;
		
		do {
			x = 2 * sampler.uniform() - 1;
			y = 2 * sampler.uniform() - 1;
			z = 2 * sampler.uniform() - 1;
			lVSqr = x*x + y*y + z*z;
		} while (lVSqr > 1);
		
		double c = 1 / Math.sqrt(lVSqr);
		return Vec3.xyz(x * c, y * c, z * c).add(n_);
	}
	
	
}
