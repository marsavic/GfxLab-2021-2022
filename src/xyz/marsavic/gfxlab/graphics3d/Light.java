package xyz.marsavic.gfxlab.graphics3d;

import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.Vec3;


/** Point light. */
public record Light (
		Vec3 p,
		Color c
) {
	
	public static Light pc(Vec3 p, Color c) {
		return new Light(p, c);
	}
	
}
