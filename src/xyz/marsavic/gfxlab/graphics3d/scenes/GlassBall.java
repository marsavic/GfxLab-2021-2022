package xyz.marsavic.gfxlab.graphics3d.scenes;

import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.gfxlab.graphics3d.Body;
import xyz.marsavic.gfxlab.graphics3d.Material;
import xyz.marsavic.gfxlab.graphics3d.Scene;
import xyz.marsavic.gfxlab.graphics3d.solids.Ball;


public class GlassBall extends Scene.Base {
	
	public GlassBall(double r) {
		addAllFrom(new OpenRoomRGTextured());
		
		bodies.add(
				Body.uniform(Ball.cr(Vec3.ZERO, 0.5), Material.GLASS.refractiveIndex(r))
		);
	}
}
