package de.softwarekollektiv.cg.uebungen.uebung7;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Texture {

	private BufferedImage bi;
	private double[][] abc;	
	
	Texture(BufferedImage bi, double[][] abc) {
		this.bi = bi;
		this.abc = abc;
	}
	
	Color getColor(double lambda1, double lambda2, double lambda3) {
		double sum = lambda1 + lambda2 + lambda3;
		double vx = (lambda1 * abc[0][0] + lambda2 * abc[1][0] + lambda3 * abc[2][0]) / sum;
		double vy = (lambda1 * abc[0][1] + lambda2 * abc[1][1] + lambda3 * abc[2][1]) / sum;
		int x = (int) (bi.getWidth() * vx);
		int y = (int) (bi.getHeight() * vy);
		return new Color(bi.getRGB(x, y));
	}
}
