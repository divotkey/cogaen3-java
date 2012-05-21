package org.cogaen.state;

import org.cogaen.core.Core;
import org.cogaen.name.CogaenId;
import org.cogaen.view.View;

public class StateMachineHelper {

	private Core core;
	private StateMachine stateMachine;

	public StateMachineHelper(Core core, StateMachine stateMachine) {
		this.stateMachine = stateMachine;
		this.core = core;
	}

	public void addViewState(View view, String stateId) {
		addViewState(view, null, stateId);
	}
	
	public void addViewState(View view, String resourceGroup, String stateId) {
		ViewState state;
		if (resourceGroup == null) {
			state = new ViewState(this.core, view);
		} else {
			state = new ViewState(this.core, view, new CogaenId("resourceGroup"));
		}
		addState(state, new CogaenId(stateId));
	}
	
	public void addState(State state, String stateId) {
		addState(state, new CogaenId(stateId));
	}
	
	public void addState(State state, CogaenId stateId) {
		this.stateMachine.addState(state, stateId);
		if (this.stateMachine.getStartState() == null) {
			this.stateMachine.setStartState(stateId);
		}
	}
	
	public void addTransition(CogaenId fromState, CogaenId toState, String eventId) {
		addTransition(fromState, toState, new CogaenId(eventId));
	}
	
	public void addTransition(CogaenId fromState, String toState, String eventId) {
		addTransition(fromState, new CogaenId(toState), new CogaenId(eventId));
	}
	
	public void addTransition(CogaenId fromState, String toState, CogaenId eventId) {
		addTransition(fromState, new CogaenId(toState), eventId);
	}
	
	public void addTransition(String fromState, String toState, String eventId) {
		addTransition(new CogaenId(fromState), new CogaenId(toState), new CogaenId(eventId));
	}

	public void addTransition(String fromState, CogaenId toState, String eventId) {
		addTransition(new CogaenId(fromState), toState, new CogaenId(eventId));		
	}
	
	public void addTransition(String fromState, String toState, CogaenId eventId) {
		addTransition(new CogaenId(fromState), new CogaenId(toState), eventId);		
	}
	
	public void addTransition(String fromState, CogaenId toState, CogaenId eventId) {
		addTransition(new CogaenId(fromState), toState, eventId);		
	}
	
	public void addTransition(CogaenId fromState, CogaenId toState, CogaenId eventId) {
		this.stateMachine.addTransition(fromState, toState, eventId);
	}

	public void setStartState(String stateId) {
		setStartState(new CogaenId(stateId));
	}	
	
	public void setStartState(CogaenId stateId) {
		this.stateMachine.setStartState(stateId);
	}
}
