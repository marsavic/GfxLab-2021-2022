package xyz.marsavic.gfxlab.gui.instruments;

import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import xyz.marsavic.gfxlab.RawImage;
import xyz.marsavic.gfxlab.gui.UtilsFX;
import xyz.marsavic.objectinstruments.instruments.ObjectInstrument;


public class InstrumentRawImage extends ObjectInstrument<RawImage> {

	private final Pane pane;
	private final ImageView imageView;
	
	private WritableImage image;
	
	
	
	public InstrumentRawImage(RawImage rawImage) {
		imageView = new ImageView();
		pane = new VBox(imageView);
		
		setObject(rawImage);
	}
	
	
	@Override
	protected void afterSetObject() {
		if ((image == null) || !UtilsFX.imageSize(image).equals(object().size())) {
			image = UtilsFX.createWritableImage(object().size());
			imageView.setImage(image);
		}
		update();
	}
	
	
	@Override
	public void update() {
		UtilsFX.imageFromRawImage(image, object());
	}
	
	
	@Override
	public Region node() {
		return pane;
	}
}
