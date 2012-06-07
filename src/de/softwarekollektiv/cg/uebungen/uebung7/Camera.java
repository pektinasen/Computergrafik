package de.softwarekollektiv.cg.uebungen.uebung7;

import org.ejml.simple.SimpleMatrix;

public class Camera {

	double posX;
	double posY;
	double posZ;
	
	//Direction the camera looks to
	double dirX;
	double dirY;
	double dirZ;
	
	//the "oben"-Komponente. should have just two components
	double topX;
	double topY;
	double topZ;
	
	double n_close;
	double n_distant;
	double aspect_ratio; // width/height
	double fov;	// Horizontal field of view (e.g., 60 degrees)
	
	void setPosition(double x, double y, double z){
		this.posX = x;
		this.posY = y;
		this.posZ = z;
	}
	
	void setDirection(double x, double y, double z){
		this.dirX = x;
		this.dirY = y;
		this.dirZ = z;
	}
	
	void setTop(double x, double y, double z){
		this.topX = x;
		this.topY = y;
		this.topZ = z;
	}
	
	SimpleMatrix getAugMatrix(){
		
		Vector n = new Vector( -dirX, -dirY, -dirZ );
		// v = oben
		Vector v = new Vector( topX, topY, topZ );
		Vector u = v.vectorProduct(n);
		
		double[][] A_t = {
				u.toArray(),
				v.toArray(),
				n.toArray()
		};
		
		SimpleMatrix At = new SimpleMatrix(A_t);
		double[][] auge_a = {{posX},{posY},{posZ}};
		SimpleMatrix auge = new SimpleMatrix(auge_a);
		
		SimpleMatrix Atn = At.negative().mult(auge);
		
		SimpleMatrix M_AW = At.combine(0, 3, Atn).combine(3,0,new SimpleMatrix(1, 4));
		M_AW.set(3, 3, 1);
		
		return M_AW;
	}
	
	SimpleMatrix getNdcMatrix(){
		double tangent = Math.tan(Math.toRadians(fov / 2));
		double ur = tangent * n_close;
		double ul = -ur;
		double vt = ur / aspect_ratio;
		double vb = -vt;
			
		double[][] M_a = {
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
		};
		
		return new SimpleMatrix(M_a);
		
	}
	
	
	
	public static void main(String[] args) {
		System.out.println(Math.tan(Math.toRadians(30)));
		
	}
	
}
