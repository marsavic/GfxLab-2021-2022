package xyz.marsavic.gfxlab;

public record Material (
		Color diffuse
) {
	public static final Material DIFFUSE = new Material(Color.WHITE);
}
