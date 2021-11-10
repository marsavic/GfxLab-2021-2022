package xyz.marsavic.gfxlab.playground.colorsamplers;

import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.ColorSamplerT;
import xyz.marsavic.utils.Numeric;


public class Spirals implements ColorSamplerT {
	
	public double offset = 0.0;
	
	@Override
	public Color sample(double t, Vector p) {
		double t_ = t + offset;
		return Color.rgb(
				Math.max(0, Numeric.sinT(-t_ + 7 * p.angle())),
				Math.max(0, Numeric.sinT(2 * t_ + 0.25 / p.length() + p.angle())),
				0.4
		);
	}
}
