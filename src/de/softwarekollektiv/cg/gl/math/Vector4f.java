package de.softwarekollektiv.cg.gl.math;

public final class Vector4f {
	private final double x, y, z, w;
	
	public final double getX() {
		return x;
	}

	public final double getY() {
		return y;
	}

	public final double getZ() {
		return z;
	}

	public final double getW() {
		return w;
	}

	public Vector4f(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Vector3f normalizeHomogeneous() {
		return new Vector3f(x / w, y / w, z / w);
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ", " + w + ")";
	}
}
