package xyz.marsavic.gfxlab.graphics3d.textures;

import javafx.scene.image.Image;
import javafx.scene.image.WritablePixelFormat;
import xyz.marsavic.geometry.Box;
import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.graphics3d.Material;
import xyz.marsavic.gfxlab.graphics3d.Texture;
import xyz.marsavic.javafx.UtilsFX;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.stream.IntStream;


public class ImageTexture implements Texture {
	
	private final Material[][] matrix;
	private final Vector size;
	
	
	
	public ImageTexture(Image img) {
		size = UtilsFX.imageSize(img);
		matrix = new Material[size.yInt()][size.xInt()];
		
		int[] buffer = new int[size.areaInt()];
		
		img.getPixelReader().getPixels(
				0, 0,
				size.xInt(), size.yInt(),
				WritablePixelFormat.getIntArgbPreInstance(),
				buffer,
				0, size.xInt()
		);
		
		IntStream.range(0, size.yInt()).parallel().forEach(y -> {
			for (int x = 0; x < size.xInt(); x++) {
				int k = y * size.xInt() + x;
				Color c = Color.code(buffer[k]);
				matrix[y][x] = Material.matte(c);
			}
		});
	}
	
	
	public ImageTexture(InputStream is) {
		this(new Image(is));
	}
	
	
	public static ImageTexture fromFile(String fileName) {
		try {
			return new ImageTexture(new FileInputStream(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	@Override
	public Material materialAt(Vector uv) {
		Vector p = uv.mod(Box.UNIT).mul(size);
		return matrix[p.yInt()][p.xInt()];
	}
}
