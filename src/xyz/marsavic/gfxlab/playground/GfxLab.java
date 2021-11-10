package xyz.marsavic.gfxlab.playground;

import xyz.marsavic.gfxlab.*;
import xyz.marsavic.gfxlab.animation.Animation;
import xyz.marsavic.gfxlab.animation.AnimationColorSampling;
import xyz.marsavic.gfxlab.animation.ToneMapperPerFrame;
import xyz.marsavic.gfxlab.graphics3d.RayTracer;
import xyz.marsavic.gfxlab.graphics3d.raytracers.RayTracerTest;
import xyz.marsavic.gfxlab.tonemapping.ToneMappingFunctionSimple;

import java.util.List;
import java.util.concurrent.ForkJoinPool;


public class GfxLab implements Runnable {
	
	public void setAnimation(Animation<Matrix<Color>> animation) {
		toneMappedAnimation = new ToneMapperPerFrame(animation, new ToneMappingFunctionSimple());
	}
	

	RayTracer rayTracer;
	Animation<Matrix<Color>> animation;
	public Animation<RawImage> toneMappedAnimation;
	
	public Animation<RawImage> toneMappedAnimation() {
		return toneMappedAnimation;
	}
	
	public void run() {
		setup(0, 0.3);
	}
	
	public void setup(double y, double z) {
		rayTracer = new RayTracerTest(y, z);
		
		animation = new AnimationColorSampling(
				Vec3.xyz(1, 640, 640),
				Transformation::toGeometric,
				rayTracer
		);
		
		toneMappedAnimation = new ToneMapperPerFrame(animation, new ToneMappingFunctionSimple());
	}
	
	
	
	// .............................................
	
	
	public List<Object> instrumentedAtStart = List.of(
			Catalog.INSTANCE,
			this
	);
	
	
	// .............................................
	

	public void startService() {
		pool.submit(() -> {
			try {
				run();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	public static final ForkJoinPool pool;
	public static final int parallelism;
	
	static {
		boolean obsRunning = ProcessHandle.allProcesses().anyMatch(ph -> ph.info().command().orElse("").contains("obs64"));
		parallelism = obsRunning ? 4 : ForkJoinPool.getCommonPoolParallelism();
		pool = new ForkJoinPool(parallelism);
	}
	
}
