        package org.cogaen.lwjgl.scene;

import org.lwjgl.opengl.GL11;

public class RectangleVisual extends Visual {

	private double halfWidth;
	private double halfHeight;
	private int glMode;
	
	public RectangleVisual(double width, double height) {
		this.halfWidth = width * 0.5;
		this.halfHeight = height * 0.5;
		this.glMode = GL11.GL_QUADS;
	}
	
	private RectangleVisual() {
		// intentionally left empty
	}
	
	@Override
	public void render() {
		getColor().apply();
		
	    GL11.glBegin(this.glMode);
        GL11.glVertex2d(-this.halfWidth * getScale(), -this.halfHeight * getScale());
        GL11.glVertex2d(this.halfWidth * getScale(), -this.halfHeight * getScale());
        GL11.glVertex2d(this.halfWidth * getScale(), this.halfHeight * getScale());
		GL11.glVertex2d(-this.halfWidth * getScale(), this.halfHeight * getScale());
	    GL11.glEnd();
	}

	@Override
	public void prolog() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
    	GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
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
		instance.glMode = this.glMode;
		
		return instance;
	}

	public void setFilled(boolean filled) {
		this.glMode = filled ? GL11.GL_QUADS : GL11.GL_LINE_LOOP;
	}
	
	public boolean isFilled() {
		return this.glMode == GL11.GL_QUADS;
	}

	public void setWidth(double width) {
		this.halfWidth = width / 2;
	}
	
	public void setHeight(double height) {
		this.halfHeight = height / 2;
	}
}
