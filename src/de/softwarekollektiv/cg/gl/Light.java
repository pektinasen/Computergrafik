package de.softwarekollektiv.cg.gl;

import de.softwarekollektiv.cg.gl.math.Vector3f;

public interface Light {
	public Vector3f getIntensity();
	public Vector3f getPosition();
}
