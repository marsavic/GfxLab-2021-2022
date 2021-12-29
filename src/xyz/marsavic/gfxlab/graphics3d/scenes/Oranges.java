package xyz.marsavic.gfxlab.graphics3d.scenes;

import javafx.scene.shape.Sphere;
import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.gfxlab.graphics3d.BSDF;
import xyz.marsavic.gfxlab.graphics3d.Body;
import xyz.marsavic.gfxlab.graphics3d.Material;
import xyz.marsavic.gfxlab.graphics3d.Scene;
import xyz.marsavic.gfxlab.graphics3d.solids.Ball;


public class Oranges extends Scene.Base {
	
	public Oranges(int n, Material material) {
		addAllFrom(new OpenRoomRG_GI());
		
		double sqrt3 = Math.sqrt(3);
		
		Vec3 dI = Vec3.xyz(1.0, 0.0, 0.0);
		Vec3 dJ = Vec3.xyz(0.5, 0.0, sqrt3/2);
		Vec3 dK = Vec3.xyz(0.5, Math.sqrt(2.0/3.0), 1.0/(2*sqrt3));
		
		Vec3 o = dI.add(dJ.add(dK)).mul((n-1)/4.0);
		
		double d = 1.6 / n;
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; i+j < n; j++) {
				for (int k = 0; i+j+k < n; k++) {
					Vec3 c = dI.mul(i).add(dJ.mul(j)).add(dK.mul(k)).sub(o).mul(d).sub(Vec3.EY.mul(1-d/2-d*o.y()));
					bodies.add(Body.uniform(Ball.cr(c, d/2), material));
				}
			}
		}
		
		bodies.add(
				Body.uniform(Ball.cr(Vec3.xyz(0.5, 0.5, 0.5), 0.4), Material.LIGHT)
		);
	}
	
	
	public Oranges(int n) {
//		this(n, Material.GLASS.reflective(Color.gray(0.1)).specular(Color.WHITE));
		this(n, new Material(BSDF.mix(BSDF.refractive(1.4), BSDF.REFLECTIVE, 0.05)));
	}
}
