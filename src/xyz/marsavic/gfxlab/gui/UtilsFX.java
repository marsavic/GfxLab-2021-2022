package xyz.marsavic.gfxlab.gui;

import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.RawImage;

import java.nio.IntBuffer;


public class UtilsFX {

	static final PixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbPreInstance();
	
	
	public static Image imageFromRawImage(RawImage rawImage) {
		int sx = rawImage.width();
		int sy = rawImage.height();
		WritableImage image = new WritableImage(sx, sy);
		imageFromRawImage(image, rawImage);
		return image;
	}
	
	
	public static void imageFromRawImage(WritableImage outImage, RawImage inRawImage) {
		int sx = inRawImage.width();
		int sy = inRawImage.height();
		outImage.getPixelWriter().setPixels(0, 0, sx, sy, pixelFormat, inRawImage.pixels(), 0, sx);
	}
	
	
	public static Vector imageSize(Image image) {
		return Vector.xy(image.getWidth(), image.getHeight());
	}

	
	public static WritableImage createWritableImage(Vector size) {
		return new WritableImage(size.xInt(), size.yInt());
	}
	
}
