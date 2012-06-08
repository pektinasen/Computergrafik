package de.softwarekollektiv.cg.gl.data;

import java.awt.Color;
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

	@Override
	public Color getColor(int face, double l1, double l2, double l3) {
		if(l1 < 0 || l2 < 0 || l3 < 0)
			return Color.BLACK;
		return new Color((float) l1, (float) l2, (float) l3);
	}

}
