package org.cogaen.state;

import java.util.ArrayList;
import java.util.List;

public class MultiState extends BasicState {

	private List<State> states = new ArrayList<State>();
	
	public void addState(State state) {
		this.states.add(state);
	}
	
	@Override
	public void onEnter() {
		super.onEnter();
		for (State state : this.states) {
			state.onEnter();
		}
	}

	@Override
	public void onExit() {
		for (State state : this.states) {
			state.onExit();
		}
		super.onExit();
	}
	
	

}
