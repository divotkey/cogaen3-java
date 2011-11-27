package org.cogaen.core;

public class CogaenBase implements Engageable {

	private Core core;
	private boolean engaged;
	
	public CogaenBase(Core core) {
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
		return core;
	}
}
