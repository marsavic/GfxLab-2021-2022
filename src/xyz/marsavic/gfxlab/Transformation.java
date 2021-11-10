package xyz.marsavic.gfxlab;


public interface Transformation {
	
	Vec3 applyTo(Vec3 p);
	
	// --------------------
	
	Transformation identity = p -> p;
	
	
	static Transformation toUnitBox(Vec3 size) {
		return p -> p.div(size);
	}
	
	
	static Transformation toGeometric(Vec3 size) {
		Vec3 s = Vec3.xyz( 1,  2, -2);
		Vec3 t = Vec3.xyz( 0, -1,  1);
		return p -> p.div(size).mul(s).add(t);
	}
	
}
