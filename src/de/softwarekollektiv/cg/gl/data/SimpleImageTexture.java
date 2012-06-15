package de.softwarekollektiv.cg.gl.data;

import java.awt.Color;
import java.awt.image.BufferedImage;

import de.softwarekollektiv.cg.gl.Texture;
import de.softwarekollektiv.cg.gl.math.Coordinate2f;
import de.softwarekollektiv.cg.gl.math.Vector3f;

public class SimpleImageTexture implements Texture {

	private final BufferedImage image;
	private final Coordinate2f a;
	private final Coordinate2f b;
	private final Coordinate2f c;

	public SimpleImageTexture(BufferedImage image, double[][] abc) {
		this(image, new Coordinate2f(abc[0][0], abc[0][1]), new Coordinate2f(
				abc[1][0], abc[1][1]), new Coordinate2f(abc[2][0], abc[2][1]));
	}

	public SimpleImageTexture(BufferedImage image, Coordinate2f a,
			Coordinate2f b, Coordinate2f c) {
		this.image = image;
		this.a = a;
		this.b = b;
		this.c = c;
	}

	@Override
	public Vector3f getColor(double lambda1, double lambda2, double lambda3) {
		double vx = lambda1 * a.getX() + lambda2 * b.getX() + lambda3
				* c.getX();
		double vy = lambda1 * a.getY() + lambda2 * b.getY() + lambda3
				* c.getY();
		int x = (int) (image.getWidth() * (1 - vx));
		int y = (int) (image.getHeight() * (1 - vy));
		int color = image.getRGB(x, y);

		// Convert to RGB.		
		Color c = new Color(color);
		return new Vector3f(c.getRed() / 255.0, c.getGreen() / 255.0, c.getBlue() / 255.0);
	}
}
