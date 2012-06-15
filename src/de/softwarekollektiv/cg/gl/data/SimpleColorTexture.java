package de.softwarekollektiv.cg.gl.data;

import de.softwarekollektiv.cg.gl.Texture;
import de.softwarekollektiv.cg.gl.math.Vector3f;

public final class SimpleColorTexture implements Texture {
	private final Vector3f color;
	
	public static final Texture WHITE = new SimpleColorTexture(Vector3f.ONE);
	public static final Texture BLACK = new SimpleColorTexture(Vector3f.ZERO);
	public static final Texture RED = new SimpleColorTexture(new Vector3f(1.0, 0.0, 0.0));
	public static final Texture GREEN = new SimpleColorTexture(new Vector3f(0.0, 1.0, 0.0));

	public SimpleColorTexture(final Vector3f color) {
		this.color = color;
	}

	@Override
	public final Vector3f getColor(final double lambda1, final double lambda2,
			final double lambda3) {
		return color;
	}
}
