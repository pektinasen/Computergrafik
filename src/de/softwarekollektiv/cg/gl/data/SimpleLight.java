package de.softwarekollektiv.cg.gl.data;

import de.softwarekollektiv.cg.gl.Light;
import de.softwarekollektiv.cg.gl.math.Vector3f;

public final class SimpleLight implements Light {

	private final Vector3f position;
	private final Vector3f intensity;
	
	public SimpleLight(double x, double y, double z, double ir, double ig, double ib) {
		this.position = new Vector3f(x, y, z);
		this.intensity = new Vector3f(ir, ig, ib);
	}
	
	@Override
	public Vector3f getIntensity() {
		return intensity;
	}

	@Override
	public Vector3f getPosition() {
		return position;
	}

}
