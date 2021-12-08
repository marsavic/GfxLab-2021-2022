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
	
	final boolean showDiffuse;
	final boolean showSpecular;
	final boolean shadows;
	
	final int maxDepth;
	
	
	
	public RayTracerTest(Scene scene, Function1<Collider, Collection<Body>> colliderFactory, Camera camera, boolean showDiffuse, boolean showSpecular, boolean shadows, int maxDepth) {
		super(scene, colliderFactory, camera);
		this.showDiffuse = showDiffuse;
		this.showSpecular = showSpecular;
		this.shadows = shadows;
		this.maxDepth = maxDepth;
	}
	
	
	public RayTracerTest(Scene scene, Function1<Collider, Collection<Body>> colliderFactory, Camera camera) {
		this(scene, colliderFactory, camera, true, true, true, 16);
	}
	
	
	@Override
	protected Color sample(Ray ray) {
		return sample(ray, maxDepth);
	}
	
	
	protected Color sample(Ray ray, int depthRemaining) {
		if (depthRemaining == 0) {
			return Color.BLACK;
		}
		
		Collider.Collision collision = collider().collide(ray);
		
		if (collision == null) {
			return scene().backgroundColor();
		}
		
		Body body = collision.body();
		Hit hit = collision.hit();
		Material material = body.materialAt(hit);
		
		Vec3 p = ray.at(hit.t());                   // Point of the collision
		Vec3 n_ = hit.n_();                         // Normalized normal to the body surface at the point of the collision
		Vec3 i = ray.d().inverse();                 // Incoming direction
		Vec3 r = GeometryUtils.reflectedN(n_, i);   // Reflected ray (i reflected over n)
		double lR = r.length();
		
		Color lightDiffuse  = Color.BLACK;          // The sum of diffuse  contributions from all the lights
		Color lightSpecular = Color.BLACK;          // The sum of specular contributions from all the lights
		
		for (Light light : scene().lights()) {
			Vec3 l = light.p().sub(p);              // Vector from p to the light;
			
			if (!(shadows && collider().collidesIn01(Ray.pd(p, l)))) {
				double lLSqr = l.lengthSquared();   // Distance from p to the light squared
				double lL = Math.sqrt(lLSqr);       // Distance from p to the light
				double cosLN = n_.dot(l) / lL;      // Cosine of the angle between l and n_
				
				if (cosLN > 0) {                    // If the light is above the surface
					Color irradiance = light.c().mul(cosLN / lLSqr);
					// The irradiance represents how much light is received by a unit area of the surface. It is
					// proportional to the cosine of the incoming angle and inversely proportional to the distance squared
					// (inverse-square law).
					lightDiffuse = lightDiffuse.add(irradiance);
					
					if (showSpecular) {
						double lr = l.dot(r);
						if (lr > 0) {                            // If the angle between l and r is acute
							double cosLR = lr / (lL * lR);       // cos angle between reflected ray and light
							lightSpecular = lightSpecular.add(irradiance.mul(Math.pow(cosLR, material.shininess())));
						}
					}
				}
			}
		}
		
		Color result = Color.BLACK;
		if (showDiffuse ) result = result.add(material.diffuse ().mul(lightDiffuse ));
		if (showSpecular) result = result.add(material.specular().mul(lightSpecular));
		
		
		if (material.reflective().notZero()) {
			// When material has reflective properties we recursively find the color visible along the ray (p, r).
			result = result.add(sample(Ray.pd(p, r), depthRemaining - 1).mul(material.reflective()));
		}
		
		
		return result;
	}
	
}
