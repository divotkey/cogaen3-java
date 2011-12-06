package org.cogaen.box2d;

import org.cogaen.event.Event;
import org.cogaen.name.CogaenId;

public class PoseUpdateEvent extends Event {

	public static final CogaenId TYPE_ID = new CogaenId("PoseUpdate");
	
	private CogaenId entityId;
	private double posX;
	private double posY;
	private double angle;

	public PoseUpdateEvent(CogaenId entityId, Pose2D pose) {
		super();
		this.entityId = entityId;
		this.posX = pose.getPosX();
		this.posY = pose.getPosY();
		this.angle = pose.getAngle();
	}

	@Override
	public CogaenId getTypeId() {
		return TYPE_ID;
	}
	
	public CogaenId getEntityId() {
		return entityId;
	}

	public double getPosX() {
		return posX;
	}

	public double getPosY() {
		return posY;
	}

	public double getAngle() {
		return angle;
	}
}
