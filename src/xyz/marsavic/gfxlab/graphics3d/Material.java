package xyz.marsavic.gfxlab.graphics3d;

import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.Color;

public record Material (
		Color diffuse,
		Color specular,
		double shininess,
		Color reflective,
		Color refractive,
		double refractiveIndex,
		
		Color emittance,
		BSDF bsdf
) implements Texture {
	
	public Material diffuse        (Color  diffuse        ) { return new Material(diffuse, specular, shininess, reflective, refractive, refractiveIndex, emittance); }
	public Material specular       (Color  specular       ) { return new Material(diffuse, specular, shininess, reflective, refractive, refractiveIndex, emittance); }
	public Material shininess      (double shininess      ) { return new Material(diffuse, specular, shininess, reflective, refractive, refractiveIndex, emittance); }
	public Material reflective     (Color  reflective     ) { return new Material(diffuse, specular, shininess, reflective, refractive, refractiveIndex, emittance); }
	public Material refractive     (Color  refractive     ) { return new Material(diffuse, specular, shininess, reflective, refractive, refractiveIndex, emittance); }
	public Material refractiveIndex(double refractiveIndex) { return new Material(diffuse, specular, shininess, reflective, refractive, refractiveIndex, emittance); }
	public Material emittance      (Color  emittance      ) { return new Material(diffuse, specular, shininess, reflective, refractive, refractiveIndex, emittance); }
	
	
	public Material(Color diffuse, Color specular, double shininess, Color reflective, Color refractive, double refractiveIndex, Color emittance) {
		this(diffuse, specular, shininess, reflective, refractive, refractiveIndex, emittance,
				BSDF.add(
						new BSDF[] {
								BSDF.diffuse   (diffuse),
								BSDF.reflective(reflective),
								BSDF.refractive(refractive, refractiveIndex)
						},
						new double[] {
								diffuse.luminance(),
								reflective.luminance(),
								refractive.luminance()
						}
				)
		);
	}
	
	
	public Material(BSDF bsdf) {
		this(Color.BLACK, Color.BLACK, 32.0, Color.BLACK, Color.BLACK, 1.4, Color.BLACK, bsdf);
	}
	
	
	@Override
	public Material materialAt(Vector uv) {
		return this;
	}
	
	
	// --- Utility constants and factory methods ---
	
	public static final Material BLACK   = new Material(Color.BLACK, Color.BLACK, 32.0, Color.BLACK, Color.BLACK, 1.4, Color.BLACK, BSDF.ABSORPTIVE);
	
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

	public static final Material LIGHT = Material.BLACK.emittance(Color.WHITE);
}