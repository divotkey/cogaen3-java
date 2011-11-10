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

import org.cogaen.entity.Component;
import org.cogaen.entity.ComponentEntity;
import org.cogaen.event.Event;
import org.cogaen.event.EventListener;
import org.cogaen.event.EventService;
import org.cogaen.name.CogaenId;

public class ControllerComponent extends Component implements ControllerState, EventListener {

	private double hPos;
	private double vPos;
	private boolean buttons[];
	
	public ControllerComponent(int nButtons) {
		this.buttons = new boolean[nButtons];
	}
	
	@Override
	public void initialize(ComponentEntity parent) {
		super.initialize(parent);
		parent.addAttribute(ControllerState.ID, this);
	}

	@Override
	public void engage() {
		super.engage();
		EventService.getInstance(getCore()).addListener(this, UpdateEvent.TYPE_ID);
	}

	@Override
	public void disengage() {
		EventService.getInstance(getCore()).removeListener(this, UpdateEvent.TYPE_ID);
		super.disengage();
	}
		
	@Override
	public void handleEvent(Event event) {
		if (!event.isOfType(UpdateEvent.TYPE_ID)) {
			return;
		}
		
		UpdateEvent update = (UpdateEvent) event;
		if (update.getEntityId().equals(getParent().getId())) {
			update.updateController(this);
		}
	}
	
	public double getVerticalPosition() {
		return this.vPos;
	}
	
	public double getHorizontalPosition() {
		return this.hPos;
	}
	
	public boolean getButton(int idx) {
		return this.buttons[idx];
	}
	
	public static abstract class UpdateEvent extends Event {

		public static final CogaenId TYPE_ID = new CogaenId("ControllerComponentUpdate");
		private CogaenId entityId;
		
		public UpdateEvent(CogaenId entityId) {
			this.entityId = entityId;
		}
		
		@Override
		public CogaenId getTypeId() {
			return TYPE_ID;
		}
		
		public CogaenId getEntityId() {
			return this.entityId;
		}
		
		public abstract void updateController(ControllerComponent comp);
	}
		
	public static class VerticalUpdateEvent extends UpdateEvent {
		
		private double vPos;
		
		public VerticalUpdateEvent(CogaenId entityId, double vPos) {
			super(entityId);
			this.vPos = vPos;
		}

		@Override
		public void updateController(ControllerComponent comp) {
			comp.vPos = this.vPos;
		}
		
	}
	
	public static class HorizontalUpdateEvent extends UpdateEvent {
		
		private double hPos;
		
		public HorizontalUpdateEvent(CogaenId entityId, double hPos) {
			super(entityId);
			this.hPos = hPos;
		}

		@Override
		public void updateController(ControllerComponent comp) {
			comp.hPos = this.hPos;
		}
		
	}
	
	public static class ButtonlUpdateEvent extends UpdateEvent {
		
		private boolean buttonState;
		private int idx;
		
		public ButtonlUpdateEvent(CogaenId entityId, boolean buttonState, int idx) {
			super(entityId);
			this.buttonState = buttonState;
			this.idx = idx;
		}

		@Override
		public void updateController(ControllerComponent comp) {
			comp.buttons[this.idx] = this.buttonState;
		}
		
	}
}
