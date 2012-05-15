package org.cogaen.action;

import org.cogaen.core.Engageable;

public class EngageAction implements Action {

	private Engageable engageable;
	
	public EngageAction(Engageable engageable) {
		this.engageable = engageable;
	}

	@Override
	public void execute() {
		this.engageable.engage();
	}

}
