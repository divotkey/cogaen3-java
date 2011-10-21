package org.cogaen.lwjgl.sound;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

public class Source {
	private int id;
	private FloatBuffer pos = (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] {0.0f, 0.0f, 0.0f}).rewind();
	private FloatBuffer vel = (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] {0.0f, 0.0f, 0.0f}).rewind();
	
	public Source(int id) {
		this.id = id;
		AL10.alSourcef(this.id, AL10.AL_PITCH, 1.0f);
		AL10.alSourcef(this.id, AL10.AL_GAIN, 1.0f);
		AL10.alSource(this.id, AL10.AL_POSITION, this.pos);
		AL10.alSource(this.id, AL10.AL_VELOCITY, this.vel);
	}
	
	public int getId() {
		return this.id;
	}
		
	public void assignSound(Sound sound) {
		AL10.alSourcei(this.id, AL10.AL_BUFFER, sound.getBufferName() );
	}
	
	public void play() {
		AL10.alSourcePlay(this.id);
	}
}
