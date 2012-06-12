package de.softwarekollektiv.cg.gl.data;

import java.util.ArrayList;
import java.util.List;

import de.softwarekollektiv.cg.gl.Face;
import de.softwarekollektiv.cg.gl.GraphicObject;
import de.softwarekollektiv.cg.gl.math.Vector3f;

/**
 * SimpleGraphicObject.
 * 
 * @author Malte Rohde <malte.rohde@inf.fu-berlin.de>
 */
public class SimpleGraphicObject implements GraphicObject {

	private final List<Face> faces = new ArrayList<Face>();
	
	public void addFace(final Face f) {
		assert(f != null);
		
		faces.add(f);
	}
	
	@Override
	public int size() {
		return faces.size();
	}

	@Override
	public Face getFace(int idx) {
		assert(idx > 0 && idx < faces.size());
		
		return faces.get(idx);
	}

	@Override
	public Vector3f getColor(int face, double l1, double l2, double l3) {
		assert(l1 >= 0 && l2 >= 0 && l3 >= 0 && (l1 + l2 + l3) == 1);
			
		return new Vector3f(1.0, 1.0, 1.0);
	}

}
