package xyz.marsavic.gfxlab.graphics3d.scenes;

import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.gfxlab.graphics3d.*;
import xyz.marsavic.gfxlab.graphics3d.solids.HalfSpace;
import xyz.marsavic.gfxlab.graphics3d.textures.Grid;

import java.util.Collections;


public class OpenRoomRGTextured_GI extends Scene.Base {
	
	public OpenRoomRGTextured_GI() {
		Texture tL = Grid.standard(Color.hsb(0.0/3, 0.5, 0.5));
		Texture tR = Grid.standard(Color.hsb(1.0/3, 0.5, 0.5));
		Texture tW = Grid.standard(Color.gray(0.5));
		
		
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
