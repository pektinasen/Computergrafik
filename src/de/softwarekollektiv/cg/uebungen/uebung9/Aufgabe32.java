package de.softwarekollektiv.cg.uebungen.uebung9;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Aufgabe32 extends JFrame {
	public static void main(String[] args) {
		new Aufgabe32();
	}

	private final LineSegment[] mirrors;
	private final float px;
	private final float py;

	private final List<LineSegment> lines;

	Aufgabe32() {
		mirrors = new LineSegment[4];
		mirrors[0] = new LineSegment(-2, 3, 2, 3);
		mirrors[1] = new LineSegment(-2, -3, 2, -3);
		mirrors[2] = new LineSegment(3, -2, 3, 2);
		mirrors[3] = new LineSegment(-3, -2, -3, 2);
		px = 2;
		py = 1;

		lines = new LinkedList<LineSegment>();
		
		// THE ALGORITHM
		for(float alpha = 0; alpha <= 2 * Math.PI; alpha += (Math.PI / 500)) {
			
			final int max_iterations = 10;			
			int cur_iteration = 0;
			int cur_mirror = -1;
			Ray cur_ray = new Ray(px, py, px + Math.sin(alpha) * 100, py + Math.cos(alpha) * 100);
			boolean reflected = false;
			do {
				reflected = false;
				for(int m = 0; m < 4; m++) {
					if(m == cur_mirror)
						continue;
					
					Point p;
					if((p = mirrors[m].intersect(cur_ray)) != null) {
						lines.add(new LineSegment(cur_ray.x, cur_ray.y, p.x, p.y));
						
						// Calculate the reflection.
						double dx = cur_ray.x - p.x;
						double dy = cur_ray.y - p.y;						
						if(m < 2)
							// Horizontal mirror? Mirror vertically.
							cur_ray = new Ray(p.x, p.y, cur_ray.x - 2 * dx, cur_ray.y);
						else
							cur_ray = new Ray(p.x, p.y, cur_ray.x, cur_ray.y - 2 * dy);					
						
						reflected = true;
						cur_mirror = m;
						break;
					}
				}
			} while(++cur_iteration <= max_iterations && reflected);
			
			// Add the final ray.
			if(cur_iteration <= max_iterations) {
				double vx = cur_ray.toX - cur_ray.x;
				double vy = cur_ray.toY - cur_ray.y;
				lines.add(new LineSegment(cur_ray.x, cur_ray.y, cur_ray.x + 10 * vx, cur_ray.y + 10 * vy));
			}
		}
		
		

		final JPanel canvas = new JPanel() {	
			int tx(double xy) {
				final double translate = 5;
				final double scale = 50;
				return (int) ((xy + translate) * scale);
			}
			
			@Override
			protected void paintComponent(Graphics g) {
				// Draw rays.
				g.setColor(Color.BLACK);
				for (LineSegment ls : lines) {
					g.drawLine(tx(ls.x1), tx(ls.y1), tx(ls.x2), tx(ls.y2));
				}

				// Draw point.
				g.setColor(Color.RED);
				g.drawRect(tx(px) - 1, tx(py) - 1, 3, 3);

				// Draw mirrors.
				g.setColor(Color.BLUE);
				for (LineSegment ls : mirrors) {
					g.drawLine(tx(ls.x1), tx(ls.y1), tx(ls.x2), tx(ls.y2));
				}
			}
		};
		this.add(canvas);
		this.setSize(600, 600);
		this.setVisible(true);
	}
	
	private static class Point {
		double x, y;
		Point(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}
	
	private static class Ray {
		double x, y;
		double toX, toY;
		
		Ray(double x, double y, double toX, double toY) {
			this.x = x;
			this.y = y;
			this.toX = toX;
			this.toY = toY;
		}
	}

	private static class LineSegment {
		double x1, y1, x2, y2;

		LineSegment(double x1, double y1, double x2, double y2) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}

		public Point intersect(Ray r) {
		    double mua, mub;
		    double denom, numera, numerb;
		   
		    denom = (r.toY - r.y) * (x2 - x1) - (r.toX - r.x) * (y2 - y1);
		    numera = (r.toX - r.x) * (y1 - r.y) - (r.toY - r.y) * (x1 - r.x);
		    numerb = (x2 - x1) * (y1 - r.y) - (y2 - y1) * (x1 - r.x);

		    // Colinear lines would mean:
		    // denom == 0 && numera == 0 && numerb == 0.
		    // Parallel lines would mean:
		    // denom == 0.
		    // So we test only one.
		    
		    if(Math.abs(denom) < 0.000000000001)
		    	return null;
		    
		    /* Is the intersection along the segment/ray? */
		    mua = numera / denom;
		    mub = numerb / denom;
		    if(mua >= 0 && mua <= 1 && mub >= 0) {
		    	double isx = x1 + mua * (x2 - x1);
		    	double isy = y1 + mua * (y2 - y1);
		    	return new Point(isx, isy);
		    }
		    
		    return null;
		}
	}
}
