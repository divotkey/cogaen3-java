package org.cogaen.lwjgl.scene;

import org.lwjgl.opengl.GL11;

public class CircleVisual extends Visual {

	private static final double DEG2RAD = Math.PI/180;
	private double radius;
	
	public CircleVisual(double radius) {
		this.radius = radius;
	}
		
	@Override
	public void render() {
		//TODO use display list
		getColor().apply();
		
		GL11.glBegin(GL11.GL_LINE_LOOP);
		double r = this.radius * getScale();
		for (int i = 0; i < 360; i += 10) {
			double degInRad = i * DEG2RAD;
			GL11.glVertex2d(Math.cos(degInRad) * r, Math.sin(degInRad) * r);
		}
		GL11.glEnd();
	}

	@Override
	public void prolog() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
    	GL11.glDisable(GL11.GL_BLEND);
	}

	@Override
	public void epilog() {
		// intentionally left empty
	}

	@Override
	public CircleVisual newInstance() {
		CircleVisual instance = new CircleVisual(this.radius);
		super.copyFields(instance);
		
		return instance;
	}

}
