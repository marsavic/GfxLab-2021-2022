package xyz.marsavic.gfxlab;

import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.gui.UtilsGL;

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
		UtilsGL.parallel(data.length, y -> Arrays.fill(data[y], value));
	}
	
	
	
	// ...........................
	
	
	public static Matrix<Color> createBlack(Vector size) {
		return new Matrix<>(size, Color.BLACK);
	}
	
	
	public static Matrix<Color> add(Matrix<Color> a, Matrix<Color> b) {
		Vector size = a.size;
		if (!b.size.equals(size)) {
			throw new IllegalArgumentException("Sizes must match");
		}
		
		Matrix<Color> result = new Matrix<>(size);
		
		int sizeX = size.xInt();
		UtilsGL.parallelY(size, y -> {
			for (int x = 0; x < sizeX; x++) {
				result.set(x, y, a.get(x, y).add(b.get(x, y)));
			}
		});
		
		return result;
	}
	
	
	public static void addTo(Matrix<Color> toChange, Matrix<Color> byHowMuch) {
		Vector size = toChange.size;
		if (!byHowMuch.size.equals(size)) {
			throw new IllegalArgumentException("Sizes must match");
		}
		
		int sizeX = size.xInt();
		UtilsGL.parallelY(size, y -> {
			for (int x = 0; x < sizeX; x++) {
				toChange.set(x, y, toChange.get(x, y).add(byHowMuch.get(x, y)));
			}
		});
	}
	
	
	public static Matrix<Color> mul(Matrix<Color> a, double k) {
		Vector size = a.size;
		Matrix<Color> result = new Matrix<>(size);
		
		int sizeX = size.xInt();
		UtilsGL.parallelY(size, y -> {
			for (int x = 0; x < sizeX; x++) {
				result.set(x, y, a.get(x, y).mul(k));
			}
		});
		
		return result;
	}
	
	
	public static void mulTo(Matrix<Color> toChange, double factor) {
		Vector size = toChange.size;
		
		int sizeX = size.xInt();
		UtilsGL.parallelY(size, y -> {
			for (int x = 0; x < sizeX; x++) {
				toChange.set(x, y, toChange.get(x, y).mul(factor));
			}
		});
	}
	
}
