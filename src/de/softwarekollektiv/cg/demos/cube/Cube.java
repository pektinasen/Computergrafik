package de.softwarekollektiv.cg.demos.cube;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.swing.JFrame;

public class Cube implements GLEventListener, MouseListener,
		MouseMotionListener {

	public static void main(String[] args) {
		new Cube(300);
	}

	private final float[] colorBlack = { 0.0f, 0.0f, 0.0f, 1.0f };
	private final float[] colorWhite = { 1.0f, 1.0f, 1.0f, 1.0f };
	private final float[] colorGray = { 0.6f, 0.6f, 0.6f, 1.0f };
	private final float[] colorRed = { 1.0f, 0.0f, 0.0f, 1.0f };
	private final float[] colorBlue = { 0.0f, 0.0f, 1.0f, 1.0f };
	private final float[] colorYellow = { 1.0f, 1.0f, 0.0f, 1.0f };
	private final float[] colorGreen = { 0.0f, 1.0f, 0.0f, 1.0f };
	private final float[] colorPurple = { 0.8f, 0.0f, 0.8f, 1.0f };

	private final int SIZE;

	private int[] prevx = { -1, -1 };
	private float[] xrot = { 0, 0 };
	private int[] prevy = { -1, -1 };
	private float[] yrot = { 0, 0 };
	private int rotator = -1;

	private final GLCanvas canvas;

	private Cube(int size) {
		SIZE = size;
		canvas = getGLCanvas();
		canvas.addGLEventListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);

		final JFrame jframe = new JFrame("Cube");
		jframe.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowevent) {
				System.exit(0);
			}
		});

		jframe.getContentPane().add(canvas, BorderLayout.CENTER);
		jframe.setSize(640, 480);
		jframe.setVisible(true);
	}

	private GLCanvas getGLCanvas() {
		GLCapabilities capabilities = new GLCapabilities();
		capabilities.setHardwareAccelerated(true);
		return new GLCanvas(capabilities);
	}

	private void drawCenteredCube(GL gl) {
		setMaterial(gl, colorRed);
		drawSquareFace(gl);

		setMaterial(gl, colorBlue);
		gl.glRotatef(90, 1.0f, 0.0f, 0.0f);
		drawSquareFace(gl);

		setMaterial(gl, colorYellow);
		gl.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
		drawSquareFace(gl);

		setMaterial(gl, colorGreen);
		gl.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
		drawSquareFace(gl);

		setMaterial(gl, colorPurple);
		gl.glRotatef(90, 1.0f, 0.0f, 0.0f);
		drawSquareFace(gl);
	}

	private void setMaterial(GL gl, float[] color) {
		gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT, color, 0);
		gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_DIFFUSE, color, 0);
		gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, colorWhite, 0);
		gl.glMateriali(GL.GL_FRONT_AND_BACK, GL.GL_SHININESS, 4);
		gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_EMISSION, colorBlack, 0);
	}

	private void drawSquareFace(GL gl) {
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex3f(-SIZE / 2, -SIZE / 2, SIZE / 2);
		gl.glVertex3f(-SIZE / 2, SIZE / 2, SIZE / 2);
		gl.glVertex3f(SIZE / 2, SIZE / 2, SIZE / 2);
		gl.glVertex3f(SIZE / 2, -SIZE / 2, SIZE / 2);
		gl.glEnd();
	}

	private void drawCenteredPyramid(GL gl) {
		gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT, new float[] {
				0.2f, 0.4f, 0.7f }, 0);
		gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_DIFFUSE, new float[] {
				0.4f, 0.3f, 0.8f }, 0);
		gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_SPECULAR, new float[] {
				0.6f, 0.5f, 1.0f }, 0);
		gl.glMateriali(GL.GL_FRONT_AND_BACK, GL.GL_SHININESS, 12);
		gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL.GL_EMISSION, colorBlack, 0);

		gl.glBegin(GL.GL_TRIANGLE_STRIP);
		gl.glVertex3f(0, SIZE / 4, -SIZE / 4);
		gl.glVertex3f(SIZE / 4, -SIZE / 4, -SIZE / 4);
		gl.glVertex3f(-SIZE / 4, -SIZE / 4, -SIZE / 4);
		gl.glVertex3f(0, 0, SIZE / 4);
		gl.glVertex3f(0, SIZE / 4, -SIZE / 4);
		gl.glVertex3f(SIZE / 4, -SIZE / 4, -SIZE / 4);
		gl.glEnd();
	}

	// #######################
	// GLEventListener methods
	// #######################

	@Override
	public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		gl.glEnable(GL.GL_DEPTH_TEST);

		// First Switch the lights on.
		gl.glEnable(GL.GL_LIGHTING);
		gl.glDisable(GL.GL_LIGHT0);
		gl.glEnable(GL.GL_LIGHT1);

		//
		// Light 1.
		//
		// Position and direction (spotlight)
		float posLight1[] = { 3.0f, 8.0f, 1.0f, 0.0f };
		float spotDirection[] = { -1.0f, -1.0f, 0.f };
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, posLight1, 0);
		gl.glLightf(GL.GL_LIGHT1, GL.GL_SPOT_CUTOFF, 60.0f);
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPOT_DIRECTION, spotDirection, 0);

		gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, colorGray, 0);
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, colorGray, 0);
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPECULAR, colorWhite, 0);
		gl.glLightf(GL.GL_LIGHT1, GL.GL_CONSTANT_ATTENUATION, 0.2f);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL.GL_MODELVIEW);

		gl.glLoadIdentity();
		gl.glRotatef(xrot[1], 0, 1, 0);
		gl.glRotatef(yrot[1], 1, 0, 0);
		drawCenteredPyramid(gl);

		gl.glLoadIdentity();
		gl.glRotatef(xrot[0], 0, 1, 0);
		gl.glRotatef(yrot[0], 1, 0, 0);
		drawCenteredCube(gl);
	}

	@Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL gl = drawable.getGL();
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(-width, width, -height, height, -SIZE, SIZE);
	}

	// #############################
	// Mouse[Motion]Listener methods
	// #############################

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// SOMEHOW, arg0.getButton() always
		// returns 0, thus we needed to implement
		// MouseListener as well.

		if (rotator != -1) {
			int x = arg0.getX();
			int y = arg0.getY();
			if (prevx[rotator] != -1 && (Math.abs(prevx[rotator] - x) < 10)) {
				xrot[rotator] -= prevx[rotator] - x;
			}
			if (prevy[rotator] != -1 && (Math.abs(prevy[rotator] - y) < 10)) {
				yrot[rotator] -= prevy[rotator] - y;
			}

			canvas.display();
			prevx[rotator] = x;
			prevy[rotator] = y;
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		switch (arg0.getButton()) {
		case MouseEvent.BUTTON1:
			rotator = 0;
			break;
		case MouseEvent.BUTTON3:
			rotator = 1;
			break;
		default:
			rotator = -1;
			break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		rotator = -1;
	}
}