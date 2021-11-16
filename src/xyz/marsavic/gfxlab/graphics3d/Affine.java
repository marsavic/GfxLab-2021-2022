package xyz.marsavic.gfxlab.graphics3d;


import xyz.marsavic.gfxlab.Transformation;
import xyz.marsavic.gfxlab.Vec3;
import xyz.marsavic.utils.Numeric;


public record Affine(
		double m00, double m01, double m02, double m03,
		double m10, double m11, double m12, double m13,
		double m20, double m21, double m22, double m23
//               0,          0,          0,          1
) implements Transformation {
	
	public static Affine IDENTITY = new Affine(
			1.0, 0.0, 0.0, 0.0,
			0.0, 1.0, 0.0, 0.0,
			0.0, 0.0, 1.0, 0.0
	);
	
	
	public static Affine unitVectors(Vec3 ex, Vec3 ey, Vec3 ez) {
		return new Affine(
				ex.x(), ey.x(), ez.x(), 0,
				ex.y(), ey.y(), ez.y(), 0,
				ex.z(), ey.z(), ez.z(), 0
		);
	}
	
	
	public Affine andThen(Affine t) {
		return new Affine(
				t.m00 * m00 + t.m01 * m10 + t.m02 * m20, t.m00 * m01 + t.m01 * m11 + t.m02 * m21, t.m00 * m02 + t.m01 * m12 + t.m02 * m22, t.m00 * m03 + t.m01 * m13 + t.m02 * m23 + t.m03,
				t.m10 * m00 + t.m11 * m10 + t.m12 * m20, t.m10 * m01 + t.m11 * m11 + t.m12 * m21, t.m10 * m02 + t.m11 * m12 + t.m12 * m22, t.m10 * m03 + t.m11 * m13 + t.m12 * m23 + t.m13,
				t.m20 * m00 + t.m21 * m10 + t.m22 * m20, t.m20 * m01 + t.m21 * m11 + t.m22 * m21, t.m20 * m02 + t.m21 * m12 + t.m22 * m22, t.m20 * m03 + t.m21 * m13 + t.m22 * m23 + t.m23
		);
	}
	
	
	@Override
	public Vec3 applyTo(Vec3 v) {
		return new Vec3(
				m00 * v.x() + m01 * v.y() + m02 * v.z() + m03,
				m10 * v.x() + m11 * v.y() + m12 * v.z() + m13,
				m20 * v.x() + m21 * v.y() + m22 * v.z() + m23
		);
	}
	
	
	public Vec3 applyWithoutTranslationTo(Vec3 v) {
		return new Vec3(
				m00 * v.x() + m01 * v.y() + m02 * v.z(),
				m10 * v.x() + m11 * v.y() + m12 * v.z(),
				m20 * v.x() + m21 * v.y() + m22 * v.z()
		);
	}
	
	
	@Override
	public Ray applyTo(Ray r) {
		return new Ray(applyTo(r.p()), applyWithoutTranslationTo(r.d()));
	}
	
	
	public Affine inverse() {
		double det = determinant();
		return new Affine(
				(m11 * m22 - m12 * m21) / det, -(m01 * m22 - m02 * m21) / det, (m01 * m12 - m02 * m11) / det, -(m01 * m12 * m23 + m02 * m13 * m21 + m03 * m11 * m22 - m03 * m12 * m21 - m02 * m11 * m23 - m01 * m13 * m22) / det,
				-(m10 * m22 - m12 * m20) / det, (m00 * m22 - m02 * m20) / det, -(m00 * m12 - m02 * m10) / det, (m00 * m12 * m23 + m02 * m13 * m20 + m03 * m10 * m22 - m03 * m12 * m20 - m02 * m10 * m23 - m00 * m13 * m22) / det,
				(m10 * m21 - m11 * m20) / det, -(m00 * m21 - m01 * m20) / det, (m00 * m11 - m01 * m10) / det, -(m00 * m11 * m23 + m01 * m13 * m20 + m03 * m10 * m21 - m03 * m11 * m20 - m01 * m10 * m23 - m00 * m13 * m21) / det
		);
	}
	
	
	public Affine transposeWithoutTranslation() {
		return new Affine(
				m00, m10, m20, 0,
				m01, m11, m21, 0,
				m02, m12, m22, 0
		);
	}
	
	
	private double determinant() {
		//noinspection UnaryPlus
		return (
				+ (m00 * m11 * m22)
				+ (m01 * m12 * m20)
				+ (m02 * m10 * m21)
				- (m02 * m11 * m20)
				- (m01 * m10 * m22)
				- (m00 * m12 * m21)
		);
	}
	
	
	public static Affine translation(Vec3 d) {
		return new Affine(
				1.0, 0.0, 0.0, d.x(),
				0.0, 1.0, 0.0, d.y(),
				0.0, 0.0, 1.0, d.z()
		);
	}
	
	public static Affine rotationAboutX(double angle) {
		return new Affine(
				1.0, 0.0, 0.0, 0.0,
				0.0, Numeric.cosT(angle), -Numeric.sinT(angle), 0.0,
				0.0, Numeric.sinT(angle), Numeric.cosT(angle), 0.0
		);
	}
	
	public static Affine rotationAboutY(double angle) {
		return new Affine(
				Numeric.cosT(angle), 0.0, Numeric.sinT(angle), 0.0,
				0.0, 1.0, 0.0, 0.0,
				-Numeric.sinT(angle), 0.0, Numeric.cosT(angle), 0.0
		);
	}
	
	public static Affine rotationAboutZ(double angle) {
		return new Affine(
				Numeric.cosT(angle), -Numeric.sinT(angle), 0.0, 0.0,
				Numeric.sinT(angle), Numeric.cosT(angle), 0.0, 0.0,
				0.0, 0.0, 1.0, 0.0
		);
	}
	
	public static Affine scaling(double c) {
		return new Affine(
				c, 0.0, 0.0, 0.0,
				0.0, c, 0.0, 0.0,
				0.0, 0.0, c, 0.0
		);
	}
	
	public static Affine scaling(Vec3 c) {
		return new Affine(
				c.x(), 0.0, 0.0, 0.0,
				0.0, c.y(), 0.0, 0.0,
				0.0, 0.0, c.z(), 0.0
		);
	}
	
	
	/**
	 * One possible linear transformation mapping EX to u and preserving angles between vectors.
	 */
	public static Affine asEX(Vec3 u) {
		// A simpler implementation using vector products is possible, but this is more efficient.
		if (u.x() != 0 || u.y() != 0) {
			double mSqr = u.x() * u.x() + u.y() * u.y();
			double lSqr = mSqr + u.z() * u.z();
			double l = Math.sqrt(lSqr);
			double m = 1.0 / Math.sqrt(mSqr);
			double q = l * m;
			
			return Affine.unitVectors(
					u,
					Vec3.xyz(-u.y() * q, u.x() * q, 0),
					Vec3.xyz(-u.x() * u.z() * m, -u.z() * u.y() * m, (u.x() * u.x() + u.y() * u.y()) * m)
			);
		} else {
			double q = 1.0 / u.z();
			return Affine.unitVectors(
					u,
					Vec3.xyz(q, 0, 0),
					Vec3.xyz(0, Math.abs(q), 0)
			);
		}
	}
	
	/**
	 * One possible linear transformation mapping EX to a vector u of unit length and preserving angles between vectors.
	 */
	public static Affine asEXN(Vec3 u_) {
		if (u_.x() != 0 || u_.y() != 0) {
			double mSqr = 1 - u_.z() * u_.z();
			double m = 1.0 / Math.sqrt(mSqr);
			
			return Affine.unitVectors(
					u_,
					Vec3.xyz(-u_.y() * m, u_.x() * m, 0),
					Vec3.xyz(-u_.x() * u_.z() * m, -u_.z() * u_.y() * m, (u_.x() * u_.x() + u_.y() * u_.y()) * m)
			);
		} else {
			return Affine.unitVectors(
					u_,
					Vec3.xyz(u_.z(), 0, 0),
					Vec3.xyz(0, Math.abs(u_.z()), 0)
			);
		}
	}
	
}
