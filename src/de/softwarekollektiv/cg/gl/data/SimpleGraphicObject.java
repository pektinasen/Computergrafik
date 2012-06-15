package de.softwarekollektiv.cg.gl.data;

import java.util.ArrayList;
import java.util.List;

import de.softwarekollektiv.cg.gl.Face;
import de.softwarekollektiv.cg.gl.GraphicObject;

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
}
