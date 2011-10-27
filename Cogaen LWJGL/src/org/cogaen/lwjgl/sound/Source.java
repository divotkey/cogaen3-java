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
