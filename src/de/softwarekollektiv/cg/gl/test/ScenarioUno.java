package de.softwarekollektiv.cg.gl.test;

import de.softwarekollektiv.cg.gl.Camera;
import de.softwarekollektiv.cg.gl.GLScene;
import de.softwarekollektiv.cg.gl.GraphicObject;
import de.softwarekollektiv.cg.gl.Light;
import de.softwarekollektiv.cg.gl.data.SimpleLight;
import de.softwarekollektiv.cg.gl.math.QuadMatrixf;

class ScenarioUno extends GLScene {
	ScenarioUno() {

		// Object 1: The World.
		GraphicObject world = new World();
		QuadMatrixf worldM = new QuadMatrixf(new double[][] {
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
		GraphicObject cube = new Cube();
		double alpha = Math.toRadians(35);
		QuadMatrixf cubeM = new QuadMatrixf(new double[][] { // Translate/Scale.
				{
					3, 0, 0, -3
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
					Math.cos(alpha), -Math.sin(alpha), 0, 0
				},
				{
					Math.sin(alpha), Math.cos(alpha), 0, 0
				},
				{
					0, 0, 1, 0
				},
				{
					0, 0, 0, 1
				}	
		}));
		
		// Lights.
		Light light1 = new SimpleLight(-9.5, -9.5, 0.5, 1.0, 0.0, 0.0);
		Light light2 = new SimpleLight(9.5, -9.5, 0.5, 0.0, 1.0, 1.0);
		
		// Camera.
		Camera cam = new Camera(0, 30, 10, 0.0, -1.0, 0.0, 0, 0, 1, 1, 60, 1, 100);
		
		// Define the scene.
		this.addGraphicObject(world, worldM);
		this.addGraphicObject(cube, cubeM);
		this.setUseLighting(true);
		this.setAmbientLight(1.0, 1.0, 1.0);
		this.addLight(light1);
		this.addLight(light2);
		this.setCamera(cam);
	}
}
