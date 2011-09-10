package org.cogaen.lwjgl.scene;

import org.lwjgl.opengl.GL11;

public class RectangleVisual extends Visual {

	private double halfWidth;
	private double halfHeight;
	private float red = 0.0f;
	private float green = 1.0f;
	private float blue = 1.0f;
	private float alpha = 0.0f;
	
	public RectangleVisual(double width, double height) {
		this.halfWidth = width * 0.5;
		this.halfHeight = height * 0.5;
	}
	
	public void setColor(double red, double green, double blue, double alpha) {
		this.red = (float) red;
		this.green = (float) green;
		this.blue = (float) blue;
		this.alpha = (float) alpha;
	}
	
	@Override
	public void render() {
		GL11.glColor4f(this.red, this.green, this.blue, this.alpha);
//	    GL11.glColor3f(0.5f,0.5f,1.0f);
	    GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(-this.halfWidth, -this.halfHeight);
        GL11.glVertex2d(this.halfWidth, -this.halfHeight);
        GL11.glVertex2d(this.halfWidth,this.halfHeight);
		GL11.glVertex2d(-this.halfWidth, this.halfHeight);
	    GL11.glEnd();
	}

}
