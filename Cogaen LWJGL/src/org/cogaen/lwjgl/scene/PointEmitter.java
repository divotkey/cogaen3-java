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

import java.util.Random;

public class PointEmitter extends Emitter {

	private Random random = new Random();
	private double radius = Math.PI * 2;
	private double minLinearSpeed = 1.0;
	private double maxLinearSpeed = 10.0;
	private double minAngularSpeed = 1.0;
	private double maxAngularSpeed = 10.0;
	private double minTimeToLive = 0.5;
	private double maxTimeToLive = 1.0;
	private double acceleration[] = new double[3];
	private double drag = 0.0;
	
	@Override
	public void emit(Particle particle) {
		double phi = this.angle -this.radius / 2 + this.radius * this.random.nextDouble();
		double speed = this.minLinearSpeed + this.maxLinearSpeed * this.random.nextDouble();
		double vx = -speed * Math.sin(phi);
		double vy = speed * Math.cos(phi);
		
		particle.setPosition(this.posX, this.posY, 0);
		particle.setVelocity(vx, vy, this.minAngularSpeed + this.maxAngularSpeed * this.random.nextDouble());
		particle.setAcceleration(this.acceleration[0], this.acceleration[1], this.acceleration[2]);
		particle.setTimeToLive(this.minTimeToLive + this.maxTimeToLive * this.random.nextDouble());
		particle.setDrag(this.drag);
		particle.activate();
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getMinLinearSpeed() {
		return minLinearSpeed;
	}

	public void setMinLinearSpeed(double minLinearSpeed) {
		this.minLinearSpeed = minLinearSpeed;
	}

	public double getMaxLinearSpeed() {
		return maxLinearSpeed;
	}

	public void setMaxLinearSpeed(double maxLinearSpeed) {
		this.maxLinearSpeed = maxLinearSpeed;
	}
	
	@Override
	protected PointEmitter newInstance() {
		PointEmitter newInstance = new PointEmitter();
		super.copyFields(newInstance);
		newInstance.radius = this.radius;
		newInstance.maxLinearSpeed = this.maxLinearSpeed;
		newInstance.minLinearSpeed = this.minLinearSpeed;
		newInstance.minAngularSpeed = this.minAngularSpeed;
		newInstance.maxAngularSpeed = this.maxAngularSpeed;
		newInstance.minTimeToLive = this.minTimeToLive;
		newInstance.maxTimeToLive = this.maxTimeToLive;
		newInstance.drag = this.drag;
		for (int i = 0; i < 3; ++i) {
			newInstance.acceleration[i] = this.acceleration[i];
		}
		
		return newInstance;
	}

	public double getMinTimeToLive() {
		return minTimeToLive;
	}

	public void setMinTimeToLive(double minTimeToLive) {
		this.minTimeToLive = minTimeToLive;
	}

	public double getMaxTimeToLive() {
		return maxTimeToLive;
	}

	public void setMaxTimeToLive(double maxTimeToLive) {
		this.maxTimeToLive = maxTimeToLive;
	}

	public void setAcceleration(int ax, int ay, int alpha) {
		this.acceleration[0] = ax;
		this.acceleration[1] = ay;
		this.acceleration[2] = alpha;
	}

	public double getDrag() {
		return drag;
	}

	public void setDrag(double drag) {
		this.drag = drag;
	}
	
}
