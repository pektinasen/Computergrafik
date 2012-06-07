package de.softwarekollektiv.cg.uebungen.uebung7;

import org.ejml.simple.SimpleMatrix;

import sun.java2d.pipe.SpanShapeRenderer.Simple;

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
	
	private int vOben;
	
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
		double uRechts = Math.tan(30) * n_close;
		double uLinks = - uRechts;
		
		double urmul = uRechts - uLinks;
		double vOben = Math.tan(30) * n_close;
		double vUnten = - vOben;
		double vrmvl = vOben - vUnten;
		
		double[][] M_a = {
				{2 * n_close / urmul, 0 , 0 , 0 },
				{0 , 2 * n_close / vrmvl, 0 , 0 },
				{0 , 0 , - ((n_distant + n_close) / (n_distant - n_close)), -(2 * n_distant * n_close / (n_distant - n_close))},
				{ 0 , 0 , -1 , 0 }
		};
		
		return new SimpleMatrix(M_a);
		
	}
	
	
	
	public static void main(String[] args) {
		double[][] d = {
				{1,2,3},
				{4,5,6},
				{7,8,9}
		};
		SimpleMatrix m1 = new SimpleMatrix(d);
		System.out.println(m1);
		double[][] a = {{1},{2},{3}};
		SimpleMatrix m2 = new SimpleMatrix(a);
		SimpleMatrix m3 = m1.combine(0, 3, m2);
		System.out.println(m3);
		double[][] b = {{0,0,0,0}};
		SimpleMatrix m4 = m3.combine(3, 0, new SimpleMatrix(b));
		System.out.println(m4);
		
		
		
		
	}
	
}
