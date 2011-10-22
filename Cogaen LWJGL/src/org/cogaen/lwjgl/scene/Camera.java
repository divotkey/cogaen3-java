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

public class Camera {

	private static final double DEG2RAD = Math.PI / 180.0;
	private static final double RAD2DEG = 180.0 / Math.PI;
	
	private double posX;
	private double posY;
	private double angle;
	private double zoom = 1.0;
	private int width;
	private int height;
	private int mask = 0xFFFF;
	private Viewport viewport;
	private boolean active = true;
	
	Camera(int width, int height) {
		this.width = width;
		this.height = height;
		this.viewport = new Viewport(0, 0, width, height);
	}
	
	public void setViewPort(int x, int y, int width, int height) {
		this.viewport.setUpperLeftPoint(x, y);
		this.viewport.setDimensions(width, height);
	}
	
	public int getMask() {
		return mask;
	}

	public void setMask(int mask) {
		this.mask = mask;
	}

	public void setPosition(double x, double y) {
		this.posX = x;
		this.posY = y;
	}
	
	public void setAngle(double angle) {
		this.angle = angle * RAD2DEG;
	}
	
	public double getAngle() {
		return this.angle * DEG2RAD;
	}
	
	public double getZoom() {
		return zoom;
	}

	public void setZoom(double zoom) {
		if (zoom <= 0) {
			throw new IllegalArgumentException("zoom factor must be greater than zero");
		}
		this.zoom = zoom;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	void applyTransform() {	
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, this.width, 0, this.height, 1, -1);
		GL11.glTranslated(this.width / 2, this.height / 2, 0.0);
		
		GL11.glScaled(this.zoom, this.zoom, 0.0);
		GL11.glRotatef((float) -this.angle, 0, 0, 1);
		GL11.glTranslated(-this.posX, -this.posY, 0);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);		
		GL11.glViewport(this.viewport.getX(), this.viewport.getY(), this.viewport.getWidth(), this.viewport.getHeight());
	}
	
}
