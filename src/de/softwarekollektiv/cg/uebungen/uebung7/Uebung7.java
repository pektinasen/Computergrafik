package de.softwarekollektiv.cg.uebungen.uebung7;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.ejml.simple.SimpleMatrix;

public class Uebung7 extends JFrame{

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
	
	void render(Graphics g){
		
		//Define the cube: Face A
		Vector v1 = new Vector(0,0,0);
		Vector v2 = new Vector(0,1,0);
		Vector v3 = new Vector(0,0,1);
		Vector v4 = new Vector(0,1,1);
		Face f1 = new Face(v1,v2,v4);
		Face f2 = new Face(v1,v4,v3);
		
		// Define the cube: Face B
		Vector w1 = new Vector(0,0,0);
		Vector w2 = new Vector(0,1,0);
		Vector w3 = new Vector(1,0,0);
		Vector w4 = new Vector(1,1,0);
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
				{50, 0, 0, 0},
				{0, 50, 0, 0},
				{0, 0, 50, 0},
				{0, 0, 0, 1}
		});
		
		// Translationsmatrix
		double alpha = Math.toRadians(-45);
		SimpleMatrix transRotMatrix = new SimpleMatrix(new double[][] {
				{Math.cos(alpha), 0 , Math.sin(alpha), 0},
				{0,1,0,-25},
				{- Math.sin(alpha), 0 , Math.cos(alpha), 75},
				{0,0,0,1}
		});
	
		SimpleMatrix uberMatrix = cam.getNdcMatrix().mult(cam.getAugMatrix()).mult(transRotMatrix.mult(scaleMatrix));
		
		
		Random r = new Random();
		for (Face f : faces){
			g.setColor(new Color(r.nextInt()));
			int[] xs = new int[3];
			int[] ys = new int[3];
			int count = 0;
			
			for (Vector v : f){
				SimpleMatrix ndcVector = uberMatrix.mult(v.getHomogenMatrix());;
				ndcVector = ndcVector.divide(ndcVector.get(3, 0));
				
				xs[count] = (int) Math.round((ndcVector.get(0, 0) + 1) * (400 / 2));
				ys[count] = (int) Math.round((ndcVector.get(1, 0) + 1) * (400 / 2));
				count++;
			}
			
			g.drawLine(xs[0], ys[0], xs[1], ys[1]);
			g.drawLine(xs[2], ys[2], xs[1], ys[1]);
			g.drawLine(xs[2], ys[2], xs[0], ys[0]);
		}
		
	}
	
	public static void main(String[] args) {
		new Uebung7();
		
	}
	
}
