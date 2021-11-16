package xyz.marsavic.gfxlab.gui.instruments;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.RawImage;
import xyz.marsavic.gfxlab.animation.Animation;
import xyz.marsavic.gfxlab.gui.UtilsGL;
import xyz.marsavic.gfxlab.resources.Resources;
import xyz.marsavic.objectinstruments.instruments.ObjectInstrument;
import xyz.marsavic.time.Profiler;
import org.kordamp.ikonli.javafx.FontIcon;


public class InstrumentAnimationRawImage extends ObjectInstrument<Animation<RawImage>> {
	
	private final Pane pane;
	private final ImageView imageView;
	private final CheckBox chbEnabled;
	private final Spinner<Integer> spnIFrame;
	private final Button btnCopyToClipboard;
	private final TextField txfFileName;
	private final Button btnSaveImage;
	
	
	private WritableImage image;
	
	private int iFrameRequested;
	private boolean frameRequested;
	private RawImage frameLastFetched;
	
	private final Profiler profilerFetch = UtilsGL.profiler(this, "fetch");
	
	private final Object monitor = new Object();
	private boolean shouldStop = false;
	
	
	
	public InstrumentAnimationRawImage(Animation<RawImage> animation) {
		imageView = new ImageView();
		chbEnabled = new CheckBox();
		chbEnabled.setSelected(true);
		spnIFrame = new Spinner<>();
		btnCopyToClipboard = new Button(null, new FontIcon(Resources.Ikons.COPY));
		btnSaveImage = new Button(null, new FontIcon(Resources.Ikons.SAVE));
		txfFileName = new TextField();
		
		btnCopyToClipboard.setOnAction(event -> UtilsGL.copyImageToClipboard(image()));
		btnSaveImage.setOnAction(event -> UtilsGL.saveImageToFileWithDialog(image()));
		
		HBox controlPanel = new HBox(
				chbEnabled,
				spnIFrame,
				btnCopyToClipboard,
				btnSaveImage,
				txfFileName
		);
		
		HBox.setHgrow(txfFileName, Priority.ALWAYS);
//		controlPanel.setPrefHeight(24);
		
		
		pane = new VBox(imageView, controlPanel);
		
		setObject(animation);

		
		Runnable loop = () -> {
			while (!shouldStop) {
				synchronized (monitor) { // Probably bad thread safety since object is not changed inside synchronized(objectMonitor)
					// TODO Add monitoring animation.onUpdate?
					while ((object() == null) || !frameRequested) { // thread safety?
						try {
							monitor.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				profilerFetch.enter();
				frameLastFetched = object().frame(iFrameRequested);
				frameRequested = false;
				profilerFetch.exit();
			}
		};
		
		Thread thread = new Thread(loop);
		thread.setDaemon(true);
		thread.start();
	}
	
	
	public InstrumentAnimationRawImage() {
		this(null);
	}
	
	
	private Image image() {
		return image;
	}
	
	
	@Override
	public void dismiss() {
		shouldStop = true;
	}
	

	private void checkSize(Vector size) {
		if ((image == null) || !UtilsGL.imageSize(image).equals(size)) {
			image = UtilsGL.createWritableImage(size);
			imageView.setImage(image);
		}
	}

	
	void showFrame(RawImage frame) {
		checkSize(frame.size());
		UtilsGL.imageFromRawImage(image, frame);
	}
	
	
	private void requestFrame(int iFrame) {
		synchronized (monitor) {
			frameRequested = true;
			iFrameRequested = iFrame;
			monitor.notifyAll();
		}
	}
	
	
	int iFrameNext = 0;
	
	
	@Override
	protected void afterSetObject() {
		iFrameNext = 0;
		if (object() != null) {
			spnIFrame.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(-1, object().nFrames() - 1, -1));
		}
		requestFrame(0);
	}
	
	
	private RawImage frameLastShown;
	
	@Override
	public void update() {
		if (!chbEnabled.isSelected()) {
			return;
		}
		
		if (object() != null) {
			if (frameLastFetched != frameLastShown) {
				frameLastShown = frameLastFetched;
				showFrame(frameLastShown);
			}
			
			Integer iFrame = spnIFrame.getValue();
			if (iFrame == -1) {
				iFrame = iFrameNext;
				iFrameNext = (iFrameNext + 1) % object().nFrames();
			}
			requestFrame(iFrame);
		}
	}
	
	
	@Override
	public Region node() {
		return pane;
	}
	
}
