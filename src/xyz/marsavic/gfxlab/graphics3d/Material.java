package xyz.marsavic.gfxlab.graphics3d;

import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.Color;

public record Material (
		Color diffuse,
		Color specular,
		double shininess,
		Color reflective,
		Color refractive,
		double refractiveIndex
) implements Texture {
	
	public Material diffuse        (Color  diffuse        ) { return new Material(diffuse, specular, shininess, reflective, refractive, refractiveIndex); }
	public Material specular       (Color  specular       ) { return new Material(diffuse, specular, shininess, reflective, refractive, refractiveIndex); }
	public Material shininess      (double shininess      ) { return new Material(diffuse, specular, shininess, reflective, refractive, refractiveIndex); }
	public Material reflective     (Color  reflective     ) { return new Material(diffuse, specular, shininess, reflective, refractive, refractiveIndex); }
	public Material refractive     (Color  refractive     ) { return new Material(diffuse, specular, shininess, reflective, refractive, refractiveIndex); }
	public Material refractiveIndex(double refractiveIndex) { return new Material(diffuse, specular, shininess, reflective, refractive, refractiveIndex); }
	
	
	// --- Utility constants and factory methods ---
	
	public static final Material BLACK   = new Material(Color.BLACK, Color.BLACK, 32.0, Color.BLACK, Color.BLACK, 1.4);
	
	public static Material matte (Color  c) { return BLACK.diffuse(c); }
	public static Material matte (double k) { return matte(Color.gray(k)); }
	public static Material matte (        ) { return matte(Color.WHITE); }
	public static final Material MATTE = matte();

	public static Material mirror(Color  c) { return BLACK.reflective(c); }
	public static Material mirror(double k) { return mirror(Color.gray(k)); }
	public static Material mirror(        ) { return mirror(Color.WHITE); }
	public static final Material MIRROR  = mirror();
	
	public static Material glass (Color c ) { return BLACK.refractive(c); }
	public static Material glass (double k) { return glass(Color.gray(k)); }
	public static Material glass (        ) { return glass(Color.WHITE); }
	public static final Material GLASS = glass();
	
	
	@Override
	public Material materialAt(Vector uv) {
		return this;
	}
	
}