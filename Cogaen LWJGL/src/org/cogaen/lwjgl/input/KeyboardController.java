package org.cogaen.lwjgl.input;

import java.util.ArrayList;
import java.util.List;

import org.cogaen.core.Core;
import org.cogaen.core.Engageable;
import org.cogaen.event.Event;
import org.cogaen.event.EventListener;
import org.cogaen.event.EventService;

public class KeyboardController implements Engageable, EventListener {

	private EventService evtSrv;
	private int hAxis[] = new int[2];
	private int vAxis[] = new int[2];
	private List<Integer> buttons = new ArrayList<Integer>();
	private boolean engaged;
	
	public KeyboardController(Core core) {
		this.evtSrv = EventService.getInstance(core);
		
		// initialize with default keys
		this.hAxis[0] = KeyCode.KEY_LEFT;
		this.hAxis[1] = KeyCode.KEY_RIGHT;
		this.vAxis[0] = KeyCode.KEY_UP;
		this.vAxis[1] = KeyCode.KEY_DOWN;
	}
	
	public void setAxisKeys(int left, int right, int up, int down) {
		this.hAxis[0] = left;
		this.hAxis[1] = right;
		this.vAxis[0] = up;
		this.vAxis[1] = down;
	}

	public void addButton(int keyCode) {
		this.buttons.add(keyCode);
	}
	
	
	@Override
	public void engage() {
		this.evtSrv.addListener(this, KeyPressedEvent.TYPE_ID);
		this.evtSrv.addListener(this, KeyReleasedEvent.TYPE_ID);
		this.engaged = true;
	}

	@Override
	public void disengage() {
		this.evtSrv.removeListener(this);
	}

	@Override
	public boolean isEngaged() {
		return this.engaged;
	}

	@Override
	public void handleEvent(Event event) {
		if (event.isOfType(KeyPressedEvent.TYPE_ID)) {
			KeyPressedEvent keyPressed = (KeyPressedEvent) event;
			handleKey(keyPressed.getKeyCode(), true);
		} else if (event.isOfType(KeyReleasedEvent.TYPE_ID)) {
			KeyReleasedEvent keyReleased = (KeyReleasedEvent) event;	
			handleKey(keyReleased.getKeyCode(), false);
		}
	}

	private void handleKey(int keyCode, boolean pressed) {
		if (keyCode == hAxis[0]) {
			this.evtSrv.dispatchEvent(new ControllerComponent.HorizontalUpdateEvent(pressed ? -1.0 : 0.0));
		} else if (keyCode == hAxis[1]) {
			this.evtSrv.dispatchEvent(new ControllerComponent.HorizontalUpdateEvent(pressed ? 1.0 : 0.0));
		} else if (keyCode == vAxis[0]) {
			this.evtSrv.dispatchEvent(new ControllerComponent.VerticalUpdateEvent(pressed ? 1.0 : 0.0));
		}  else if (keyCode == vAxis[1]) {
			this.evtSrv.dispatchEvent(new ControllerComponent.VerticalUpdateEvent(pressed ? -1.0 : 0.0));
		} else {
			int idx = this.buttons.indexOf(keyCode);
			if (idx >= 0) {
				this.evtSrv.dispatchEvent(new ControllerComponent.ButtonlUpdateEvent(pressed, idx));
			}
		}
	}
}
