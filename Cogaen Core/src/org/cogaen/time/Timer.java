/* 
 -----------------------------------------------------------------------------
                    Cogaen - Component-based Game Engine V3
 -----------------------------------------------------------------------------
 This software is developed by the Cogaen Development Team. Please have a 
 look at our project home page for further details: http://www.cogaen.org
    
 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 Copyright (c) Roman Divotkey, 2010-2011

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

package org.cogaen.time;

public class Timer {

	private double time;
	private double deltaTime;
	private double scale;
	private boolean paused = false;
	
	Timer() {
		this.scale = 1.0;
		reset();
	}

	public void reset(double time) {
		this.time = time;
		this.deltaTime = 0.0;
	}
	
	public void reset() {
		reset(0.0);
	}
	
	public double getTime() {
		return this.time;
	}
	
	public double getDeltaTime() {
		return this.deltaTime;
	}
	
	void update(double dt) {
		if (this.paused) {
			this.deltaTime = 0.0;
		} else {
			this.deltaTime = dt * this.scale;
			this.time += this.deltaTime;		
		}
	}
	
	public void setScale(double scale) {
		this.scale = scale;
	}
	
	public double getScale() {
		return this.scale;
	}
	
	public boolean isPaused() {
		return this.paused;
	}
	
	public void setPaused(boolean paused) {
		this.paused = paused;
	}
}
