package de.softwarekollektiv.cg.gl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import de.softwarekollektiv.cg.gl.Face;
import de.softwarekollektiv.cg.gl.math.QuadMatrixf;
import de.softwarekollektiv.cg.gl.math.Vector3f;
import de.softwarekollektiv.cg.gl.math.Vector4f;

public class Renderer {

	public static ZBuffer render(int width, int height, GLScene scene) {
		assert (scene != null);

		// #####################
		// Building the world.
		// #####################

		// Max face size: Partition faces until each
		// face' size is at most mfs.
		double mfs = scene.getMaxFaceSize();

		// All patches in the world.
		List<Patch> patches = new ArrayList<Patch>();

		for (int gidx = 0; gidx < scene.getNumObjects(); gidx++) {
			GraphicObject obj = scene.getGraphicObject(gidx);
			QuadMatrixf worldMatrix = scene.getTransformationMatrix(gidx);

			for (int faceId = 0; faceId < obj.size(); faceId++) {
				Face f = obj.getFace(faceId);

				// First transform normal vector to world coordinates.
				Vector3f N = worldMatrix
						.mult(f.getNormal().getHomogeneousVector4f())
						.normalizeHomogeneous().normalize();

				// Transform face to world coordinates.
				Vector3f[] face_vertices = new Vector3f[3];
				for (int i = 0; i < 3; i++)
					face_vertices[i] = worldMatrix.mult(
							f.getVertex(i).getHomogeneousVector4f())
							.normalizeHomogeneous();

				// Using this matrices, we will later re-base the
				// barycentric coordinates (relative to the patch
				// vertices) to the face base vertices.
				final QuadMatrixf leftInverse = new QuadMatrixf(new double[][] {
						{ 0, 1, 0.5 }, { 0, 0, 0.5 }, { 1, 0, 0 } });
				final QuadMatrixf rightInverse = new QuadMatrixf(
						new double[][] { { 0, 0, 0.5 }, { 1, 0, 0.5 },
								{ 0, 1, 0 } });

				// Create patches in world coordinates.
				Patch initial_patch = new Patch(face_vertices,
						QuadMatrixf.createIdentity(3), f, N);

				List<Patch> t1 = new ArrayList<Patch>();
				t1.add(initial_patch);
				double cur_patch_size = initial_patch.size;
				while (cur_patch_size > mfs) {
					List<Patch> t2 = new ArrayList<Patch>();
					while (!t1.isEmpty()) {
						Patch p = t1.remove(0);
						Vector3f d = p.vertices[0].add(p.vertices[1])
								.scale(0.5);
						t2.add(new Patch(new Vector3f[] { p.vertices[2],
								p.vertices[0], d },
								p.inverse.mult(leftInverse), f, N));
						t2.add(new Patch(new Vector3f[] { p.vertices[1],
								p.vertices[2], d }, p.inverse
								.mult(rightInverse), f, N));
					}

					t1 = t2;
					cur_patch_size /= 2;
				}

				patches.addAll(t1);

			} // End of Faces loop.
		} // End of GraphicObjects loop.

		// #######################
		// Light.
		// #######################

		if (scene.getUseRadiosity()) {
			
			// TODO remove
			System.out.println("Enter radiosity!");
			long now = System.currentTimeMillis();
			
			// Radiosity!

			// Step 1: Calculate view factors between each 2 patches.
			// +++
			// Step 2: Precalculate visibility matrix.
			double[][] view_factors = new double[patches.size()][patches.size()];
			boolean[][] visibility = new boolean[patches.size()][patches.size()];
			for (int i = 0; i < patches.size(); i++) {
				Patch Ai = patches.get(i);
				for (int j = 0; j < patches.size(); j++) {
					if (j == i) {
						view_factors[i][j] = 0.0;
						continue;
					}

					Patch Aj = patches.get(j);
					double Ajs = triangle_size(Aj.vertices);

					// Get triangle centroids.
					Vector3f p = Ai.vertices[0].add(Ai.vertices[1])
							.add(Ai.vertices[2]).scale(1.0 / 3);
					Vector3f q = Aj.vertices[0].add(Aj.vertices[1])
							.add(Aj.vertices[2]).scale(1.0 / 3);

					// Calculate cosinus alpha & beta.
					Vector3f pq = q.subtract(p);
					double cosalpha = Ai.normal.scalarProduct(pq) / pq.length();
					Vector3f qp = p.subtract(q);
					double cosbeta = Aj.normal.scalarProduct(qp) / qp.length();

					// Use approximation formula.
					double r = pq.length();
					double vf = Math.abs(cosalpha) * Math.abs(cosbeta) * Ajs
							/ (Math.PI * r * r);
					if (vf < 0 || vf > 1) {
						System.out.println("oo");
						vf = 0;
					}
					view_factors[i][j] = vf;

					// Visibilitity: Beware, this is O(n^3).
					// Small optimization: Only calculate vis, if
					// i is smaller than j.
					if (i < j) {
						boolean visible = true;
						for (int k = 0; k < patches.size(); k++) {
							if (k == i || k == j)
								continue;

							if (triangle_line_intersect(
									patches.get(k).vertices, p, q)) {
								visible = false;
								break;
							}
						}
						visibility[i][j] = visible;
					}
				}
			}

			// Step 3: Iteratively solve global illumination equation.
			// Bi = Ei + pi * SUMj(Fij * Bj) where
			// - B{i,j} is the radiosity of patches i and j,
			// - Ei is the light emission of patch i,
			// - pi is the diffuse reflection coefficient of patch i,
			// - Fij is the view factor between patch i and j
			double[][] Bs = new double[3][];
			for (int col = 0; col < 3; col++) {

				// Gathering approach.
				double[] B = new double[patches.size()];
				for (int p = 0; p < patches.size(); p++) 
					B[p] = patches.get(p).face.getLight().get(col);
				
				for (int iteration = 0; iteration < scene.getRadiosityIterations(); iteration++) {
					double[] Bn = new double[patches.size()];
					for (int p = 0; p < patches.size(); p++) {
						Bn[p] = patches.get(p).face.getLight().get(col);
						for (int p2 = 0; p2 < patches.size(); p2++) {
							double pj = patches.get(p2).face.getMaterial()
									.getDiffuseReflectionCoefficient().get(col);

							// Only transfer light if patches see each other.
							if ((p < p2 && visibility[p][p2])
									|| (p2 < p && visibility[p2][p2]))
								Bn[p] += pj * view_factors[p][p2] * B[p2];
						}
					}
					B = Bn;
				}

				Bs[col] = B;
			}

			// Step 4: Convert radiosity to intensities for rasterization.
			for (int p = 0; p < patches.size(); p++) {
				Vector3f intensity = new Vector3f(Bs[0][p], Bs[1][p], Bs[2][p]);
				patches.get(p).intensities[0] = intensity;
				patches.get(p).intensities[1] = intensity;
				patches.get(p).intensities[2] = intensity;
			}
			
			// TODO remove
			System.out.println("Leaving radiosity after " + (System.currentTimeMillis() - now) +  "ms !");

		} else {

			// Use simple Phong illumination. Hopefully, the user provided some
			// light sources.

			for (Patch patch : patches) {
				for (int i = 0; i < 3; i++) {
					// Calculate light intensity.
					patch.intensities[i] = PhongLightning.getIntensity(scene,
							patch.face, patch.normal, patch.vertices[i]);
				}
			}
		}

		// #########################
		// Rasterization.
		// #########################

		// z-Buffer for view obstruction detection.
		ZBuffer zbuf = new ZBuffer(width, height, scene.getBackgroundColor(), scene.getLightness());

		// Matrix: World coordinates -> NDC.
		final QuadMatrixf ndcMatrix = scene.getCamera().getNDCMatrix();

		for (Patch patch : patches) {

			// Transform to NDC.
			for (int i = 0; i < 3; i++) {

				// Transform vertex to NDC.
				Vector4f vndc = ndcMatrix.mult(patch.vertices[i]
						.getHomogeneousVector4f());

				// Normalize homogeneous component.
				Vector3f vndcCartesian = vndc.normalizeHomogeneous();

				// View port.
				patch.vertices[i] = new Vector3f(
						((vndcCartesian.getX() + 1) * (width / 2)),
						((vndcCartesian.getY() + 1) * (height / 2)),
						vndcCartesian.getZ());
			}

			rasterPatch(patch, zbuf);

		} // End of Patch loop.

		// ##########################
		// Antialiasing.
		// ##########################

		// Smooth edges.
		zbuf = zbuf.smooth();

		return zbuf;
	}

