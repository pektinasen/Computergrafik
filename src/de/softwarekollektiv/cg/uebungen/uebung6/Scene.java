package de.softwarekollektiv.cg.uebungen.uebung6;

import java.awt.Color;
import java.awt.Graphics;

class Scene {

	// Ambient reflection coefficient of the plane.
	float kar;
	float kag;
	float kab;

	// Intensity of ambient light.
	float iar;
	float iag;
	float iab;

	// Diffuse reflection coefficient of the plane.
	float kdr;
	float kdg;
	float kdb;

	// Intensity of light spot.
	float ilr;
	float ilg;
	float ilb;

	// Position of light spot in relation to the ball,
	// i.e. the ball center is the coordinate origin here.
	float plx;
	float ply;
	float plz;

	// Constants.
	float c1;
	float c2;
	float c3;
	
	// Specular reflection coefficient of the plane.
	float ks;
	
	// Material constant for specular reflection.
	float n;

	void draw(Graphics g, int width, int height) {
		// Calculate ball dimensions: Set ball diameter to 95% of shorter
		// side of tile, ball center to center of tile.
		// The ball's z-coordinate is zero!
		float radius = ((width < height) ? width : height) * 0.95f / 2;
		float radius2 = radius * radius;
		float mx = width / 2.0f;
		float my = height / 2.0f;

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {

				// Test whether point is actually on the ball (circle).
				if ((x - mx) * (x - mx) + (y - my) * (y - my) > radius2)
					continue;

				// ###########################
				// Ambient light.
				// ###########################
				
				float res_ambient_r = kar * iar;
				float res_ambient_g = kag * iag;
				float res_ambient_b = kab * iab;

				// ###########################
				// Diffuse light.
				// ###########################
				
				// Surface vector for current surface on ball.
				float Nx = (float) x - mx;
				float Ny = (float) y - my;
				float Nz = (float) -Math.sqrt(radius2 - Nx * Nx - Ny * Ny);
				
				// Light vector.
				float Lx = plx - Nx;
				float Ly = ply - Ny;
				float Lz = plz - Nz;
				
				// Distance to light source.
				float rl = (float) Math.sqrt(Lx * Lx + Ly * Ly + Lz * Lz);
				
				// Normalize vectors.
				Lx /= rl;
				Ly /= rl;
				Lz /= rl;
				Nx /= radius;
				Ny /= radius;
				Nz /= radius;
				
				// Arc between vectors.
				float costheta = Lx * Nx + Ly * Ny + Lz * Nz;
				
				// Only emit diffuse reflection if the arc is between
				// zero and 90 degrees.
				if(costheta < 0 || costheta > 1)
					costheta = 0;
				
				// Const part of formula.
				float cfactor = c1 + c2 * rl + c3 * rl * rl;
				float diffuse_c = costheta / cfactor;
											
				float res_diffuse_r = diffuse_c * ilr * kdr;
				float res_diffuse_g = diffuse_c * ilg * kdg;
				float res_diffuse_b = diffuse_c * ilb * kdb;
				
				// ###########################
				// Specular light.
				// ###########################
				
				// We assume the camera position to be 
				// always on top of the ball.
				float Ax = 0;
				float Ay = 0;
				float Az = -1;
				
				float LNp = Lx * Nx + Ly * Ny + Lz * Nz;
				float NAp = Nx * Ax + Ny * Ay + Nz * Az;
				float ALp = Ax * Lx + Ay * Ly + Az * Lz;		
				float cosalpha = 2 * LNp * NAp - ALp;
				
				// Same here.
				if(cosalpha < 0 || cosalpha > 1)
					cosalpha = 0;
				
				// Const part of the formula.
				float specular_c = (float) (ks * Math.pow(cosalpha, n) / cfactor); 
				
				float res_specular_r = specular_c * ilr;
				float res_specular_g = specular_c * ilg;
				float res_specular_b = specular_c * ilb;

				// ###########################
				// Finish.
				// ###########################
				
				float res_r = res_ambient_r + res_diffuse_r + res_specular_r;
				float res_g = res_ambient_g + res_diffuse_g + res_specular_g;
				float res_b = res_ambient_b + res_diffuse_b + res_specular_b;
				
				// Cut colors at 1.0f.
				res_r = (res_r > 1.0f) ? 1.0f : res_r;
				res_g = (res_g > 1.0f) ? 1.0f : res_g;
				res_b = (res_b > 1.0f) ? 1.0f : res_b;
				
				g.setColor(new Color(res_r, res_g, res_b));
				g.drawRect(x, y, 1, 1);

			}
		}
	}
}
