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

public class CircleVisual extends Visual {

	private static final double DEG2RAD = Math.PI/180;
	private double radius;
	private int glMode;
	
	public CircleVisual(double radius) {
		this.radius = radius;
		this.glMode = GL11.GL_POLYGON;
	}
		
	@Override
	public void render() {
		//TODO use display list
		getColor().apply();
		
		GL11.glBegin(this.glMode);
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

	public boolean isFilled() {
		return this.glMode == GL11.GL_POLYGON;
	}
	
	public void setFilled(boolean filled) {
		this.glMode = filled ? GL11.GL_POLYGON : GL11.GL_LINE_LOOP;
	}
	
}