	private final static class Patch {
		Patch(Vector3f[] vertices, QuadMatrixf inverse, Face face,
				Vector3f normal) {
			this.face = face;
			this.normal = normal;
			this.vertices = vertices;
			this.inverse = inverse;
			this.size = triangle_size(vertices);
			this.intensities = new Vector3f[3];
		}

		final Face face;
		final Vector3f normal;
		final double size;
		final QuadMatrixf inverse;

		// NOTE: vertices & intensities are changed within the
		// rendering pipeline. 'size' is only accurate in world
		// coordinates (i.e., after initialization).
		Vector3f[] vertices;
		Vector3f[] intensities;
	}

	private static double triangle_size(Vector3f[] triangle) {
		return triangle[1].subtract(triangle[0])
				.vectorProduct(triangle[2].subtract(triangle[0])).length() / 2;
	}

	private static boolean triangle_line_intersect(Vector3f[] triangle,
			Vector3f p, Vector3f q) {
		return false;
	}

	private static void rasterPatch(Patch p, ZBuffer zbuf) {
	
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

			rasterLine(xl, xr, yi, Mb, p, zbuf);

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

			rasterLine(xl, xr, yi, Mb, p, zbuf);

			yi++;
			xl += dxl;
			xr += dxr;
		}
	}

	private static void rasterLine(double xl, double xr, int yi,
			QuadMatrixf Mb, Patch p, ZBuffer zbuf) {
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
				Vector3f col = p.face.getTexture().getColor(real_lambda1,
						real_lambda2, real_lambda3);

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
				Vector3f color = new Vector3f(r, g, b);			

				// Draw into zBuffer.
				zbuf.setPixel(xi, yi, pz, color);
			}

			xi++;
		}
	}
}
