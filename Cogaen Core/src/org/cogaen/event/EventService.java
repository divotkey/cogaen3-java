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

package org.cogaen.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cogaen.core.AbstractService;
import org.cogaen.core.Core;
import org.cogaen.core.ServiceException;
import org.cogaen.core.Updateable;
import org.cogaen.logging.LoggingService;
import org.cogaen.name.CogaenId;
import org.cogaen.util.Bag;

public class EventService extends AbstractService implements Updateable {

	public static final CogaenId ID = new CogaenId("org.cogaen.event.EventService");
	public static final String NAME = "Cogaen Event Service";
	public static final String LOGGING_SOURCE = "EVNT";
	public static final boolean DEFAULT_FAST_EVENT_DISPATCH = true;
	
	private Map<CogaenId, Bag<EventListener>> listenerMap = new HashMap<CogaenId, Bag<EventListener>>();
	private List<Event> events1 = new ArrayList<Event>();
	private List<Event> events2 = new ArrayList<Event>();
	private List<Event> currentEvents = events1;
	private boolean fastEventDispatch = DEFAULT_FAST_EVENT_DISPATCH;
	private LoggingService logger;
	
	public static EventService getInstance(Core core) {
		return (EventService) core.getService(ID);
	}
	
	public EventService() {
		addDependency(LoggingService.ID);
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
	protected void doPause() {
		getCore().removeUpdateable(this);
		super.doPause();
	}

	@Override
	protected void doResume() {
		super.doResume();
		getCore().addUpdateable(this);
	}

	@Override
	protected void doStart() throws ServiceException {
		super.doStart();
		this.logger = LoggingService.getInstance(getCore());
		getCore().addUpdateable(this);
	}

	@Override
	protected void doStop() {
		for (Event event : this.events1) {
			event.release();
		}
		this.events1.clear();

		for (Event event : this.events2) {
			event.release();
		}
		this.events2.clear();
		
		if (getStatus() != Status.PAUSED) {
			getCore().removeUpdateable(this);
		}
		this.logger = null;
		super.doStop();
	}

	@Override
	public void update() {
		do {
			List<Event> events = this.currentEvents;
			swapEventList();
			for (Event event : events) {
				fireEvent(event);
				event.release();
			}
			events.clear();
		} while (this.fastEventDispatch && !this.currentEvents.isEmpty());
	}
	
	public void dispatchEvent(Event event) {
		this.currentEvents.add(event);
	}
		
	public void addListener(EventListener listener, CogaenId eventId) {
		Bag<EventListener> listeners = this.listenerMap.get(eventId);
		
		if (listeners == null) {
			listeners = new Bag<EventListener>();
			this.listenerMap.put(eventId, listeners);
			listeners.add(listener);
		} else if (!listeners.contains(listener)) {
			listeners.add(listener);
		} else {
			this.logger.logWarning(LOGGING_SOURCE, 
					"attempt to add listener for event type " 
					+ eventId + " twice (" + listener.getClass().getName() + ")");
		}
	}
	
	public void removeListener(EventListener listener, CogaenId eventId) {
		Bag<EventListener> listeners = this.listenerMap.get(eventId);
		
		if (listeners == null || !listeners.remove(listener) ) {
			this.logger.logWarning(LOGGING_SOURCE, 
					"attempt to remove unregistered listener for event type " 
					+ eventId + " (" + listener.getClass().getName() + ")");
		}
	}
	
	public void removeListener(EventListener listener) {
		for (Bag<EventListener> bag : this.listenerMap.values()) {
			bag.remove(listener);
		}
	}
	
	public boolean hasListener(EventListener listener, CogaenId eventId) {
		Bag<EventListener> listeners = this.listenerMap.get(eventId);
		return listeners != null ? listeners.contains(listener) : false;
	}
	
	public boolean isFastEventDispatch() {
		return fastEventDispatch;
	}

	public void setFastEventDispatch(boolean fastEventDispatch) {
		this.fastEventDispatch = fastEventDispatch;
	}
	
	private void swapEventList() {
		if(this.currentEvents == events1) {
			this.currentEvents = events2;
		} else {
			this.currentEvents = events1;
		}
	}

	private void fireEvent(Event event) {
		Bag<EventListener> listeners = this.listenerMap.get(event.getTypeId());
		
		if (listeners == null) {
			// no listeners for this event
			return;
		}
		
		for (listeners.reset(); listeners.hasNext();) {
			listeners.next().handleEvent(event);
		}
	}
	
}
