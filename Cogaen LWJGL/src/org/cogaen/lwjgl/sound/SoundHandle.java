package org.cogaen.lwjgl.sound;

import java.io.InputStream;
import java.nio.IntBuffer;

import org.cogaen.core.Core;
import org.cogaen.resource.ResourceException;
import org.cogaen.resource.ResourceHandle;
import org.cogaen.resource.ResourceService;
import org.lwjgl.BufferUtils;
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
		
		InputStream is = ResourceService.getInstance(core).getStream(this.filename);
		if (is == null) {
			throw new ResourceException("resource not found " + this.filename);
		}
		
		WaveData waveFile = WaveData.create(is);
		AL10.alBufferData(buffer.get(0), waveFile.format, waveFile.data, waveFile.samplerate);
		waveFile.dispose();
		
		this.sound = new Sound(buffer.get(0));
	}

	@Override
	public void unload(Core core) {
		IntBuffer buffer = (IntBuffer) BufferUtils.createIntBuffer(1).put(this.sound.getBufferName()).rewind();
		AL10.alDeleteBuffers(buffer);
		this.sound = null;
	}

	@Override
	public Object getResource() {
		return this.sound;
	}
}
