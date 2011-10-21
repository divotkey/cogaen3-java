package org.cogaen.lwjgl.sound;

public class Sound {

	private int bufferName;
	
	public Sound(int bufferName) {
		this.bufferName = bufferName;
	}
	
	public int getBufferName() {
		return this.bufferName;
	}
}
