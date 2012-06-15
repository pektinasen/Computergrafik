package de.softwarekollektiv.cg.gl;

import de.softwarekollektiv.cg.gl.math.Vector3f;

public interface Texture {
	public Vector3f getColor(double lambda1, double lambda2, double lambda3);
}
