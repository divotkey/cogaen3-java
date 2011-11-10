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

public class Particle {
	
	private double position[] = new double[3];
	private double velocity[] = new double[3];
	private double acceleration[] = new double[3];
	private double externalAcceleration[] = new double[3];
	private double timeToLive;
	private double lifeTime = 0;
	private double drag = 0;

	public void update(double dt) {
		for (int i = 0; i < 3; ++i) {
			this.acceleration[i] = -this.velocity[i] * this.drag + this.externalAcceleration[i];
			this.velocity[i] += this.acceleration[i] * dt;
			this.position[i] += this.velocity[i] * dt;
		}
		
		this.lifeTime -= dt;
	}

	public void activate() {
		this.lifeTime = this.timeToLive;
	}
	
	public boolean isDead() {
		return this.lifeTime <= 0;
	}
	
	public double getTimeToLive() {
		return this.timeToLive;
	}
	
	public void setTimeToLive(double ttl) {
		this.timeToLive = ttl;
	}
	
	public void setPosition(double x, double y, double phi) {
		this.position[0] = x;
		this.position[1] = y;
		this.position[2] = phi;
	}
	
	public void setVelocity(double vx, double vy, double omega) {
		this.velocity[0] = vx;
		this.velocity[1] = vy;
		this.velocity[2] = omega;
	}
	
	public void setAcceleration(double ax, double ay, double alpha) {
		this.externalAcceleration[0] = ax;
		this.externalAcceleration[1] = ay;
		this.externalAcceleration[2] = alpha;
	}

	public double getPosX() {
		return this.position[0];
	}

	public double getPosY() {
		return this.position[1];
	}

	public double getAngle() {
		return this.position[2];
	}
	
	public double getLifeTime() {
		return this.lifeTime;
	}

	public double getDrag() {
		return drag;
	}

	public void setDrag(double drag) {
		this.drag = drag;
	}
}
