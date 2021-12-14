package xyz.marsavic.gfxlab.graphics3d.textures;

import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.graphics3d.Material;
import xyz.marsavic.gfxlab.graphics3d.Texture;


public class ColorSpace implements Texture {
	
	@Override
	public Material materialAt(Vector uv) {
		return Material.matte(Color.rgb(uv.x(), uv.y(), 1 - uv.x() - uv.y()));
	}
	
}
