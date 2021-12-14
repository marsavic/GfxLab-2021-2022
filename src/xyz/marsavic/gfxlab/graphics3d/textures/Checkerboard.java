package xyz.marsavic.gfxlab.graphics3d.textures;

import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.graphics3d.Material;
import xyz.marsavic.gfxlab.graphics3d.Texture;


public class Checkerboard implements Texture {
	
	private final Vector size;
	private final Material materialA, materialB;
	
	
	
	public Checkerboard(Vector size, Material materialA, Material materialB) {
		this.size = size;
		this.materialA = materialA;
		this.materialB = materialB;
	}
	

	@Override
	public Material materialAt(Vector uv) {
		Vector p = uv.div(size).floor();
		return ((p.xInt() ^ p.yInt()) & 1) == 0 ? materialA : materialB;
	}
	
}
