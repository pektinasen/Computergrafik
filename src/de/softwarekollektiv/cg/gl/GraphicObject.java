package de.softwarekollektiv.cg.gl;

import de.softwarekollektiv.cg.gl.math.Vector3f;

public interface GraphicObject {
	public int size();
	public Face getFace(int face);
	public Vector3f getColor(int face, double l1, double l2, double l3);
}
