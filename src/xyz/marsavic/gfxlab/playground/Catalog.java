package xyz.marsavic.gfxlab.playground;

import xyz.marsavic.functions.interfaces.Function1;
import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.*;
import xyz.marsavic.gfxlab.animation.Animation;
import xyz.marsavic.gfxlab.animation.AnimationColorSampling;
import xyz.marsavic.gfxlab.animation.RendererAggregate;
import xyz.marsavic.gfxlab.playground.animations.Strobe;
import xyz.marsavic.gfxlab.playground.animations.StrobeSync;
import xyz.marsavic.gfxlab.playground.colorsamplers.Blobs;
import xyz.marsavic.gfxlab.playground.colorsamplers.Spirals;
import xyz.marsavic.utils.Numeric;


public class Catalog {
	
	public static final Catalog INSTANCE = new Catalog();
	
	// Non-static methods for use with ObjectInstruments (until the support for static methods arrives)
	
	public ColorSamplers colorSamplers() { return colorSamplers; }
	public TransformationsFromSize transformationsFromSize() { return transformationsFromSize; }
	public Animations animations() { return animations; }
	
	
	// ---------------------------------------------------------------------------------------------------

	
	public static class ColorSamplers {
		public ColorSamplerT gradient () { return (t, p) -> Color.rgb(p.x(), 1-p.x(), 0.5 + 0.5 * Numeric.sinT(t)); }
		public ColorSamplerT oklab    () { return (t, p) -> Color.oklabPolar(t, p.x(), p.y()); }
		public ColorSamplerT gammaTest() { return (t, p) -> Color.gray(p.x() < 320 ? 0.5 : (p.xInt() ^ p.yInt()) & 1); }
		public Blobs blobs    () { return new Blobs(); }
		public Spirals spirals  () { return new Spirals(); }
	}
	
	public static final ColorSamplers colorSamplers = new ColorSamplers();

	
	// ---------------------------------------------------------------------------------------------------

	
	public static class TransformationsFromSize {
		public Function1<Transformation, Vec3> geometric() { return Transformation::toGeometric; }
		public Function1<Transformation, Vec3> unitBox  () { return Transformation::toUnitBox; }
	}
	
	public static final TransformationsFromSize transformationsFromSize = new TransformationsFromSize();

	
	// ---------------------------------------------------------------------------------------------------

	
	public static class Animations {
		public Animation<Matrix<Color>> strobe    () { return new Strobe(Vector.xy(500, 500)); }
		public Animation<Matrix<Color>> strobeSync() { return new StrobeSync(Vector.xy(500, 500)); }
		
		public AnimationColorSampling gradient () { return new AnimationColorSampling(Vec3.xyz( 100, 640, 640), transformationsFromSize.unitBox  (), colorSamplers.gradient ()); }
		public AnimationColorSampling gammaTest() { return new AnimationColorSampling(Vec3.xyz(   1, 640, 640),                                      colorSamplers.gammaTest()); }
		public AnimationColorSampling blobs    () { return new AnimationColorSampling(Vec3.xyz(1600, 400, 400), transformationsFromSize.geometric(), colorSamplers.blobs    ()); }
		public AnimationColorSampling spirals  () { return new AnimationColorSampling(Vec3.xyz( 200, 400, 400), transformationsFromSize.geometric(), colorSamplers.spirals  ()); }
		public AnimationColorSampling oklab    () { return new AnimationColorSampling(Vec3.xyz( 200, 400, 400), transformationsFromSize.unitBox  (), colorSamplers.oklab    ()); }
		
		public RendererAggregate blobsAggregate() { return new RendererAggregate(blobs()); }
	}
	
	public static final Animations animations = new Animations();
	
	
	// ---------------------------------------------------------------------------------------------------
	
}
