package de.softwarekollektiv.cg.gl;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import de.softwarekollektiv.cg.gl.Face;
import de.softwarekollektiv.cg.gl.math.QuadMatrixf;
import de.softwarekollektiv.cg.gl.math.Vector3f;
import de.softwarekollektiv.cg.gl.math.Vector4f;

public class Renderer {

	public static void render(Graphics g, int width, int height, GLScene scene) {
		assert (g != null && scene != null);

		// Matrix: World coordinates -> NDC.
		final QuadMatrixf ndcMatrix = scene.getCamera().getNDCMatrix();

		// z-Buffer for view obstruction detection.
		ZBuffer zbuf = new ZBuffer(width, height, scene.getBackgroundColor());

		// Max face size: Partition faces until each
		// face' size is at most mfs.
		double mfs = scene.getMaxFaceSize();

		for (int gidx = 0; gidx < scene.getNumObjects(); gidx++) {
			GraphicObject obj = scene.getGraphicObject(gidx);
			QuadMatrixf worldMatrix = scene.getTransformationMatrix(gidx);

			for (int faceId = 0; faceId < obj.size(); faceId++) {
				Face f = obj.getFace(faceId);
				Material m = f.getMaterial();
				Texture t = f.getTexture();

				// First transform normal vector to world coordinates.
				Vector3f N = worldMatrix.mult(
						f.getNormal().getHomogeneousVector4f())
						.normalizeHomogeneous();

				// Transform face to world coordinates.
				Vector3f[] face_vertices = new Vector3f[3];
				for (int i = 0; i < 3; i++)
					face_vertices[i] = worldMatrix.mult(
							f.getVertex(i).getHomogeneousVector4f())
							.normalizeHomogeneous();

				// Create patches in world coordinates.
				List<Patch> patches;
				{
					// Using this matrices, we will later re-base the 
					// barycentric coordinates (relative to the patch
					// vertices) to the face base vertices.
					final QuadMatrixf leftInverse = new QuadMatrixf(
							new double[][] { { 0, 1, 0.5 }, { 0, 0, 0.5 },
									{ 1, 0, 0 } });
					final QuadMatrixf rightInverse = new QuadMatrixf(
							new double[][] { { 0, 0, 0.5 }, { 1, 0, 0.5 },
									{ 0, 1, 0 } });

					List<Patch> t1 = new ArrayList<Patch>();
					List<Patch> t2 = new ArrayList<Patch>();

					t1.add(new Patch(face_vertices, QuadMatrixf
							.createIdentity(3)));
					double cur_patch_size = triangle_size(face_vertices);
					while (cur_patch_size > mfs) {
						while (!t1.isEmpty()) {
							Patch p = t1.remove(0);
							Vector3f d = p.vertices[0].add(p.vertices[1])
									.scale(0.5);
							t2.add(new Patch(new Vector3f[] { p.vertices[2],
									p.vertices[0], d }, p.inverse
									.mult(leftInverse)));
							t2.add(new Patch(new Vector3f[] { p.vertices[1],
									p.vertices[2], d }, p.inverse
									.mult(rightInverse)));
						}

						List<Patch> t3 = t1;
						t1 = t2;
						t2 = t3;
						cur_patch_size /= 2;
					}

					patches = t1;
				}

				for (Patch patch : patches) {
					
					// Calculate patch light levels and transform to NDC.
					for (int i = 0; i < 3; i++) {
						Vector3f vertex = patch.vertices[i];

						// Calculate light intensity in world coordinates.
						patch.intensities[i] = PhongLightning.getIntensity(scene, f,
								N, vertex);

						// Transform vertex to NDC.
						Vector4f vndc = ndcMatrix.mult(vertex
								.getHomogeneousVector4f());

						// Normalize homogeneous component.
						Vector3f vndcCartesian = vndc.normalizeHomogeneous();

						// View port.
						patch.vertices[i] = new Vector3f(
								((vndcCartesian.getX() + 1) * (width / 2)),
								((vndcCartesian.getY() + 1) * (height / 2)),
								vndcCartesian.getZ());
					}

					rasterPatch(patch, m, t, zbuf);

				} // Patch loop.
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

	private final static class Patch {
		Patch(Vector3f[] vertices, QuadMatrixf inverse) {
			this.vertices = vertices;
			this.intensities = new Vector3f[3];
			this.inverse = inverse;
		}

		final Vector3f[] vertices;
		final Vector3f[] intensities;
		final QuadMatrixf inverse;
	}

	private static double triangle_size(Vector3f[] triangle) {
		return triangle[1].subtract(triangle[0])
				.vectorProduct(triangle[2].subtract(triangle[0])).length() / 2;
	}

	private static void rasterPatch(Patch p, Material m, Texture t, ZBuffer zbuf) {

		// Prepare for barycentric coordinates.
		QuadMatrixf Mb;
		{
			Vector3f A = p.vertices[0];
			Vector3f B = p.vertices[1];
			Vector3f C = p.vertices[2];

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
		Vector3f[] sorted = Arrays.copyOf(p.vertices, 3);
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

			rasterLine(xl, xr, yi, Mb, p, m, t, zbuf);

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

			rasterLine(xl, xr, yi, Mb, p, m, t, zbuf);

			yi++;
			xl += dxl;
			xr += dxr;
		}
	}

	private static void rasterLine(double xl, double xr, int yi, QuadMatrixf Mb, 
			Patch p, Material m, Texture t, ZBuffer zbuf) {
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

				// Interpolate z with barycentric coordinates relative
				// to patch vertices.
				double pz = lambda1 * p.vertices[0].getZ() + lambda2
						* p.vertices[1].getZ() + lambda3 * p.vertices[2].getZ();

				// Barycentric coordinates relative to face vertices.
				Vector3f Lr = p.inverse.mult(L);
				double real_lambda1 = Lr.getX();
				double real_lambda2 = Lr.getY();
				double real_lambda3 = Lr.getZ();

				// Get color of point.
				Vector3f col = t.getColor(real_lambda1, real_lambda2,
						real_lambda3);

				// Interpolate intensity.
				double r = col.getX()
						* (lambda1 * p.intensities[0].getX() + lambda2
								* p.intensities[1].getX() + lambda3
								* p.intensities[2].getX());
				double g = col.getY()
						* (lambda1 * p.intensities[0].getY() + lambda2
								* p.intensities[1].getY() + lambda3
								* p.intensities[2].getY());
				double b = col.getZ()
						* (lambda1 * p.intensities[0].getZ() + lambda2
								* p.intensities[1].getZ() + lambda3
								* p.intensities[2].getZ());

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
