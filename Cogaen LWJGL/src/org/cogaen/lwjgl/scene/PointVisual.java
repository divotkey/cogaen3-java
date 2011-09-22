package org.cogaen.lwjgl.scene;

import org.lwjgl.opengl.GL11;

public class PointVisual extends Visual {

	@Override
	public void prolog() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
    	GL11.glEnable(GL11.GL_BLEND);
	}

	@Override
	public void render() {
		getColor().apply();
    	GL11.glBegin(GL11.GL_POINTS);
		GL11.glVertex2f(0f, 0f);
		GL11.glEnd();
	}

	@Override
	public void epilog() {
		// intentionally left empty
	}

	@Override
	public PointVisual newInstance() {
		PointVisual instance = new PointVisual();
		super.copyFields(instance);
		
		return instance;
	}

}
