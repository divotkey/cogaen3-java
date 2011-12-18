package org.cogaen.box2d;

import org.cogaen.event.Event;
import org.cogaen.name.CogaenId;

public class BodyEngagedEvent extends Event {

	static final CogaenId TYPE_ID = new CogaenId("BodyEngaged");

	private CogaenId entityId;
	
	public BodyEngagedEvent(CogaenId entityId) {
		this.entityId = entityId;
	}

	@Override
	public CogaenId getTypeId() {
		return TYPE_ID;
	}

	public CogaenId getEntityId() {
		return entityId;
	}
}
