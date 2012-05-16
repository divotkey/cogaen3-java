package org.cogaen.action;

import org.cogaen.core.Core;

public abstract class BasicAction implements Action {

	private Core core;
	
	public BasicAction(Core core) {
		this.core = core;
	}
	
	public final Core getCore() {
		return this.core;
	}

}
