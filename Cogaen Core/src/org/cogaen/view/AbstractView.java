package org.cogaen.view;

import org.cogaen.core.Core;
import org.cogaen.core.Engageable;

public class AbstractView implements Engageable {

	private Core core;
	private boolean engaged;

	public AbstractView(Core core) {
		this.core = core;
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
