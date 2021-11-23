package xyz.marsavic.gfxlab.graphics3d.scenes;

import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.gfxlab.graphics3d.Body;
import xyz.marsavic.gfxlab.graphics3d.Light;
import xyz.marsavic.gfxlab.graphics3d.Material;
import xyz.marsavic.gfxlab.graphics3d.Scene;
import xyz.marsavic.gfxlab.graphics3d.solids.Ball;
import xyz.marsavic.gfxlab.graphics3d.solids.HalfSpace;

import java.util.Collections;


public class Test extends Scene.Base {
	
	public Test(double lightZ) {
		Collections.addAll(bodies,
				Body.uniform(
						Ball.cr(Vec3.xyz(1, 0, 4), 2),
						new Material(Color.hsb(0, 0.8, 1.0))
				),
				Body.uniform(
						Ball.cr(Vec3.xyz(-0.3, -0.6, 2.7), 0.7),
						new Material(Color.hsb(0.66, 0.8, 1.0))
				),
				Body.uniform(
						HalfSpace.pn(Vec3.xyz(0, -1, 0), Vec3.xyz(0, 1, 0)),
						new Material(Color.hsb(0.33, 0.8, 1.0))
				),
				Body.uniform(
						HalfSpace.pn(Vec3.xyz(1, 0, 0), Vec3.xyz(-1, 0, 0)),
						new Material(Color.hsb(0.33, 0.8, 1.0))
				)
		);
		
		Collections.addAll(lights,
				Light.pc(Vec3.xyz(-1, 1, lightZ), Color.WHITE)
		);
		
	}
	
}
