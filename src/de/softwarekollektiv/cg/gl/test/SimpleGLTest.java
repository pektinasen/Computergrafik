package de.softwarekollektiv.cg.gl.test;

import java.awt.Graphics;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.softwarekollektiv.cg.gl.Renderer;

/**
 * Assignment 7 using the new GL framework.
 */
@SuppressWarnings("serial")
public class SimpleGLTest extends JFrame {

	public static void main(String[] args) throws IOException {
		new SimpleGLTest();
	}

	private final ScenarioUno scene;
	private final JPanel canvas;
	private final Object lock = new Object();
	private double alpha = 35;

	SimpleGLTest() throws IOException {
		scene = new ScenarioUno();

		final int width = 800;
		final int height = 600;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width, height);
		canvas = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				synchronized (lock) {
					Renderer.render(g, width, height, scene);
				}
			}
		};
		canvas.setSize(width, height);
		this.add(canvas);
		this.setVisible(true);

		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					alpha += 10;
					alpha %= 360;
					synchronized (lock) {
						scene.update(alpha);
						canvas.repaint();
					}
					try {
						Thread.sleep(20000);
					} catch (InterruptedException e) {
						return;
					}
				}
			}

		}).start();
	}
}
