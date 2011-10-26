package org.cogaen.view;

import org.cogaen.core.Core;
import org.cogaen.core.Engageable;
import org.cogaen.name.CogaenId;

public class AbstractHud implements Engageable {

	private Core core;
	private boolean engaged;
	
	public AbstractHud(Core core) {
		this.core = core;
	}

	public void registerResources(CogaenId groupId) {
		// intentionally left empty
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

	public final Core getCore() {
		return this.core;
	}
}
