package xyz.marsavic.gfxlab.graphics3d;

import xyz.marsavic.gfxlab.Color;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public interface Scene {
	
	Collection<Body> bodies();
	Collection<Light> lights();
	Color backgroundColor();
	
	
	
	public static class Base implements Scene {
		protected List<Body> bodies = new ArrayList<>();
		protected List<Light> lights = new ArrayList<>();
		protected Color backgroundColor = Color.BLACK;
		
		
		@Override
		public Collection<Body> bodies() {
			return bodies;
		}
		
		@Override
		public Collection<Light> lights() {
			return lights;
		}
		
		@Override
		public Color backgroundColor() {
			return backgroundColor;
		}
		
		
		public void addBodiesFrom(Scene other) {
			bodies.addAll(other.bodies());
		}
		
		public void addLightsFrom(Scene other) {
			lights.addAll(other.lights());
		}
		
		public void addAllFrom(Scene other) {
			addBodiesFrom(other);
			addLightsFrom(other);
		}
		
	}
	
}
