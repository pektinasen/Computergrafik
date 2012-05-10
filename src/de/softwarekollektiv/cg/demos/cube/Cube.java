package de.softwarekollektiv.cg.demos.cube;

import java.awt.BorderLayout;
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

public class Cube {

	public static void main(String[] args) {
		new Cube();
	}

	private static final int SIZE = 160;
	private static float angle = 0;

	private Cube() {
		GLCanvas canvas = getGLCanvas();
		canvas.addGLEventListener(new RotatingCubeListener());
		Animator anim = new Animator(canvas);
		addCanvasToFrame(canvas, anim);
		anim.start();
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
		gl.glColor4f(1.0f, 0.0f, 0.0f, 0.0f);
		drawSquareFace(gl);
		gl.glColor4f(1.0f, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(90, 1.0f, 0.0f, 0.0f);
		drawSquareFace(gl);
		gl.glColor4f(0.0f, 0.0f, 1.0f, 0.0f);
		gl.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
		drawSquareFace(gl);
		gl.glColor3f(0.0f, 0.0f, 0.0f);
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

	private class RotatingCubeListener implements GLEventListener {

		@Override
		public void init(GLAutoDrawable drawable) {
			GL2 gl = drawable.getGL().getGL2();
			gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); // erasing color
			gl.glColor3f(0.0f, 0.0f, 0.0f); // drawing color
		}

		@Override
		public void dispose(GLAutoDrawable drawable) {
		}

		@Override
		public void display(GLAutoDrawable drawable) {
			GL2 gl = drawable.getGL().getGL2();
			gl.glClear(GL.GL_COLOR_BUFFER_BIT);

			angle += 10;
			gl.glMatrixMode(GL2.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glRotatef(-80, 1, 1, 0);
			gl.glRotatef(angle / 16, 1, -1, 1);
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
	}
}