package org.cogaen.lwjgl.scene;

import org.lwjgl.opengl.GL11;

public class Color implements ReadableColor {

	public static final ReadableColor WHITE = new Color(1, 1, 1, 1);
	public static final ReadableColor BLACK = new Color(0, 0, 0, 1);
	public static final ReadableColor CYAN = new Color(0, 1, 1, 1);
	public static final ReadableColor RED = new Color(1, 0, 0, 1);
	public static final ReadableColor BLUE = new Color(0, 0, 1, 1);
	public static final ReadableColor GREEN = new Color(0, 1, 0, 1);
	public static final ReadableColor ORANGE = new Color(1, 0.64, 0, 1);
	public static final ReadableColor YELLOW = new Color(1, 1, 0, 1);
	public static final ReadableColor GRAY = new Color(0.5, 0.5, 0.5);
	public static final ReadableColor LIGHT_GRAY = new Color(0.75, 0.75, 0.75);
	public static final ReadableColor DARK_GRAY = new Color(0.25, 0.25, 0.25);
	
	private double red;
	private double green;
	private double blue;
	private double alpha;
	
	public Color(ReadableColor color) {
		this(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}
	
	public Color(double red, double green, double blue) {
		this(red, green, blue, 1.0);
	}
	
	public Color(double red, double green, double blue, double alpha) {
		this.red = red; 
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}
	
	public double getRed() {
		return red;
	}

	public void setRed(double red) {
		this.red = red;
	}

	public double getGreen() {
		return green;
	}

	public void setGreen(double green) {
		this.green = green;
	}

	public double getBlue() {
		return blue;
	}

	public void setBlue(double blue) {
		this.blue = blue;
	}

	public double getAlpha() {
		return alpha;
	}


	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public void setColor(ReadableColor color) {
		this.red = color.getRed();
		this.green = color.getGreen();
		this.blue = color.getBlue();
		this.alpha = color.getAlpha();
	}

	public void apply() {
		GL11.glColor4d(this.red, this.green, this.blue, this.alpha);
	}
	
}
