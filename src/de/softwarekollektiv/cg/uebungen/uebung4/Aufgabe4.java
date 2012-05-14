package de.softwarekollektiv.cg.uebungen.uebung4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Aufgabe4 extends JFrame {
	
	private static final long serialVersionUID = 1L;

	public static void main(String[] args){
		JFrame frame = new Aufgabe4();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(400, 400));
		frame.setVisible(true);
		
		final ColorComponent colorContent = new ColorComponent();
		
		JSlider slider = new JSlider(1,1000);
		slider.setValue(100);
		
		Container pane = frame.getContentPane();
		BorderLayout layout = new BorderLayout();
		pane.setLayout(layout);
		pane.add(colorContent,BorderLayout.CENTER);
		pane.add(slider,BorderLayout.PAGE_END);
		colorContent.setVisible(true);
		
		slider.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				
				JSlider s = (JSlider) e.getSource();
				colorContent.r = s.getValue();
				colorContent.repaint();
				
			}
		});
	}
}

class ColorComponent extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public int r = 100;
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		for (int x = 1; x < this.getWidth(); x++)
			for (int y = 1; y < this.getHeight(); y++){
				g.setColor(new Color((x*x + y * y ) % r * r));
				g.fillRect(x, y, 1, 1);
				
			}		
	}
}
