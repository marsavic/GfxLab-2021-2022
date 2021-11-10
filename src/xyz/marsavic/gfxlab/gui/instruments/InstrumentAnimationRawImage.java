package xyz.marsavic.gfxlab.gui.instruments;

import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.animation.Animation;
import xyz.marsavic.gfxlab.RawImage;
import xyz.marsavic.gfxlab.gui.UtilsFX;
import xyz.marsavic.objectinstruments.instruments.ObjectInstrument;


public class InstrumentAnimationRawImage extends ObjectInstrument<Animation<RawImage>> {
	
	private final Pane pane;
	private final ImageView imageView;
	
	private WritableImage image;
	
	private int iFrameNext;
	
	
	
	public InstrumentAnimationRawImage(Animation<RawImage> animation) {
		imageView = new ImageView();
		pane = new VBox(imageView);
		
		setObject(animation);
	}
	
	
	public InstrumentAnimationRawImage() {
		this(null);
	}
	
	
	@Override
	protected void afterSetObject() {
		iFrameNext = 0;
	}
	
	
	private void checkSize(Vector size) {
		if ((image == null) || !UtilsFX.imageSize(image).equals(size)) {
			image = UtilsFX.createWritableImage(size);
			imageView.setImage(image);
		}
	}
	
	
	@Override
	public void update() {
		if (object() != null) {
			iFrameNext %= object().nFrames();
			RawImage frame = object().frame(iFrameNext++);
			checkSize(frame.size());
			UtilsFX.imageFromRawImage(image, frame);
		} else {
			imageView.setImage(null);
		}
	}
	
	
	@Override
	public Region node() {
		return pane;
	}
}
