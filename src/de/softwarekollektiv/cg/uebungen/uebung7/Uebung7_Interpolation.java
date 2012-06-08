package de.softwarekollektiv.cg.uebungen.uebung7;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.ejml.simple.SimpleMatrix;

import sun.java2d.loops.DrawLine;

import de.softwarekollektiv.cg.gl.math.Coordinate2f;
import de.softwarekollektiv.cg.uebungen.uebung3.Point;

import static de.softwarekollektiv.cg.uebungen.uebung7.Vector.Vector;

public class Uebung7_Interpolation extends JFrame {

	Camera cam;
	
	public Uebung7_Interpolation() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600, 600);
		JPanel canvas = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				render(g);
			}
		};
		canvas.setSize(600, 600);
		this.add(canvas);
		this.setVisible(true);

//		canvas.revalidate();

	}

	protected void render(Graphics g) {

		Vector v1_1 = new Vector(-10, 0, 20);
		Vector v1_2 = new Vector(0, 0, 20);

		Vector v2_1 = new Vector(-1, 0, 18);
		Vector v2_2 = new Vector(5, 0, 26);

		Vector v3_1 = new Vector(7, 0, 20);
		Vector v3_2 = new Vector(7, 0, 30);
		

		List<Vector[]> vs = Arrays.asList(new Vector[][] { { v1_1, v1_2 },
				{ v2_1, v2_2 }, { v3_1, v3_2 } });


		cam = new Uebung7_Camera();
		
		List<Coordinate2f> l1 = interpolateInBildschirmCoordinates(vs, g );

		List<Coordinate2f> l2 = interpolateInWorldCoordinates(vs, g);
		
		for (int i = 0; i < 3; i++){
			Coordinate2f c1 = l1.get(i);
			Coordinate2f c2 = l2.get(i);
			
			Vector v1 = Vector(c1.getX(), c1.getY());
			Vector v2 = Vector(c2.getX(), c2.getY());
			
			double d1 = v1.length();
			double d2 = v2.length();
		
			double error = Math.max(d1,d2) / Math.min(d1,d2);
			System.out.println("Fehler Strecke "+ (i+1) +"\n\t" + error);
			
		}

		

	}
	
	
	private List<Coordinate2f> interpolateInBildschirmCoordinates(List<Vector[]> vs,
			Graphics g) {
		
		List<Coordinate2f> list = new LinkedList<Coordinate2f>();
		SimpleMatrix ndcMatrix = cam.getNdcMatrix().mult(cam.getAugMatrix());

		int count= 1;
		
		for (Vector[] a_vs : vs) {
			SimpleMatrix v1 = ndcMatrix.mult(a_vs[0].getHomogenMatrix());
			SimpleMatrix v2 = ndcMatrix.mult(a_vs[1].getHomogenMatrix());

			v1 = v1.divide(v1.get(3, 0));
			v2 = v2.divide(v2.get(3, 0));

			Coordinate2f c1 = new Coordinate2f(
					((v1.get(0, 0) + 1) * (400 / 2)),
					((v1.get(2, 0) + 1) * (400 / 2)));
			Coordinate2f c2 = new Coordinate2f(
					((v2.get(0, 0) + 1) * (400 / 2)),
					((v2.get(2, 0) + 1) * (400 / 2)));	
			
			Vector v = (drawLine(g, c1, c2, count++));
			list.add(new Coordinate2f(v.x, v.y));
		}
		
		return list;
	}

	private List<Coordinate2f> interpolateInWorldCoordinates(List<Vector[]> vs, Graphics g) {
		
		List<Coordinate2f> list = new LinkedList<Coordinate2f>();
		
		int count= 1;
		for (Vector[] v : vs) {
			Vector mitte = v[0].plus(v[1]).mult(0.5);
			
			SimpleMatrix ndcMatrix = cam.getNdcMatrix().mult(cam.getAugMatrix());
			
			SimpleMatrix mitteNdc = ndcMatrix.mult(mitte.getHomogenMatrix());
			mitteNdc = mitteNdc.divide(mitteNdc.get(3,0));
			
			Coordinate2f c1 = new Coordinate2f(
					((mitteNdc.get(0, 0) + 1) * (400 / 2)),
					((mitteNdc.get(2, 0) + 1) * (400 / 2)));
			
			g.setColor(Color.blue);
			g.drawRect((int)Math.round(c1.getX()), (int)Math.round(c1.getY()), 3, 3);
			System.out.println("Mittelpunkt in Weltkoordinate von gerade " + count++ + "\n\t" +  (int) Math.round(c1.getX()) + " " + (int) Math.round(c1.getY()));
			list.add(c1);
		}
		return list;
	}

	/**
	 * it's bresenham with linear interpolation of color value
	 * @param g
	 * @param from
	 * @param to
	 */
	private Vector drawLine(Graphics g, Coordinate2f from, Coordinate2f to, int count) {
		
		Vector vec1 = Vector(from.getX(), from.getY());
		Vector vec2 = Vector(to.getX(), to.getY());
		Vector mitte = vec1.plus(vec2).mult(0.5);
		
		
		double step = 255 / vec2.minus(vec1).length();
		
		int fx = (int) Math.round(from.getX());;
		int fy = (int) Math.round(from.getY());;
		int tx = (int) Math.round(to.getX());;
		int ty = (int) Math.round(to.getY());;
		
		long distance = Math.round(Math.sqrt((tx - fx) * (tx - fx) + (ty - fy) * (ty - fy) ));
		
		int dx = Math.abs(tx - fx);
		int sx = fx < tx ? 1 : -1;
		int dy = -Math.abs(ty - fy);
		int sy = fy < ty ? 1 : -1;
		int err = dx + dy;
		int e2;
		
		int color = 0;
		
		int x = (int) Math.round(from.getX());
		int y = (int) Math.round(from.getY());
	
		for(;;) {
			if (x == (int)Math.round(mitte.x) && y == (int)Math.round(mitte.y)) {
				System.out.println("Mittelpunkt in Bildschirmkoordinaten von gerade " + count + "\n\t" + x + " " + y);
				g.drawRect(x, y, 3, 3);
			
			}
			
			
			g.setColor(new Color(color,color,color));
			color = (int) (color + step);
			g.drawRect(x, y, 1, 1);
			if(tx == x && ty == y) break;
			e2 = 2 * err;
			if(e2 > dy) {
				err += dy; 
				x += sx;
			}
			if(e2 < dx) {
				err += dx;
				y += sy;
			}
		}
		
		return mitte;
	}
	

	public static void main(String[] args) {

		Uebung7_Interpolation frame = new Uebung7_Interpolation();

	}
}
