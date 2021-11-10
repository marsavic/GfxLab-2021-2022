package xyz.marsavic.gfxlab.playground.colorsamplers;

import xyz.marsavic.geometry.Vector;
import xyz.marsavic.gfxlab.Color;
import xyz.marsavic.gfxlab.ColorSamplerT;
import xyz.marsavic.random.sampling.Sampler;
import xyz.marsavic.utils.Numeric;

public class Blobs implements ColorSamplerT {
	
	private final int n;
	
	public double m = 0.1;
	public double threshold = 0.2;
	
	private final double[] fx;
	private final double[] fy;
	private final double[] px;
	private final double[] py;
	
	private final Sampler sampler = new Sampler();
	
	
	public Blobs(int n) {
		this.n = n;
		fx = new double[n];
		fy = new double[n];
		px = new double[n];
		py = new double[n];
		shuffle();
	}
	
	
	public Blobs() {
		this(3);
	}
	
	
	public void shuffle() {
		for (int i = 0; i < n; i++) {
			fx[i] = Math.floor(sampler.uniform(-10, 11));
			fy[i] = Math.floor(sampler.uniform(-10, 11));
			px[i] = sampler.uniform();
			py[i] = sampler.uniform();
		}
	}
	
	
	@Override
	public Color sample(double t, Vector q) {
		double s = 0;
		for (int i = 0; i < n; i++) {
			Vector c = Vector.xy(Numeric.cosT(fx[i] * t + px[i]), Numeric.sinT(fy[i] * t + py[i])).mul(0.5);
			double d = q.sub(c).lengthSquared();
			s += Math.exp(-d / m);
		}
		double k = s / 3 - threshold;
		return (k < 0 ? Color.rgb(0.3, 0.0, 0.4) : Color.rgb(0.9, 0.8, 0.1)).mul(Math.abs(k));
	}
}
