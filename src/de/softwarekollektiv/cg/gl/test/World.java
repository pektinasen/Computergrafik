package de.softwarekollektiv.cg.gl.test;

import java.util.ArrayList;
import java.util.List;

import de.softwarekollektiv.cg.gl.Face;
import de.softwarekollektiv.cg.gl.GraphicObject;
import de.softwarekollektiv.cg.gl.Material;
import de.softwarekollektiv.cg.gl.Texture;
import de.softwarekollektiv.cg.gl.data.SimpleColorTexture;
import de.softwarekollektiv.cg.gl.data.SimpleFace;
import de.softwarekollektiv.cg.gl.data.SimpleMaterial;
import de.softwarekollektiv.cg.gl.math.Vector3f;

public class World implements GraphicObject {

	private final List<Face> f;
	
	World(boolean wall_light) {
		final int RECURSION_DEPTH = 0;
		f = new ArrayList<Face>((int) (8 * Math.pow(2.0, RECURSION_DEPTH)));
		
		// A room of 20mx20m, no roof, one wall missing.
		Texture tw = new SimpleColorTexture(new Vector3f(0.2, 0.2, 0.2));
		Texture tf = new SimpleColorTexture(new Vector3f(0.0, 0.5, 0.4));
		Material m = SimpleMaterial.STONE;
			
		// Floor.
		Vector3f w0f00 = new Vector3f(0.0, 0.0, 0.0);
		Vector3f w0f01 = new Vector3f(0.0, 20.0, 0.0);
		Vector3f w0f02 = new Vector3f(20.0, 20.0, 0.0);
		Vector3f w0f10 = new Vector3f(20.0, 20.0, 0.0);
		Vector3f w0f11 = new Vector3f(20.0, 0.0, 0.0);
		Vector3f w0f12 = new Vector3f(0.0, 0.0, 0.0);
		f.add(new SimpleFace(w0f00, w0f01, w0f02, m, tf));
		f.add(new SimpleFace(w0f10, w0f11, w0f12, m, tf));
		
		// Left wall.
		Vector3f w1f00 = new Vector3f(0.0, 0.0, 0.0);
		Vector3f w1f01 = new Vector3f(0.0, 20.0, 0.0);
		Vector3f w1f02 = new Vector3f(0.0, 20.0, 20.0);
		Vector3f w1f10 = new Vector3f(0.0, 20.0, 20.0);
		Vector3f w1f11 = new Vector3f(0.0, 0.0, 20.0);
		Vector3f w1f12 = new Vector3f(0.0, 0.0, 0.0);
		if(wall_light) {
			f.add(new SimpleFace(w1f00, w1f01, w1f02, m, tw, Vector3f.ONE));
			f.add(new SimpleFace(w1f10, w1f11, w1f12, m, tw, Vector3f.ONE));
		} else {
			f.add(new SimpleFace(w1f00, w1f01, w1f02, m, tw));
			f.add(new SimpleFace(w1f10, w1f11, w1f12, m, tw));
		}
		
		// Back wall.
		Vector3f w2f00 = new Vector3f(0.0, 0.0, 0.0);
		Vector3f w2f01 = new Vector3f(20.0, 0.0, 0.0);
		Vector3f w2f02 = new Vector3f(20.0, 0.0, 20.0);
		Vector3f w2f10 = new Vector3f(20.0, 0.0, 20.0);
		Vector3f w2f11 = new Vector3f(0.0, 0.0, 20.0);
		Vector3f w2f12 = new Vector3f(0.0, 0.0, 0.0);
		f.add(new SimpleFace(w2f00, w2f01, w2f02, m, tw));
		f.add(new SimpleFace(w2f10, w2f11, w2f12, m, tw));	
		
		// Right wall.
		Vector3f w3f00 = new Vector3f(20.0, 0.0, 0.0);
		Vector3f w3f01 = new Vector3f(20.0, 20.0, 0.0);
		Vector3f w3f02 = new Vector3f(20.0, 20.0, 20.0);
		Vector3f w3f10 = new Vector3f(20.0, 20.0, 20.0);
		Vector3f w3f11 = new Vector3f(20.0, 0.0, 20.0);
		Vector3f w3f12 = new Vector3f(20.0, 0.0, 0.0);
		if(wall_light) {
			f.add(new SimpleFace(w3f00, w3f01, w3f02, m, tw, Vector3f.ONE));
			f.add(new SimpleFace(w3f10, w3f11, w3f12, m, tw, Vector3f.ONE));
		} else {
			f.add(new SimpleFace(w3f00, w3f01, w3f02, m, tw));
			f.add(new SimpleFace(w3f10, w3f11, w3f12, m, tw));	
		}
	}
	
	@Override
	public int size() {
		return f.size();
	}

	@Override
	public Face getFace(int idx) {
		return f.get(idx);
	}
}
