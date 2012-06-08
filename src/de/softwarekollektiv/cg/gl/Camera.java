package de.softwarekollektiv.cg.gl;

import org.ejml.simple.SimpleMatrix;

import de.softwarekollektiv.cg.gl.math.Vector3f;

/**
 * Camera.
 * 
 * @author Sascha Gennrich <sascha.gennrich@googlemail.com>
 * @author Malte Rohde <malte.rohde@inf.fu-berlin.de>
 */
public final class Camera {

	private final SimpleMatrix m;

	/**
	 * Helper function to simplify camera configuration.
	 * See constructor for parameter descriptions.
	 */
	static public Camera createDefault(final double x, final double y, final double z,
			final double dirx, final double diry, final double dirz) {
		return new Camera(x, y, z, dirx, diry, dirz, 0, 0, 1, 1, 60, 10,
				100);
	}

	/**
	 * Construct a Camera.
	 * 
	 * Note: Although the 'top' direction is specified with 3 components, the
	 * camera's 'top' will always be orthogonal to the view direction.
	 * 
	 * @param x
	 *            camera's x-position
	 * @param y
	 *            camera's y-position
	 * @param z
	 *            camera's z-position
	 * @param dirx
	 *            camera's view direction, x-component
	 * @param diry
	 *            camera's view direction, y-component
	 * @param dirz
	 *            camera's view direction, z-component
	 * @param topx
	 *            camera's 'top' direction, x-component
	 * @param topy
	 *            camera's 'top' direction, y-component
	 * @param topz
	 *            camera's 'top' direction, z-component
	 * @param aspect_ratio
	 *            camera's aspect ratio, width/height
	 * @param fovx
	 *            field of view (horizontal), in degrees
	 * @param n_close
	 *            closest visible plane
	 * @param n_distant
	 *            farest visible plane
	 */
	public Camera(double x, double y, double z, double dirx, double diry,
			double dirz, double topx, double topy, double topz,
			double aspect_ratio, double fovx, double n_close, double n_distant) {

		// View to eye coordinates.
		Vector3f n = new Vector3f(-dirx, -diry, -dirz);
		
		// TODO Don't expect top{x,y,z} to be orthogonal to {x,y,z}!
		Vector3f v = new Vector3f(topx, topy, topz);
		Vector3f u = v.vectorProduct(n);

		SimpleMatrix M_uvn = new SimpleMatrix(new double[][] {
				u.toArray(), v.toArray(), n.toArray() 
		});
		SimpleMatrix M_eye = new SimpleMatrix(new double[][] {
				{ x }, { y }, { z }
		});
		SimpleMatrix M_VE = M_uvn.combine(0, 3, M_uvn.negative().mult(M_eye)).combine(3, 0,
				new SimpleMatrix(1, 4));
		M_VE.set(3, 3, 1);

		// Eye to NDC coordinates.
		double tangent = Math.tan(Math.toRadians(fovx / 2));
		double ur = tangent * n_close;
		double ul = -ur;
		double vt = ur / aspect_ratio;
		double vb = -vt;
		SimpleMatrix M_ENDC = new SimpleMatrix(new double[][] {
				{
					2 * n_close / (ur - ul),
					0,
					(ur + ul) / (ur - ul),
					0
				},
				{
					0,
					2 * n_close / (vt - vb),
					(vt + vb) / (vt - vb),
					0
				},
				{
					0,
					0,
					-((n_distant + n_close) / (n_distant - n_close)),
					-(2 * n_distant * n_close / (n_distant - n_close))
				},
				{
					0,
					0,
					-1,
					0
				}
		});
		
		this.m = M_ENDC.mult(M_VE);
	}

	final SimpleMatrix getNDCMatrix() {
		return m;
	}
}
