package org.cogaen.entity;

import org.cogaen.core.Engageable;

public class Component implements Engageable {

	private Entity parent;
	
	@Override
	public void engage() {
		// intentionally left empty
	}

	@Override
	public void disengage() {
		// intentionally left empty
	}

	@Override
	public boolean isEngaged() {
		// TODO Auto-generated method stub
		return false;
	}
	
	void setParent(Entity parent) {
		this.parent = parent;
	}
	
	public Entity getParent() {
		return this.parent;
	}

}
