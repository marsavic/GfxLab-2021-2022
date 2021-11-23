package xyz.marsavic.gfxlab.graphics3d.scenes;

import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.gfxlab.graphics3d.Body;
import xyz.marsavic.gfxlab.graphics3d.Light;
import xyz.marsavic.gfxlab.graphics3d.Scene;
import xyz.marsavic.gfxlab.graphics3d.solids.HalfSpace;

import java.util.Collections;


public class OpenRoom extends Scene.Base {
	
	public OpenRoom() {
		Collections.addAll(bodies,
				Body.uniform(HalfSpace.pn(Vec3.xyz( 1,  0,  0), Vec3.xyz(-1,  0,  0))),
				Body.uniform(HalfSpace.pn(Vec3.xyz(-1,  0,  0), Vec3.xyz( 1,  0,  0))),
				Body.uniform(HalfSpace.pn(Vec3.xyz( 0,  1,  0), Vec3.xyz( 0, -1,  0))),
				Body.uniform(HalfSpace.pn(Vec3.xyz( 0, -1,  0), Vec3.xyz( 0,  1,  0))),
				Body.uniform(HalfSpace.pn(Vec3.xyz( 0,  0,  1), Vec3.xyz( 0,  0, -1)))
		);
		
		Collections.addAll(lights,
				Light.p(Vec3.ZERO)
		);
	}
	
}
