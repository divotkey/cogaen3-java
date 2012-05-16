package org.cogaen.view;

import java.util.ArrayList;
import java.util.List;

import org.cogaen.action.Action;
import org.cogaen.core.Core;
import org.cogaen.event.Event;
import org.cogaen.event.EventDispatcher;
import org.cogaen.name.CogaenId;

public class ActionView extends View {

	private EventDispatcher dispatcher;
	private List<Action> engageActions = new ArrayList<Action>();
	private List<Action> disengageActions = new ArrayList<Action>();
	
	public ActionView(Core core) {
		super(core);
		this.dispatcher = new EventDispatcher(getCore());
		addEngageable(this.dispatcher);
	}
	
	public void addEngageAction(Action action) {
		this.engageActions.add(action);
	}

	public void addDisengageAction(Action action) {
		this.disengageActions.add(action);
	}
	
	public void addAction(CogaenId eventType, Action action) {
		this.dispatcher.addAction(eventType, action);
	}

	public Event getCurrentEvent() {
		return this.dispatcher.getCurrentEvent();
	}
	
	@Override
	public void engage() {
		super.engage();
		for (Action action : this.engageActions) {
			action.execute();
		}
	}

	@Override
	public void disengage() {
		for (Action action : this.disengageActions) {
			action.execute();
		}
		super.disengage();
	}
}
