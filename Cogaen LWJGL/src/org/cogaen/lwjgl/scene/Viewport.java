package org.cogaen.lwjgl.scene;

public class Viewport {
	
	private int x;
	private int y;
	private int height;
	private int width;
	private double halfWidth;
	private double halfHeight;
	
	public Viewport(int x, int y, int width, int height) {
		setUpperLeftPoint(x, y);
		setDimensions(width, height);
	}
	
	public void setDimensions(int width, int height) {
		if (width <= 0 || height <= 0) {
			throw new IllegalArgumentException("viewport dimensions must be greater zero");			
		}
		this.width = width;
		this.height = height;
		this.halfWidth = this.width / 2.0;
		this.halfHeight = this.height / 2.0;
	}

	public void setUpperLeftPoint(int x, int y) {
		if (x < 0 || y < 0) {
			throw new IllegalArgumentException("coordinates of must not be below zero");
		}
		
		this.x = x;
		this.y = y;
	}
		
	public double getHalfWidth() {
		return this.halfWidth;
	}
	
	public double getHalfHeight() {
		return this.halfHeight;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getX() {
		return this.x;		
	}
	
	public int getY() {
		return this.y;
	}
}

