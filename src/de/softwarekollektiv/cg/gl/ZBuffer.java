package de.softwarekollektiv.cg.gl;

import java.awt.Color;
import java.awt.Graphics;

class ZBuffer {

	private final Color[][] pixels;
	private final double[][] zindex;
	private final int width;
	private final int height;

	ZBuffer(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new Color[width][height];
		zindex = new double[width][height];
	}

	void setPixel(int x, int y, double z, Color c) {
		z += 3;
		if (zindex[x][y] < z) {
			zindex[x][y] = z;
			pixels[x][y] = c;
		}
	}

	void draw(Graphics g) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (zindex[x][y] >= 3) {
					g.setColor(pixels[x][y]);
					g.drawRect(x, y, 1, 1);
				}
			}
		}
	}
}
