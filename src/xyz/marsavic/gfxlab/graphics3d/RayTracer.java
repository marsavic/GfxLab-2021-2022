package xyz.marsavic.gfxlab.graphics3d;

import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.ColorSamplerT;
import xyz.marsavic.gfxlab.Vec3;


public abstract class RayTracer implements ColorSamplerT {

	protected abstract Color sample(Ray ray);
	
	
	@Override
	public Color sample(double t, Vector p) {
		return sample(Ray.pd(Vec3.ZERO, Vec3.pz(p, 1)));
	}
	
}
