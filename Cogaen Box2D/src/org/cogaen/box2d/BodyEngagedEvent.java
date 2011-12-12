package org.cogaen.box2d;

import org.cogaen.event.Event;
import org.cogaen.name.CogaenId;
import org.jbox2d.dynamics.Body;

public class BodyEngagedEvent extends Event {

	static final CogaenId TYPE_ID = new CogaenId("BodyEngaged");

	private Body body;
	
	public BodyEngagedEvent(Body body) {
		this.body = body;
	}

	@Override
	public CogaenId getTypeId() {
		return TYPE_ID;
	}

	public Body getBody() {
		return body;
	}

}
