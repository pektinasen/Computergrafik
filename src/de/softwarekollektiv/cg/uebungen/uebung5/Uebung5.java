package de.softwarekollektiv.cg.uebungen.uebung5;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Uebung5 extends JFrame {

	private static final long serialVersionUID = 1L;

	final JSpinner[] values = new JSpinner[6];

	public Uebung5() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(800, 600));
		setVisible(true);

		final ColorComponent colorContent = new ColorComponent();

		Container pane = getContentPane();
		BorderLayout layout = new BorderLayout();
		pane.setLayout(layout);

		
		JPanel spinners = new JPanel();
		
		ChangeListener listener = new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				colorContent.repaint();

			}
		};
		
		
		char c = 'a';
		for (int i = 0; i < 6; i++) {
			SpinnerNumberModel model = new SpinnerNumberModel(0, -10000, 10000, 0.01);  
			values[i] = new JSpinner(model);
			
			spinners.add(new JLabel(c++ + " "));
			spinners.add(values[i]);
			
			values[i].addChangeListener(listener);
		}
		

	

	
//	        JSpinner spinner = new JSpinner(model);  
//	        JSpinner.NumberEditor editor = (JSpinner.NumberEditor)spinner.getEditor();  
//	        DecimalFormat format = editor.getFormat();  
//	        format.setMinimumFractionDigits(3);  
//	        editor.getTextField().setHorizontalAlignment(SwingConstants.CENTER);  
//	        Dimension d = spinner.getPreferredSize();  
//	        d.width = 85;  
//	        spinner.setPreferredSize(d);  
//		
			

		pane.add(spinners, BorderLayout.PAGE_START);
		pane.add(colorContent, BorderLayout.CENTER);
		colorContent.setVisible(true);

	}

	public static void main(String[] args) {
		Uebung5 frame = new Uebung5();

	}

	class ColorComponent extends JPanel {

		private static final long serialVersionUID = 1L;

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			double a = (Double) values[0].getValue();
			double b = (Double)  values[1].getValue();
			double c = (Double)  values[2].getValue();
			double d = (Double)  values[3].getValue();
			double e = (Double)  values[4].getValue();
			double f = - (Double) values[5].getValue();

			
			
			g.setColor(new Color(0.0f, 0.0f, 0.0f));
			for (int x = -this.getWidth(); x < 2 * this.getWidth(); x++) {
				for (int y = -this.getHeight(); y < 2 * this.getHeight(); y++) {
					double z = a * x * x + b * x * y + c * y * y + d * x + e
							* y + f;

					if (z <= 0)
						g.fillRect(x + 0, y + 800, 1, 1);
				}
			}
		}
	}
}
