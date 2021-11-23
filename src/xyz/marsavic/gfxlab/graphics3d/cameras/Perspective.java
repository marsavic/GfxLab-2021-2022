package xyz.marsavic.gfxlab.graphics3d.cameras;

import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.gfxlab.graphics3d.Camera;
import xyz.marsavic.gfxlab.graphics3d.Ray;
import xyz.marsavic.utils.Numeric;


public record Perspective (
	double k
) implements Camera {
	
	@Override
	public Ray sampleExitingRay(Vector p) {
		return Ray.pd(Vec3.ZERO, Vec3.pz(p.mul(k), 1));
	}
	
	
	public static Camera fov(double angle) {
		return new Perspective(Numeric.tanT(angle / 2));
	}
	
}
