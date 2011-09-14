package org.cogaen.lwjgl.scene;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

public class SpriteVisual extends Visual {

	private Texture texture;
	private double halfWidth;
	private double halfHeight;
	private Color color = Color.WHITE;
	
	public SpriteVisual(Texture texture, double width, double height) {
		this.texture = texture;
		this.halfWidth = width / 2;
		this.halfHeight = height / 2;
	}
	
	@Override
	public void render() {
    	this.color.apply();
    	
		GL11.glEnable(GL11.GL_TEXTURE_2D);
    	GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    	texture.bind();	// or GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
    	
	    GL11.glBegin(GL11.GL_QUADS);
	    GL11.glTexCoord2f(0.0f, this.texture.getHeight());
	    GL11.glVertex2d(-this.halfWidth, -this.halfHeight);
	    
	    GL11.glTexCoord2f(this.texture.getWidth(), this.texture.getHeight());
        GL11.glVertex2d(this.halfWidth, -this.halfHeight);
        
	    GL11.glTexCoord2f(this.texture.getWidth(), 0);
        GL11.glVertex2d(this.halfWidth, this.halfHeight);
        
	    GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex2d(-this.halfWidth, this.halfHeight);
	    GL11.glEnd();
	}

}
