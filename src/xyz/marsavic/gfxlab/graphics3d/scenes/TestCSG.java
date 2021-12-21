package xyz.marsavic.gfxlab.graphics3d.scenes;

import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.gfxlab.graphics3d.*;
import xyz.marsavic.gfxlab.graphics3d.solids.Ball;
import xyz.marsavic.gfxlab.graphics3d.solids.Box;


public class TestCSG extends Scene.Base {
	
	public TestCSG(double t) {
		addAllFrom(new OpenRoomRGTextured());
		
		Solid sA = Box.$.r(0.5).transformed(Affine.rotationAboutX(0.1).andThen(Affine.rotationAboutY(0.1 + t)));
		Solid sB = Ball.cr(Vec3.xyz(0, 0, 0), 0.62);
		Solid sC = Ball.cr(Vec3.xyz(0, 0, 0), 0.68);
		Solid s = Solid.intersection(Solid.difference(sA, sB), sC);
		
//		Material m = Material.matte(Color.hsb(0.16, 0.8, 0.9)).specular(Color.WHITE);
		Material m = Material.GLASS;
		
		bodies.add(Body.uniform(s, m));
	}
}
