package org.cogaen.view;

import org.cogaen.core.Engageable;
import org.cogaen.name.CogaenId;

public class EntityRepresentation implements Engageable {

	private CogaenId entityId;
	private boolean engaged;
	
	public EntityRepresentation(CogaenId entityId) {
		this.entityId = entityId;
	}

	@Override
	public void engage() {
		this.engaged = true;
	}

	@Override
	public void disengage() {
		this.engaged = false;
	}

	@Override
	public final boolean isEngaged() {
		return this.engaged;
	}
	
	public final CogaenId getEntityId() {
		return this.entityId;
	}
	
}
