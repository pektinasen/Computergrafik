package de.softwarekollektiv.cg.uebungen.uebung6;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
		
		SceneInformation si = new SceneInformation();
		si.setVisible(true);
		this.add(si, BorderLayout.EAST);

		Canvas c = new Canvas(si);
		c.setVisible(true);
		this.add(c, BorderLayout.CENTER);

		// First row: Specular
		
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
		
		s = new Scene();
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
		s.ks = 0.1f;
		s.n = 0.2f;
		c.addScene(s);
		
		s = new Scene();
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
		s.ks = 0.1f;
		s.n = 2f;
		c.addScene(s);
		
		s = new Scene();
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
		s.ks = 0.05f;
		s.n = 4f;
		c.addScene(s);
		
		s = new Scene();
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
		s.ks = 0.5f;
		s.n = 8f;
		c.addScene(s);
		
		// Second row: Light position.
		
		s = new Scene();
		// Ambient reflection.
		s.kar = 0.5f;
		s.kag = 0.0f;
		s.kab = 0.5f;
		s.iar = 0.5f;
		s.iag = 0.0f;
		s.iab = 0.5f;
		// Diffuse & specular reflection.
		s.kdr = 0.0f;
		s.kdg = 0.8f;
		s.kdb = 0.8f;
		s.ilr = 0.2f;
		s.ilg = 0.3f;
		s.ilb = 0.4f;
		s.plx = -400f;
		s.ply = -400f;
		s.plz = -1000f;
		s.c1 = 0.001f;
		s.c2 = 0.0001f;
		s.c3 = 0.0000001f;
		s.ks = 0.4f;
		s.n = 2f;
		c.addScene(s);
		
		s = new Scene();
		// Ambient reflection.
		s.kar = 0.5f;
		s.kag = 0.0f;
		s.kab = 0.5f;
		s.iar = 0.5f;
		s.iag = 0.0f;
		s.iab = 0.5f;
		// Diffuse & specular reflection.
		s.kdr = 0.0f;
		s.kdg = 0.8f;
		s.kdb = 0.8f;
		s.ilr = 0.2f;
		s.ilg = 0.3f;
		s.ilb = 0.4f;
		s.plx = 400f;
		s.ply = -400f;
		s.plz = -1500f;
		s.c1 = 0.001f;
		s.c2 = 0.0001f;
		s.c3 = 0.0000001f;
		s.ks = 0.4f;
		s.n = 2f;
		c.addScene(s);
		
		s = new Scene();
		// Ambient reflection.
		s.kar = 0.5f;
		s.kag = 0.0f;
		s.kab = 0.5f;
		s.iar = 0.5f;
		s.iag = 0.0f;
		s.iab = 0.5f;
		// Diffuse & specular reflection.
		s.kdr = 0.0f;
		s.kdg = 0.8f;
		s.kdb = 0.8f;
		s.ilr = 0.2f;
		s.ilg = 0.3f;
		s.ilb = 0.4f;
		s.plx = 400f;
		s.ply = 400f;
		s.plz = -2000f;
		s.c1 = 0.001f;
		s.c2 = 0.0001f;
		s.c3 = 0.0000001f;
		s.ks = 0.4f;
		s.n = 2f;
		c.addScene(s);
		
		s = new Scene();
		// Ambient reflection.
		s.kar = 0.5f;
		s.kag = 0.0f;
		s.kab = 0.5f;
		s.iar = 0.5f;
		s.iag = 0.0f;
		s.iab = 0.5f;
		// Diffuse & specular reflection.
		s.kdr = 0.0f;
		s.kdg = 0.8f;
		s.kdb = 0.8f;
		s.ilr = 0.2f;
		s.ilg = 0.3f;
		s.ilb = 0.4f;
		s.plx = -400f;
		s.ply = 400f;
		s.plz = -2500f;
		s.c1 = 0.001f;
		s.c2 = 0.0001f;
		s.c3 = 0.0000001f;
		s.ks = 0.4f;
		s.n = 2f;
		c.addScene(s);
		
		
		validate();
	}
	
	private static class SceneInformation extends JPanel {
		private JLabel ka;
		private JLabel kd;
		private JLabel ia;
		private JLabel il;
		private JLabel pl;
		private JLabel ks;
		private JLabel n;
		private JLabel c;
		
		SceneInformation() {		
			this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			
			JLabel tka = new JLabel("Ambient reflection coefficient of the ball (r/g/b):");
			ka = new JLabel("0 / 0 / 0");
			JLabel tkd = new JLabel("Diffuse reflection coefficient of the ball (r/g/b):");
			kd = new JLabel("0 / 0 / 0");
			JLabel tia = new JLabel("Intensity of ambient light (r/g/b):");
			ia = new JLabel("0 / 0 / 0");
			JLabel til = new JLabel("Intensity of light source (r/g/b):");
			il = new JLabel("0 / 0 / 0");
			JLabel tpl = new JLabel("Position of light source (x/y/z):");
			pl = new JLabel("0 / 0 / 0");
			JLabel tks = new JLabel("Specular reflection coefficient of the ball:");
			ks = new JLabel("0");
			JLabel tn = new JLabel("Specular exponent:");
			n = new JLabel("0");
			JLabel tc = new JLabel("Constants (c1, c2, c3):");
			c = new JLabel("0, 0, 0");
			
			this.add(tka);
			this.add(ka);
			this.add(tkd);
			this.add(kd);
			this.add(tia);
			this.add(ia);
			this.add(til);
			this.add(il);
			this.add(tpl);
			this.add(pl);
			this.add(tks);
			this.add(ks);
			this.add(tn);
			this.add(n);
			this.add(tc);
			this.add(c);
		}

		private void updateInformation(Scene scene) {
			ka.setText(scene.kar + " / " + scene.kag + " / "+ scene.kab);
			kd.setText(scene.kdr + " / " + scene.kdg + " / "+ scene.kdb);
			ia.setText(scene.iar + " / " + scene.iag + " / "+ scene.iab);
			il.setText(scene.ilr + " / " + scene.ilg + " / "+ scene.ilb);
			pl.setText(scene.plx + " / " + scene.ply + " / "+ scene.plz);
			ks.setText(new Float(scene.ks).toString());
			n.setText(new Float(scene.n).toString());
			c.setText(scene.c1 + ", " + scene.c2 + ", " + scene.c3);			
		}
	}

	private static class Canvas extends JPanel implements MouseMotionListener {

		private static final int ROWS = 4;
		private static final int COLUMNS = 5;

		private final SceneInformation si;
		private final Scene[][] scenes = new Scene[COLUMNS][ROWS];
		private int num = 0;
		private int six = -1;
		private int siy = -1;

		
		private Canvas(SceneInformation si) {
			this.si = si;
			this.addMouseMotionListener(this);
		}

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

		@Override
		public void mouseDragged(MouseEvent arg0) {
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			int x = (int) (((float) arg0.getX()) / this.getWidth() * COLUMNS);
			int y = (int) (((float) arg0.getY()) / this.getHeight() * ROWS);
			if(scenes[x][y] != null && (six != x || siy != y)) {
				si.updateInformation(scenes[x][y]);
				six = x;
				siy = y;
			}
		}
	}
}
