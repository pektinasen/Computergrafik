package de.softwarekollektiv.cg.gl.test;

import de.softwarekollektiv.cg.gl.Face;
import de.softwarekollektiv.cg.gl.GraphicObject;
import de.softwarekollektiv.cg.gl.Material;
import de.softwarekollektiv.cg.gl.data.SimpleColorTexture;
import de.softwarekollektiv.cg.gl.data.SimpleFace;
import de.softwarekollektiv.cg.gl.data.SimpleMaterial;
import de.softwarekollektiv.cg.gl.math.Vector3f;
import de.softwarekollektiv.cg.gl.Texture;

class Cube implements GraphicObject {

	private final Face[] faces = new Face[12];
	
	Cube() {
		// A simple 2x2m cube.
		Texture t = new SimpleColorTexture(Vector3f.ONE);
		Material m = SimpleMaterial.METALL;
		
		Vector3f a = new Vector3f(-1, 1, -1);
		Vector3f b = new Vector3f(1, 1, -1);
		Vector3f c = new Vector3f(1, 1, 1);
		Vector3f d = new Vector3f(-1, 1, 1);
		Vector3f e = new Vector3f(-1, -1, -1);
		Vector3f f = new Vector3f(1, -1, -1);
		Vector3f g = new Vector3f(1, -1, 1);
		Vector3f h = new Vector3f(-1, -1, 1);
		
		faces[0] = new SimpleFace(a, b, c, m, t);
		faces[1] = new SimpleFace(c, d, a, m, t);
		
		faces[2] = new SimpleFace(b, f, g, m, t);
		faces[3] = new SimpleFace(g, c, b, m, t);
		
		faces[4] = new SimpleFace(d, c, g, m, t);
		faces[5] = new SimpleFace(g, h, d, m, t);
		
		faces[6] = new SimpleFace(e, a, d, m, t);
		faces[7] = new SimpleFace(d, h, e, m, t);
		
		faces[8] = new SimpleFace(e, a, b, m, t);
		faces[9] = new SimpleFace(b, f, e, m, t);
		
		faces[10] = new SimpleFace(f, e, h, m, t);
		faces[11] = new SimpleFace(h, g, f, m, t);
	}
	
	@Override
	public int size() {
		return 12;
	}

	@Override
	public Face getFace(int face) {
		return faces[face];
	}

}
