package de.softwarekollektiv.cg.gl;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.Comparator;

import de.softwarekollektiv.cg.gl.Face;
import de.softwarekollektiv.cg.gl.math.QuadMatrixf;
import de.softwarekollektiv.cg.gl.math.Vector3f;
import de.softwarekollektiv.cg.gl.math.Vector4f;

public class Renderer {
	public static void render(Graphics g, int width, int height, GLScene scene) {
		assert (g != null && scene != null);

		final QuadMatrixf ndcMatrix = scene.getCamera().getNDCMatrix();
		ZBuffer zbuf = new ZBuffer(width, height, scene.getBackgroundColor());

		for (int gidx = 0; gidx < scene.getNumObjects(); gidx++) {
			GraphicObject obj = scene.getGraphicObject(gidx);
			QuadMatrixf worldMatrix = scene.getTransformationMatrix(gidx);

			for (int faceId = 0; faceId < obj.size(); faceId++) {
				Face f = obj.getFace(faceId);

				// First transform normal vector to world coordinates.
				Vector3f N = worldMatrix.mult(
						f.getNormal().getHomogeneousVector4f())
						.normalizeHomogeneous();

				Vector3f[] vertices = new Vector3f[3];
				Vector3f[] intensities = new Vector3f[3];
				for (int i = 0; i < 3; i++) {
					Vector3f v = f.getVertex(i);

					// Transform to world coordinates.
					Vector4f vworld = worldMatrix.mult(v
							.getHomogeneousVector4f());

					// Calculate light intensity in world coordinates.
					if (scene.getUseLightning())
						intensities[i] = PhongLightning.getIntensity(scene, f,
								N, vworld.normalizeHomogeneous());
					else
						intensities[i] = Vector3f.ONE;

					// Transform vertex to NDC.
					Vector4f vndc = ndcMatrix.mult(vworld);

					// Normalize homogeneous component.
					Vector3f vndcCartesian = vndc.normalizeHomogeneous();

					// View port.
					vertices[i] = new Vector3f(
							((vndcCartesian.getX() + 1) * (width / 2)),
							((vndcCartesian.getY() + 1) * (height / 2)),
							vndcCartesian.getZ());
				}

				rasterTriangle(vertices, intensities, f, zbuf);

			} // Faces loop.
		} // GraphicObjects loop.

		// Smooth edges.
		zbuf = zbuf.smooth();

		// Flip screen.
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Vector3f pixel = zbuf.getPixel(x, y);
				Color col = new Color((float) pixel.getX(),
						(float) pixel.getY(), (float) pixel.getZ());
				g.setColor(col);
				g.drawRect(width - x, height - y, 1, 1);
			}
		}
	}

	private static void rasterTriangle(Vector3f[] vertices,
			Vector3f[] intensities, Face face, ZBuffer zbuf) {

		// Prepare for barycentric coordinates.
		QuadMatrixf Mb;
		{
			Vector3f A = vertices[0];
			Vector3f B = vertices[1];
			Vector3f C = vertices[2];

			double fac = 1 / (A.getX() * (B.getY() - C.getY()) + B.getX()
					* (C.getY() - A.getY()) + C.getX() * (A.getY() - B.getY()));
			Mb = new QuadMatrixf(new double[][] {
					{ B.getY() - C.getY(), C.getX() - B.getX(),
							B.getX() * C.getY() - B.getY() * C.getX() },
					{ C.getY() - A.getY(), A.getX() - C.getX(),
							C.getX() * A.getY() - C.getY() * A.getX() },
					{ A.getY() - B.getY(), B.getX() - A.getX(),
							A.getX() * B.getY() - A.getY() * B.getX() } })
					.scale(fac);
		}

		// Sort.
		Vector3f[] sorted = Arrays.copyOf(vertices, 3);
		Arrays.sort(sorted, new Comparator<Vector3f>() {
			public int compare(Vector3f arg0, Vector3f arg1) {
				return (arg0.getY() < arg1.getY() ? -1 : ((arg0.getY() > arg1
						.getY() ? 1 : 0)));
			}
		});

		// Test whether middle point is left or right of line.
		double ot = sorted[0].getX() * (sorted[2].getY() - sorted[1].getY())
				+ sorted[2].getX() * (sorted[1].getY() - sorted[0].getY())
				+ sorted[1].getX() * (sorted[0].getY() - sorted[2].getY());
		int left = ot > 0 ? 1 : 2;
		int right = 2 - left + 1;

		// #############################
		// Simple scan-line algorithm.
		// #############################

		// Lower half.
		double dyl = sorted[left].getY() - sorted[0].getY();
		double dxl = (sorted[left].getX() - sorted[0].getX()) / dyl;
		double dyr = sorted[right].getY() - sorted[0].getY();
		double dxr = (sorted[right].getX() - sorted[0].getX()) / dyr;
		int yi = (int) Math.ceil(sorted[0].getY());
		double xl = sorted[0].getX() + (yi - sorted[0].getY()) * dxl;
		double xr = sorted[0].getX() + (yi - sorted[0].getY()) * dxr;

		while (yi <= sorted[1].getY()) {

			rasterLine(xl, xr, yi, vertices, intensities, Mb, face, zbuf);

			yi++;
			xl += dxl;
			xr += dxr;
		}

		// Upper half.
		if (left == 1) {
			dyl = sorted[2].getY() - sorted[1].getY();
			dxl = (sorted[2].getX() - sorted[1].getX()) / dyl;

			xl = sorted[1].getX() + (yi - sorted[1].getY()) * dxl;
		} else {
			dyr = sorted[2].getY() - sorted[1].getY();
			dxr = (sorted[2].getX() - sorted[1].getX()) / dyr;

			xr = sorted[1].getX() + (yi - sorted[1].getY()) * dxr;
		}

		while (yi <= sorted[2].getY()) {

			rasterLine(xl, xr, yi, vertices, intensities, Mb, face, zbuf);

			yi++;
			xl += dxl;
			xr += dxr;
		}

	}

	private static void rasterLine(double xl, double xr, int yi,
			Vector3f[] vertices, Vector3f[] intensities, QuadMatrixf Mb,
			Face face, ZBuffer zbuf) {
		int xi = (int) Math.ceil(xl);
		while (xi <= xr) {

			// Calculate barycentric coordinates.
			Vector3f P = new Vector3f(xi, yi, 1.0);
			Vector3f L = Mb.mult(P);
			double lambda1 = L.getX();
			double lambda2 = L.getY();
			double lambda3 = L.getZ();

			// Only draw points in triangle.
			if (lambda1 >= 0 && lambda2 >= 0 && lambda3 >= 0) {

				// Interpolate z.
				double pz = lambda1 * vertices[0].getZ() + lambda2
						* vertices[1].getZ() + lambda3 * vertices[2].getZ();

				// Get color of point.
				Vector3f col = face.getColor(lambda1, lambda2, lambda3);

				// Interpolate intensity.
				double r = col.getX()
						* (lambda1 * intensities[0].getX() + lambda2
								* intensities[1].getX() + lambda3
								* intensities[2].getX());
				double g = col.getY()
						* (lambda1 * intensities[0].getY() + lambda2
								* intensities[1].getY() + lambda3
								* intensities[2].getY());
				double b = col.getZ()
						* (lambda1 * intensities[0].getZ() + lambda2
								* intensities[1].getZ() + lambda3
								* intensities[2].getZ());

				// Cut off colors.
				r = (r > 1.0) ? 1.0 : ((r < 0.0) ? 0.0 : r);
				g = (g > 1.0) ? 1.0 : ((g < 0.0) ? 0.0 : g);
				b = (b > 1.0) ? 1.0 : ((b < 0.0) ? 0.0 : b);
				Vector3f color = new Vector3f(r, g, b);

				// Draw into zBuffer.
				zbuf.setPixel(xi, yi, pz, color);
			}

			xi++;
		}
	}
}
