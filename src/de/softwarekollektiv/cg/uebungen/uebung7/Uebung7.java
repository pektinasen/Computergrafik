package de.softwarekollektiv.cg.uebungen.uebung7;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.ejml.simple.SimpleMatrix;

import de.softwarekollektiv.cg.gl.math.Coordinate2f;

public class Uebung7 extends JFrame {

	Uebung7() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(400, 400);
		JPanel canvas = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				render(g);
			}
		};
		canvas.setSize(400, 400);
		this.add(canvas);
		this.setVisible(true);
		canvas.revalidate();
	}

	void render(Graphics g) {

		// Define the cube: Face A
		Vector v1 = new Vector(0, 0, 0);
		Vector v2 = new Vector(0, 1, 0);
		Vector v3 = new Vector(0, 0, 1);
		Vector v4 = new Vector(0, 1, 1);
		Face f1 = new Face(v1, v2, v4);
		Face f2 = new Face(v1, v4, v3);

		// Define the cube: Face B
		Vector w1 = new Vector(0, 0, 0);
		Vector w2 = new Vector(0, 1, 0);
		Vector w3 = new Vector(1, 0, 0);
		Vector w4 = new Vector(1, 1, 0);
		Face k1 = new Face(w1, w2, w4);
		Face k2 = new Face(w1, w4, w3);

		List<Face> faces = Arrays.asList(f1, f2, k1, k2);

		// Define the Camera
		Camera cam = new Camera();
		cam.setPosition(0, 0, 0);
		cam.setDirection(0, 0, 1);
		cam.setTop(0, 1, 0);
		cam.n_close = 1;
		cam.n_distant = 100;
		cam.aspect_ratio = 1;
		cam.fov = 60;

		// erstmal gross machen
		SimpleMatrix scaleMatrix = new SimpleMatrix(new double[][] {
				{ 50, 0, 0, 0 }, { 0, 50, 0, 0 }, { 0, 0, 50, 0 },
				{ 0, 0, 0, 1 } });

		// Translationsmatrix
		double alpha = Math.toRadians(-45);
		SimpleMatrix transRotMatrix = new SimpleMatrix(new double[][] {
				{ Math.cos(alpha), 0, Math.sin(alpha), 0 }, { 0, 1, 0, -25 },
				{ -Math.sin(alpha), 0, Math.cos(alpha), 75 }, { 0, 0, 0, 1 } });

		SimpleMatrix uberMatrix = cam.getNdcMatrix().mult(cam.getAugMatrix())
				.mult(transRotMatrix.mult(scaleMatrix));

		Random r = new Random();
		for (Face f : faces) {
			g.setColor(new Color(r.nextInt()));
			Coordinate2f[] vertices = new Coordinate2f[3];
			for (int i = 0; i < 3; i++) {
				Vector v = f.getVertex(i);
				
				// Transform to NDC.
				SimpleMatrix ndcVector = uberMatrix.mult(v.getHomogenMatrix());
				
				// Normalize.
				ndcVector = ndcVector.divide(ndcVector.get(3, 0));
				
				// Viewport.
				double ndcx = ndcVector.get(0, 0);
				double ndcy = ndcVector.get(1, 0);
				vertices[i] = new Coordinate2f(((ndcx + 1) * (400 / 2)), ((ndcy + 1) * (400 / 2)));
			}
			/*
			
			g.setColor(Color.BLACK);
			g.drawLine((int) vertices[0].getX(), (int) vertices[0].getY(), (int) vertices[1].getX(),(int)  vertices[1].getY());
			g.drawLine((int) vertices[2].getX(), (int) vertices[2].getY(),(int)  vertices[1].getX(),(int)  vertices[1].getY());
			g.drawLine((int) vertices[2].getX(), (int) vertices[2].getY(), (int) vertices[0].getX(),(int)  vertices[0].getY());
			*/
			
			// Prepare for barycentric coordinates.
			SimpleMatrix Mb = new SimpleMatrix(new double[][] {
					{
						vertices[0].getX() - vertices[2].getX(),
						vertices[1].getX() - vertices[2].getX(),
					},
					{
						vertices[0].getY() - vertices[2].getY(),
						vertices[1].getY() - vertices[2].getY(),
					}
			}).invert();

			// Sort:
			Coordinate2f[] pixel = Arrays.copyOf(vertices, 3);
			Arrays.sort(pixel, new Comparator<Coordinate2f>() {
				public int compare(Coordinate2f arg0, Coordinate2f arg1) {
					return (arg0.getY() < arg1.getY() ? -1
							: ((arg0.getY() > arg1.getY() ? 1 : 0)));
				}
			});

			// Raster:
			double ot = pixel[0].getX() * (pixel[2].getY() - pixel[1].getY()) + 
						pixel[2].getX() * (pixel[1].getY() - pixel[0].getY()) +
						pixel[1].getX() * (pixel[0].getY() - pixel[2].getY());
			int left = ot > 0 ? 1 : 2;
			int right = 2 - left + 1;

			double[] dxl = new double[2];
			double[] dxr = new double[2];
			double dyl = pixel[left].getY() - pixel[0].getY();
			dxl[0] = (pixel[left].getX() - pixel[0].getX()) / dyl;
			double dyr = pixel[right].getY() - pixel[0].getY();
			dxr[0] = (pixel[right].getX() - pixel[0].getX()) / dyr;
			
			if(left == 1) {
				dyl = pixel[2].getY() - pixel[1].getY();
				dxl[1] = (pixel[2].getX() - pixel[1].getX()) / dyl;
				dxr[1] = dxr[0];
			} else {
				dyr = pixel[2].getY() - pixel[1].getY();
				dxr[1] = (pixel[2].getX() - pixel[1].getX()) / dyr;
				dxl[1] = dxl[0];
			}

			double xl = pixel[0].getX();
			double xr = xl;
			int yi = (int) Math.round(pixel[0].getY());
			
			double[] t = {pixel[1].getY(), pixel[2].getY()};
			for(int ti = 0; ti < 2; ti++) {
				for(; yi <= (int) Math.round(t[ti]); yi++) {
					for(int xi = (int) Math.round(xl); xi <= (int) Math.round(xr); xi++) {
						SimpleMatrix lambda = Mb.mult(new SimpleMatrix(new double[][] {
								{
									xi - vertices[2].getX()
								},
								{
									yi - vertices[2].getY()
								}
						}));
						double lambda1 = lambda.get(0, 0);
						double lambda2 = lambda.get(1, 0);
						double lambda3 = 1 - lambda1 - lambda2;
						
						g.setColor(f.getColor(lambda1, lambda2, lambda3));
						g.fillRect(xi, yi, 1, 1);				
					}
					
					xl += dxl[ti];
					xr += dxr[ti];
				}
			}
			

			

			/*
			
			// Baryzentrische Koordinaten.
			SimpleMatrix lambda = new SimpleMatrix(new double[][] {
					{
						vertices[0].getX() - vertices[2].getX(),
						vertices[1].getX() - vertices[2].getX(),
					},
					{
						vertices[0].getY() - vertices[2].getY(),
						vertices[1].getY() - vertices[2].getY(),
					}
			}).invert().mult(
			
			double lambda1 = lambda.get(0, 0);
			double lambda2 = lambda.get(1, 0);
			double lambda3 = 1 - lambda1 - lambda2;
								
*/
			
		}
	}

	public static void main(String[] args) {
		new Uebung7();

	}

}
