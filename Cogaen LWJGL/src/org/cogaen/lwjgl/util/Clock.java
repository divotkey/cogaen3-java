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

package org.cogaen.lwjgl.util;

import java.security.InvalidParameterException;

import org.lwjgl.Sys;

/**
 * Used to measure elapsed time using Java's high precision timer.
 */
public class Clock {

	/** Duration of one nanosecond in seconds. */
	private static final double ONE_NANOSECOND = 0.000000001;

	/** Default value used to limit delta time. */
	private static long DEFAULT_MAX_DELTA_TIME = 250000000;
	
	/** Current delta time in nanoseconds. */
	private long dt;
	
	/** Last time stamp in nanoseconds. */
	private long last;

	/** Maximum delta time in nanoseconds. */
	private long maxDeltaTime = DEFAULT_MAX_DELTA_TIME;

	private long timerResolution;
	private double oneSecond;
	
	/**
	 * Creates a new instance of {@code Clock}.
	 * During initialization {@code reset} is called.
	 */
	public Clock() {
		this.timerResolution = Sys.getTimerResolution();
		this.oneSecond = 1.0 / this.timerResolution;
		reset();
	}
	
	/**
	 * Resets all values to zero.
	 */
	public final void reset() {
		this.dt = 0;
		this.last = Sys.getTime();
	}
	
	/**
	 * Calculates new values for delta time. Elapsed time is measured using
	 * Java's high precision timer.
	 */
	public void tick() {
		long now = Sys.getTime();
		this.dt = now - last;
		this.last = now;
		
		if (this.dt > this.maxDeltaTime) {
			this.dt = maxDeltaTime;
		}
	}
	
	/**
	 * Returns current delta time in seconds.
	 * @return delta time in seconds
	 */
	public double getDelta() {
		return this.dt * this.oneSecond;
	}
	
	/**
	 * Returns current delta time in nanoseconds.
	 * @return delta time in nanoseconds
	 */
	public long getNanoDelta() {
		return this.dt;
	}

	/**
	 * Returns maximum delta time used.
	 * @return the maxDeltaTime
	 */
	public long getMaxDeltaTime() {
		return maxDeltaTime;
	}

	/**
	 * Sets maximum delta time used.
	 * @param maxDeltaTime the maxDeltaTime to set
	 */
	public void setMaxDeltaTime(long maxDeltaTime) {
		if (maxDeltaTime <= 0) {
			throw new InvalidParameterException("maxDeltaTime must be greater than zero");
		}
		this.maxDeltaTime = maxDeltaTime;
	}
	
}
