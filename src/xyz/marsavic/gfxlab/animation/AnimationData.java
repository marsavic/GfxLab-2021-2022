package xyz.marsavic.gfxlab.animation;

import xyz.marsavic.functions.interfaces.Function0;
import xyz.marsavic.functions.interfaces.Function1;
import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.Matrix;
import xyz.marsavic.gfxlab.RawImage;
import xyz.marsavic.gfxlab.Vec3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Thread-safe
 */
public class AnimationData<F> extends Animation<F> {

	private final List<F> frames;
	
	
	
	public AnimationData(int nFrames, Function0<F> frameSupplier) {
		frames = Collections.synchronizedList(new ArrayList<>(nFrames));
		for (int frame = 0; frame < nFrames; frame++) {
			frames.add(frameSupplier.at());
		}
	}
	

	public AnimationData(int nFrames) {
		this(nFrames, () -> null);
	}

	
	public AnimationData(Vec3 size, Function1<F, Vector> frameSupplierBySize) {
		this((int) size.x(), () -> frameSupplierBySize.at(size.p12()));
	}
	
	
	@Override
	public void dispose() {
	}
	
	
	@Override
	public int nFrames() {
		return frames.size();
	}
	
	
	@Override
	public F frame(int iFrame) {
		return frames.get(iFrame);
	}
	
	
	protected void setFrame(int iFrame, F frame) {
		frames.set(iFrame, frame);
		dispatcherAnimationUpdate.fire(new EventAnimationUpdate(iFrame));
	}


	
	public static AnimationData<RawImage> createRawImage(Vec3 size) {
		return new AnimationData<>((int) size.x(), () -> new RawImage(size.p12()));
	}

	public static AnimationData<Matrix<?>> createMatrix(Vec3 size) {
		return new AnimationData<>((int) size.x(), () -> new Matrix<>(size.p12()));
	}

	public static <E> AnimationData<Matrix<E>> createMatrix(Vec3 size, E initialValue) {
		return new AnimationData<>((int) size.x(), () -> new Matrix<>(size.p12(), initialValue));
	}

	public static AnimationData<Matrix<Color>> createMatrixColor(Vec3 size) {
		return new AnimationData<>((int) size.x(), () -> new Matrix<>(size.p12(), Color.BLACK));
	}
	
}
