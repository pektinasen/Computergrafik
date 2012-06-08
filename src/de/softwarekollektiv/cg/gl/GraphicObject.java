package de.softwarekollektiv.cg.gl;

import java.awt.Color;

public interface GraphicObject {
	public int size();
	public Face getFace(int face);
	public Color getColor(int face, double l1, double l2, double l3);
}
