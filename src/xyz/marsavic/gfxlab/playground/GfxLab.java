package xyz.marsavic.gfxlab.playground;

import xyz.marsavic.gfxlab.*;
import xyz.marsavic.gfxlab.animation.*;
import xyz.marsavic.gfxlab.graphics3d.RayTracer;
import xyz.marsavic.gfxlab.graphics3d.raytracers.RayTracerTest;
import xyz.marsavic.gfxlab.tonemapping.ToneMappingFunctionSimple;
import xyz.marsavic.objectinstruments.annotations.GadgetDouble;


public class GfxLab {
	
	RayTracer rayTracer;
	Animation<Matrix<Color>> animation;
	Animation<RawImage> toneMappedAnimation;

	
	@GadgetDouble(p = -2, q = 2)
	public double planeY = -1.0;
	
	@GadgetDouble(p = 2, q = 20)
	public double ballZ = 3;

	

	synchronized void setup() {
		rayTracer = new RayTracerTest(planeY, ballZ);
		
		animation =
				new RendererAggregateLastFrame(
						new AnimationColorSampling(
								Vec3.xyz(1, 640, 640),
								Transformation::toGeometric,
								rayTracer
						)
				)
		;
		
		toneMappedAnimation = new ToneMapperPerFrame(animation, new ToneMappingFunctionSimple(), true);
	}
	
	
	// .......................................................................................................
	
	
	
	// This method is called when any of this class' public fields is changed.
	public synchronized void onChange() {
		if (animation instanceof Renderer renderer) {
			// Some cleanup because renderers can run their own threads.
			renderer.dispose();
		}
		
		setup();
	}
	
	
	// TODO Bug: not using the full cpu when the animation is AnimationColorSampling (for fast samplers).
	
	public synchronized void setAnimation(Animation<Matrix<Color>> animation) {
		toneMappedAnimation = new ToneMapperPerFrame(animation, new ToneMappingFunctionSimple());
	}
	
	
	public Animation<RawImage> toneMappedAnimation() {
		return toneMappedAnimation;
	}
	
	
	public void gc() {
		System.gc();
	}
	
}
