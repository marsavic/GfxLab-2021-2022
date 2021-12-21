package xyz.marsavic.gfxlab.graphics3d;


public interface Solid {
	
	/**
	 * Returns the first hit of the ray into the surface of the solid, occurring after the given time.
	 * If there is no hit, returns null.
	 */
	Hit firstHit(Ray ray, double afterTime);

	
	default Solid transformed(Affine t) {
		return new Solid() {
			private final Affine tInv = t.inverse();
			private final Affine tInvTransposed = tInv.transposeWithoutTranslation();
			
			@Override
			public Hit firstHit(Ray ray, double afterTime) {
				Ray rayO = tInv.applyTo(ray);
				Hit hitO = Solid.this.firstHit(rayO, afterTime);
				if (hitO == null) {
					return null;
				}
				return hitO.withN(tInvTransposed.applyTo(hitO.n()));
			}
		};
	}
	
	
	public static Solid union(Solid... solids) {
		return new Solid() {
			@Override
			public Hit firstHit(Ray ray, double afterTime) {
				int n = solids.length;
				
				boolean[] in = new boolean[n];
				Hit[] hits = new Hit[n];
				double[] t = new double[n];
				
				int inCount = 0;
				
				for (int i = 0; i < n; i++) {
					Hit hit = solids[i].firstHit(ray, afterTime);
					if (hit == null) {
						t[i] = Double.POSITIVE_INFINITY;
						in[i] = false;
					} else {
						hits[i] = hit;
						t[i] = hit.t();
						in[i] = ray.d().dot(hit.n()) > 0;
						if (in[i]) {
							inCount++;
						}
					}
				}
				
				while (true) {
					double tFirst = Double.POSITIVE_INFINITY;
					int iFirst = -1;
					for (int i = 0; i < n; i++) {
						if (t[i] < tFirst) {
							tFirst = t[i];
							iFirst = i;
						}
					}
					
					if (iFirst == -1) {
						return null;
					}
					
					if (in[iFirst]) {
						if (--inCount == 0) {
							return hits[iFirst];
						}
						in[iFirst] = false;
					} else {
						if (inCount++ == 0) {
							return hits[iFirst];
						}
						in[iFirst] = true;
					}
					
					hits[iFirst] = solids[iFirst].firstHit(ray, tFirst);
					t[iFirst] = hits[iFirst] == null ? Double.POSITIVE_INFINITY : hits[iFirst].t();
				}
			}
		};
	}
	
	
	public static Solid intersection(Solid... solids) {
		return new Solid() {
			@Override
			public Hit firstHit(Ray ray, double afterTime) {
				int n = solids.length;
				
				boolean[] in = new boolean[n];
				Hit[] hits = new Hit[n];
				double[] t = new double[n];
				
				int inCount = 0;
				
				for (int i = 0; i < n; i++) {
					Hit hit = solids[i].firstHit(ray, afterTime);
					if (hit == null) {
						t[i] = Double.POSITIVE_INFINITY;
						in[i] = false;
					} else {
						hits[i] = hit;
						t[i] = hit.t();
						in[i] = ray.d().dot(hit.n()) > 0;
						if (in[i]) {
							inCount++;
						}
					}
				}
				
				while (true) {
					double tFirst = Double.POSITIVE_INFINITY;
					int iFirst = -1;
					for (int i = 0; i < n; i++) {
						if (t[i] < tFirst) {
							tFirst = t[i];
							iFirst = i;
						}
					}
					
					if (iFirst == -1) {
						return null;
					}
					
					if (in[iFirst]) {
						if (inCount-- == n) {
							return hits[iFirst];
						}
						in[iFirst] = false;
					} else {
						if (++inCount == n) {
							return hits[iFirst];
						}
						in[iFirst] = true;
					}
					
					hits[iFirst] = solids[iFirst].firstHit(ray, tFirst);
					t[iFirst] = hits[iFirst] == null ? Double.POSITIVE_INFINITY : hits[iFirst].t();
				}
			}
		};
	}
	
	public static Solid difference(Solid... solids) {
		return new Solid() {
			@Override
			public Hit firstHit(Ray ray, double afterTime) {
				int n = solids.length;
				
				boolean[] in = new boolean[n];
				Hit[] hits = new Hit[n];
				double[] t = new double[n];
				
				int inCount = 0;
				
				for (int i = 0; i < n; i++) {
					Hit hit = solids[i].firstHit(ray, afterTime);
					if (hit == null) {
						t[i] = Double.POSITIVE_INFINITY;
						in[i] = false;
					} else {
						hits[i] = hit;
						t[i] = hit.t();
						in[i] = ray.d().dot(hit.n()) > 0;
						if (in[i]) {
							inCount++;
						}
					}
				}
				
				while (true) {
					double tFirst = Double.POSITIVE_INFINITY;
					int iFirst = -1;
					for (int i = 0; i < n; i++) {
						if (t[i] < tFirst) {
							tFirst = t[i];
							iFirst = i;
						}
					}
					
					if (iFirst == -1) {
						return null;
					}
					
					if (iFirst == 0) {
						if (!in[1]) {
							return hits[0];
						}
						in[0] ^= true;
					} else {
						if (in[0]) {
							return hits[1].inverse();
						}
						in[1] ^= true;
					}
					
					hits[iFirst] = solids[iFirst].firstHit(ray, tFirst);
					t[iFirst] = hits[iFirst] == null ? Double.POSITIVE_INFINITY : hits[iFirst].t();
				}
			}
		};
	}
	
	
	
}
