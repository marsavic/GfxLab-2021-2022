package xyz.marsavic.gfxlab.graphics3d;

import xyz.marsavic.geometry.Vector;


public interface Camera {
	Ray sampleExitingRay(Vector p);
}
