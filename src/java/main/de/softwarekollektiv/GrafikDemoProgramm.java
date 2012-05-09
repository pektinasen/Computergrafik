package de.softwarekollektiv;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import sun.net.www.content.text.plain;

class TestPanel extends JPanel implements MouseListener {

	Point from = new Point(10, 10);
	Point to = null;

	List<Point> points = new LinkedList<Point>();
	
	

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.gray);
		Dimension d = getSize(); // loesche die Anzeige

		g.fillRect(0, 0, d.width, d.height);
		g.setColor(Color.white);
		for (Point p : points) {
			g.fillRect(p.x, p.y, 1, 1);			
		}
	}

	public TestPanel() {
		addMouseListener(this);
	}

	// ////////////////////
	// Eingabe (Maus) //
	// ////////////////////

	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}

	// mousePressed(): Mausknopf wurde betaetigt
	// Hier: neue Punktkoordinaten aufnehmen
	public void mousePressed(MouseEvent e) {
		if (to == null) {
			to = new Point(e.getX(), e.getY());
			points.add(to);
			bresenham(from, to);
		} else {
			from = new Point(e.getX(), e.getY());
			points.clear();
			points.add(from);
			
			to = null;
		}
		
		setPixel(e.getX(), e.getY());
	
	}

	public void bresenham(Point from, Point to) {
		// REM Bresenham-Algorithmus für eine Linie im ersten Oktanten in
		// Pseudo-Basic
		// dx = xend-xstart
		int dx = to.x - from.x;
		// dy = yend-ystart
		int dy = to.y - from.y;
		// REM im ersten Oktanten muss 0 < dy <= dx sein
		//
		// REM Initialisierungen
		// x = xstart
		int x = from.x;
		// y = ystart
		int y = from.y;
		// SETPIXEL x,y
		setPixel(x, y);
		// fehler = dx/2
		int error = dx / 2;
		//
		// REM Pixelschleife: immer ein Schritt in schnelle Richtung, hin und
		// wieder auch einer in langsame
		// WHILE x < xend
		while (x < to.x) {

			// REM Schritt in schnelle Richtung
			// x = x + 1
			x++;
			// fehler = fehler-dy
			error -= dy;
			// IF fehler < 0 THEN
			if (error < 0) {

				// REM Schritt in langsame Richtung
				// y = y + 1
				y = y + 1;
				// fehler = fehler + dx
				error += dx;
				// END IF
			}
			// SETPIXEL x,y
			setPixel(x, y);
			// WEND
		}
	}

	private void setPixel(int x, int y) {
		points.add( new Point(x, y));
		this.repaint();

	}

}

public class GrafikDemoProgramm {
	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("Grafik-Testprogramm");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 400);
		JPanel panel = new TestPanel();

		frame.getContentPane().add(panel);
		// Display the window.
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

}
