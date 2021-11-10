package xyz.marsavic.gfxlab;

import xyz.marsavic.geometry.Vector;
import xyz.marsavic.utils.Utils;

import java.util.Arrays;


public class Matrix<E> {
	
	private final Vector size;
	private final E[][] data;
	
	
	public Matrix(Vector size, E initialValue) {
		this.size = size.floor();
		//noinspection unchecked
		data = (E[][]) new Object[size.yInt()][size.xInt()];
		
		if (initialValue != null) {
			fill(initialValue);
		}
	}
	
	
	public Matrix(Vector size) {
		this(size, null);
	}
	
	
	public Vector size() {
		return size;
	}
	
	
	public E get(int x, int y) {
		return data[y][x];
	}
	
	
	public E get(Vector p) {
		return get(p.xInt(), p.yInt());
	}
	
	
	public void set(int x, int y, E value) {
		data[y][x] = value;
	}
	
	
	public void set(Vector p, E value) {
		set(p.xInt(), p.yInt(), value);
	}
	
	
	public void fill(E value) {
		Utils.parallel(data.length, y -> Arrays.fill(data[y], value));
	}
	
	
	
	public static Matrix<Color> createBlack(Vector size) {
		return new Matrix<>(size, Color.BLACK);
	}
	
}
