package de.softwarekollektiv.cg.demos.cube;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

import com.jogamp.opengl.util.Animator;

public class Cube implements GLEventListener, MouseMotionListener {

	public static void main(String[] args) {
		new Cube(300);
	}

	private final float[] colorBlack = { 0.0f, 0.0f, 0.0f, 1.0f };
	private final float[] colorWhite = { 1.0f, 1.0f, 1.0f, 1.0f };
	private final float[] colorGray = { 0.6f, 0.6f, 0.6f, 1.0f };
	private final float[] colorRed = { 1.0f, 0.0f, 0.0f, 1.0f };
	private final float[] colorBlue = { 0.0f, 0.0f, 0.1f, 1.0f };
	private final float[] colorYellow = { 1.0f, 1.0f, 0.0f, 1.0f };
	private final float[] colorLightYellow = { .5f, .5f, 0.0f, 1.0f };
	private final float[] colorGreen = { 0.0f, 1.0f, 0.0f, 1.0f };
	private final float[] colorPurple = { 0.8f, 0.0f, 0.8f, 1.0f };

	private final int SIZE;

	private int prevx = -1;
	private float xrot = 0;
	private int prevy = -1;
	private float yrot = 0;

	private final GLCanvas canvas;

	private Cube(int size) {
		SIZE = size;
		canvas = getGLCanvas();
		canvas.addGLEventListener(this);
		canvas.addMouseMotionListener(this);
		Animator anim = new Animator(canvas);
		addCanvasToFrame(canvas, anim);
		// anim.start();
	}

	private void addCanvasToFrame(GLCanvas canvas, final Animator anim) {
		final JFrame jframe = new JFrame("Cube");
		jframe.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowevent) {
				anim.stop();
				System.exit(0);
			}
		});

		jframe.getContentPane().add(canvas, BorderLayout.CENTER);
		jframe.setSize(640, 480);
		jframe.setVisible(true);
	}

	private GLCanvas getGLCanvas() {
		GLProfile profile = GLProfile.getDefault();
		GLCapabilities capabilities = new GLCapabilities(profile);
		capabilities.setHardwareAccelerated(true);
		return new GLCanvas(capabilities);
	}

	private void drawCenteredCube(GL2 gl) {
		setMaterial(gl, colorRed);
		drawSquareFace(gl);

		setMaterial(gl, colorBlue);
		gl.glRotatef(90, 1.0f, 0.0f, 0.0f);
		drawSquareFace(gl);

		setMaterial(gl, colorYellow);
		gl.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
		drawSquareFace(gl);

		/*setMaterial(gl, colorLightYellow);
		gl.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
		drawSquareFace(gl);

		setMaterial(gl, colorPurple);
		gl.glRotatef(90, 1.0f, 0.0f, 0.0f);
		drawSquareFace(gl);*/
	}

	private void setMaterial(GL2 gl, float[] color) {
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, color, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, color, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, colorWhite, 0);
		gl.glMateriali(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, 4);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, colorBlack, 0);
	}

	private void drawSquareFace(GL2 gl) {
		gl.glBegin(GL2.GL_QUADS);
		gl.glTexCoord2f(0, 0);
		gl.glVertex3f(-SIZE / 2, -SIZE / 2, SIZE / 2);
		gl.glTexCoord2f(0, 1);
		gl.glVertex3f(-SIZE / 2, SIZE / 2, SIZE / 2);
		gl.glTexCoord2f(1, 1);
		gl.glVertex3f(SIZE / 2, SIZE / 2, SIZE / 2);
		gl.glTexCoord2f(1, 0);
		gl.glVertex3f(SIZE / 2, -SIZE / 2, SIZE / 2);
		gl.glEnd();
	}

	// #######################
	// GLEventListener methods
	// #######################

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		// First Switch the lights on.
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glDisable(GL2.GL_LIGHT0);
		gl.glEnable(GL2.GL_LIGHT1);

		//
		// Light 0.
		//
		// Default from the red book.
		//
		// gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient);
		// gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position);

		//
		// Light 1.
		//
		// Position and direction (spotlight)
		float posLight1[] = { 1.0f, 1.f, 1.f, 0.0f };
		float spotDirection[] = { -1.0f, -1.0f, 0.f };
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, posLight1, 0);
		gl.glLightf(GL2.GL_LIGHT1, GL2.GL_SPOT_CUTOFF, 60.0f);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPOT_DIRECTION, spotDirection, 0);

		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, colorGray, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, colorGray, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, colorWhite, 0);
		gl.glLightf(GL2.GL_LIGHT1, GL2.GL_CONSTANT_ATTENUATION, 0.2f);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glRotatef(xrot, 0, 1, 0);
		gl.glRotatef(yrot, 1, 0, 0);
		drawCenteredCube(gl);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(-width, width, -height, height, -SIZE, SIZE);
	}

	// #######################
	// MouseListener methods
	// #######################

	@Override
	public void mouseDragged(MouseEvent arg0) {
		int x = arg0.getX();
		int y = arg0.getY();
		if (prevx != -1 && (Math.abs(prevx - x) < 10)) {
			xrot -= prevx - x;
		}
		if (prevy != -1 && (Math.abs(prevy - y) < 10)) {
			yrot -= prevy - y;
		}

		canvas.display();
		prevx = x;
		prevy = y;
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	}
}