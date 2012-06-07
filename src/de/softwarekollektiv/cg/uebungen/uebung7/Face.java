package de.softwarekollektiv.cg.uebungen.uebung7;

import java.util.Iterator;

/**
 * A Face consisting of three vertees in object coordinates.
 * @author Sascha
 *
 */
public class Face implements Iterable<Vector>{
	

	Vector v1;
	Vector v2;
	Vector v3;

	public Face(Vector v1, Vector v2, Vector v3) {
		super();
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
	}

	@Override
	public Iterator<Vector> iterator() {
		// TODO Auto-generated method stub
		return new Iterator<Vector>() {
			
			int i = -1;
			
			@Override
			public boolean hasNext() {
				// TODO Auto-generated method stub
				return (i < 2);
			}

			@Override
			public Vector next() {
				i++;
				if (i == 0 ) {
					return v1;
				} 
				if (i == 1 ) {
					return v2;
				}
				if (i == 2 ) {
					return v3;
				}
				return null;
			}

			@Override
			public void remove() {
				System.err.println("Not supported Operation");
			}
		};
	}


}
