package xyz.marsavic.gfxlab.graphics3d.scenes;

import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.gfxlab.graphics3d.Body;
import xyz.marsavic.gfxlab.graphics3d.Light;
import xyz.marsavic.gfxlab.graphics3d.Scene;
import xyz.marsavic.gfxlab.graphics3d.Texture;
import xyz.marsavic.gfxlab.graphics3d.solids.HalfSpace;
import xyz.marsavic.gfxlab.graphics3d.textures.Grid;

import java.util.Collections;


public class OpenRoomRGTextured extends Scene.Base {
	
	public OpenRoomRGTextured() {
//		Material mL0 = Material.MATTE.diffuse(Color.hsb(0      , 0.5, 0.7));
//		Material mL1 = Material.MATTE.diffuse(Color.hsb(0      , 0.5, 0.6));
//		Texture tL = new Checkerboard(Vector.xy(0.25, 0.25), mL0, mL1);
//
//		Material mR0 = Material.MATTE.diffuse(Color.hsb(1.0 / 3, 0.5, 0.7));
//		Material mR1 = Material.MATTE.diffuse(Color.hsb(1.0 / 3, 0.5, 0.6));
//		Texture tR = new Checkerboard(Vector.xy(0.25, 0.25), mR0, mR1);
//
//		Material mW0 = Material.MATTE.diffuse(Color.gray(0.7));
//		Material mW1 = Material.MATTE.diffuse(Color.gray(0.6));
//		Texture tW = new Checkerboard(Vector.xy(0.25, 0.25), mW0, mW1);

		
		Texture tL = Grid.standard(Color.hsb(0.0/3, 0.5, 0.7));
		Texture tR = Grid.standard(Color.hsb(1.0/3, 0.5, 0.7));
		Texture tW = Grid.standard(Color.gray(0.7));
		
		
		Collections.addAll(bodies,
				Body.textured(HalfSpace.pn(Vec3.xyz( 1,  0,  0), Vec3.xyz(-1,  0,  0)), tL),
				Body.textured(HalfSpace.pn(Vec3.xyz(-1,  0,  0), Vec3.xyz( 1,  0,  0)), tR),
				Body.textured(HalfSpace.pn(Vec3.xyz( 0,  1,  0), Vec3.xyz( 0, -1,  0)), tW),
				Body.textured(HalfSpace.pn(Vec3.xyz( 0, -1,  0), Vec3.xyz( 0,  1,  0)), tW),
				Body.textured(HalfSpace.pn(Vec3.xyz( 0,  0,  1), Vec3.xyz( 0,  0, -1)), tW)
		);
		
		Collections.addAll(lights,
				Light.p(Vec3.xyz(-0.5, 0.75, -0.5)),
				Light.p(Vec3.xyz(-0.5, 0.75,  0.5)),
				Light.p(Vec3.xyz( 0.5, 0.75, -0.5)),
				Light.p(Vec3.xyz( 0.5, 0.75,  0.5))
		);
	}
	
}
