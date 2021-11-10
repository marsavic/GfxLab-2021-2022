package xyz.marsavic.gfxlab;

import xyz.marsavic.geometry.Vector;
import xyz.marsavic.random.RNG;
import xyz.marsavic.utils.Numeric;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;


public class Vec3 {
	public static final Vec3 ZERO = xyz(0, 0, 0);
	public static final Vec3 ONES = xyz(1, 1, 1);
	public static final Vec3 EX   = xyz(1, 0, 0);
	public static final Vec3 EY   = xyz(0, 1, 0);
	public static final Vec3 EZ   = xyz(0, 0, 1);
	public static final Vec3 EXY  = xyz(1, 1, 0);
	public static final Vec3 EYZ  = xyz(0, 1, 1);
	public static final Vec3 EZX  = xyz(1, 0, 1);
	public static final Vec3 EXYZ = xyz(1, 1, 1);
	public static final Vec3[] E  = {EX, EY, EZ};

	public static final Vec3 P012 = xyz(0, 1, 2);
	public static final Vec3 P021 = xyz(0, 2, 1);
	public static final Vec3 P102 = xyz(1, 0, 2);
	public static final Vec3 P120 = xyz(1, 2, 0);
	public static final Vec3 P201 = xyz(2, 0, 1);
	public static final Vec3 P210 = xyz(2, 1, 0);
	public static final Vec3[] PERMUTATIONS = {P012, P021, P102, P120, P201, P210};
	
	private final double x, y, z;
	
	
	
	public Vec3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	
	public static Vec3 xyz(double x, double y, double z) {
		return new Vec3(x, y, z);
	}
	
	
	public static Vec3 fromArray(int[] i) {
		return xyz(i[0], i[1], i[2]);
	}
	
	
	public static Vec3 fromArray(double[] i) {
		return xyz(i[0], i[1], i[2]);
	}
	
	
	public static Vec3 xp(double x, Vector p) {
		return xyz(x, p.x(), p.y());
	}
	
	
	public static Vec3 py(Vector p, double y) {
		return xyz(p.x(), y, p.y());
	}
	
	
	public static Vec3 pz(Vector p, double z) {
		return xyz(p.x(), p.y(), z);
	}
	
	
	
	
	public double x() {
		return x;
	}
	
	
	public double y() {
		return y;
	}
	
	
	public double z() {
		return z;
	}
	
	
	public double get(int i) {
		return switch (i) {
			case 0 -> x();
			case 1 -> y();
			case 2 -> z();
			default -> throw new IllegalArgumentException();
		};
	}
	
	
	public double[] toArray() {
		return new double[] { x(), y(), z() };
	}
	
	
	public int[] toArrayInt() {
		return new int[] { (int) x(), (int) y(), (int) z() };
	}
	
	
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		Vec3 vec3 = (Vec3) o;
		
