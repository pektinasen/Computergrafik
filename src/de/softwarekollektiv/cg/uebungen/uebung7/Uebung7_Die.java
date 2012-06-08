package de.softwarekollektiv.cg.uebungen.uebung7;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.ejml.simple.SimpleMatrix;

import de.softwarekollektiv.cg.gl.math.Coordinate2f;

@SuppressWarnings("serial")
public class Uebung7_Die extends JFrame {

	Uebung7_Die() throws IOException {
		setup();
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
	
	private List<Face> faces;
	private Camera cam;
	
	void setup() throws IOException {
		
		// Textures
		File img = new File("dice_six.png");
		BufferedImage bimg = ImageIO.read(img);
		
		Texture tf1 = new Texture(bimg, new double[][]{{0, 0}, {0, 1}, {1, 1}});
		Texture tf2 = new Texture(bimg, new double[][]{{0, 0}, {1, 1}, {1, 0}});

		// Define the cube: Face A
		Vector v1 = new Vector(0, 0, 0);
		Vector v2 = new Vector(0, 1, 0);
		Vector v3 = new Vector(0, 0, 1);
		Vector v4 = new Vector(0, 1, 1);
		Face f1 = new Face(v1, v2, v4, tf1);
		Face f2 = new Face(v1, v4, v3, tf2);
		
		// Define the cube: Face B
		Vector w1 = new Vector(0, 0, 0);
		Vector w2 = new Vector(0, 1, 0);
		Vector w3 = new Vector(1, 0, 0);
		Vector w4 = new Vector(1, 1, 0);
		Face k1 = new Face(w1, w2, w4, tf1);
		Face k2 = new Face(w1, w4, w3, tf2);
		
		faces = Arrays.asList(new Face[]{f1, f2, k1, k2});
		
		// Define the Camera
		cam = new Uebung7_Camera();
		
	}

	void render(Graphics g) {
		
		// Scaling.
		SimpleMatrix scaleMatrix = new SimpleMatrix(new double[][] {
				{ 50, 0, 0, 0 }, { 0, 50, 0, 0 }, { 0, 0, 50, 0 },
				{ 0, 0, 0, 1 } });

		// Translate/Rotate.
		double alpha = Math.toRadians(-45);
		SimpleMatrix transRotMatrix = new SimpleMatrix(new double[][] {
				{ Math.cos(alpha), 0, Math.sin(alpha), 0 }, { 0, 1, 0, -25 },
				{ -Math.sin(alpha), 0, Math.cos(alpha), 75 }, { 0, 0, 0, 1 } });

		// Rendering pipeline.
		SimpleMatrix uberMatrix = cam.getNdcMatrix().mult(cam.getAugMatrix())
				.mult(transRotMatrix.mult(scaleMatrix));

		for (Face f : faces) {
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

			// Sort.
			Coordinate2f[] sorted = Arrays.copyOf(vertices, 3);
			Arrays.sort(sorted, new Comparator<Coordinate2f>() {
				public int compare(Coordinate2f arg0, Coordinate2f arg1) {
					return (arg0.getY() < arg1.getY() ? -1
							: ((arg0.getY() > arg1.getY() ? 1 : 0)));
				}
			});

			// Raster. 
			double ot = sorted[0].getX() * (sorted[2].getY() - sorted[1].getY()) + 
						sorted[2].getX() * (sorted[1].getY() - sorted[0].getY()) +
						sorted[1].getX() * (sorted[0].getY() - sorted[2].getY());
			int left = ot > 0 ? 1 : 2;
			int right = 2 - left + 1;

			// Simple scanline algorithm.
			double[] dxl = new double[2];
			double[] dxr = new double[2];
			double dyl = sorted[left].getY() - sorted[0].getY();
			dxl[0] = (sorted[left].getX() - sorted[0].getX()) / dyl;
			double dyr = sorted[right].getY() - sorted[0].getY();
			dxr[0] = (sorted[right].getX() - sorted[0].getX()) / dyr;
			if(left == 1) {
				dyl = sorted[2].getY() - sorted[1].getY();
				dxl[1] = (sorted[2].getX() - sorted[1].getX()) / dyl;
				dxr[1] = dxr[0];
			} else {
				dyr = sorted[2].getY() - sorted[1].getY();
				dxr[1] = (sorted[2].getX() - sorted[1].getX()) / dyr;
				dxl[1] = dxl[0];
			}

			double xl = sorted[0].getX();
			double xr = xl;
			int yi = (int) Math.round(sorted[0].getY());
			
			double[] t = {sorted[1].getY(), sorted[2].getY()};
			for(int ti = 0; ti < 2; ti++) {
				for(; yi <= (int) Math.round(t[ti]); yi++) {
					for(int xi = (int) Math.round(xl); xi <= (int) Math.round(xr); xi++) {
						// Calculate barycentric coordinates.
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
						
						// Get texture.
						g.setColor(f.getColor(lambda1, lambda2, lambda3));
						g.fillRect(xi, yi, 1, 1);				
					}
					
					xl += dxl[ti];
					xr += dxr[ti];
				}
			}			
		}
	}

	public static void main(String[] args) throws IOException {
		new Uebung7_Die();
	}

}
