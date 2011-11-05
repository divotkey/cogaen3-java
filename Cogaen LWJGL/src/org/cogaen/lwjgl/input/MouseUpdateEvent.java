package org.cogaen.lwjgl.input;

import org.cogaen.event.Event;
import org.cogaen.name.CogaenId;

public class MouseUpdateEvent extends Event {

	public static final CogaenId TYPE_ID = new CogaenId("MouseUpdate");

	private double posX;
	private double posY;
	
	public MouseUpdateEvent(double posX, double posY) {
		this.posX = posX;
		this.posY = posY;
	}

	@Override
	public CogaenId getTypeId() {
		return TYPE_ID;
	}

	public double getPosX() {
		return posX;
	}

	public double getPosY() {
		return posY;
	}
	
}
