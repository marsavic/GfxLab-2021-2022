package xyz.marsavic.gfxlab.graphics3d.scenes;

import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.gfxlab.graphics3d.BSDF;
import xyz.marsavic.gfxlab.graphics3d.Body;
import xyz.marsavic.gfxlab.graphics3d.Material;
import xyz.marsavic.gfxlab.graphics3d.Scene;
import xyz.marsavic.gfxlab.graphics3d.solids.Ball;

import java.util.Collections;


public class TestGI extends Scene.Base {
	
	public TestGI() {
		addAllFrom(new OpenRoomRGTextured_GI());
		
		Material glass = new Material(BSDF.mix(BSDF.refractive(3.0), BSDF.REFLECTIVE, 0.05));
		
		Collections.addAll(bodies,
				Body.uniform(Ball.cr(Vec3.xyz(-0.2, -0.5,  0.0), 0.3), glass),
				Body.uniform(Ball.cr(Vec3.xyz( 0.5,  0.5,  0.5), 0.4), Material.LIGHT),
				Body.uniform(Ball.cr(Vec3.xyz( 0.5, -0.5, -0.3), 0.3), Material.MIRROR)
		);
	}
	
}
