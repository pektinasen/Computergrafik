package de.softwarekollektiv.cg.gl.math;

public final class Vector3f {
	
	public static final Vector3f ZERO = new Vector3f(0.0, 0.0, 0.0);
	public static final Vector3f ONE = new Vector3f(1.0, 1.0, 1.0);
	
	private final double x;
	private final double y;
	private final double z;

	public Vector3f(double x, double y, double z) {		
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public final Vector3f add(final Vector3f v) {
		assert(v != null);
		
		return new Vector3f(this.x + v.x, this.y + v.y, this.z + v.z);
	}
	
	public final Vector3f subtract(final Vector3f v) {
		assert(v != null);
		
		return new Vector3f(this.x - v.x, this.y - v.y, this.z - v.z);
	}
	
	public final double scalarProduct(final Vector3f v) {
		assert(v != null);
		
		return this.x * v.x + this.y * v.y + this.z * v.z;
	}
	
	public final Vector3f vectorProduct(final Vector3f v) {
		assert(v != null);
		
		return new Vector3f(this.y * v.z - this.z * v.y, this.z * v.x - this.x
				* v.z, this.x * v.y - this.y * v.x);
	}
	
	public final Vector3f scale(double s) {
		return new Vector3f(x * s, y * s, z * s);
	}
	
	public final Vector4f getHomogeneousVector4f() {
		return new Vector4f(x, y, z, 1);
	}

	public final double[] toArray() {
		return new double[] { x, y, z };
	}

	public final Vector3f normalize() {
		double len = Math.sqrt(x * x + y * y + z * z);
		return new Vector3f(x / len, y / len, z / len);
	}

	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getZ() {
		return z;
	}

	public double length() {
		return Math.sqrt(x * x + y * y + z * z);
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}
}
