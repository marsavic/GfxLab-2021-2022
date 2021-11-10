package xyz.marsavic.gfxlab.animation;

import xyz.marsavic.functions.interfaces.Function1;
import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.Matrix;
import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.gfxlab.ColorSampler;
import xyz.marsavic.gfxlab.Transformation;
import xyz.marsavic.random.RNG;
import xyz.marsavic.utils.Utils;


public class AnimationColorSampling extends Animation<Matrix<Color>> {
	
	private final ColorSampler colorSampler;
	private final Transformation transformation;
	
	private final int nFrames;
	private final Vec3 size;
	private final Vector sizeFrame;
	private final RNG[] rngs;
	
	
	
	public AnimationColorSampling(Vec3 size, Transformation transformation, ColorSampler colorSampler) {
		this.transformation = transformation;
		this.colorSampler = colorSampler;
		
		this.size = size;
		nFrames = (int) size.x();
		sizeFrame = size.p12();

		rngs = new RNG[nFrames];
		for (int i = 0; i < nFrames; i++) {
			rngs[i] = new RNG();
		}
//		rngs = Utils.createArray(nFrames, RNG::new); // TODO !?
	}
	
	
	public AnimationColorSampling(Vec3 size, Function1<Transformation, Vec3> transformationFromSize, ColorSampler colorSampler) {
		this(size, transformationFromSize.at(size), colorSampler);
	}

	
	public AnimationColorSampling(Vec3 size, ColorSampler colorSampler) {
		this(size, Transformation.identity, colorSampler);
	}
	
	
	@Override
	public void dispose() {
	}
	
	
	public Vec3 size() {
		return size;
	}
	
	
	public Vector sizeFrame() {
		return sizeFrame;
	}
	
	
	@Override
	public int nFrames() {
		return nFrames;
	}
	
	
	public ColorSampler colorSampler() {
		return colorSampler;
	}
	
	
	public void advance(int iFrame) {
		rngs[iFrame] = rngs[iFrame].split();
		dispatcherAnimationUpdate.fire(new EventAnimationUpdate(iFrame));
	}
	
	
	@Override
	public Matrix<Color> frame(int iFrame) {
		Matrix<Color> frame = new Matrix<>(sizeFrame);
		RNG rng = rngs[iFrame];                           // TODO should start from the same seed if asked again without advance
		
		Utils.parallelY(sizeFrame, y -> {
			int sizeFrameX = sizeFrame.xInt();
			for (int x = 0; x < sizeFrameX; x++) {
				Vec3 offset = Vec3.random(rng);
				//noinspection SuspiciousNameCombination
				Vec3 pViewSpace = Vec3.xyz(iFrame, x, y).add(offset);
				Vec3 p = transformation.applyTo(pViewSpace);
				
				Color c = colorSampler.sample(p);
				
				frame.set(x, y, c);
			}
		});
		
		return frame;
	}
	
}
