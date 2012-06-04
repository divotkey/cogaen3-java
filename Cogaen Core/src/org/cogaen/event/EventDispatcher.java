package org.cogaen.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.cogaen.core.CogaenBase;
import org.cogaen.core.Core;
import org.cogaen.name.CogaenId;

public class EventDispatcher extends CogaenBase implements EventListener{

	private Map<CogaenId, ArrayList<EventListener>> listenerMap = new HashMap<CogaenId, ArrayList<EventListener>>();
	public EventDispatcher(Core core) {
		super(core);
	}

	public void addListener(String eventTypeId, EventListener listener) {
		addListener(new CogaenId(eventTypeId), listener);
	}
	
	public void addListener(CogaenId eventTypeId, EventListener listener) {
		if (isEngaged()) {
			throw new IllegalStateException("can't add listener if event dispatcher is engaged");
		}
		
		ArrayList<EventListener> listeners = this.listenerMap.get(eventTypeId);
		if (listeners == null) {
			listeners = new ArrayList<EventListener>();
			this.listenerMap.put(eventTypeId, listeners);
		}
		
		listeners.add(listener);
	}

	@Override
	public void engage() {
		super.engage();
		
		EventService evtSrv = EventService.getInstance(getCore());
		for (CogaenId eventTypeId : this.listenerMap.keySet()) {
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
		ArrayList<EventListener> listeners = this.listenerMap.get(event.getTypeId());
		if (listeners == null || listeners.isEmpty()) {
			return;
		}

		int size = listeners.size();
		for (int i = 0; i < size; ++i) {
			EventListener listener = listeners.get(i);
			listener.handleEvent(event);
		}
	}
}
