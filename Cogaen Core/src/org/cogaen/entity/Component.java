package org.cogaen.entity;

import org.cogaen.core.Core;
import org.cogaen.core.Engageable;
import org.cogaen.event.Event;
import org.cogaen.event.EventListener;

public class Component implements Engageable, EventListener {

	private ComponentEntity parent;
	private boolean engaged = false;

	public void initialize(ComponentEntity parent) {
		this.parent = parent;
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
		
	public final ComponentEntity getParent() {
		return this.parent;
	}
	
	public final Core getCore() {
		return this.parent.getCore();
	}

	@Override
	public void handleEvent(Event event) {
		// intentionally left empty
	}

}
