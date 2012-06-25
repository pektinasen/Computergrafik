package de.softwarekollektiv.cg.gl.data;

import de.softwarekollektiv.cg.gl.Material;
import de.softwarekollektiv.cg.gl.math.Vector3f;

public final class SimpleMaterial implements Material {

	private final Vector3f arc;
	private final Vector3f drc;
	private final double src;
	private final double sn;

	public static final Material PAPER = new SimpleMaterial(0.9, 0.9, 0.9, 0.9,
			0.9, 0.9, 2, 3);
	public static final Material STONE = new SimpleMaterial(0.5, 0.5, 0.5, 0.3,
			0.3, 0.3, 3, 5);
	public static final Material METALL = new SimpleMaterial(0.6, 0.6, 0.6,
			0.8, 0.8, 0.8, 5, 15);

	/**
	 * Construct a SimpleMaterial.
	 * 
	 * @param arcr
	 *            ambient reflection coefficient, 'red' component
	 * @param arcg
	 *            ~ ~ ~, 'green' component
	 * @param arcb
	 *            ~ ~ ~, 'blue' component
	 * @param drcr
	 *            diffuse reflection coefficient, 'red' component
	 * @param drcg
	 *            ~ ~ ~, 'green' component
	 * @param drcb
	 *            ~ ~ ~, 'blue' component
	 * @param src
	 *            specular reflection coefficient
	 * @param sn
	 *            specular power constant
	 */
	public SimpleMaterial(final double arcr, final double arcg,
			final double arcb, final double drcr, final double drcg,
			final double drcb, final double src, final double sn) {
		this.arc = new Vector3f(arcr, arcg, arcb);
		this.drc = new Vector3f(drcr, drcg, drcb);
		this.src = src;
		this.sn = sn;
	}

	@Override
	public final Vector3f getAmbientReflectionCoefficient() {
		return arc;
	}

	@Override
	public final Vector3f getDiffuseReflectionCoefficient() {
		return drc;
	}

	@Override
	public final double getSpecularReflectionCoefficient() {
		return src;
	}

	@Override
	public final double getSpecularN() {
		return sn;
	}

}
