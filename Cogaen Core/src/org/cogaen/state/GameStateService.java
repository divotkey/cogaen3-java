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

import org.cogaen.action.LoggingAction;
import org.cogaen.core.AbstractService;
import org.cogaen.core.Core;
import org.cogaen.core.CoreListener;
import org.cogaen.core.Service;
import org.cogaen.core.ServiceException;
import org.cogaen.event.EventService;
import org.cogaen.logging.LoggingService;
import org.cogaen.logging.LoggingService.Priority;
import org.cogaen.name.CogaenId;

/**
 * This service can be used to manage all major game sates. It works like a
 * finite state machine and has a predefined start and end state.
 */
public class GameStateService extends AbstractService implements CoreListener {

	public static final CogaenId ID = new CogaenId("org.cogaen.event.GameStateService");
	public static final String NAME = "Cogaen Game State Service";
	public static final String LOGGING_SOURCE = "GMST";
	
	/** Identifier of the predefined start state of this state machine. */
	public static final CogaenId START_STATE_ID = new CogaenId("StartState");
	
	/** Identifier of the predefined end state of this state machine. */
	public static final CogaenId END_STATE_ID = new CogaenId("EndState");
	
	private LoggingService logger;
	private DeterministicStateMachine stateMachine;
	
	public static GameStateService getInstance(Core core) {
		return (GameStateService) core.getService(ID);
	}
	
	public GameStateService() {
		addDependency(LoggingService.ID);
		addDependency(EventService.ID);
	}
		
	/**
	 * Adds an instance of a state.
	 * 
	 * @param state {@code State} game state to be added.
	 * @param stateId {@code CogaenId} unique identifier of the state instance to be added. 
	 */
	public void addState(State state, CogaenId stateId) {
		verifyStatus();

		this.stateMachine.addState(new TwinState(createLoggingState(stateId), state), stateId);
		this.logger.logInfo(LOGGING_SOURCE, "added game state " + stateId);
	}
	
	/**
	 * Switches to the specified state instance.
	 * <p>If the specified state is already the current state of this state
	 * machine, the state is re-entered. This means {@code onExit()} and
	 * {@code onEnter()} of the specified stated is called.</p>
	 * 
	 * @param stateId {@code CogaenId} unique identifier of the state instance to be switched to.
	 */
	public void setCurrentState(CogaenId stateId) {
		verifyStatus();
		this.stateMachine.setCurrentState(stateId);
	}
	
	/**
	 * Returns the identifier of the currently active state.
	 * 
	 * @return identifier of the current state.
	 */
	public CogaenId getCurrentState() {
		verifyStatus();
		return this.stateMachine.getCurrentState();
	}
	
	/**
	 * Determines if the end state of this state machine has been reached.
	 * 
	 * @return {@code true} if the currently active state is the predefined
	 * end state of this state machine.
	 * 
	 * @see #END_STATE_ID
	 */
	public boolean isEndState() {
		verifyStatus();
		return getCurrentState().equals(END_STATE_ID);
	}
	
	public void addTransition(CogaenId fromState, CogaenId toState, CogaenId eventId) {
		verifyStatus();
		this.stateMachine.addTransition(fromState, toState, eventId);
	}
	
	private void verifyStatus() {
		if (getStatus() == Service.Status.STOPPED) {
			throw new IllegalStateException();
		}
	}
	
	@Override
	public CogaenId getId() {
		return ID;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected void doStart() throws ServiceException {
		super.doStart();
		this.logger = LoggingService.getInstance(getCore());
		this.stateMachine = new DeterministicStateMachine(getCore());
		this.stateMachine.addState(createLoggingState(START_STATE_ID), START_STATE_ID);
		this.stateMachine.addState(createLoggingState(END_STATE_ID), END_STATE_ID);
		this.stateMachine.setStartState(START_STATE_ID);
		this.stateMachine.engage();
		
		getCore().addListener(this);
	}

	@Override
	protected void doStop() {
		this.stateMachine.disengage();
		getCore().removeListener(this);
		super.doStop();
	}

	private State createLoggingState(CogaenId stateId) {
		ActionState loggingState = new ActionState();
		loggingState.addEnterAction(new LoggingAction(getCore(), Priority.NOTICE, LOGGING_SOURCE, "entering game state " + stateId));
		loggingState.addExitAction(new LoggingAction(getCore(), Priority.NOTICE, LOGGING_SOURCE, "exiting game state " + stateId));		
		
		return loggingState;
	}

	@Override
	public void shutdownInitiated() {
		if (!getCurrentState().equals(END_STATE_ID)) {
			setCurrentState(END_STATE_ID);
		}
	}
	
}
