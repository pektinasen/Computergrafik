package de.softwarekollektiv.cg.gl;

import de.softwarekollektiv.cg.gl.math.Vector3f;

public interface Face {
	public Vector3f getVertex(int vertex);
	public Vector3f getNormal();
	public Material getMaterial();
	public Texture getTexture();
}
