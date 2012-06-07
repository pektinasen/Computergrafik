package de.softwarekollektiv.cg.uebungen.uebung7;

import java.awt.Color;
import java.util.Arrays;
import java.util.Iterator;

/**
 * A Face consisting of three vertees in object coordinates.
 * @author Sascha
 *
 */
public class Face implements Iterable<Vector>{
	

	Vector[] v = new Vector[3];

	public Face(Vector v1, Vector v2, Vector v3) {
		super();
		this.v[0] = v1;
		this.v[1] = v2;
		this.v[2] = v3;
	}
	
	public Vector getVertex(int idx) {
		return v[idx];
	}

	@Override
	public Iterator<Vector> iterator() {
		return Arrays.asList(v).iterator();
	}

	public Color getColor(double lambda1, double lambda2, double lambda3) {
		return new Color((float) lambda1, (float) lambda2, (float) lambda3);
	}


}
