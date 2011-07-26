package org.cogaen.state;

import org.cogaen.core.Engageable;

public class EngagingState extends BasicState {

	private Engageable engageable;
	
	public EngagingState(Engageable engageable) {
		this.engageable = engageable;
	}

	@Override
	public void onEnter() {
		super.onEnter();
		this.engageable.engage();
	}

	@Override
	public void onExit() {
		this.engageable.disengage();
		super.onExit();
	}
	
	
}
