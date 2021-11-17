package xyz.marsavic.gfxlab.graphics3d;

import xyz.marsavic.functions.interfaces.Function1;
import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.ColorSamplerT;
import xyz.marsavic.gfxlab.Vec3;

import java.util.Collection;


public abstract class RayTracer implements ColorSamplerT {
	
	private final Scene scene;
	private final Collider collider;

	
	
	public RayTracer(Scene scene, Function1<Collider, Collection<Body>> colliderFactory) {
		this.scene = scene;
		this.collider = colliderFactory.at(scene.bodies());
	}
	
	public Scene scene() {
		return scene;
	}
	
	public Collider collider() {
		return collider;
	}
	
	
	protected abstract Color sample(Ray ray);
	
	
	@Override
	public Color sample(double t, Vector p) {
		return sample(Ray.pd(Vec3.ZERO, Vec3.pz(p, 1)));
	}
	
}
