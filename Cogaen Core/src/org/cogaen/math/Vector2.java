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

public class Vector2 {

	public double x;
	public double y;
	
	public Vector2() {
		this.x = 0.0;
		this.y = 0.0;
	}
	
	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2(Vector2 v1) {
		this.x = v1.x;
		this.y = v1.y;
	}

	public void normalize(Vector2 v1) {
		double length = v1.length();
		this.x = v1.x / length;
		this.y = v1.y / length;
	}
	
	public void normalize() {
		double length = length();
		if (length != 0.0) {
			this.x /= length;
			this.y /= length;
		}
	}
	
	public double length() {
		return Math.sqrt(lengthSquared());
	}
	
	public double lengthSquared() {
		return this.x * this.x + this.y * this.y;
	}
		
	public void add(Vector2 v1) {
		this.x += v1.x;
		this.y += v1.y;
	}
	
	public void add(Vector2 v1, Vector2 v2) {
		this.x = v1.x + v2.x;
		this.y = v1.y + v2.y;
	}
	
	public void sub(Vector2 v1) {
		this.x -= v1.x;
		this.y -= v1.y;
	}
	
	public void sub(Vector2 v1, Vector2 v2) {
		this.x = v1.x - v2.x;
		this.y = v1.y - v2.y;
	}
	
	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void set(Vector2 v1) {
		this.x = v1.x;
		this.y = v1.y;
	}
	
	public void scale(double s) {
		this.x *= s;
		this.y *= s;
	}
	
	public void scale(double s, Vector2 v1) {
		this.x = v1.x * s;
		this.y = v1.y * s;
	}
	
	/**
	 * Sets the value of this vector to the scalar multiplication of itself and
	 * then adds vector v1 (this = s*this + v1). 
	 * 
	 * @param s the scalar value
	 * @param v1 the vector to be added
	 */
	public void scaleAdd(double s, Vector2 v1) {
		this.x = this.x * s + v1.x;
		this.y = this.y * s + v1.y;
	}
	
	/**
	 * Sets the value of this vector to the scalar multiplication of vector v1
	 * and then adds vector v2 (this = s*v1 + v2). 
	 * 
	 * @param s the scalar value
	 * @param v1 the vector to be multiplied
	 * @param v2 the vector to be added
	 */
	public void scaleAdd(double s, Vector2 v1, Vector2 v2) {
		this.x = v1.x * s + v2.x;
		this.y = v1.y * s + v2.y;
	}
	
	public double dot(Vector2 v1) {
		return this.x * v1.x + this.y * v1.y;
	}
	
	public void negate() {
		this.x = -this.x;
		this.y = -this.y;
	}
	
	public void negate(Vector2 v1) {
		this.x = -v1.x;
		this.y = -v1.y;
	}
	
	public void rotate(double angle) {
		double cosa = Math.cos(angle);
		double sina = Math.sin(angle);
		
		double xt = this.x * cosa - this.y * sina;
		this.y = this.y * cosa + this.x * sina;
		this.x = xt;
	}
	
}