		return
				(vec3.x() == x()) &&
				(vec3.y() == y()) &&
				(vec3.z() == z())
				;
	}
	
	@Override
	public int hashCode() {
		int result;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	
	public Vec3 add(Vec3 o) {
		return xyz(x() + o.x(), y() + o.y(), z() + o.z());
	}
	
	
	public Vec3 sub(Vec3 o) {
		return xyz(x() - o.x(), y() - o.y(), z() - o.z());
	}
	
	
	public Vec3 mul(double k) {
		return xyz(x() * k, y() * k, z() * k);
	}
	
	
	public Vec3 mul(Vec3 o) {
		return xyz(x() * o.x(), y() * o.y(), z() * o.z());
	}


	public Vec3 div(double k) {
		return xyz(x() / k, y() / k, z() / k);
	}
	
	
	public Vec3 div(Vec3 o) {
		return xyz(x() / o.x(), y() / o.y(), z() / o.z());
	}
	
	
	
	public double lengthSquared() {
		return x() * x() + y() * y() + z() * z();
	}
	
	
	public double length() {
		return Math.sqrt(lengthSquared());
	}
	
	
	public Vec3 normalized_() {
		return div(length());
	}
	
	
	public Vec3 normalizedTo(double l) {
		return mul(l / length());
	}
	
	
	public Vec3 inverse() {
		return xyz(-x(), -y(), -z());
	}
	
	
	public double dot(Vec3 o) {
		return x() * o.x() + y() * o.y() + z() * o.z();
	}
	
	
	public Vec3 cross(Vec3 o) {
		return xyz(
			y() * o.z() - z() * o.y(),
			z() * o.x() - x() * o.z(),
			x() * o.y() - y() * o.x()
		);
	}
	
	
	public Vec3 projection(Vec3 d) {
		return d.mul(this.dot(d) / d.lengthSquared());
	}
	

	public Vec3 projectionN(Vec3 d_) {
		return d_.mul(this.dot(d_));
	}
	
	
	public Vec3 rejection(Vec3 d) {
		return this.sub(projection(d));
	}
	
	
	public Vec3 rejectionN(Vec3 d_) {
		return this.sub(projectionN(d_));
	}
	
	
	public double min() {
		return Math.min(Math.min(x(), y()), z());
	}

	
	public double max() {
		return Math.max(Math.max(x(), y()), z());
	}
	
	
	public Vec3 minIndicator() {
		if (x() < y()) {
			return x() < z() ? Vec3.EX : Vec3.EZ;
		} else {
			return y() < z() ? Vec3.EY : Vec3.EZ;
		}
	}
	
	
	public Vec3 maxIndicator() {
		if (x() > y()) {
			return x() > z() ? Vec3.EX : Vec3.EZ;
		} else {
			return y() > z() ? Vec3.EY : Vec3.EZ;
		}
	}
	
	
	public int minIndex() {
		if (x() < y()) {
			return x() < z() ? 0 : 2;
		} else {
			return y() < z() ? 1 : 2;
		}
	}
	
	
	public int maxIndex() {
		if (x() > y()) {
			return x() > z() ? 0 : 2;
		} else {
			return y() > z() ? 1 : 2;
		}
	}
	
	
	public Vec3 ranks() {
		if (x() < y()) {
			if (y() < z()) {
				return P012;
			} else {
				if (x() < z()) {
					return P021;
				} else {
					return P201;
				}
			}
		} else {
			if (x() < z()) {
				return P102;
			} else {
				if (y() < z()) {
					return P120;
				} else {
					return P210;
				}
			}
		}
	}
	
	
	public Vec3 sign() {
		return xyz(Numeric.sign(x()), Numeric.sign(y()), Numeric.sign(z()));
	}
	
	
	public Vec3 abs() {
		return xyz(Math.abs(x()), Math.abs(y()), Math.abs(z()));
	}
	
	
	public boolean allZero() {
		return (x() == 0) && (y() == 0) && (z() == 0);
	}

	public boolean anyZero() {
		return (x() == 0) || (y() == 0) || (z() == 0);
	}
	
	
	public Vec3 floor() {
		return xyz(Math.floor(x()), Math.floor(y()), Math.floor(z()));
	}
	
	public Vec3 floor(Vec3 d) {
		return this.div(d).floor().mul(d);
	}
	
	public Vec3 ceil() {
		return xyz(Math.ceil(x()), Math.ceil(y()), Math.ceil(z()));
	}
	
	public Vec3 ceil(Vec3 d) {
		return this.div(d).ceil().mul(d);
	}
	
	public Vec3 round() {
		return xyz(Math.round(x()), Math.round(y()), Math.round(z()));
	}
	
	public Vec3 round(Vec3 d) {
		return this.div(d).round().mul(d);
	}
	
/** Snaps each component to floor or ceil, depending on whether the corresponding component of s is not one. */

	public Vec3 snap(Vec3 s) {
		return Vec3.xyz (
			s.x() != 1 ? Math.floor(x()) : Math.ceil(x()),
			s.y() != 1 ? Math.floor(y()) : Math.ceil(y()),
			s.z() != 1 ? Math.floor(z()) : Math.ceil(z())
		);
	}
	
	
	
	public double product() {
		return x() * y() * z();
	}
	
	
	public Vec3 f(DoubleUnaryOperator f) {
		return Vec3.f(f, this);
	}
	
	
	public Vec3 f(DoubleBinaryOperator f, Vec3 v) {
		return Vec3.f(f, this, v);
	}
	
	
	public Vec3[] split() {
		return new Vec3[] {
				Vec3.xyz(x(), 0, 0),
				Vec3.xyz(0, y(), 0),
				Vec3.xyz(0, 0, z()),
		};
	}
	
	
	public Vec3 only(int i) {
		return switch (i) {
			case 0 -> Vec3.xyz(x(), 0, 0);
			case 1 -> Vec3.xyz(0, y(), 0);
			case 2 -> Vec3.xyz(0, 0, z());
			default -> throw new IllegalArgumentException();
		};
	}

	
	public Vector p01() {
		return Vector.xy(x(), y());
	}

	public Vector p12() {
		return Vector.xy(y(), z());
	}
	
	
	@Override
	public String toString() {
		return String.format("(%6.2f, %6.2f, %6.2f)", x(), y(), z());
	}
	
	
	public static Vec3 f(DoubleUnaryOperator f, Vec3 u) {
		return Vec3.xyz(
			f.applyAsDouble(u.x()),
			f.applyAsDouble(u.y()),
			f.applyAsDouble(u.z())
		);
	}
	
	
	public static Vec3 f(DoubleBinaryOperator f, Vec3 u, Vec3 v) {
		return Vec3.xyz(
			f.applyAsDouble(u.x(), v.x()),
			f.applyAsDouble(u.y(), v.y()),
			f.applyAsDouble(u.z(), v.z())
		);
	}
	
	
	public static Vec3 min(Vec3 a, Vec3 b) {
		return xyz(
				Math.min(a.x(), b.x()),
				Math.min(a.y(), b.y()),
				Math.min(a.z(), b.z())
		);
	}
	
	
	public static Vec3 max(Vec3 a, Vec3 b) {
		return xyz(
				Math.max(a.x(), b.x()),
				Math.max(a.y(), b.y()),
				Math.max(a.z(), b.z())
		);
	}
	
	
	public static Vec3 lerp(Vec3 v0, Vec3 v1, double t) {
		return v0.mul(1 - t).add(v1.mul(t));
	}

	
	public static Vec3 random(RNG rng) {
		return Vec3.xyz(rng.nextDouble(), rng.nextDouble(), rng.nextDouble());
	}
	
}
