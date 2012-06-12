package de.softwarekollektiv.cg.gl;

import java.awt.Color;
import java.awt.Graphics;

import de.softwarekollektiv.cg.gl.math.Vector3f;

class ZBuffer {

	private final Vector3f[][] pixel;
	private final double[][] zindex;
	private final int width;
	private final int height;

	ZBuffer(int width, int height, final Vector3f bgcol) {
		this.width = width;
		this.height = height;
		this.pixel = new Vector3f[width][height];
		this.zindex = new double[width][height];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				zindex[x][y] = Double.MAX_VALUE;
				pixel[x][y] = bgcol;
			}
		}
	}

	void setPixel(int x, int y, double z, final Vector3f c) {
		if (z < zindex[x][y]) {
			zindex[x][y] = z;
			pixel[x][y] = c;
		}
	}

	void draw(Graphics g) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Color col = new Color((float) pixel[x][y].getX(),
						(float) pixel[x][y].getY(), (float) pixel[x][y].getZ());
				g.setColor(col);
				g.drawRect(x, y, 1, 1);
			}
		}
	}

}
