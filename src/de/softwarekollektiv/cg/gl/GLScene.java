package de.softwarekollektiv.cg.gl;

import java.util.ArrayList;
import java.util.List;

import de.softwarekollektiv.cg.gl.math.QuadMatrixf;
import de.softwarekollektiv.cg.gl.math.Vector3f;

public class GLScene {
	private Camera cam;
	private Vector3f ambient_light;
	private Vector3f phong_constants;
	private boolean use_lightning;
	
	private final List<GraphicObject> objs = new ArrayList<GraphicObject>();
	private final List<QuadMatrixf> transm = new ArrayList<QuadMatrixf>();
	private final List<Light> lights = new ArrayList<Light>();
	
	public GLScene() {
		// Set stupid phong constants to sane values.
		phong_constants = new Vector3f(0.001, 0.0001, 0.0000001);
	}
	
	// #######################
	// Public API.
	// #######################
	
	public void setCamera(final Camera cam) {
		assert(cam != null);
		
		this.cam = cam;
	}
	
	public void addGraphicObject(final GraphicObject go, final QuadMatrixf t) {
		assert(go != null && t != null);
		
		objs.add(go);
		transm.add(t);
	}
	
	public void addLight(final Light l) {
		assert(l != null);
		
		lights.add(l);
	}
	
	public void setUseLighting(boolean active) {
		use_lightning = active;
	}
	
	public void setAmbientLight(double r, double g, double b) {
		this.ambient_light = new Vector3f(r, g, b);
	}
	
	// #######################
	// Internals.
	// #######################
	
	final Camera getCamera() {
		assert(cam != null);
		
		return cam;
	}
	
	final int getNumObjects() {
		return objs.size();
	}
	
	final GraphicObject getGraphicObject(final int idx) {
		assert(idx > 0 && idx < objs.size());
		
		return objs.get(idx);
	}
	
	final QuadMatrixf getTransformationMatrix(final int idx) {
		assert(idx > 0 && idx < objs.size());
		
		return transm.get(idx);
	}
		
	final int getNumLights() {
		return lights.size();
	}
	
	final Light getLight(final int idx) {
		assert(idx > 0 && idx < lights.size());
		
		return lights.get(idx);
	}
	
	final Vector3f getAmbientLight() {
		assert(ambient_light != null);
		
		return ambient_light;
	}
	
	final Vector3f getPhongConstants() {
		return phong_constants;
	}
	
	final boolean getUseLightning() {
		return use_lightning;
	}
}