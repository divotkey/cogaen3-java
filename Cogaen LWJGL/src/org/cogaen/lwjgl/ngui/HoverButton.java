package org.cogaen.lwjgl.ngui;

import org.cogaen.core.Core;
import org.cogaen.event.Event;
import org.cogaen.event.EventListener;
import org.cogaen.event.EventService;
import org.cogaen.lwjgl.input.MouseUpdateEvent;

public class HoverButton extends BaseGui implements EventListener {

	public HoverButton(Core core) {
		super(core);
	}

	@Override
	public void engage() {
		super.engage();
		EventService evtSrv = EventService.getInstance(getCore());
		evtSrv.addListener(this, MouseUpdateEvent.TYPE_ID);
	}

	@Override
	public void disengage() {
		EventService.getInstance(getCore()).removeListener(this);
		super.disengage();
	}

	@Override
	public void handleEvent(Event event) {
		if (event.isOfType(MouseUpdateEvent.TYPE_ID)) {
			handleMouseUpdate((MouseUpdateEvent) event);
		}
	}

	private void handleMouseUpdate(MouseUpdateEvent event) {
		
	}
}
