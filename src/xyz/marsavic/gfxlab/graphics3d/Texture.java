package xyz.marsavic.gfxlab.graphics3d;


import xyz.marsavic.geometry.Vector;


public interface Texture {
	
	Material materialAt(Vector uv);
	
}
