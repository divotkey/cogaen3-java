package org.cogaen.state;

import org.cogaen.core.Core;
import org.cogaen.name.CogaenId;
import org.cogaen.view.View;

public class GameStateHelper {

	private GameStateService stateSrv;
	private Core core;
	
	public GameStateHelper(Core core) {
		this.core = core;
		this.stateSrv = GameStateService.getInstance(core);
	}
	
	public void addViewState(View view, String resourceGroup, String stateId) {
		ViewState viewState = new ViewState(this.core, view, new CogaenId(resourceGroup));
		addState(viewState, new CogaenId(stateId));
	}
	
	public void addState(State state, String stateId) {
		addState(state, new CogaenId(stateId));
	}
	
	public void addState(State state, CogaenId stateId) {
		this.stateSrv.addState(state, stateId);
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
		this.stateSrv.addTransition(fromState, toState, eventId);
	}
	
	public void setCurrentState(String stateId) {
		setCurrentState(new CogaenId(stateId));
	}
	
	public void setCurrentState(CogaenId stateId) {
		this.stateSrv.setCurrentState(stateId);
	}
	
}
