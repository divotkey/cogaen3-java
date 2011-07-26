package org.cogaen.event;

import org.cogaen.name.CogaenId;

public abstract class Event {
	
	public abstract CogaenId getTypeId();

	public void release() {
		// intentionally left empty
	}
	
	public boolean isOfType(CogaenId id) {
		return getTypeId().equals(id);
	}
	
}
