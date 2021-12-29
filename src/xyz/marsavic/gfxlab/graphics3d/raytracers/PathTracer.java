package xyz.marsavic.gfxlab.graphics3d.raytracers;

import xyz.marsavic.functions.interfaces.Function1;
import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.gfxlab.graphics3d.*;
import xyz.marsavic.random.sampling.Sampler;

import java.util.Collection;


public class PathTracer extends RayTracerSimple {
	
	final int maxDepth;
	final Sampler sampler = new Sampler(); // Not really ok because of concurrency.
	
	
	public PathTracer(Scene scene, Function1<Collider, Collection<Body>> colliderFactory, Camera camera, int maxDepth) {
		super(scene, colliderFactory, camera);
		this.maxDepth = maxDepth;
	}
	
	
	@Override
	protected Color sample(Ray ray) {
		return radiance(ray, maxDepth);
	}
	
	
	private Color radiance(Ray ray, int depthRemaining) {
		if (depthRemaining <= 0) return Color.BLACK;
		
		Collider.Collision collision = collider().collide(ray);
		
		Body body = collision.body();
		if (body == null) {
			return scene().backgroundColor();
		}
		
		Material material = body.materialAt(collision.hit());
		Color result = material.emittance();
		
		Vec3 i = ray.d().inverse();                              // Incoming direction
		Vec3 n = collision.hit().n();                            // Normal to the body surface at the point of collision
		BSDF.Result bsdfResult = material.bsdf().sample(sampler, n, i);
		
		if (bsdfResult.color().notZero()) {
			Vec3 p = ray.at(collision.hit().t());                // Point of collision
			Ray rayScattered = Ray.pd(p, bsdfResult.out());
			Color rO = radiance(rayScattered, depthRemaining - 1);
			Color rI = rO.mul(bsdfResult.color());
			result = result.add(rI);
		}
		
		return result;
	}
	
	
}
