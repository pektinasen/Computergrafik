package de.softwarekollektiv.cg.gl.data;

import de.softwarekollektiv.cg.gl.Face;
import de.softwarekollektiv.cg.gl.Material;
import de.softwarekollektiv.cg.gl.math.Vector3f;

/**
 * SimpleFace. A Triangle.
 * 
 * @author Malte Rohde <malte.rohde@inf.fu-berlin.de>
 */
public class SimpleFace implements Face {

	private final Vector3f[] v = new Vector3f[3];
	private final Material m;
	private final Vector3f n;
		
	/**
	 * Construct a SimpleFace.
	 * 
	 * A SimpleFace is always a triangle and has to be
	 * specified counterclockwise.
	 * 
	 * @param a vertex a
	 * @param b vertex b
	 * @param c vertex c
	 */
	public SimpleFace(final Vector3f a, final Vector3f b, final Vector3f c, Material m) {
		assert(a != null && b != null && c != null && m != null);
		
		this.v[0] = a;
		this.v[1] = b;
		this.v[2] = c;
		
		this.m = m;
		
		final Vector3f ba = b.subtract(a);
		final Vector3f ca = c.subtract(a);
		this.n = ba.vectorProduct(ca).normalize();
	}
	
	@Override
	public final Vector3f getVertex(final int idx) {
		assert(idx > 0 && idx < 2);
		
		return v[idx];
	}

	@Override
	public final Vector3f getNormal() {
		return n;
	}

	@Override
	public final Material getMaterial() {
		return m;
	}

}
