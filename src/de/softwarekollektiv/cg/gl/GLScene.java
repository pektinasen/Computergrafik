package de.softwarekollektiv.cg.gl;

import java.util.ArrayList;
import java.util.List;

import de.softwarekollektiv.cg.gl.math.QuadMatrixf;
import de.softwarekollektiv.cg.gl.math.Vector3f;

public class GLScene {
	private Camera cam;
	private Vector3f ambient_light;
	private double lightness;
	private Vector3f phong_constants;
	private double max_face_size;
	private Vector3f bgcol;
	private boolean use_radiosity;
	private int radiosity_iterations;
	
	private final List<GraphicObject> objs = new ArrayList<GraphicObject>();
	private final List<QuadMatrixf> transm = new ArrayList<QuadMatrixf>();
	private final List<Light> lights = new ArrayList<Light>();
	
	public GLScene() {
		// Set stupid phong constants to sane values.
		phong_constants = new Vector3f(0.01, 0.001, 0.00001);
		
		// Defaults.
		bgcol = Vector3f.ZERO;
		radiosity_iterations = 3;
		lightness = 1.00;
	}
	
	// #######################
	// Public API.
	// #######################
	
	public void clearObjects() {
		objs.clear();
		transm.clear();
	}
	
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
	
	public void setMaxFaceSize(double size) {
		max_face_size = size;
	}
	
	public void setAmbientLight(double r, double g, double b) {
		ambient_light = new Vector3f(r, g, b);
	}
	
	public void setLightness(double l) {
		lightness = l;
	}
	
	public void setBackgroundColor(Vector3f color) {
		bgcol = color;
	}
	
	public void setUseRadiosity(boolean active) {
		use_radiosity = active;
	}
	
	public void setRadiosityIterations(int iterations) {
		radiosity_iterations = iterations;
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
	
	final double getLightness() {
		return lightness;
	}
	
	final Vector3f getPhongConstants() {
		return phong_constants;
	}
	
	final double getMaxFaceSize() {
		return max_face_size;
	}
	
	final Vector3f getBackgroundColor() {
		return bgcol;
	}
	
	final boolean getUseRadiosity() {
		return use_radiosity;
	}

	int getRadiosityIterations() {
		return radiosity_iterations;
	}
}
