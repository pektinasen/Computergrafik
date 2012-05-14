package de.softwarekollektiv.cg.uebungen.uebung4;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Aufgabe4 extends JFrame {
	
	public static void main(String[] args){
		JFrame frame = new Aufgabe4();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(400, 400));
		frame.setVisible(true);
		
		JPanel colorContent = new ColorComponent();
		
		frame.getContentPane().add(colorContent);
		colorContent.setVisible(true);
		
		
	}
}

class ColorComponent extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int r = 100;
		for (int x = 1; x < this.getWidth(); x++)
			for (int y = 1; y < this.getHeight(); y++){
				g.setColor(new Color((x*x + y * y ) % r * r));
				g.fillRect(x, y, 1, 1);
				
			}
		
	}
}
