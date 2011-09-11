package org.cogaen.entity;

import org.cogaen.core.Core;
import org.cogaen.core.Engageable;

public class Component implements Engageable {

	private ComponentEntity parent;
	private boolean engaged = false;
	
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
	
	final void setParent(ComponentEntity parent) {
		this.parent = parent;
	}
	
	public final ComponentEntity getParent() {
		return this.parent;
	}
	
	public final Core getCore() {
		return this.parent.getCore();
	}

}
