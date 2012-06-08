package de.softwarekollektiv.cg.uebungen.uebung7;

import org.ejml.simple.SimpleMatrix;

public class Vector {

	final double x;
	final double y;
	final double z;
	
	public Vector(double x, double y, double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector minus(Vector v){
		return new Vector(this.x-v.x, this.y - v.y , this.z - v.z);
	}
	
	public Vector plus(Vector v){
		return new Vector(this.x+v.x, this.y + v.y , this.z + v.z);
	}
	
	public Vector mult(double d){
		return new Vector(d * this.x, this.y *d , this.z *d);
	}
	
	public double mult(Vector v){
		return this.x * v.x +  this.y * v.y + this.z * v.z;
	}
	
	public double length(){
		return Math.sqrt(this.mult(this));
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
	
	public static Vector Vector(double x, double y,double z ) {
		return new Vector(x,y,z);
	}
	
	public static Vector Vector(double x, double y ) {
		return new Vector(x,y,0 );
	}
	
	
}
