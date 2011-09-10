package org.cogaen.lwjgl.scene;

import org.lwjgl.opengl.GL11;

public class TestVisual extends Visual {

	@Override
	public void render() {
	    // set the color of the quad (R,G,B,A)
	    GL11.glColor3f(0.5f,0.5f,1.0f);
	    	
	    // draw quad
	    GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(-0.5f, -0.5f);
        GL11.glVertex2f(0.5f, -0.5f);
        GL11.glVertex2f(0.5f, 0.5f);
		GL11.glVertex2f(-0.5f, 0.5f);
	    GL11.glEnd();
		
	}

}
