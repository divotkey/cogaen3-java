package org.cogaen.lwjgl.input;

import org.cogaen.event.Event;
import org.cogaen.name.CogaenId;

public class KeyPressedEvent extends Event {

	public static final CogaenId TYPE_ID = new CogaenId("KeyPressed");
	
	private int keyCode;
	
	public KeyPressedEvent(int keyCode) {
		this.keyCode = keyCode;
	}
	
	@Override
	public CogaenId getTypeId() {
		return TYPE_ID;
	}

	public int getKeyCode() {
		return this.keyCode;
	}
}
