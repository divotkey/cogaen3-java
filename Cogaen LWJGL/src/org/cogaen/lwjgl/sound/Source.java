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

package org.cogaen.lwjgl.sound;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;


public class Source {
	private FloatBuffer pos = BufferUtils.createFloatBuffer(3);
	private FloatBuffer vel = BufferUtils.createFloatBuffer(3);
	
	private int id;
	private SoundService soundService;
	
	public Source(SoundService soundService, int id) {
		this.soundService = soundService;
		this.id = id;
		reset();
	}
	
	public int getId() {
		return this.id;
	}
		
	public void assignSound(Sound sound) {
		this.soundService.assignSound(this.id, sound.getBufferName());
	}
	
	public void playSound() {
		this.soundService.play(this.id);
	}
	
	public void stopSound() {
		this.soundService.stop(this.id);
	}
	
	public void setPitch(double pitch) {
		this.soundService.setPitch(this.id, pitch);
	}
	
	public void setGain(double gain) {
		this.soundService.setGain(this.id, gain);
	}
	
	public void setPosition(double x, double y, double z) {
		this.pos.rewind();
		this.pos.put((float) x);
		this.pos.put((float) y);
		this.pos.put((float) z);
		this.pos.rewind();
		this.soundService.setPosition(this.id, this.pos);
	}
	
	public void setVelocity(double x, double y, double z) {
		this.vel.rewind();
		this.vel.put((float) x);
		this.vel.put((float) y);
		this.vel.put((float) z);
		this.vel.rewind();
		this.soundService.setVelocity(this.id, vel);
	}
	
	public void setLooping(boolean looping) {
		this.soundService.setLooping(this.id, looping);
	}

	public void reset() {
		setPitch(1.0);
		setGain(1.0);
		setVelocity(0, 0, 0);
		setPosition(0, 0, 0);
	}
}
