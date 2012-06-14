package de.softwarekollektiv.cg.gl;

import de.softwarekollektiv.cg.gl.math.Vector3f;

class PhongLightning {
	static Vector3f getIntensity(GLScene scene, Face f, Vector3f N, Vector3f point) {		
		
		// NOTE: Remember, .get{X,Y,Z}() corresponds to (R,G,B)!
		
		// ###########################
		// Ambient light.
		// ###########################
		
		Vector3f arc = f.getMaterial().getAmbientReflectionCoefficient();
		Vector3f al = scene.getAmbientLight();
		Vector3f res_ambient = new Vector3f(
			arc.getX() * al.getX(),
			arc.getY() * al.getY(),
			arc.getZ() * al.getZ()
		);

		// ###########################
		// Diffuse light.
		// ###########################
							
		// Surface vector.
		Vector3f Nnorm = N.normalize();
		
		// Phong constants.
		Vector3f cphong = scene.getPhongConstants();
		
		// Diffuse reflection coefficient.
		Vector3f drc = f.getMaterial().getDiffuseReflectionCoefficient();
		
		// Vector to eye.
		Vector3f A = scene.getCamera().getPosition().subtract(point);
		Vector3f Anorm = A.normalize();
		
		// Accumulators.
		double res_diffuse_r = 0;
		double res_diffuse_g = 0;
		double res_diffuse_b = 0;
		double res_specular_r = 0;
		double res_specular_g = 0;
		double res_specular_b = 0;
		
		for(int lidx = 0; lidx < scene.getNumLights(); lidx++) {
			Light light = scene.getLight(lidx);
			Vector3f lposition = light.getPosition();
			Vector3f lintensity = light.getIntensity();
			
			// Light vector.
			Vector3f L = lposition.subtract(point);
			Vector3f Lnorm = L.normalize();
			
			// Distance to light source & constant factor.
			double rl = L.length();
			double cfactor = cphong.getX() + cphong.getY() * rl + cphong.getZ() * rl * rl;
			
			double costheta = Lnorm.scalarProduct(Nnorm);
			
			// Only emit diffuse reflection if the arc is between
			// zero and 90 degrees.
			if(costheta >= 0) {
			
				double diffuse_c = costheta / cfactor;
				res_diffuse_r += diffuse_c * lintensity.getX() * drc.getX();
				res_diffuse_g += diffuse_c * lintensity.getY() * drc.getY();
				res_diffuse_b += diffuse_c * lintensity.getZ() * drc.getZ();
				
			}
			
			// ###########################
			// Specular light.
			// ###########################
							
			double LNp = Lnorm.scalarProduct(Nnorm);
			double NAp = Nnorm.scalarProduct(Anorm);
			double ALp = Anorm.scalarProduct(Lnorm);	
			double cosalpha = 2 * LNp * NAp - ALp;
			
			// Same here.
			if(cosalpha >= 0) {
				// Const part of the formula.
				float specular_c = (float) (f.getMaterial().getSpecularReflectionCoefficient() * Math.pow(cosalpha, f.getMaterial().getSpecularN()) / cfactor); 
				
				res_specular_r += specular_c * lintensity.getX();
				res_specular_g += specular_c * lintensity.getY();
				res_specular_b += specular_c * lintensity.getZ();
			}
		}
		
		Vector3f res_diffuse = new Vector3f(res_diffuse_r, res_diffuse_g, res_diffuse_b);
		Vector3f res_specular = new Vector3f(res_specular_r, res_specular_g, res_specular_b);

		// ###########################
		// Finish.
		// ###########################
		
		return res_ambient.add(res_diffuse).add(res_specular).normalize();
	}
}
