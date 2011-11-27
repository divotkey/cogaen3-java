/* 
-----------------------------------------------------------------------------
                   Cogaen - Component-based Game Engine V3
-----------------------------------------------------------------------------
This software is developed by the Cogaen Development Team. Please have a 
look at our project home page for further details: http://www.cogaen.org
   
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
Copyright (c) 2010-2011 Roman Divotkey

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
*/

package org.cogaen.lwjgl.scene;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

public class SpriteVisual extends Visual {

	private Texture texture;
	private double halfWidth;
	private double halfHeight;
	private boolean flipVertical;
	private int blendMode = GL11.GL_ONE_MINUS_SRC_ALPHA;
	
	SpriteVisual(Texture texture, double width, double height) {
		super(Color.WHITE);
		this.texture = texture;
		this.halfWidth = width / 2;
		this.halfHeight = height / 2;
	}
	
	private SpriteVisual() {
		// intentionally left empty
	}
	
	public void setSize(double width, double height) {
		this.halfWidth = width / 2;
		this.halfHeight = height / 2;
	}
	
	public boolean isFlipVertical() {
		return flipVertical;
	}

	public void setFlipVertical(boolean upsideDown) {
		this.flipVertical = upsideDown;
	}
	
	@Override
	public void render() {
		getColor().apply();
		
		if (this.flipVertical) {
			GL11.glScaled(1, -1, 1);
		}
    	
	    GL11.glBegin(GL11.GL_QUADS);
	    GL11.glTexCoord2f(0.0f, this.texture.getHeight());
	    GL11.glVertex2d(-this.halfWidth * getScale(), -this.halfHeight * getScale());
	    
	    GL11.glTexCoord2f(this.texture.getWidth(), this.texture.getHeight());
        GL11.glVertex2d(this.halfWidth * getScale(), -this.halfHeight * getScale());
        
	    GL11.glTexCoord2f(this.texture.getWidth(), 0);
        GL11.glVertex2d(this.halfWidth * getScale(), this.halfHeight * getScale());
        
	    GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex2d(-this.halfWidth * getScale(), this.halfHeight * getScale());
	    GL11.glEnd();
	}

	@Override
	public void prolog() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
    	GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, this.blendMode);
    	texture.bind();	// or GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
	}

	@Override
	public void epilog() {
		// intentionally left empty
	}

	@Override
	public SpriteVisual newInstance() {
		SpriteVisual instance = new SpriteVisual();
		super.copyFields(instance);
		instance.halfWidth = this.halfWidth;
		instance.halfHeight = this.halfHeight;
		instance.texture = this.texture;
		instance.blendMode = this.blendMode;
		
		return instance;
	}

	public void setAdditive(boolean value) {
		this.blendMode = value ? GL11.GL_ONE : GL11.GL_ONE_MINUS_SRC_ALPHA;
	}
	
	public boolean isAdditive() {
		return this.blendMode == GL11.GL_ONE;
	}
	
	public double getWidth() {
		return this.halfWidth * 2;
	}
	
	public double getHeight() {
		return this.halfHeight * 2;
	}
}
