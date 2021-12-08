package xyz.marsavic.gfxlab.graphics3d.scenes;

import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.gfxlab.graphics3d.Body;
import xyz.marsavic.gfxlab.graphics3d.Material;
import xyz.marsavic.gfxlab.graphics3d.Scene;
import xyz.marsavic.gfxlab.graphics3d.solids.Ball;

import java.util.Collections;


public class MirrorRoom extends Scene.Base {
	
	public MirrorRoom(double reflectivity) {
		addAllFrom(new OpenRoom(
				Material.mirror(reflectivity)
						.diffuse(Color.gray(0.1))
				)
		);
		
		Collections.addAll(bodies,
				Body.uniform(
						Ball.cr(Vec3.ZERO, 0.5),
						Material.matte(Color.hsb(0.7, 0.7, 0.8))
				)
		);
		
	}
	
}
