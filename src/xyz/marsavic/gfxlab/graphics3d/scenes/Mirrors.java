package xyz.marsavic.gfxlab.graphics3d.scenes;

import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.gfxlab.graphics3d.Body;
import xyz.marsavic.gfxlab.graphics3d.Material;
import xyz.marsavic.gfxlab.graphics3d.Scene;
import xyz.marsavic.gfxlab.graphics3d.solids.Ball;


public class Mirrors extends Scene.Base {
	
	public Mirrors(int n, double phi) {
		addAllFrom(new OpenRoomRG());
	
		
		double r = 0.5;
		double rBall = Vector.polar(1.0 / n).sub(Vector.UNIT_X).length() * r / 2;
		
		for (int i = 0; i < n; i++) {
			double alpha = 1.0 * i / n + phi;
			Vector p = Vector.polar(r, alpha);
			bodies.add(Body.uniform(
					Ball.cr(Vec3.pz(p, 0), rBall),
					Material.BLACK.reflective(Color.hsb(0.7, 0.3, 0.9)))
			);
		}
	}
	
}
