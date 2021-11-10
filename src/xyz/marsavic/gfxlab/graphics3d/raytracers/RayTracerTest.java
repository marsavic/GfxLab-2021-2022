package xyz.marsavic.gfxlab.graphics3d.raytracers;

import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.gfxlab.graphics3d.Hit;
import xyz.marsavic.gfxlab.graphics3d.Ray;
import xyz.marsavic.gfxlab.graphics3d.RayTracer;
import xyz.marsavic.gfxlab.graphics3d.solids.Ball;
import xyz.marsavic.gfxlab.graphics3d.solids.HalfSpace;


public class RayTracerTest extends RayTracer {

	Ball ball;
	HalfSpace halfSpace;
	
	
	public RayTracerTest(double y, double z) {
		 ball = Ball.cr(Vec3.xyz(0, 0, 10*z), 1);
		 halfSpace = HalfSpace.pn(Vec3.xyz(0, -1+y, 0), Vec3.xyz(0, 1, 0));
	}
	
	
	@Override
	protected Color sample(Ray ray) {
		Hit hit1 = ball.firstHit(ray, 0.0);
		Hit hit2 = halfSpace.firstHit(ray, 0.0);
		
		double t = Math.min(hit1.t(), hit2.t());
		
		return t == Double.POSITIVE_INFINITY ? Color.BLACK : Color.gray(1 / (1 + t));
	}
	
}
