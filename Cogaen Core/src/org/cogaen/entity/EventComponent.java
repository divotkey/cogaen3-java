package org.cogaen.entity;

import java.util.ArrayList;
import java.util.List;

import org.cogaen.event.Event;
import org.cogaen.event.EventService;

public class EventComponent extends Component {

	private List<Event> engageEvents = new ArrayList<Event>();
	private List<Event> disengageEvents = new ArrayList<Event>();
	
	public EventComponent() {
		this(null, null);
	}
	
	public EventComponent(Event engageEvent, Event disengageEvent) {
		addEngageEvent(engageEvent);
		addDisengageEvent(disengageEvent);
	}
	
	@Override
	public void engage() {
		super.engage();
		EventService evtSrv = EventService.getInstance(getCore());
		for (Event event : this.engageEvents) {
			evtSrv.dispatchEvent(event);
		}
	}

	@Override
	public void disengage() {
		EventService evtSrv = EventService.getInstance(getCore());
		for (Event event : this.disengageEvents) {
			evtSrv.dispatchEvent(event);
		}
		super.disengage();
	}

	
	public void addEngageEvent(Event event) {
		if (event != null) {
			this.engageEvents.add(event);
		}
	}
	
	public void addDisengageEvent(Event event) {
		if (event != null) {
			this.disengageEvents.add(event);
		}
	}
	
	
}
