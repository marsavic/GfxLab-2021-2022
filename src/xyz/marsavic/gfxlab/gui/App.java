package xyz.marsavic.gfxlab.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import xyz.marsavic.gfxlab.playground.GfxLab;
import xyz.marsavic.gfxlab.gui.instruments.InstrumentAnimationRawImage;
import xyz.marsavic.gfxlab.resources.Resources;
import xyz.marsavic.objectinstruments.Context;
import xyz.marsavic.objectinstruments.Panel;
import xyz.marsavic.objectinstruments.instruments.InstrumentObject;
import xyz.marsavic.objectinstruments.instruments.InstrumentText;
import xyz.marsavic.objectinstruments.instruments.PollingInstrument;
import xyz.marsavic.reactions.collections.ReactiveSet;
import xyz.marsavic.reactions.collections.ReactiveSetBacked;
import xyz.marsavic.statistics.DistributionStatistics;


public class App extends Application {
	
	private final GfxLab gfxLab = new GfxLab();
	
	private final ReactiveSet<Object> specialObjects = ReactiveSetBacked.withLinkedHashSet();
	
	{
		DistributionStatistics<String> ds = new DistributionStatistics<>(String::length);
//		specialObjects.addAll(gfxLab.animations);
	}
	
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("GFX Lab");
		
		Panel panelL = new Panel();
//		Context contextL = Context.defaultForObjectSet(new ReactiveSetUnion<>(specialObjects, panelL.objectSet()));
		Context contextL = Context.defaultForPanel(panelL);
		for (var object : gfxLab.instrumentedAtStart) {
			panelL.addInstrument(InstrumentObject.of(object, contextL));
		}
		
		Panel panelR = new Panel();
		
		panelR.addInstruments(
				new PollingInstrument<>(new InstrumentAnimationRawImage(), () -> gfxLab.toneMappedAnimation),
				new InstrumentText(() -> Profiling.infoTextSystem() + Profiling.infoTextProfilers(), 170)
		);
		
		
		/*
		ReactiveObjectSet<Object> objectSet = panel.objectSet();
		
		List<Tuple2<Class<?>, Function0<Factory<?>>>> factoryCreators =
				new ArrayList<>(Factory.defaultFactoryCreatorsList);
		
		factoryCreators.add(
				new Tuple2<>(RawImage.class, FactoryBoolean::new)
		);

		
		Context context = new Context(
				objectSet,
				TypeMap.concat(
						TypeMap.firstMatch(factoryCreators),
						TypeMap.f(type -> () -> new FactoryReference<>(objectSet.ofType(type)))
				)
		);
*/


//		ScrollPane sp = new ScrollPane(panelL.region());

//		sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
//		sp.setFitToWidth(true);
//		sp.setFitToHeight(true);
//		sp.setPannable(true);
		
		Pane root = new HBox(panelL.region(), panelR.region());
		HBox.setHgrow(panelL.region(), Priority.ALWAYS);
		
		Scene scene = new Scene(root, 1066, 866);
		
		scene.getStylesheets().setAll(Resources.stylesheetURL);
		
		primaryStage.getIcons().setAll(Resources.iconsApplication());
		primaryStage.setScene(scene);
//		primaryStage.size.setMaximized(true);
		primaryStage.show();
		
		
		gfxLab.startService();
	}
}
