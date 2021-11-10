package xyz.marsavic.gfxlab.graphics3d;


import xyz.marsavic.gfxlab.Vec3;


public record HitData(double t, Vec3 n) implements Hit {
	
	
	public static HitData tn(double t, Vec3 n) {
		return new HitData(t, n);
	}
	
	
	public static HitData from(Hit hit) {
		return HitData.tn(hit.t(), hit.n());
	}
	
}
