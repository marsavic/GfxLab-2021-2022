package xyz.marsavic.gfxlab.graphics3d.cameras;

import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.Transformation;
import xyz.marsavic.gfxlab.graphics3d.Camera;
import xyz.marsavic.gfxlab.graphics3d.Ray;


public record TransformedCamera (
	Camera source,
	Transformation transformation
) implements Camera {
	
	@Override
	public Ray sampleExitingRay(Vector p) {
		Ray raySource = source().sampleExitingRay(p);
		return transformation().applyTo(raySource);
	}
	
}
