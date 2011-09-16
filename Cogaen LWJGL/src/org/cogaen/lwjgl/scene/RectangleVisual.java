package org.cogaen.lwjgl.scene;

import org.lwjgl.opengl.GL11;

public class RectangleVisual extends ColorVisual {

	private double halfWidth;
	private double halfHeight;
	
	public RectangleVisual(double width, double height) {
		this.halfWidth = width * 0.5;
		this.halfHeight = height * 0.5;
	}
	
	@Override
	public void render() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
    	GL11.glDisable(GL11.GL_BLEND);
		getColor().apply();
		
	    GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(-this.halfWidth, -this.halfHeight);
        GL11.glVertex2d(this.halfWidth, -this.halfHeight);
        GL11.glVertex2d(this.halfWidth,this.halfHeight);
		GL11.glVertex2d(-this.halfWidth, this.halfHeight);
	    GL11.glEnd();
	}

}
