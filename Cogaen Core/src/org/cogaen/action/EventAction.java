package org.cogaen.action;

import org.cogaen.core.Core;
import org.cogaen.event.Event;
import org.cogaen.event.EventService;
import org.cogaen.event.SimpleEvent;
import org.cogaen.name.CogaenId;

public class EventAction implements Action {

	private EventService evtSrv;
	private Event event;

	public EventAction(Core core, Event event) {
		this.evtSrv = EventService.getInstance(core);
		this.event = event;
	}
	
	public EventAction(Core core, CogaenId eventTypeId) {
		this(core, new SimpleEvent(eventTypeId));
	}
	
	@Override
	public void execute() {
		this.evtSrv.dispatchEvent(this.event);
	}

}
