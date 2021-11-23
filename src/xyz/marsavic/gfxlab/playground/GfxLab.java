package xyz.marsavic.gfxlab.playground;

import xyz.marsavic.gfxlab.*;
import xyz.marsavic.gfxlab.animation.*;
import xyz.marsavic.gfxlab.graphics3d.*;
import xyz.marsavic.gfxlab.graphics3d.cameras.Perspective;
import xyz.marsavic.gfxlab.graphics3d.cameras.TransformedCamera;
import xyz.marsavic.gfxlab.graphics3d.raytracers.RayTracerTest;
import xyz.marsavic.gfxlab.graphics3d.scenes.DiscoRoom;
import xyz.marsavic.gfxlab.tonemapping.ColorTransformForColorMatrix;
import xyz.marsavic.gfxlab.tonemapping.ToneMappingFunctionSimple;
import xyz.marsavic.objectinstruments.annotations.GadgetDouble;


public class GfxLab {
	
	Scene scene;
	Camera camera;
	RayTracer rayTracer;
	Animation<Matrix<Color>> animation;
	ToneMappingFunction toneMappingFunction;
	Animation<RawImage> toneMappedAnimation;
	
	
	@GadgetDouble(p = 0, q = 5)
	public double brightnessFactor = 1.0;
	
	public int nBalls = 0;
	
	public int nLights = 256;
	
	public int seed = 0;
	
	@GadgetDouble(p = -0.05, q = 0.05)
	public double y = 0;
	
	@GadgetDouble(p = 0, q = 0.5)
	public double fovAngle = 0.14;
	
	
	
	
	synchronized void setup() {
		scene = new DiscoRoom(nBalls, nLights, seed);
		
		camera = new TransformedCamera(
				Perspective.fov(fovAngle),
				Affine.IDENTITY
						.andThen(Affine.translation(Vec3.xyz(0, 0, -3)))
						.andThen(Affine.rotationAboutY(y))
		);
		
		rayTracer = new RayTracerTest(scene, Collider.BruteForce::new, camera);
		
		animation =
				new RendererAggregateLastFrame(
						new AnimationColorSampling(
								Vec3.xyz(1, 640, 640),
								Transformation::toGeometric,
								rayTracer
						)
				)
		;
		
		toneMappingFunction = new ToneMappingFunctionSimple(
				new ColorTransformForColorMatrix.Multiply(brightnessFactor)
		);
		
		toneMappedAnimation = new ToneMapperPerFrame(animation, toneMappingFunction, true);
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
		toneMappedAnimation = new ToneMapperPerFrame(animation, toneMappingFunction, true);
	}
	
	
	public Animation<RawImage> toneMappedAnimation() {
		return toneMappedAnimation;
	}
	
	
	public void gc() {
		System.gc();
	}
	
}
