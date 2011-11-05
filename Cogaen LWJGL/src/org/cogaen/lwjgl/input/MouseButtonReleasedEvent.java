package org.cogaen.lwjgl.input;

import org.cogaen.event.Event;
import org.cogaen.name.CogaenId;

public class MouseButtonReleasedEvent extends Event {

	public static final CogaenId TYPE_ID = new CogaenId("MouseButtonReleased");

	private int button;
	private double posX;
	private double posY;
	
	public MouseButtonReleasedEvent(int x, int y, int button) {
		this.button = button;
		this.posX = x;
		this.posY = y;
	}

	@Override
	public CogaenId getTypeId() {
		return TYPE_ID;
	}

	public int getButton() {
		return button;
	}

	public double getPosX() {
		return posX;
	}

	public double getPosY() {
		return posY;
	}
	
}
