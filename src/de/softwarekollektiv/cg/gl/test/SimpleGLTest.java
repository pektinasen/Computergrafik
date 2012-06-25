package de.softwarekollektiv.cg.gl.test;

import java.awt.Graphics;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.softwarekollektiv.cg.gl.Renderer;
import de.softwarekollektiv.cg.gl.ZBuffer;

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
	
	private ZBuffer zbuf;
	private final Object zbuf_lock = new Object();

	SimpleGLTest() throws IOException {
		final int width = 800;
		final int height = 600;
		
		scene = new ScenarioUno();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width, height);
		canvas = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				synchronized (zbuf_lock) {
					if(zbuf != null) {
						zbuf.paintOnCanvas(g);
						System.out.println("Canvas updated.");
					}
				}
			}
		};
		canvas.setSize(width, height + 20);
		this.add(canvas);
		this.setVisible(true);

		new Thread(new Runnable() {
			private double fps = 1.0 / 60;
		    private int maxms = (int) (1000 / fps);
			
			@Override
			public void run() {
				System.out.println("Animating... (at " + fps + "fps)");
				while (true) {				
					long startms = System.currentTimeMillis();
					
					scene.update();
					ZBuffer zbuf2 = Renderer.render(width, height, scene);
					
					synchronized (zbuf_lock) {
						zbuf = zbuf2;
						canvas.repaint();
					}
					
					long t = System.currentTimeMillis() - startms;
					if(t > maxms) {
						fps /= 2;
						maxms = (int) (1000 / fps);
						System.out.println("Can't keep up! Reducing frame rate to " + fps + "fps.");
						continue;
					}
					try {
						Thread.sleep(maxms - t);
					} catch (InterruptedException e) {
						return;
					}
				}
			}

		}).start();
	}
}
