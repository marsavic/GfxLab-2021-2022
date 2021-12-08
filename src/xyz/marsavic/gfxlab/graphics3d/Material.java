package xyz.marsavic.gfxlab.graphics3d;

import xyz.marsavic.gfxlab.Color;

public record Material (
		Color diffuse,
		Color specular,
		double shininess,
		Color reflective
) {
	
	public Material diffuse   (Color  diffuse   ) { return new Material(diffuse, specular, shininess, reflective); }
	public Material specular  (Color  specular  ) { return new Material(diffuse, specular, shininess, reflective); }
	public Material shininess (double shininess ) { return new Material(diffuse, specular, shininess, reflective); }
	public Material reflective(Color  reflective) { return new Material(diffuse, specular, shininess, reflective); }
	
	
	// --- Utility constants and factory methods ---
	
	public static final Material BLACK   = new Material(Color.BLACK, Color.BLACK, 32.0, Color.BLACK);
	
	public static Material matte (Color  c) { return BLACK.diffuse(c); }
	public static Material matte (double k) { return matte(Color.gray(k)); }
	public static Material matte (        ) { return matte(Color.WHITE); }
	public static final Material MATTE = matte();

	public static Material mirror(Color  c) { return BLACK.reflective(c); }
	public static Material mirror(double k) { return mirror(Color.gray(k)); }
	public static Material mirror(        ) { return mirror(Color.WHITE); }
	public static final Material MIRROR  = mirror();
	
	
}