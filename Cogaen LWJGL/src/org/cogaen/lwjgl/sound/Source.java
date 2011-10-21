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
	
	public void stop() {
		AL10.alSourceStop(this.id);
	}
	
	public void setPitch(double pitch) {
		AL10.alSourcef(this.id, AL10.AL_PITCH, (float) pitch);
	}
	
	public void setGain(double gain) {
		AL10.alSourcef(this.id, AL10.AL_GAIN, (float) gain);		
	}
	
	public void setPosition(double x, double y, double z) {
		pos.rewind();
		pos.put((float) x);
		pos.put((float) y);
		pos.put((float) z);
		pos.rewind();
		AL10.alSource(this.id, AL10.AL_POSITION, this.pos);
	}
	
	public void setLooping(boolean value) {
		AL10.alSourcei(this.id, AL10.AL_LOOPING, value ? AL10.AL_TRUE : AL10.AL_FALSE);
	}
}
