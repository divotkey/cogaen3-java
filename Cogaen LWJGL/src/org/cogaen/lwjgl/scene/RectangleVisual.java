package org.cogaen.lwjgl.scene;

import org.lwjgl.opengl.GL11;

public class RectangleVisual extends Visual {

	private double halfWidth;
	private double halfHeight;
	private Color color = Color.CYAN;
	
	public RectangleVisual(double width, double height) {
		this.halfWidth = width * 0.5;
		this.halfHeight = height * 0.5;
	}
		
	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	public void render() {
		this.color.apply();
	    GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(-this.halfWidth, -this.halfHeight);
        GL11.glVertex2d(this.halfWidth, -this.halfHeight);
        GL11.glVertex2d(this.halfWidth,this.halfHeight);
		GL11.glVertex2d(-this.halfWidth, this.halfHeight);
	    GL11.glEnd();
	}

}
