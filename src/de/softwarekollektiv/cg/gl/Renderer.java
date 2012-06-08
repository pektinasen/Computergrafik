package de.softwarekollektiv.cg.gl;

import java.awt.Graphics;
import java.util.Arrays;
import java.util.Comparator;

import de.softwarekollektiv.cg.gl.Face;
import de.softwarekollektiv.cg.gl.math.Coordinate2f;
import de.softwarekollektiv.cg.gl.math.QuadMatrixf;
import de.softwarekollektiv.cg.gl.math.Vector3f;
import de.softwarekollektiv.cg.gl.math.Vector4f;

public class Renderer {
	public static void render(Graphics g, int width, int height, GLScene scene) {
		assert (g != null && scene != null);

		final QuadMatrixf ndcMatrix = scene.getCamera().getNDCMatrix();
		final ZBuffer zbuf = new ZBuffer(width, height);

		for (int gidx = 0; gidx < scene.getNumObjects(); gidx++) {
			GraphicObject obj = scene.getGraphicObject(gidx);
			QuadMatrixf m = ndcMatrix.mult(scene.getTransformationMatrix(gidx));

			for (int faceId = 0; faceId < obj.size(); faceId++) {
				Face f = obj.getFace(faceId);

				Coordinate2f[] vertices = new Coordinate2f[3];
				Vector3f[] intensities = new Vector3f[3];
				for (int i = 0; i < 3; i++) {
					Vector3f v = f.getVertex(i);
					Vector4f vhomogen = v.getHomogeneousVector4f();

					// Calculate light intensities in world coordinates.
					if (scene.getUseLightning())
						intensities[i] = PhongLightning.getIntensity(scene, f,
								v);
					else
						intensities[i] = new Vector3f(1.0, 1.0, 1.0);
					
					// Transform vertex to NDC.
					Vector4f ndcVector = m.mult(vhomogen);

					// Normalize homogeneous component.
					ndcVector = ndcVector.normalizeHomogeneous();

					// View port.
					double ndcx = ndcVector.getX();
					double ndcy = ndcVector.getY();
					vertices[i] = new Coordinate2f(((ndcx + 1) * (400 / 2)),
							((ndcy + 1) * (400 / 2)));
				}


				
				rasterTriangle(vertices, intensities, obj, faceId, zbuf);

			} // Faces loop.
		} // GraphicObjects loop.

		// Flip screen.
		zbuf.draw(g);
	}

	private static void rasterTriangle(Coordinate2f[] vertices,
			Vector3f[] intensities, GraphicObject obj, int faceId, ZBuffer zbuf) {
		// Prepare for barycentric coordinates.
		QuadMatrixf Mb = new QuadMatrixf(new double[][] {
				{ vertices[0].getX() - vertices[2].getX(),
						vertices[1].getX() - vertices[2].getX(), },
				{ vertices[0].getY() - vertices[2].getY(),
						vertices[1].getY() - vertices[2].getY(), } }).invert();

		// Sort.
		Coordinate2f[] sorted = Arrays.copyOf(vertices, 3);
		Arrays.sort(sorted, new Comparator<Coordinate2f>() {
			public int compare(Coordinate2f arg0, Coordinate2f arg1) {
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
		// With intensity interpolation.
		// #############################

		// Lower half.
		double dyl = sorted[left].getY() - sorted[0].getY();
		double dxl = (sorted[left].getX() - sorted[0].getX()) / dyl;
		double dyr = sorted[right].getY() - sorted[0].getY();
		double dxr = (sorted[right].getX() - sorted[0].getX()) / dyr;
		int yi = (int) Math.round(sorted[0].getY() + 0.5);
		double xl = sorted[0].getX() + (yi - sorted[0].getY()) * dxl;
		double xr = sorted[0].getX() + (yi - sorted[0].getY()) * dxr;
		
		Vector3f dil = intensities[left].subtract(intensities[0]).scale(1 / dyl);
		Vector3f dir = intensities[right].subtract(intensities[0]).scale(1 / dyr);
		Vector3f di = dir.subtract(dil).scale(Math.abs(dxr - dxl));
		Vector3f il = intensities[0].add(dil.scale(yi - sorted[0].getY()));
			
		while (yi <= sorted[1].getY()) {

			rasterLine(xl, xr, yi, il, di, Mb, vertices, obj, faceId, zbuf);

			yi++;
			xl += dxl;
			xr += dxr;
			il = il.add(dil);
		}

		// Upper half.
		if (left == 1) {
			dyl = sorted[2].getY() - sorted[1].getY();
			dxl = (sorted[2].getX() - sorted[1].getX()) / dyl;
			dil = intensities[2].subtract(intensities[1]).scale(1 / dyl);
			
			xl = sorted[1].getX() + (yi - sorted[1].getY()) * dxl;
			xr += dxr;
			il = intensities[1].add(dil.scale(yi - sorted[1].getY()));
		} else {
			dyr = sorted[2].getY() - sorted[1].getY();
			dxr = (sorted[2].getX() - sorted[1].getX()) / dyr;
			dir = intensities[2].subtract(intensities[1]).scale(1 / dyr);
			
			xl += dxl;
			xr = sorted[1].getX() + (yi - sorted[1].getY()) * dxr;
			il = il.add(dil);
		}
		yi++;
		di = dir.subtract(dil).scale(Math.abs(dxr - dxl));
		
		while(yi <= sorted[2].getY()) {

			rasterLine(xl, xr, yi, il, di, Mb, vertices, obj, faceId, zbuf);

			yi++;
			xl += dxl;
			xr += dxr;
			il = il.add(dil);
		}

	}

	private static void rasterLine(double xl, double xr, int yi, Vector3f il, Vector3f di,
			QuadMatrixf Mb, Coordinate2f[] vertices, GraphicObject obj,
			int faceId, ZBuffer zbuf) {
		int xi = (int) Math.round(xl + 0.5);
		while(xi <= xr) {
			// Calculate barycentric coordinates.
			Coordinate2f lambda = Mb.mult(new Coordinate2f(xi - vertices[2].getX(),
					yi - vertices[2].getY()));
			double lambda1 = lambda.getX();
			double lambda2 = lambda.getY();
			double lambda3 = 1 - lambda1 - lambda2;

			// Get color of point.
			Vector3f col = obj.getColor(faceId, lambda1, lambda2, lambda3);
			try {
				col = new Vector3f(col.getX() * il.getX(), col.getY() * il.getY(), col.getZ() * il.getZ());
			} catch(Exception e) {
				col = new Vector3f(0, 0, 0);
			}
			
			// TODO remove
			if((col.getX() < 0 || col.getX() > 1) ||
			  (col.getY() < 0 || col.getY() > 1) ||
			  (col.getZ() < 0 || col.getZ() > 1))
				col = new Vector3f(0, 0, 0);

			// Draw into zBuffer.
			zbuf.setPixel(xi, yi, 5, col);
			
			xi++;
			il = il.add(di);
		}
	}
}
