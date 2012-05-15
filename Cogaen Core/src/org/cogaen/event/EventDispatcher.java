package org.cogaen.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.cogaen.action.Action;
import org.cogaen.core.CogaenBase;
import org.cogaen.core.Core;
import org.cogaen.name.CogaenId;

public class EventDispatcher extends CogaenBase implements EventListener{

	private Map<CogaenId, ArrayList<Action>> actionMap = new HashMap<CogaenId, ArrayList<Action>>();
	private Event currentEvent;
	
	public EventDispatcher(Core core) {
		super(core);
	}

	public void addAction(String eventTypeId, Action action) {
		addAction(new CogaenId(eventTypeId), action);
	}
	
	public void addAction(CogaenId eventTypeId, Action action) {
		if (isEngaged()) {
			throw new IllegalStateException("can't add action if event handler is engaged");
		}
		
		ArrayList<Action> actions = this.actionMap.get(eventTypeId);
		if (actions == null) {
			actions = new ArrayList<Action>();
			this.actionMap.put(eventTypeId, actions);
		}
		
		actions.add(action);
	}

	@Override
	public void engage() {
		super.engage();
		
		EventService evtSrv = EventService.getInstance(getCore());
		for (CogaenId eventTypeId : this.actionMap.keySet()) {
			evtSrv.addListener(this, eventTypeId);
		}
	}

	@Override
	public void disengage() {
		EventService.getInstance(getCore()).removeListener(this);
		super.disengage();
	}

	@Override
	public void handleEvent(Event event) {
		ArrayList<Action> actions = this.actionMap.get(event.getTypeId());
		if (actions == null || actions.isEmpty()) {
			return;
		}

		this.currentEvent = event;
		int size = actions.size();
		for (int i = 0; i < size; ++i) {
			Action action = actions.get(i);
			action.execute();
		}
		this.currentEvent = null;
	}

	public Event getCurrentEvent() {
		return this.currentEvent;
	}
}
