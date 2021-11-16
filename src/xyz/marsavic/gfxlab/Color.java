package xyz.marsavic.gfxlab;

import xyz.marsavic.utils.Numeric;

import java.util.function.DoubleUnaryOperator;

/**
 * Colors in linear sRGB color space.
 */
public class Color {
	public static final Color BLACK = rgb(0, 0, 0);
	public static final Color WHITE = rgb(1, 1, 1);
	
	
	final double r, g, b;
 
 
 
	private Color(double r, double g, double b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
 
	
	public static Color rgb(double r, double g, double b) {
		return new Color(r, g, b);
	}
	
	
	public static Color rgb(Vec3 v) {
		return Color.rgb(v.x(), v.y(), v.z());
	}
	
	
	public static Color gray(double k) {
		return rgb(k, k, k);
	}
	
	
	public static Color hsb(double h, double s, double b) {
		h = Numeric.mod1(h);
		int base = (int) (h * 6.0);
		double f = h * 6.0 - base;
		double p = b * (1.0 - s);
		double q = b * (1.0 - s * f);
		double t = b * (1.0 - s * (1.0 - f));
		return switch (base) {
			case 0 -> Color.rgb(b, t, p);
			case 1 -> Color.rgb(q, b, p);
			case 2 -> Color.rgb(p, b, t);
			case 3 -> Color.rgb(p, q, b);
			case 4 -> Color.rgb(t, p, b);
			case 5 -> Color.rgb(b, p, q);
			default -> null;
		};
	}
	
	
	public static Color oklab(double l, double a, double b) {
		double l_ = l + 0.3963377774f * a + 0.2158037573f * b;
		double m_ = l - 0.1055613458f * a - 0.0638541728f * b;
		double s_ = l - 0.0894841775f * a - 1.2914855480f * b;
		
		double cl = l_ * l_ * l_;
		double cm = m_ * m_ * m_;
		double cs = s_ * s_ * s_;
		
		double cr =  4.0767245293f * cl - 3.3072168827f * cm + 0.2307590544f * cs;
		double cg = -1.2681437731f * cl + 2.6093323231f * cm - 0.3411344290f * cs;
		double cb = -0.0041119885f * cl - 0.7034763098f * cm + 1.7068625689f * cs;
		
		return
			(
				cr < 0 || cr > 1 ||
				cg < 0 || cg > 1 ||
				cb < 0 || cb > 1
			) ? Color.BLACK :
			Color.rgb(cr, cg, cb);
	}
	

	public static Color oklabPolar(double h, double c, double l) {
		return oklab(
			l,
			c * Numeric.cosT(h),
			c * Numeric.sinT(h)
		);
	}
	
	
	public static Color code(int code) {
		return rgb(
				byteToValue((code >> 16) & 0xFF),
				byteToValue((code >>  8) & 0xFF),
				byteToValue((code      ) & 0xFF)
		);
	}
	
	
	
	public Color add(Color o) {
		return rgb(r + o.r, g + o.g, b + o.b);
	}
	
	
	public Color sub(Color o) {
		return rgb(r - o.r, g - o.g, b - o.b);
	}
	
	
	public Color mul(double c) {
		return rgb(r * c, g * c, b * c);
	}
	
	
	public Color mul(Color o) {
		return rgb(r * o.r, g * o.g, b * o.b);
	}
	
	
	public Color div(double c) {
		return rgb(r / c, g / c, b / c);
	}
	
	
	public Color pow(double c) {
		return rgb(
				Math.pow(r, c),
				Math.pow(g, c),
				Math.pow(b, c)
		);
	}
	
	
	public Color f(DoubleUnaryOperator f) {
		return rgb(
				f.applyAsDouble(r),
				f.applyAsDouble(g),
				f.applyAsDouble(b)
		);
	}
	
	
	public double luminance() {
		return
				0.212655 * r +
				0.715158 * g +
				0.072187 * b;
	}
	
	
	/**
	 * For colors with components in [0, 1].
	 */
	public double perceivedLightness() {
		double y = luminance(); // y should be between 0.0 and 1.0
		
		if (y <= 216.0 / 24389.0 ) {      // The CIE standard states 0.008856 but 216/24389 is the intent for 0.008856451679036
			return y * (24389.0 / 27.0);  // The CIE standard states 903.3, but 24389/27 is the intent, making 903.296296296296296
		} else {
			return Math.pow(y, 1.0 / 3.0) * 116.0 - 16.0;
		}
	}
	
	
	public double max() {
		return Math.max(r, Math.max(g, b));
	}
	
	
	public int code() {
		return
				(0xFF000000)    |
				(valueToByte(r) << 16) |
				(valueToByte(g) <<  8) |
				(valueToByte(b)      );
	}
	
	
	public int codeClamp() {
		return
				(0xFF000000)    |
				(valueToByteClamp(r) << 16) |
				(valueToByteClamp(g) <<  8) |
				(valueToByteClamp(b)      );
	}
	
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		Color color = (Color) o;
		
		if (Double.compare(color.r, r) != 0) return false;
		if (Double.compare(color.g, g) != 0) return false;
		return Double.compare(color.b, b) == 0;
	}
	
	
	@Override
	public int hashCode() {
		int result;
		long temp;
		temp = Double.doubleToLongBits(r);
		result = (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(g);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(b);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	
	
	public boolean zero() {
		return (r == 0.0 && g == 0.0 && b == 0.0);
	}
	
	public boolean notZero() {
		return (r != 0.0 || g != 0.0 || b != 0.0);
	}
	
	public boolean one() {
		return (r == 1.0 && g == 1.0 && b == 1.0);
	}
	
	public boolean notOne() {
		return (r != 1.0 || g != 1.0 || b != 1.0);
	}
	
	
	@Override
	public String toString() {
		return String.format("(r: %6.3f, g: %6.3f, b: %6.3f | Y: %6.3f)", r, g, b, luminance());
	}
	

	// ====================================================================================================
	
	
	/**
	 * sRGB gamma function. Approx. pow(v, 2.2).
	 */
	public static double inverseGamma(double v) {
		if (v <= 0.04045) {
			return v / 12.92;
		} else {
			return Math.pow((v + 0.055) / 1.055, 2.4);
		}
	}
	
	
	/**
	 * Inverse of sRGB gamma function. Approx. pow(v, 1 / 2.2).
	 */
	public static double gamma(double v) {
		if (v <= 0.0031308) {
			return v * 12.92;
		} else {
			return 1.055 * Math.pow(v, 1.0 / 2.4) - 0.055;
		}
	}
	
	
	public static int valueToByte(double x) {
		return (int) (gamma(x) * 255 + 0.5);
	}
	
	
	public static int valueToByteClamp(double x) {
		return Math.min(valueToByte(x), 255);
	}
	
	
	public static double byteToValue(int x) {
		return inverseGamma(x / 255.0);
	}
	
}
