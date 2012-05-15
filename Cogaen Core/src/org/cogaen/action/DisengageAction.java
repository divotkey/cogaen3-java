package org.cogaen.action;

import org.cogaen.core.Engageable;

public class DisengageAction implements Action {

	private Engageable engageable;
	
	public DisengageAction(Engageable engageable) {
		this.engageable = engageable;
	}

	@Override
	public void execute() {
		this.engageable.disengage();
	}

}
