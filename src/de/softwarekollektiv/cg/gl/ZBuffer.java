package de.softwarekollektiv.cg.gl;

import java.awt.Color;
import java.awt.Graphics;

import de.softwarekollektiv.cg.gl.math.Vector3f;

public class ZBuffer {

	private final Vector3f[][] pixel;
	private final double[][] zindex;
	private final int width;
	private final int height;

	private ZBuffer(int width, int height, final Vector3f[][] pixel, final double[][] zindex) {
		this.width = width;
		this.height = height;
		this.pixel = pixel;
		this.zindex = zindex;
	}
	
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
		if(x < 0 || y < 0 || x >= width || y >= height)
			return;
					
		if (z < zindex[x][y]) {
			zindex[x][y] = z;
			pixel[x][y] = c;
		}
	}

	ZBuffer smooth() {
		Vector3f[][] newpixel = new Vector3f[width][height];
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				newpixel[x][y] = Vector3f.ZERO;
				newpixel[x][y] = newpixel[x][y].add(getPixel(x - 1, y - 1).scale(1.0 / 36));
				newpixel[x][y] = newpixel[x][y].add(getPixel(x - 1, y).scale(4.0 / 36));
				newpixel[x][y] = newpixel[x][y].add(getPixel(x - 1, y + 1).scale(1.0 / 36));
				newpixel[x][y] = newpixel[x][y].add(getPixel(x, y - 1).scale(4.0 / 36));
				newpixel[x][y] = newpixel[x][y].add(getPixel(x, y).scale(16.0 / 36));
				newpixel[x][y] = newpixel[x][y].add(getPixel(x, y + 1).scale(4.0 / 36));
				newpixel[x][y] = newpixel[x][y].add(getPixel(x + 1, y - 1).scale(1.0 / 36));
				newpixel[x][y] = newpixel[x][y].add(getPixel(x + 1, y).scale(4.0 / 36));
				newpixel[x][y] = newpixel[x][y].add(getPixel(x + 1, y + 1).scale(1.0 / 36));
			}
		}
		return new ZBuffer(width, height, newpixel, zindex);
	}
	
	public Vector3f getPixel(int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height)
			return Vector3f.ZERO;
			
		return pixel[x][y];
	}
	
	public void paintOnCanvas(Graphics g) {
		// Flip screen.
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Vector3f pixel = getPixel(x, y);
				Color col = new Color((float) pixel.getX(),
						(float) pixel.getY(), (float) pixel.getZ());
				g.setColor(col);
				g.drawRect(width - x, height - y, 1, 1);
			}
		}
	}
}
