package de.softwarekollektiv.cg.uebungen.uebung6;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Uebung6 extends JFrame {
	public static void main(String[] args) {
		new Uebung6();
	}

	private Uebung6() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(800, 600);
		this.setVisible(true);

		Canvas c = new Canvas();
		c.setVisible(true);
		this.add(c);

		Scene s = new Scene();
		// Ambient reflection.
		s.kar = 0.6f;
		s.kag = 0.0f;
		s.kab = 0.0f;
		s.iar = 0.6f;
		s.iag = 0.0f;
		s.iab = 0.0f;
		// Diffuse & specular reflection.
		s.kdr = 0.0f;
		s.kdg = 0.8f;
		s.kdb = 0.8f;
		s.ilr = 0.9f;
		s.ilg = 0.5f;
		s.ilb = 0.7f;
		s.plx = 400f;
		s.ply = 800f;
		s.plz = -1200f;
		s.c1 = 0.001f;
		s.c2 = 0.0001f;
		s.c3 = 0.0000001f;
		s.ks = 0.21f;
		s.n = 0.2f;
		c.addScene(s);
	}

	private static class Canvas extends JPanel {

		private static final int ROWS = 4;
		private static final int COLUMNS = 5;

		private final Scene[][] scenes = new Scene[COLUMNS][ROWS];
		private int num = 0;

		private void addScene(Scene b) {
			if (num > ROWS * COLUMNS)
				throw new RuntimeException("Too many balls.");

			int x = num % COLUMNS;
			int y = num / COLUMNS;
			scenes[x][y] = b;
			num++;
		}

		@Override
		protected void paintComponent(Graphics g) {
			// Draw black background.
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			
			int tileWidth = this.getWidth() / COLUMNS;
			int tileHeight = this.getHeight() / ROWS;

			for (int x = 0; x < COLUMNS; x++) {
				for (int y = 0; y < ROWS; y++) {
					if (scenes[x][y] != null) {
						int xoffset = x * tileWidth;
						int yoffset = y * tileHeight;
						Graphics drawRect = g.create(xoffset, yoffset,
								tileWidth, tileHeight);
						scenes[x][y].draw(drawRect, tileWidth, tileHeight);
						drawRect.dispose();
					}
				}
			}
		}
	}
}
