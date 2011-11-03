package org.cogaen.entity;

import java.util.ArrayList;
import java.util.List;

import org.cogaen.action.Action;
import org.cogaen.core.Updateable;
import org.cogaen.time.TimeService;
import org.cogaen.time.Timer;

public class ActionComponent extends Component implements Updateable {

	private List<Action> engageActions = new ArrayList<Action>();
	private List<Action> disengageActions = new ArrayList<Action>();
	private List<Action> delayedActions = new ArrayList<Action>();
	private double delay;
	private boolean repeat;
	private Timer timer;
	private double timestamp;
	
	public ActionComponent() {
		this(null, null, null, 1.0, false);
	}

	public ActionComponent(Action engageAction, Action disengageAction) {
		this(engageAction, disengageAction, null, 0, false);
	}

	public ActionComponent(Action delayedAction, double delay, boolean repeat) {
		this(null, null, delayedAction, delay, repeat);
	}
	
	public ActionComponent(Action engageAction, Action disengageAction, Action delayedAction, double delay, boolean repeat) {
		addEngageAction(engageAction);
		addDisengageAction(disengageAction);
		addDelayedAction(delayedAction);
		this.setDelay(delay);
		this.setRepeat(repeat);
	}
	
	@Override
	public void engage() {
		super.engage();
		for (Action action : this.engageActions) {
			action.execute();
		}
		
		if (this.delayedActions.size() > 0) {
			EntityService.getInstance(getCore()).addUpdateable(this);
			this.timer = TimeService.getInstance(getCore()).getTimer();
			this.timestamp = this.timer.getTime() + this.delay;
		}
	}

	@Override
	public void disengage() {
		for (Action action : this.disengageActions) {
			action.execute();
		}
		
		if (this.delayedActions.size() > 0) {
			EntityService.getInstance(getCore()).removeUpdateable(this);
		}
		super.disengage();
	}

	@Override
	public void update() {
		if (this.timestamp <= this.timer.getTime()) {
			for (Action action : this.delayedActions) {
				action.execute();
			}
			
			if (this.repeat) {
				this.timestamp = this.timer.getTime() + this.delay;
			} else {
				EntityService.getInstance(getCore()).removeUpdateable(this);
			}
		}
	}
	
	public void addEngageAction(Action action) {
		if (action != null) {
			this.engageActions.add(action);
		}
	}
	
	public void addDisengageAction(Action action) {
		if (action != null) {
			this.disengageActions.add(action);
		}
	}
	
	public void addDelayedAction(Action action) {
		if (isEngaged()) {
			throw new IllegalStateException("component is engaged, adding delayed actions is not allowed");
		}
		if (action != null) {
			this.delayedActions.add(action);
		}
	}

	public boolean isRepeat() {
		return repeat;
	}

	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}

	public double getDelay() {
		return delay;
	}

	public void setDelay(double delay) {
		this.delay = delay;
	}
}
