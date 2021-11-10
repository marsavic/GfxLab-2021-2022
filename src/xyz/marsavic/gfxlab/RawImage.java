package xyz.marsavic.gfxlab;


import xyz.marsavic.geometry.Vector;


public record RawImage(
		int width,
		int[] pixels
) {
	
	public RawImage(Vector size) {
		this(size.xInt(), new int[size.areaInt()]);
	}
	
	
	public int height() {
		return pixels().length / width();
	}
	
	
	public Vector size() {
		return Vector.xy(width(), height());
	}
	
	
	public void copyFrom(RawImage source) {
		if (!size().equals(source.size())) {
			throw new IllegalArgumentException("Sizes must match");
		}
		System.arraycopy(source.pixels(), 0, pixels(), 0, width);
	}
	
}
