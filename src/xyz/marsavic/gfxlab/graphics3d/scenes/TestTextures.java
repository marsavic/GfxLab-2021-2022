package xyz.marsavic.gfxlab.graphics3d.scenes;

import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.gfxlab.graphics3d.Body;
import xyz.marsavic.gfxlab.graphics3d.Material;
import xyz.marsavic.gfxlab.graphics3d.Scene;
import xyz.marsavic.gfxlab.graphics3d.Texture;
import xyz.marsavic.gfxlab.graphics3d.solids.Ball;
import xyz.marsavic.gfxlab.graphics3d.textures.Checkerboard;


public class TestTextures extends Scene.Base {
	
	public TestTextures() {
		addAllFrom(new OpenRoomRGTextured());
		
		Texture t = new Checkerboard(Vector.xy(0.125, 0.125), Material.matte(0.7), Material.matte(0.3));
		
		bodies.add(
				Body.textured(Ball.cr(Vec3.ZERO, 0.5), t)
		);
	}
}
