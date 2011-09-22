package org.cogaen.lwjgl.scene;

import org.lwjgl.opengl.GL11;

public class RectangleVisual extends Visual {

	private double halfWidth;
	private double halfHeight;
	
	public RectangleVisual(double width, double height) {
		this.halfWidth = width * 0.5;
		this.halfHeight = height * 0.5;
	}
	
	private RectangleVisual() {
		// intentionally left empty
	}
	
	@Override
	public void render() {
		getColor().apply();
		
	    GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(-this.halfWidth * getScale(), -this.halfHeight * getScale());
        GL11.glVertex2d(this.halfWidth * getScale(), -this.halfHeight * getScale());
        GL11.glVertex2d(this.halfWidth * getScale(), this.halfHeight * getScale());
		GL11.glVertex2d(-this.halfWidth * getScale(), this.halfHeight * getScale());
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
	public RectangleVisual newInstance() {
		RectangleVisual instance = new RectangleVisual();
		super.copyFields(instance);
		instance.halfWidth = this.halfWidth;
		instance.halfHeight = this.halfHeight;
		
		return instance;
	}

}
