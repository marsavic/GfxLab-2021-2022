package xyz.marsavic.gfxlab.graphics3d.raytracers;

import xyz.marsavic.functions.interfaces.Function1;
import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.graphics3d.Material;
import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.gfxlab.graphics3d.*;

import java.util.Collection;

/**
 * A raytracer that renders only the diffuse color of the bodies using diffuse (Lambertian) shading.
 */
public class RayTracerTest extends RayTracer {
	
	public RayTracerTest(Scene scene, Function1<Collider, Collection<Body>> colliderFactory, Camera camera) {
		super(scene, colliderFactory, camera);
	}
	
	
	@Override
	protected Color sample(Ray ray) {
		Collider.Collision collision = collider().collide(ray);
		
		if (collision == null) {
			return scene().backgroundColor();
		}
		
		Body body = collision.body();
		Hit hit = collision.hit();
		Material material = body.materialAt(hit);
		
		Vec3 p = ray.at(hit.t());               // Point of the collision
		Vec3 n_ = hit.n_();                     // Normalized normal to the body surface at the point of the collision
		
		Color lightDiffuse = Color.BLACK;       // The sum of diffuse contributions from all the lights
		
		for (Light light : scene().lights()) {
			Vec3 l = light.p().sub(p);          // Vector from p to the light;
			
			Ray rayToLight = Ray.pd(p, l);
			
			Collider.Collision collisionLight = collider().collide(rayToLight);
			
			if ((collisionLight == null) || (collisionLight.hit().t() > 1)) {
				double lLSqr = l.lengthSquared();   // Distance from p to the light squared
				double lL = Math.sqrt(lLSqr);       // Distance from p to the light
				double cosLN = n_.dot(l) / lL;      // Cosine of the angle between l and n_
				
				if (cosLN > 0) {
					Color irradiance = light.c().mul(cosLN / lLSqr);
					// The irradiance represents how much light is received by a unit area of the surface. It is
					// proportional to the cosine of the incoming angle and inversely proportional to the distance squared
					// (inverse-square law).
					lightDiffuse = lightDiffuse.add(irradiance);
				}
			}
		}
		
		return material.diffuse().mul(lightDiffuse);
	}
	
}
