package xyz.marsavic.gfxlab.resources;

import javafx.scene.image.Image;

import java.util.Arrays;
import java.util.Objects;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.materialdesign2.MaterialDesignC;


public class Resources {
	
	private static Image[] iconsApplication = null;
	
	public static final String stylesheetURL = xyz.marsavic.objectinstruments.resources.Resources.stylesheetURL;
	
	
	public static Image[] iconsApplication() {
		if (iconsApplication == null) {
			String iconName = "mars";
			
			iconsApplication = Arrays.stream((new String[]{"016", "024", "032", "048", "064", "128"})).map(size ->
					new Image(Objects.requireNonNull(Resources.class.getResourceAsStream("icons/" + iconName + " " + size + ".png")))
			).toArray(Image[]::new);
		}
		return iconsApplication;
	}
	
	
	public static class Ikons {
		// https://kordamp.org/ikonli/cheat-sheet-materialdesign2.html
		public static final Ikon COPY = MaterialDesignC.CONTENT_COPY;
		public static final Ikon SAVE = MaterialDesignC.CONTENT_SAVE;
	}
	
}
