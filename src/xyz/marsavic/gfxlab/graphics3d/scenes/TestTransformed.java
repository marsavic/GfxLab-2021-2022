package xyz.marsavic.gfxlab.graphics3d.scenes;

import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.gfxlab.graphics3d.*;
import xyz.marsavic.gfxlab.graphics3d.solids.Ball;
import xyz.marsavic.gfxlab.graphics3d.textures.Checkerboard;


public class TestTransformed extends Scene.Base {
	
	public TestTransformed(double phiX, double phiY, double phiZ) {
		addAllFrom(new OpenRoomRGTextured());
		
//		Texture t = new Checkerboard(Vector.xy(0.125, 0.125), Material.matte(0.7), Material.matte(0.3));
		Texture t = Material.BLACK.specular(Color.WHITE);
		
		bodies.add(
				Body.textured(
						Ball.cr(Vec3.ZERO, 0.5).transformed(Affine.IDENTITY
								.andThen(Affine.scaling(Vec3.xyz(0.3, 1, 1)))
								.andThen(Affine.rotationAboutX(phiX))
								.andThen(Affine.rotationAboutY(phiY))
								.andThen(Affine.rotationAboutZ(phiZ))
						),
						t
				)
		);
	}
}
