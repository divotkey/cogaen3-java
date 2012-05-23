package org.cogaen.lwjgl.ngui;

import org.cogaen.core.Core;
import org.cogaen.event.Event;
import org.cogaen.event.EventListener;
import org.cogaen.event.EventService;
import org.cogaen.lwjgl.input.MouseUpdateEvent;
import org.cogaen.lwjgl.scene.Color;
import org.cogaen.lwjgl.scene.LocalToGlobal;
import org.cogaen.lwjgl.scene.ViewToOverlay;

public class HoverButton extends Label implements EventListener {

	private ViewToOverlay viewToOverlay;
	private LocalToGlobal localToGlobal;
	private double hWidth;
	private double hHeight;
	
	public HoverButton(Core core, String fontResource) {
		super(core, fontResource);
		this.viewToOverlay = new ViewToOverlay(getCore());
		this.localToGlobal = new LocalToGlobal();
	}

	@Override
	public void engage() {
		super.engage();
		EventService evtSrv = EventService.getInstance(getCore());
		evtSrv.addListener(this, MouseUpdateEvent.TYPE_ID);
		this.hWidth = getWidth() / 2;
		this.hHeight = getHeight() / 2;
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

	@Override
	public void setText(String text) {
		super.setText(text);
		if (isEngaged()) {
			this.hWidth = getWidth() / 2;
			this.hHeight = getHeight() / 2;
		}
	}

	private void handleMouseUpdate(MouseUpdateEvent event) {		
		this.viewToOverlay.transform(event.getPosX(), event.getPosY());
		this.localToGlobal.transform(getBaseNode());
		
		if (this.viewToOverlay.getOverlayX() < this.localToGlobal.getGlobalX() - this.hWidth) {
			setColor(Color.GREEN);
			return;
		}
		
		if (this.viewToOverlay.getOverlayX() > this.localToGlobal.getGlobalX() + this.hWidth) {
			setColor(Color.GREEN);
			return;
		}
		
		if (this.viewToOverlay.getOverlayY() < this.localToGlobal.getGlobalY() - this.hWidth) {
			setColor(Color.GREEN);
			return;
		}
		
		if (this.viewToOverlay.getOverlayY() > this.localToGlobal.getGlobalY() + this.hWidth) {
			setColor(Color.GREEN);
			return;
		}
		setColor(Color.YELLOW);
	}
}
