package de.softwarekollektiv.cg.gl.math;

import org.ejml.simple.SimpleMatrix;

public final class Vector3f {
	private final double x;
	private final double y;
	private final double z;

	public Vector3f(double x, double y, double z) {		
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public final Vector3f subtract(final Vector3f v) {
		assert(v != null);
		
		return new Vector3f(this.x - v.x, this.y - v.y, this.z - v.z);
	}

	public final Vector3f vectorProduct(final Vector3f v) {
		assert(v != null);
		
		return new Vector3f(this.y * v.z - this.z * v.y, this.z * v.x - this.x
				* v.z, this.x * v.y - this.y * v.x);
	}

	public final SimpleMatrix getHomogeneousMatrix() {
		return new SimpleMatrix(new double[][] { { x }, { y }, { z }, { 1 } });
	}

	public final double[] toArray() {
		return new double[] { x, y, z };
	}

	public final Vector3f normalize() {
		double len = Math.sqrt(x * x + y * y + z * z);
		return new Vector3f(x / len, y / len, z / len);
	}
}
