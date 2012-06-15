package de.softwarekollektiv.cg.gl;

import de.softwarekollektiv.cg.gl.math.Vector3f;

public interface Material {
	public Vector3f getAmbientReflectionCoefficient();
	public Vector3f getDiffuseReflectionCoefficient();
	public double getSpecularReflectionCoefficient();
	public double getSpecularN();
}
