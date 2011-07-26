package org.cogaen.state;

public class TwinState extends BasicState {

	private State state1;
	private State state2;
	
	public TwinState(State state1, State state2) {
		this.state1 = state1;
		this.state2 = state2;
	}
	
	@Override
	public void onEnter() {
		if (this.state1 != null) {
			this.state1.onEnter();
		}
		if (this.state2 != null) {
			this.state2.onEnter();
		}
	}

	@Override
	public void onExit() {
		if (this.state1 != null) {
			this.state1.onExit();
		}
		if (this.state2 != null) {
			this.state2.onExit();
		}
	}

}
