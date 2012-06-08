package de.softwarekollektiv.cg.gl;

import java.awt.Graphics;
import java.util.Arrays;
import java.util.Comparator;

import org.ejml.simple.SimpleMatrix;

import de.softwarekollektiv.cg.gl.Face;
import de.softwarekollektiv.cg.gl.math.Coordinate2f;
import de.softwarekollektiv.cg.gl.math.Vector3f;

public class Renderer {
	public static void render(Graphics g, GLScene scene) {
		assert(g != null && scene != null);
		
		final SimpleMatrix ndcMatrix = scene.getCamera().getNDCMatrix();
		
		for(int gidx = 0; gidx < scene.getNumObjects(); gidx++) {
			GraphicObject obj = scene.getGraphicObject(gidx);
			SimpleMatrix m = ndcMatrix.mult(scene.getTransformationMatrix(gidx));
			
			for(int faceId = 0; faceId < obj.size(); faceId++) {
				Face f = obj.getFace(faceId);
				
				Coordinate2f[] vertices = new Coordinate2f[3];
				for(int i = 0; i < 3; i++) {
					Vector3f v = f.getVertex(i);
					
					// Transform vertex to NDC.
					SimpleMatrix ndcVector = m.mult(v.getHomogeneousMatrix());
					
					// Normalize homogeneous component.
					ndcVector = ndcVector.divide(ndcVector.get(3, 0));
					
					// View port.
					double ndcx = ndcVector.get(0, 0);
					double ndcy = ndcVector.get(1, 0);
					vertices[i] = new Coordinate2f(((ndcx + 1) * (400 / 2)), ((ndcy + 1) * (400 / 2)));
				}
				
				// Prepare for barycentric coordinates.
				SimpleMatrix Mb = new SimpleMatrix(new double[][] {
						{
							vertices[0].getX() - vertices[2].getX(),
							vertices[1].getX() - vertices[2].getX(),
						},
						{
							vertices[0].getY() - vertices[2].getY(),
							vertices[1].getY() - vertices[2].getY(),
						}
				}).invert();
				
				// #################################
				// Rastering. Only triangles so far.
				// #################################
				
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
							SimpleMatrix lambda = Mb.mult(new SimpleMatrix(new double[][] {
									{
										xi - vertices[2].getX()
									},
									{
										yi - vertices[2].getY()
									}
							}));
							double lambda1 = lambda.get(0, 0);
							double lambda2 = lambda.get(1, 0);
							double lambda3 = 1 - lambda1 - lambda2;
							
							// Get color of point.
							g.setColor(obj.getColor(faceId, lambda1, lambda2, lambda3));
							g.fillRect(xi, yi, 1, 1);				
						}
						
						xl += dxl[ti];
						xr += dxr[ti];
					}
				}	// Scan-line loop.
			} // Faces loop.
		} // GraphicObjects loop.
	}
}
