package xyz.marsavic.gfxlab.playground;

import xyz.marsavic.gfxlab.*;
import xyz.marsavic.gfxlab.animation.*;
import xyz.marsavic.gfxlab.graphics3d.*;
import xyz.marsavic.gfxlab.graphics3d.raytracers.RayTracerTest;
import xyz.marsavic.gfxlab.graphics3d.solids.Ball;
import xyz.marsavic.gfxlab.graphics3d.solids.HalfSpace;
import xyz.marsavic.gfxlab.tonemapping.ColorTransformForColorMatrix;
import xyz.marsavic.gfxlab.tonemapping.ToneMappingFunctionSimple;
import xyz.marsavic.objectinstruments.annotations.GadgetDouble;

import java.util.Collection;
import java.util.List;


public class GfxLab {
	
	Scene scene;
	RayTracer rayTracer;
	Animation<Matrix<Color>> animation;
	ToneMappingFunction toneMappingFunction;
	Animation<RawImage> toneMappedAnimation;
	
	
	@GadgetDouble(p = 0, q = 5)
	public double brightnessFactor = 1.0;
	
	@GadgetDouble(p = -10, q = 10)
	public double lightZ = 0;
	
	
	
	synchronized void setup() {
		scene = new Scene() {
			@Override
			public Collection<Body> bodies() {
				return List.of(
		            Body.uniform(
							Ball.cr(Vec3.xyz(1, 0, 4), 2),
				            new Material(Color.hsb(0, 0.8, 1.0))
		            ),
		            Body.uniform(
							Ball.cr(Vec3.xyz(-0.3, -0.6, 2.7), 0.7),
				            new Material(Color.hsb(0.66, 0.8, 1.0))
		            ),
		            Body.uniform(
							HalfSpace.pn(Vec3.xyz(0, -1, 0), Vec3.xyz(0, 1, 0)),
							new Material(Color.hsb(0.33, 0.8, 1.0))
		            ),
		            Body.uniform(
							HalfSpace.pn(Vec3.xyz(1, 0, 0), Vec3.xyz(-1, 0, 0)),
							new Material(Color.hsb(0.33, 0.8, 1.0))
		            )
				);
			}
			
			@Override
			public Collection<Light> lights() {
				return List.of(
						Light.pc(Vec3.xyz(-1, 1, lightZ), Color.WHITE)
				);
			}
		};
		
		rayTracer = new RayTracerTest(scene, Collider.BruteForce::new);
		
		animation =
//				new RendererAggregateLastFrame(
						new AnimationColorSampling(
								Vec3.xyz(1, 640, 640),
								Transformation::toGeometric,
								rayTracer
						)
//				)
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
