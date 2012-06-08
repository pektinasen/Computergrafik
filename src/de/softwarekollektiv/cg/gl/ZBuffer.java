package de.softwarekollektiv.cg.gl;

import java.awt.Color;
import java.awt.Graphics;

import de.softwarekollektiv.cg.gl.math.Vector3f;

class ZBuffer {

	private final Vector3f[][] pixels;
	private final double[][] zindex;
	private final int width;
	private final int height;

	ZBuffer(int width, int height) {
		this.width = width;
		this.height = height;
		pixels = new Vector3f[width][height];
		zindex = new double[width][height];
	}

	void setPixel(int x, int y, double z, Vector3f c) {
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
					Color col = new Color((float) pixels[x][y].getX(),
							(float) pixels[x][y].getY(),
							(float) pixels[x][y].getZ());
					g.setColor(col);
					g.drawRect(x, y, 1, 1);
				}
			}
		}
	}
}
