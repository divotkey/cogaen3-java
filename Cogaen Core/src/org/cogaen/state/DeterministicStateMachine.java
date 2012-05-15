/* 
 -----------------------------------------------------------------------------
                    Cogaen - Component-based Game Engine V3
 -----------------------------------------------------------------------------
 This software is developed by the Cogaen Development Team. Please have a 
 look at our project home page for further details: http://www.cogaen.org
    
 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 Copyright (c) 2010-2011 Roman Divotkey

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 */

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
			throw new RuntimeException("unknown target state " + toStateId);
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
	
	/**
	 * Switches to the specified state instance.
	 * 
	 * <p>If the specified state is already the current state of this state
	 * machine, the state is re-entered. This means {@code onExit()} and
	 * {@code onEnter()} of the specified stated is called.</p>
	 * 
	 * @param stateId {@code CogaenId} unique identifier of the state instance
	 * to be switched to.
	 */
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
