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

public abstract class Visual {

	private Color color;
	private double scale = 1.0;
	private int mask = 0xFFFF;

	public Visual() {
		this(Color.CYAN);
	}
	
	public Visual(ReadableColor color) {
		this.color = new Color(color);
	}
		
	public final void setColor(ReadableColor color) {
		this.color.setColor(color);
	}
	
	public final Color getColor() {
		return this.color;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}
	
	public double getScale() {
		return this.scale;
	}
	
	public void setMask(int mask) {
		this.mask = mask;
	}
	
	public int getMask() {
		return this.mask;
	}
	
	protected void copyFields(Visual newInstance) {
		newInstance.color.setColor(this.color);
		newInstance.scale = this.scale;
		newInstance.mask = this.mask;
	}
	
	public abstract void prolog();
	public abstract void render();
	public abstract void epilog();
	public abstract Visual newInstance();
}
