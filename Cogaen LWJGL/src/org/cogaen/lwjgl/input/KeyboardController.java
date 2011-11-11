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

package org.cogaen.lwjgl.input;

import java.util.ArrayList;
import java.util.List;

import org.cogaen.core.Core;
import org.cogaen.core.Engageable;
import org.cogaen.event.Event;
import org.cogaen.event.EventListener;
import org.cogaen.event.EventService;
import org.cogaen.name.CogaenId;

public class KeyboardController implements Engageable, EventListener {

	private EventService evtSrv;
	private int axis[] = new int[4];
	private List<Integer> buttons = new ArrayList<Integer>();
	private boolean engaged;
	private CogaenId entityId;
	private boolean keyStates[] = new boolean[4];
	
	public KeyboardController(Core core, CogaenId entityId) {
		this.entityId = entityId;
		this.evtSrv = EventService.getInstance(core);
		
		// initialize with default keys
		this.axis[0] = KeyCode.KEY_LEFT;
		this.axis[1] = KeyCode.KEY_RIGHT;
		this.axis[2] = KeyCode.KEY_UP;
		this.axis[3] = KeyCode.KEY_DOWN;
	}
	
	public void setAxisKeys(int left, int right, int up, int down) {
		this.axis[0] = left;
		this.axis[1] = right;
		this.axis[2] = up;
		this.axis[3] = down;
	}

	public void addButton(int keyCode) {
		this.buttons.add(keyCode);
	}
	
	@Override
	public void engage() {
		this.evtSrv.addListener(this, KeyPressedEvent.TYPE_ID);
		this.evtSrv.addListener(this, KeyReleasedEvent.TYPE_ID);
		this.engaged = true;
		for (int i = 0; i < this.keyStates.length; ++i) {
			this.keyStates[i] = false;
		}
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
		for (int i = 0; i < axis.length; ++i) {
			if (keyCode == this.axis[i]) {
				this.keyStates[i] = pressed;
			}
		}
		
		if (keyCode == this.axis[0] || keyCode == this.axis[1]) {
			if (this.keyStates[0] && this.keyStates[1] || !this.keyStates[0] && !this.keyStates[1]) {
				this.evtSrv.dispatchEvent(new ControllerComponent.HorizontalUpdateEvent(this.entityId, 0.0));
			} else if (keyStates[0]) {
				this.evtSrv.dispatchEvent(new ControllerComponent.HorizontalUpdateEvent(this.entityId, -1.0));				
			} else {
				this.evtSrv.dispatchEvent(new ControllerComponent.HorizontalUpdateEvent(this.entityId, 1.0));								
			}
		} else if (keyCode == axis[2] || keyCode == this.axis[3]) {
			if (this.keyStates[2] && this.keyStates[3] || !this.keyStates[2] && !this.keyStates[3]) {
				this.evtSrv.dispatchEvent(new ControllerComponent.VerticalUpdateEvent(this.entityId, 0.0));
			} else if (keyStates[3]) {
				this.evtSrv.dispatchEvent(new ControllerComponent.VerticalUpdateEvent(this.entityId, -1.0));				
			} else {
				this.evtSrv.dispatchEvent(new ControllerComponent.VerticalUpdateEvent(this.entityId, 1.0));								
			}
		} else {
			int idx = this.buttons.indexOf(keyCode);
			if (idx >= 0) {
				this.evtSrv.dispatchEvent(new ControllerComponent.ButtonlUpdateEvent(this.entityId, pressed, idx));
			}
		}
	}
}
