package org.cogaen.lwjgl.scene;

import org.lwjgl.opengl.GL11;

public class Color {

	public static final Color CYAN = new Color(0, 1, 1);
	public static final Color RED = new Color(1, 0, 0);
	public static final Color GREEN = new Color(0, 1, 0);
	public static final Color BLUE = new Color(0, 0, 1);
	public static final Color YELLOW = new Color(1, 1, 0);
	
	private double red;
	private double green;
	private double blue;
	private double alpha;
	
	public Color(double red, double green, double blue) {
		this(red, green, blue, 0.0);
	}

	public Color(double red, double green, double blue, double alpha) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}
	
	void apply() {
		GL11.glColor4d(this.red, this.green, this.blue, this.alpha);		
	}
	
}
