package de.softwarekollektiv.cg.uebungen.uebung7;

public class Uebung7_Camera extends Camera{

	public Uebung7_Camera() {
		this.setPosition(0, 0, 0);
		this.setDirection(0, 0, 1);
		this.setTop(0, 1, 0);
		this.n_close = 1;
		this.n_distant = 100;
		this.aspect_ratio = 1;
		this.fov = 60;
	}
	
}
