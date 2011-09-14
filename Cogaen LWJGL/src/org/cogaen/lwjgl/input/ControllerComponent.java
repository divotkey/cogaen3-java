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
	
	public ControllerComponent(CogaenId attributeId) {
		this(0);
	}
	
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
		update.updateController(this);
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
		
		@Override
		public CogaenId getTypeId() {
			return TYPE_ID;
		}
		
		public abstract void updateController(ControllerComponent comp);
	}
		
	public static class VerticalUpdateEvent extends UpdateEvent {
		
		private double vPos;
		
		public VerticalUpdateEvent(double vPos) {
			this.vPos = vPos;
		}

		@Override
		public void updateController(ControllerComponent comp) {
			comp.vPos = this.vPos;
		}
		
	}
	
	public static class HorizontalUpdateEvent extends UpdateEvent {
		
		private double hPos;
		
		public HorizontalUpdateEvent(double hPos) {
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
		
		
		public ButtonlUpdateEvent(boolean buttonState, int idx) {
			this.buttonState = buttonState;
			this.idx = idx;
		}

		@Override
		public void updateController(ControllerComponent comp) {
			comp.buttons[this.idx] = this.buttonState;
		}
		
	}
}