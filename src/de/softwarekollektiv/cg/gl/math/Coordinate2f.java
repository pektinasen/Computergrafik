package de.softwarekollektiv.cg.gl.math;

public final class Coordinate2f {
	private final double x;
	private final double y;	

	public Coordinate2f(final double x, final double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "x: "+x +" y: "+y;
	}
}
