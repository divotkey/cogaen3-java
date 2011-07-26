package org.cogaen.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cogaen.core.Core;
import org.cogaen.event.Event;
import org.cogaen.event.EventListener;
import org.cogaen.event.EventService;
import org.cogaen.name.CogaenId;

public class DeterministicStateMachine implements StateMachine, EventListener {

	private Map<CogaenId, State> states = new HashMap<CogaenId, State>();
	private List<Transition> transitions = new ArrayList<Transition>();
	private EventService evtSrv;
	private boolean engaged = false;
	private CogaenId currentStateId;
	private CogaenId startStateId;
	
	public DeterministicStateMachine(Core core) {
		this.evtSrv = EventService.getInstance(core);
	}
	
	@Override
	public boolean hasState(CogaenId stateId) {
		return states.containsKey(stateId);
	}
		
	@Override
	public void setStartState(CogaenId stateId) {
		if (!hasState(stateId)) {
			throw new RuntimeException("unknown start state " + stateId);
		}
		this.startStateId = stateId;
	}
	
	@Override
	public void addState(State state, CogaenId stateId) {
		State  oldState = this.states.put(stateId, state);
		
		if (oldState != null) {
			this.states.put(stateId, oldState);
			throw new RuntimeException("ambiguous state id " + stateId);
		}
	}
	
	@Override
	public void addTransition(CogaenId fromStateId, CogaenId toStateId, CogaenId eventId) {
		// validate parameters
		if (!hasState(fromStateId)) {
			throw new RuntimeException("unknown source state " + fromStateId);
		}
		
		if (!hasState(toStateId)) {
			throw new RuntimeException("unknown target state " + fromStateId);
		}
		
		if (eventId == null) {
			throw new NullPointerException("event identifier must not be null");
		}
		
		// ensure this transition is unambiguous
		if (hasTransition(fromStateId, eventId)) {
			throw new RuntimeException("ambiguous transition");
		}
		
		// store transition
		this.transitions.add(new Transition(fromStateId, toStateId, eventId));
		
		// if already engaged, start listening on specified events immediately
		if (isEngaged()) {
			if (!this.evtSrv.hasListener(this, eventId)) {
				this.evtSrv.addListener(this, eventId);
			}
		}
	}
	
	@Override
	public void engage() {
		if (isEngaged()) {
			throw new IllegalStateException("already engaged");
		}
		
		if (this.startStateId == null) {
			throw new RuntimeException("no start state specified");
		}
		
		// register for all events that might can trigger transitions
		for (Transition trans : this.transitions) {
			if (!this.evtSrv.hasListener(this, trans.eventId)) {
				this.evtSrv.addListener(this, trans.eventId);
			}
		}		
		this.engaged = true;
		switchState(this.startStateId);
	}
	
	@Override
	public void disengage() {
		if (!isEngaged()) {
			throw new IllegalStateException("not engaged");
		}
		
		assert(this.currentStateId != null);
		switchState(null);
		
		this.evtSrv.removeListener(this);
		this.engaged = false;
	}
	
	@Override
	public boolean isEngaged() {
		return this.engaged;
	}
		
	@Override
	public void handleEvent(Event event) {
		for (Transition trans : this.transitions) {
			if (trans.fromStateId.equals(this.currentStateId) && event.isOfType(trans.eventId)) {
				switchState(trans.toStateId);
				break;
			}
		}
	}
	
	public void setCurrentState(CogaenId stateId) {
		if (!isEngaged()) {
			throw new IllegalStateException("not engaged");
		}
		
		if (stateId == null) {
			throw new NullPointerException("state identifier must not be null");
		}
		
		switchState(stateId);
	}
	
	public CogaenId getCurrentState() {
		return this.currentStateId;
	}
	
	private boolean hasTransition(CogaenId fromStateId, CogaenId eventId) {
		for (Transition trans : this.transitions) {
			if (trans.fromStateId.equals(fromStateId) && trans.eventId.equals(eventId)) {
				return true;
			}
		}
		return false;
	}

	private void switchState(CogaenId targetId) {
		if (this.currentStateId != null) {
			this.states.get(this.currentStateId).onExit();
		}
		
		if (targetId != null) {
			this.states.get(targetId).onEnter();
		}
		this.currentStateId = targetId;
	}

	private static class Transition {
		public CogaenId fromStateId;
		public CogaenId toStateId;
		public CogaenId eventId;
		
		public Transition(CogaenId fromStateId, CogaenId toStateId, CogaenId eventId) {
			this.fromStateId = fromStateId;
			this.toStateId = toStateId;
			this.eventId = eventId;
		}
	}
	
}
