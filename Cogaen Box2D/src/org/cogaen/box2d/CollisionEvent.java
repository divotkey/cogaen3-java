package org.cogaen.box2d;

import org.cogaen.event.Event;
import org.cogaen.name.CogaenId;

public class CollisionEvent extends Event {

	public static final CogaenId TYPE_ID = new CogaenId("Collision");

	private CogaenId entityIdA;
	private CogaenId entityIdB;
	
	public CollisionEvent(CogaenId entityIdA, CogaenId entityIdB) {
		this.entityIdA = entityIdA;
		this.entityIdB = entityIdB;
	}

	@Override
	public CogaenId getTypeId() {
		return TYPE_ID;
	}

	public CogaenId getEntityIdA() {
		return entityIdA;
	}

	public CogaenId getEntityIdB() {
		return entityIdB;
	}
	
	public CogaenId getOpponent(CogaenId entityId) {
		if (this.entityIdA.equals(entityId)) {
			return this.entityIdB;
		} else if (this.entityIdB.equals(entityId)) {
			return this.entityIdA;
		} else {
			return null;
		}
	}

}
