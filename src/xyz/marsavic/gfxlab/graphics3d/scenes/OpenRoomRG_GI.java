package xyz.marsavic.gfxlab.graphics3d.scenes;

import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.gfxlab.graphics3d.Body;
import xyz.marsavic.gfxlab.graphics3d.Material;
import xyz.marsavic.gfxlab.graphics3d.Scene;
import xyz.marsavic.gfxlab.graphics3d.solids.HalfSpace;

import java.util.Collections;


public class OpenRoomRG_GI extends Scene.Base {
	
	public OpenRoomRG_GI() {
		Collections.addAll(bodies,
				Body.uniform(HalfSpace.pn(Vec3.xyz( 1,  0,  0), Vec3.xyz(-1,  0,  0)), Material.matte(Color.hsb(0.0/3, 0.5, 0.7))),
				Body.uniform(HalfSpace.pn(Vec3.xyz(-1,  0,  0), Vec3.xyz( 1,  0,  0)), Material.matte(Color.hsb(1.0/3, 0.5, 0.7))),
				Body.uniform(HalfSpace.pn(Vec3.xyz( 0,  1,  0), Vec3.xyz( 0, -1,  0)), Material.matte(0.7)),
				Body.uniform(HalfSpace.pn(Vec3.xyz( 0, -1,  0), Vec3.xyz( 0,  1,  0)), Material.matte(0.7)),
				Body.uniform(HalfSpace.pn(Vec3.xyz( 0,  0,  1), Vec3.xyz( 0,  0, -1)), Material.matte(0.7))
		);
	}
	
}
