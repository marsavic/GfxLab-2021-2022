package xyz.marsavic.gfxlab.graphics3d;

import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.random.sampling.Sampler;
import xyz.marsavic.utils.Numeric;


public interface BSDF {
	// Cos term is included
	// Integrated over a hemisphere of unit area
	
	Result sample(Sampler sampler, Vec3 n_, Vec3 i);
	
	
	public static record Result (
			Vec3 out,
			Color color
	) {
		public static final Result ABSORBED = new Result(null, Color.BLACK);
	}
	
	
	default BSDF mul(Color color) {
		return (s, n_, i) -> {
			Result res = BSDF.this.sample(s, n_, i);
			return new Result(res.out, res.color.mul(color));
		};
	}
	
	
	default BSDF mul(double k) {
		return mul(Color.gray(k));
	}
	
	
	// ===================================================================================================
	// Utility instances and factories.
	
	
	/** Interpolates between two bsdfs */
	static BSDF mix(BSDF bsdf0, BSDF bsdf1, double k) {
		if (k == 0) return bsdf0;
		if (k == 1) return bsdf1;
		
		return (sampler, n_, i) ->
				sampler.uniform() < k ?
						bsdf1.sample(sampler, n_, i) :
						bsdf0.sample(sampler, n_, i);
	}
	
	
	/** Returns the sum of the specified bsdfs. The sampling is weighted according to the given weights (but the
	 * analytical bsdfs is not dependent on the weights) */
	static BSDF add(BSDF[] bsdfs, double[] weights) {
		if (bsdfs.length != weights.length) {
			throw new IllegalArgumentException();
		}
		
		double sum = 0;
		int m = 0;
		for (double w : weights) {
			if (w > 0) {
				m++;
				sum += w;
			}
		}
		
		if (m == 0) return BSDF.ABSORPTIVE;
		
		BSDF[] bsdfs_ = new BSDF[m];
		double[] weights_ = new double[m];
		m = 0;
		for (int j = 0; j < bsdfs.length; j++) {
			double w = weights[j];
			if (w > 0) {
				bsdfs_[m] = bsdfs[j];
				weights_[m] = weights[j] / sum;
				m++;
			}
		}
		
		// Optimizations:
		if (m == 1) return bsdfs_[0];
		if (m == 2) return mix(bsdfs_[0], bsdfs_[1], 1 - weights_[0]);
		
		
		return (sampler, n_, i) -> {
			double u = sampler.uniform();
			double s = 0;
			int j = 0;
			while (s <= u) {
				s += weights_[j++];
			}
			j--;
			
			Result bsdfResult = bsdfs_[j].sample(sampler, n_, i);
			return new Result(
				bsdfResult.out(),
				bsdfResult.color().div(weights_[j])
			);
		};
	}
	
	
	
	
	BSDF ABSORPTIVE = (sampler, n_, i) -> Result.ABSORBED;
	
	BSDF REFLECTIVE = (sampler, n_, i) -> new Result(GeometryUtils.reflectedN(n_, i), Color.WHITE);
	
	BSDF TRANSMISSIVE = transmissive(Color.WHITE);
	
	
	static BSDF transmissive(Color c) {
		return (sampler, n_, i) -> new Result(i.inverse(), c);
	}
	
	
	static BSDF diffuse(Color c) {
		return (sampler, n_, i) -> new Result(
				GeometryUtils.sampleHemisphereCosineDistributedRejectionN(sampler, n_),
				c
		);
		
//		Alternative, using uniform sampling
//		return (sampler, n_, i) -> {
//			Vec3 out = Utils.sampleHemisphereUniform(sampler, n_);
//			return new Result(out, c.mul(out.dot(n_) * 2));
//		};
	}
	
	
	static BSDF diffuse(double k) {
		return diffuse(Color.gray(k));
	}
	
	
	static BSDF reflective(double k) {
		return reflective(Color.gray(k));
	}
	
	
	static BSDF reflective(Color c) {
		return (sampler, n_, i) -> new Result(GeometryUtils.reflected(n_, i), c);
	}
	

	// YES: 0 = perfectly specular
	// NO : 1 = perfectly diffuse.
	// YES: Reflected direction in expectation.
	static BSDF glossy(Color c, double s) {
		return (sampler, n_, i) -> {
			Vec3 r = GeometryUtils.reflected(n_, i);
			Vec3 d = GeometryUtils.sampleHemisphereCosineDistributedRejectionN(sampler, n_);
			
			Vec3 b = r.div(r.dot(n_)).sub(n_);
			d = d.add(b.mul(d.dot(n_)));
			
			Vec3 o = Vec3.lerp(r.normalized_(), d, s);
			return new Result(o, c);
		};
	}

/*
	// YES: 0 = perfectly specular
	// YES: 1 = perfectly diffuse.
	// NO : Reflected direction in expectation.
	static BSDF glossy(Color c, double s) {
		return (sampler, n_, i) -> {
			Vec3 d = Utils.sampleHemisphereCosineDistributed(sampler, n_);
			Vec3 r = Utils.reflected(n_, i.normalized_());
			Vec3 o = Vec3.lerp(r, d, s);
			return new Result(o, c);
		};
	}
*/
	
/*
	// YES: 0 = perfectly specular (experimentally shown)
	// NO : 1 = perfectly diffuse.
	// ?  : Reflected direction in expectation. (Probably not, but can't confirm visually)
	// Subjective: Better looking than previous one.
	static BSDF glossy(Color c, double s) {
		return (sampler, n_, i) -> {
			Vec3 r = i;
			while (true) {
				Vec3 u = Utils.sampleHemisphereCosineDistributed(sampler, n_);
				Vec3 n = Vec3.lerp(n_, u, s);
				r = Utils.reflected(n, r);
				if (r.dot(n_) > 0) {
					return new Result(r, c);
				}
			}
		};
	}
*/
	
	
	static BSDF glossy(double k, double s) {
		return glossy(Color.gray(k), s);
	}
	
	
	static BSDF glossyRefractive(Color c, double refractiveIndex, double s) {
		return (sampler, n_, i) -> {
			Vec3 r = GeometryUtils.refractedN(refractiveIndex, n_, i.normalized_());
			Vec3 d = GeometryUtils.sampleHemisphereCosineDistributedRejectionN(sampler, n_).mul(Numeric.sign(n_.dot(r)));
			
			Vec3 b = r.div(r.dot(n_)).sub(n_);
			d = d.add(b.mul(d.dot(n_)));
			
			Vec3 o = Vec3.lerp(r, d, s);
			return new Result(o, c);
		};
	}
	
	
	static BSDF glossyRefractive(double k, double refractiveIndex, double s) {
		return glossyRefractive(Color.gray(k), refractiveIndex, s);
	}
	
	
	static BSDF refractive(double refractiveIndex) {
		return refractive(1.0, refractiveIndex);
	}
	
	
	static BSDF refractive(double k, double refractiveIndex) {
		return refractive(Color.gray(k), refractiveIndex);
	}
	
	
	static BSDF refractive(Color c, double refractiveIndex) {
		// To do: fresnel coefficients for the ratio between the refracted and the reflected part.

		return (sampler, n_, i) -> new Result(GeometryUtils.refractedN(refractiveIndex, n_, i), c);
	}
	
	
}
