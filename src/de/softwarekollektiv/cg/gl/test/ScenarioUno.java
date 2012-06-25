package de.softwarekollektiv.cg.gl.test;

import de.softwarekollektiv.cg.gl.Camera;
import de.softwarekollektiv.cg.gl.GLScene;
import de.softwarekollektiv.cg.gl.GraphicObject;
import de.softwarekollektiv.cg.gl.math.QuadMatrixf;

class ScenarioUno extends GLScene {
	
	private final GraphicObject world;
	private final QuadMatrixf worldM;
	private final GraphicObject cube;
	
	private double alpha;
	
	void update() {	
		alpha += 20;
		alpha %= 360;
		
		double radalpha = Math.toRadians(alpha);
		QuadMatrixf cubeM = new QuadMatrixf(new double[][] { // Translate/Scale.
				{
					3, 0, 0, -5
				},
				{
					0, 3, 0, 0
				},
				{
					0, 0, 3, 4
				},
				{
					0, 0, 0, 1
				}	
		}).mult(new QuadMatrixf(new double[][] { // Rotate before.
				{
					Math.cos(radalpha), -Math.sin(radalpha), 0, 0
				},
				{
					Math.sin(radalpha), Math.cos(radalpha), 0, 0
				},
				{
					0, 0, 1, 0
				},
				{
					0, 0, 0, 1
				}	
		}));
		
		this.clearObjects();
		this.addGraphicObject(world, worldM);
		this.addGraphicObject(cube, cubeM);
	}
	
	ScenarioUno() {
		// Object 1: The World.
		world = new World(true);
		worldM = new QuadMatrixf(new double[][] {
				{
					1, 0, 0, -10
				},
				{
					0, 1, 0, -10
				},
				{
					0, 0, 1, 0
				},
				{
					0, 0, 0, 1
				}
		});
		
		// Object 2: The Cube.
		cube = new Cube();
		
		// Lights.
//		Light light1 = new SimpleLight(-9.5, -9.5, 0.5, 1.0, 0.0, 0.0);
//		Light light2 = new SimpleLight(9.5, -9.5, 0.5, 0.0, 1.0, 1.0);
//		
		// Camera.
		Camera cam = new Camera(0, 30, 10, 0.0, -1.0, 0.0, 0, 0, 1, 1, 60, 1, 100);
		
		// Define the scene.
		this.setMaxFaceSize(1.0);
		this.setUseRadiosity(true);
		this.setAmbientLight(1.0, 1.0, 1.0);
//		this.addLight(light1);
//		this.addLight(light2);
		this.setCamera(cam);
		
		// Initial alpha.
		alpha = 25;
		update();
	}
}
