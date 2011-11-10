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

import java.io.InputStream;
import java.nio.IntBuffer;

import org.cogaen.core.Core;
import org.cogaen.resource.ResourceException;
import org.cogaen.resource.ResourceHandle;
import org.cogaen.resource.ResourceService;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.newdawn.slick.openal.WaveData;

public class SoundHandle extends ResourceHandle {

	private Sound sound;
	private String filename;
	
	public SoundHandle(String filename) {
		this.filename = filename;
	}
	
	@Override
	public boolean isLoaded() {
		return this.sound != null;
	}

	@Override
	public void load(Core core) throws ResourceException {
		IntBuffer buffer = BufferUtils.createIntBuffer(1);
		AL10.alGenBuffers(buffer);
		int error = AL10.alGetError();
		if (error != AL10.AL_NO_ERROR) {
			throw new ResourceException("unable to create buffer, error: " + error);
		}		
		
		InputStream is = ResourceService.getInstance(core).getStream(this.filename);
		if (is == null) {
			throw new ResourceException("resource not found " + this.filename);
		}
		
		WaveData waveFile = WaveData.create(is);
		AL10.alBufferData(buffer.get(0), waveFile.format, waveFile.data, waveFile.samplerate);
		error = AL10.alGetError();
		if (error != AL10.AL_NO_ERROR) {
			throw new ResourceException("unable to load sound buffer, error: " + error);
		}		
		waveFile.dispose();
		
		this.sound = new Sound(buffer.get(0));
	}

	@Override
	public void unload(Core core) throws ResourceException {
		if (!AL.isCreated()) {
			return;
		}
		
		IntBuffer buffer = (IntBuffer) BufferUtils.createIntBuffer(1).put(this.sound.getBufferName()).rewind();
		AL10.alDeleteBuffers(buffer);
		int error = AL10.alGetError();
		if (error != AL10.AL_NO_ERROR) {
			throw new ResourceException("unable to unload sound buffer, error: " + error);
		}		
		this.sound = null;
	}

	@Override
	public Object getResource() {
		return this.sound;
	}
}
