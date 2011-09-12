package org.cogaen.lwjgl.scene;

import org.lwjgl.opengl.GL11;

public class CircleVisual extends ColorVisual {

	private static final double DEG2RAD = Math.PI/180;
	private double radius;
	
	public CircleVisual(double radius) {
		this.radius = radius;
	}
	
	@Override
	public void render() {
		getColor().apply();
		
		GL11.glBegin(GL11.GL_LINE_LOOP);
		for (int i = 0; i < 360; i += 20) {
			double degInRad = i * DEG2RAD;
			GL11.glVertex2d(Math.cos(degInRad) * this.radius, Math.sin(degInRad) * radius);
		}
		GL11.glEnd();
	}

}
