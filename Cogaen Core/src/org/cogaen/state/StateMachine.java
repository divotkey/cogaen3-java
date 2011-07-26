package org.cogaen.state;

import org.cogaen.core.Engageable;
import org.cogaen.name.CogaenId;

public interface StateMachine extends Engageable {
	
	public boolean hasState(CogaenId stateId);
	public void addState(State state, CogaenId stateId);
	public void addTransition(CogaenId fromState, CogaenId toState, CogaenId eventId);
	public void setStartState(CogaenId stateId);
	
}
