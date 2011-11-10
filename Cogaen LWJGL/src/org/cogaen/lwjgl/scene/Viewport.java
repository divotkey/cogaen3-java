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

public class Viewport {
	
	private int x;
	private int y;
	private int height;
	private int width;
	private double halfWidth;
	private double halfHeight;
	
	public Viewport(int x, int y, int width, int height) {
		setUpperLeftPoint(x, y);
		setDimensions(width, height);
	}
	
	public void setDimensions(int width, int height) {
		if (width <= 0 || height <= 0) {
			throw new IllegalArgumentException("viewport dimensions must be greater zero");			
		}
		this.width = width;
		this.height = height;
		this.halfWidth = this.width / 2.0;
		this.halfHeight = this.height / 2.0;
	}

	public void setUpperLeftPoint(int x, int y) {
		if (x < 0 || y < 0) {
			throw new IllegalArgumentException("coordinates of must not be below zero");
		}
		
		this.x = x;
		this.y = y;
	}
		
	public double getHalfWidth() {
		return this.halfWidth;
	}
	
	public double getHalfHeight() {
		return this.halfHeight;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getX() {
		return this.x;		
	}
	
	public int getY() {
		return this.y;
	}
}

