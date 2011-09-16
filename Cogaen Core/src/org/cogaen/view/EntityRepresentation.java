package org.cogaen.view;

import org.cogaen.core.Engageable;

public class EntityRepresentation implements Engageable {

	private boolean engaged;
	
	public EntityRepresentation() {
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
	
	
}
