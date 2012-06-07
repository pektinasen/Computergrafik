package de.softwarekollektiv.cg.uebungen.uebung7;

import org.ejml.simple.SimpleMatrix;

public class Vector {

	double x;
	double y;
	double z;
	
	public Vector(double x, double y, double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector vectorProduct(Vector v){
		return new Vector(
				this.y * v.z - this.z * v.y,
				this.z * v.x - this.x * v.z,
				this.x * v.y - this.y * v.x
				);
	}
	
	public SimpleMatrix getHomogenMatrix(){
	
		return new SimpleMatrix(new double[][]{
				{x},
				{y},
				{z},
				{1}
		});
	}
	
	public double[] toArray(){
		return new double[]{x,y,z};
	}
	
}
