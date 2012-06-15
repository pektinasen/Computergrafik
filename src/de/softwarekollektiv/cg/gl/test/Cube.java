package de.softwarekollektiv.cg.gl.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.softwarekollektiv.cg.gl.Face;
import de.softwarekollektiv.cg.gl.GraphicObject;
import de.softwarekollektiv.cg.gl.Material;
import de.softwarekollektiv.cg.gl.data.SimpleColorTexture;
import de.softwarekollektiv.cg.gl.data.SimpleFace;
import de.softwarekollektiv.cg.gl.data.SimpleImageTexture;
import de.softwarekollektiv.cg.gl.data.SimpleMaterial;
import de.softwarekollektiv.cg.gl.math.Vector3f;
import de.softwarekollektiv.cg.gl.Texture;

class Cube implements GraphicObject {

	private final Face[] faces = new Face[12];
	
	Cube() {
		// A simple 2x2m cube.
		Texture t = new SimpleColorTexture(new Vector3f(0.5, 0.5, 0.5));
		Material m = SimpleMaterial.METALL;
		Material m2 = new SimpleMaterial(1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1, 2);
		
		Vector3f a = new Vector3f(-1, 1, -1);
		Vector3f b = new Vector3f(1, 1, -1);
		Vector3f c = new Vector3f(1, 1, 1);
		Vector3f d = new Vector3f(-1, 1, 1);
		Vector3f e = new Vector3f(-1, -1, -1);
		Vector3f f = new Vector3f(1, -1, -1);
		Vector3f g = new Vector3f(1, -1, 1);
		Vector3f h = new Vector3f(-1, -1, 1);
		
		Texture tf1, tf2;
		try {
			BufferedImage bimg = ImageIO.read(new File("alexa.jpg"));
			tf1 = new SimpleImageTexture(bimg, new double[][] { { 0, 0 },
					{ 1, 0 }, { 1, 1 } });
			tf2 = new SimpleImageTexture(bimg, new double[][] { { 1, 1 },
					{ 0, 1 }, { 0, 0 } });
		} catch (IOException e1) {
			System.out.println(e1);
			tf1 = SimpleColorTexture.RED;
			tf2 = SimpleColorTexture.GREEN;
		}
		
		faces[0] = new SimpleFace(a, b, c, m, t);
		faces[1] = new SimpleFace(c, d, a, m, t);
		
		faces[2] = new SimpleFace(b, f, g, m2, tf1);
		faces[3] = new SimpleFace(g, c, b, m2, tf2);
		
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
