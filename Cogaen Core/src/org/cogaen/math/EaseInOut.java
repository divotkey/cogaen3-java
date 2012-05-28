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

package org.cogaen.math;

/**
 * Provides critically damped ease-in/ease-out smoothing.
 * <p>For more detailed information see:
 * <ul>
 * <li><cite>Thomas Lowe, Critically Damped Ease-In/Ease-Out Smoothing,<br>
 * Game Programming Gems 4, Charles River Media, 2004</cite></li>
 * </ul>
 * </p>
 */
public class EaseInOut {

	private static final double DEFAULT_EASETIME = 1.0;
	private static final double DEFAULT_EPSILON = 0.001;
	
	private double easeTime;
	private double velocity;
	private double curValue;
	private double targetValue;
	private double epsilon = DEFAULT_EPSILON;
		
	public EaseInOut(double initialValue, double easeTime) {
		setEaseTime(easeTime);
		reset(initialValue);
	}
	
	public EaseInOut(double initialValue) {
		this(initialValue, DEFAULT_EASETIME);
	}
	
	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}
	
	public double getEpsilon() {
		return this.epsilon;
	}
	
	public EaseInOut() {
		this(0.0, DEFAULT_EASETIME);
	}
	
	public boolean isTargetValue() {
		return Math.abs(this.curValue - this.targetValue) < epsilon;
	}
	
	public void reset(double value) {
		this.curValue = value;
		this.targetValue = value;
		this.velocity = 0.0;
	}
	
	public double getCurrentValue() {
		return this.curValue;
	}
			
	public double getTargetValue() {
		return this.targetValue;
	}
	
	public void setTargetValue(double target) {
		this.targetValue = target;
	}
	
	public double getEaseTime() {
		return this.easeTime;
	}
	
	public void setEaseTime(double easeTime) {
		assert(easeTime >= 0.0);
		this.easeTime = easeTime;
	}
	
	public void update(double targetValue, double dt) {
		setTargetValue(targetValue);
		update(dt);
	}
	
	public void update(double dt) {
		double omega = 2.0 / this.easeTime;
		double x = omega * dt;
		double exp = 1.0 / (1.0 + x + 0.48 * x * x * 0.235 * x * x * x);	
		
		double change = this.curValue - this.targetValue;
		double temp = (this.velocity + omega * change) * dt;
		this.velocity = (this.velocity - omega * temp) * exp;			
		this.curValue = this.targetValue + (change + temp) * exp;		
	}
	
}
