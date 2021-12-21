package xyz.marsavic.gfxlab;

import xyz.marsavic.gfxlab.graphics3d.solids.Box;


public interface BoxedObjectFactory<T> {

	T b(Box b);
	T pd(Vec3 p, Vec3 d);
	T pq(Vec3 p, Vec3 q);
	T cr(Vec3 c, Vec3 r);
	T r(Vec3 r);
	T e();
	
	default T r(double r) { return r(Vec3.xyz(r, r, r)); }
	
	
	Vec3 E_2 = Vec3.EXYZ.div(2);
	
	
	interface B<T> extends BoxedObjectFactory<T> {
		@Override default T pd(Vec3 p, Vec3 d) { return b(Box.$.pd(p, d)); }
		@Override default T pq(Vec3 p, Vec3 q) { return b(Box.$.pq(p, q)); }
		@Override default T cr(Vec3 c, Vec3 r) { return b(Box.$.cr(c, r)); }
		@Override default T r(Vec3 r) { return b(Box.$.r(r)); }
		@Override default T e() { return b(Box.$.e()); }
	}
	
	interface CR<T> extends BoxedObjectFactory<T> {
		@Override default T b(Box b) { return cr(b.c(), b.r()); }
		@Override default T pd(Vec3 p, Vec3 d) { Vec3 r = d.div(2); return cr(p.add(r), r); }
		@Override default T pq(Vec3 p, Vec3 q) { return cr(p.add(q).div(2), q.sub(p).div(2)); }
		@Override default T r(Vec3 r) { return cr(Vec3.ZERO, r); }
		@Override default T e() { return cr(E_2, E_2); }
	}
	
	interface PQ<T> extends BoxedObjectFactory<T> {
		@Override default T b(Box b) { return pq(b.p(), b.q()); }
		@Override default T pd(Vec3 p, Vec3 d) { return pq(p, p.add(d)); }
		@Override default T cr(Vec3 c, Vec3 r) { return pq(c.sub(r), c.add(r)); }
		@Override default T r(Vec3 r) { return pq(r.inverse(), r); }
		@Override default T e() { return pq(Vec3.ZERO, Vec3.EXYZ); }
	}
	
	interface PD<T> extends BoxedObjectFactory<T> {
		@Override default T b(Box b) { return pd(b.p(), b.d()); }
		@Override default T pq(Vec3 p, Vec3 q) { return pd(p, q.sub(p)); }
		@Override default T cr(Vec3 c, Vec3 r) { return pd(c.sub(r), r.mul(2)); }
		@Override default T r(Vec3 r) { return pd(r.inverse(), r.mul(2)); }
		@Override default T e() { return pd(Vec3.ZERO, Vec3.EXYZ); }
	}
	
}


