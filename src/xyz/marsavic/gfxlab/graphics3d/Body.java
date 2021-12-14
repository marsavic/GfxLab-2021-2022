package xyz.marsavic.gfxlab.graphics3d;


import xyz.marsavic.geometry.Vector;

public interface Body {
	
	/**
	 * Geometric solid representing the shape of the body.
	 */
	Solid solid();
	
	/**
	 * Material at the specified hit on the surface of the body.
	 */
	Material materialAt(Hit hit);
	
	
	
	/**
	 * Returns a body having the given material at each surface point.
	 */
	public static Body uniform(Solid solid, Material material) {
		return new Body() {
			@Override
			public Solid solid() {
				return solid;
			}
			
			@Override
			public Material materialAt(Hit p) {
				return material;
			}
		};
	}
	

	/**
	 * Returns a body having the default material at each surface point.
	 */
	public static Body uniform(Solid solid) {
		return uniform(solid, Material.MATTE);
	}

	
	public static Body textured(Solid solid, Texture texture) {
		return new Body() {
			@Override
			public Solid solid() {
				return solid;
			}
			
			@Override
			public Material materialAt(Hit p) {
				return texture.materialAt(p.uv());
			}
		};
	}
}
