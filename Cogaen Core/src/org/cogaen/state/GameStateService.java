package org.cogaen.state;

import org.cogaen.action.LoggingAction;
import org.cogaen.core.AbstractService;
import org.cogaen.core.Core;
import org.cogaen.core.Service;
import org.cogaen.core.ServiceException;
import org.cogaen.event.EventService;
import org.cogaen.logging.LoggingService;
import org.cogaen.logging.LoggingService.Priority;
import org.cogaen.name.CogaenId;

public class GameStateService extends AbstractService {

	public static final CogaenId ID = new CogaenId("org.cogaen.event.GameStateService");
	public static final String NAME = "Cogaen Game State Service";
	public static final String LOGGING_SOURCE = "GMST";
	public static final CogaenId START_STATE_ID = new CogaenId("StartState");
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
		
	public void addState(State state, CogaenId stateId) {
		verifyStatus();

		this.stateMachine.addState(new TwinState(createLoggingState(stateId), state), stateId);
		this.logger.logInfo(LOGGING_SOURCE, "added game state " + stateId);
	}
	
	public void setCurrentState(CogaenId stateId) {
		verifyStatus();
		this.stateMachine.setCurrentState(stateId);
	}
	
	public CogaenId getCurrentState() {
		verifyStatus();
		return this.stateMachine.getCurrentState();
	}
	
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
	}

	@Override
	protected void doStop() {
		this.stateMachine.disengage();
		super.doStop();
	}

	private State createLoggingState(CogaenId stateId) {
		ActionState loggingState = new ActionState();
		loggingState.addEnterAction(new LoggingAction(getCore(), Priority.INFO, LOGGING_SOURCE, "entering game state " + stateId));
		loggingState.addExitAction(new LoggingAction(getCore(), Priority.INFO, LOGGING_SOURCE, "exiting game state " + stateId));		
		
		return loggingState;
	}
	
}
