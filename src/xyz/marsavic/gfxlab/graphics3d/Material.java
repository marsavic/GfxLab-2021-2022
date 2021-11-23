package xyz.marsavic.gfxlab.graphics3d;

import xyz.marsavic.gfxlab.Color;

public record Material (
		Color diffuse
) {
	public static final Material DIFFUSE = new Material(Color.WHITE);
}