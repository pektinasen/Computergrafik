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
	private Texture t;

	public Face(Vector v1, Vector v2, Vector v3) {
		super();
		this.v[0] = v1;
		this.v[1] = v2;
		this.v[2] = v3;
	}
	
	public Face(Vector v1, Vector v2, Vector v3, Texture tf1) {
		this(v1, v2, v3);
		this.t = tf1;
	}

	public Vector getVertex(int idx) {
		return v[idx];
	}

	@Override
	public Iterator<Vector> iterator() {
		return Arrays.asList(v).iterator();
	}

	public Color getColor(double lambda1, double lambda2, double lambda3) {
		if(lambda1 < 0) {
//			System.out.println("lambda1: " + lambda1);
			return Color.BLACK;
		}
		if(lambda2 < 0) {
//			System.out.println("lambda2: " + lambda2);
			return Color.BLACK;
		}
		if(lambda3 < 0) {
//			System.out.println("lambda3: " + lambda3);
			return Color.BLACK;
		}
		if(t != null)
			return t.getColor(lambda1, lambda2, lambda3);
		else
			return new Color((float) lambda1, (float) lambda2, (float) lambda3);
	}


}
