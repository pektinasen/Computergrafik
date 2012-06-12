package de.softwarekollektiv.cg.gl.test;

import java.awt.Graphics;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.softwarekollektiv.cg.gl.Camera;
import de.softwarekollektiv.cg.gl.Face;
import de.softwarekollektiv.cg.gl.GLScene;
import de.softwarekollektiv.cg.gl.GraphicObject;
import de.softwarekollektiv.cg.gl.Material;
import de.softwarekollektiv.cg.gl.Renderer;
import de.softwarekollektiv.cg.gl.data.SimpleFace;
import de.softwarekollektiv.cg.gl.data.SimpleGraphicObject;
import de.softwarekollektiv.cg.gl.data.SimpleLight;
import de.softwarekollektiv.cg.gl.data.SimpleMaterial;
import de.softwarekollektiv.cg.gl.math.QuadMatrixf;
import de.softwarekollektiv.cg.gl.math.Vector3f;

/**
 * Assignment 7 using the new GL framework.
 */
@SuppressWarnings("serial")
public class SimpleGLTest extends JFrame {
	
	public static void main(String[] args) throws IOException {
		new SimpleGLTest();
	}
	
	private GLScene scene;
	
	SimpleGLTest() throws IOException {
		final int width = 800;
		final int height = 600;
		setup();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width, height);
		JPanel canvas = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Renderer.render(g, width, height, scene, new Vector3f(0.0, 0.0, 0.0));
			}
		};
		canvas.setSize(width, height);
		this.add(canvas);
		this.setVisible(true);
	
		canvas.revalidate();
	}
	
	void setup() throws IOException {	
		// Define the cube.
		GraphicObject cube = setupCube();
		QuadMatrixf cubem = setupCubeTransf();
	
		// Define the Camera
		Camera cam = new Camera(0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 60, 1, 100);
		
		// Define the scene.
		scene = new GLScene();
		scene.setCamera(cam);
		scene.addGraphicObject(cube, cubem);
		scene.setUseLighting(true);
		scene.setAmbientLight(0.5, 0.5, 0.2);
		scene.addLight(new SimpleLight(75, 95, 15, 0.9, 0.9, 0.9));
		scene.addLight(new SimpleLight(-20, -30, 55, 0, 0.9, 0.9));
	}
	
	private GraphicObject setupCube() {
		// Define a material:
		Material m = new SimpleMaterial(0.6, 0.4, 0.4, 0.0, 0.8, 0.8, 0.1, 0.5);
		
		// Define the cube: Face A
		Vector3f v1 = new Vector3f(0, 0, 0);
		Vector3f v2 = new Vector3f(0, 1, 0);
		Vector3f v3 = new Vector3f(0, 0, 1);
		Vector3f v4 = new Vector3f(0, 1, 1);
		Face f1 = new SimpleFace(v1, v2, v4, m);
		Face f2 = new SimpleFace(v1, v4, v3, m);
		
		// Define the cube: Face B
		Vector3f w1 = new Vector3f(0, 0, 0);
		Vector3f w2 = new Vector3f(0, 1, 0);
		Vector3f w3 = new Vector3f(1, 0, 0);
		Vector3f w4 = new Vector3f(1, 1, 0);
		Face k1 = new SimpleFace(w1, w2, w4, m);
		Face k2 = new SimpleFace(w1, w4, w3, m);
		
		// Define the cube: Face C
		Vector3f u1 = new Vector3f(0, 0, 0);
		Vector3f u2 = new Vector3f(0, 0, 1);
		Vector3f u3 = new Vector3f(1, 0, 0);
		Vector3f u4 = new Vector3f(1, 0, 1);
		Face h1 = new SimpleFace(u1, u2, u4, m);
		Face h2 = new SimpleFace(u1, u4, u3, m);
		
		// Define the GraphicObject.
		SimpleGraphicObject cube = new SimpleGraphicObject();
		cube.addFace(f1);
		cube.addFace(f2);
		cube.addFace(k1);
		cube.addFace(k2);
		cube.addFace(h1);
		cube.addFace(h2);
		
		return cube;
	}
	
	private QuadMatrixf setupCubeTransf() {
		QuadMatrixf scaleMatrix = new QuadMatrixf(new double[][] {
				{ 50, 0, 0, 0 }, { 0, 50, 0, 0 }, { 0, 0, 50, 0 },
				{ 0, 0, 0, 1 } });

		// Translate/Rotate.
		double alpha = Math.toRadians(-45);
		QuadMatrixf transRotMatrix = new QuadMatrixf(new double[][] {
				{ Math.cos(alpha), 0, Math.sin(alpha), 0 }, { 0, 1, 0, -25 },
				{ -Math.sin(alpha), 0, Math.cos(alpha), 75 }, { 0, 0, 0, 1 } });
		
		return transRotMatrix.mult(scaleMatrix);
	}
}
