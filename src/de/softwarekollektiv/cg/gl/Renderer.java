package de.softwarekollektiv.cg.gl;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.Comparator;

import de.softwarekollektiv.cg.gl.Face;
import de.softwarekollektiv.cg.gl.math.Coordinate2f;
import de.softwarekollektiv.cg.gl.math.QuadMatrixf;
import de.softwarekollektiv.cg.gl.math.Vector4f;

public class Renderer {
	public static void render(Graphics g, int width, int height, GLScene scene) {
		assert(g != null && scene != null);
		
		final QuadMatrixf ndcMatrix = scene.getCamera().getNDCMatrix();
		final ZBuffer zbuf = new ZBuffer(width, height);
		
		for(int gidx = 0; gidx < scene.getNumObjects(); gidx++) {
			GraphicObject obj = scene.getGraphicObject(gidx);
			QuadMatrixf m = ndcMatrix.mult(scene.getTransformationMatrix(gidx));
			
			for(int faceId = 0; faceId < obj.size(); faceId++) {
				Face f = obj.getFace(faceId);
				
				Coordinate2f[] vertices = new Coordinate2f[3];
				for(int i = 0; i < 3; i++) {
					Vector4f vhomogen = f.getVertex(i).getHomogeneousVector4f();
					
					// Transform vertex to NDC.
					Vector4f ndcVector = m.mult(vhomogen);
					
					// Normalize homogeneous component.
					ndcVector = ndcVector.normalizeHomogeneous();
					
					// View port.
					double ndcx = ndcVector.getX();
					double ndcy = ndcVector.getY();
					vertices[i] = new Coordinate2f(((ndcx + 1) * (400 / 2)), ((ndcy + 1) * (400 / 2)));
				}
				
				rasterTriangle(vertices, obj, faceId, zbuf);
				
			} // Faces loop.
		} // GraphicObjects loop.
		
		// Flip screen.
		zbuf.draw(g);
	}
	
	private static void rasterTriangle(Coordinate2f[] vertices, GraphicObject obj, int faceId, ZBuffer zbuf) {
		// Prepare for barycentric coordinates.
		QuadMatrixf Mb = new QuadMatrixf(new double[][] {
				{
					vertices[0].getX() - vertices[2].getX(),
					vertices[1].getX() - vertices[2].getX(),
				},
				{
					vertices[0].getY() - vertices[2].getY(),
					vertices[1].getY() - vertices[2].getY(),
				}
		}).invert();
				
		// Sort.
		Coordinate2f[] sorted = Arrays.copyOf(vertices, 3);
		Arrays.sort(sorted, new Comparator<Coordinate2f>() {
			public int compare(Coordinate2f arg0, Coordinate2f arg1) {
				return (arg0.getY() < arg1.getY() ? -1
						: ((arg0.getY() > arg1.getY() ? 1 : 0)));
			}
		});
		
		// Test whether middle point is left or right of line.
		double ot = sorted[0].getX() * (sorted[2].getY() - sorted[1].getY()) + 
					sorted[2].getX() * (sorted[1].getY() - sorted[0].getY()) +
					sorted[1].getX() * (sorted[0].getY() - sorted[2].getY());
		int left = ot > 0 ? 1 : 2;
		int right = 2 - left + 1;

		// Simple scan-line algorithm.
		double[] dxl = new double[2];
		double[] dxr = new double[2];
		double dyl = sorted[left].getY() - sorted[0].getY();
		dxl[0] = (sorted[left].getX() - sorted[0].getX()) / dyl;
		double dyr = sorted[right].getY() - sorted[0].getY();
		dxr[0] = (sorted[right].getX() - sorted[0].getX()) / dyr;
		if(left == 1) {
			dyl = sorted[2].getY() - sorted[1].getY();
			dxl[1] = (sorted[2].getX() - sorted[1].getX()) / dyl;
			dxr[1] = dxr[0];
		} else {
			dyr = sorted[2].getY() - sorted[1].getY();
			dxr[1] = (sorted[2].getX() - sorted[1].getX()) / dyr;
			dxl[1] = dxl[0];
		}

		double xl = sorted[0].getX();
		double xr = xl;
		int yi = (int) Math.round(sorted[0].getY());
		
		double[] t = {sorted[1].getY(), sorted[2].getY()};
		for(int ti = 0; ti < 2; ti++) {
			for(; yi <= (int) Math.round(t[ti]); yi++) {
				for(int xi = (int) Math.round(xl); xi <= (int) Math.round(xr); xi++) {
					
					// Calculate barycentric coordinates.
					Coordinate2f lambda = Mb.mult(new Coordinate2f(xi - vertices[2].getX(), yi - vertices[2].getY()));
					double lambda1 = lambda.getX();
					double lambda2 = lambda.getY();
					double lambda3 = 1 - lambda1 - lambda2;
					
					// Get color of point.
					Color col = obj.getColor(faceId, lambda1, lambda2, lambda3);
					
					// Draw into zBuffer.
					zbuf.setPixel(xi, yi, 5, col);
				}
				
				xl += dxl[ti];
				xr += dxr[ti];
			}
		}
	}
}
