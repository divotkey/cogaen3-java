package org.cogaen.lwjgl.gui;

import org.cogaen.core.Core;
import org.cogaen.event.Event;
import org.cogaen.event.EventListener;
import org.cogaen.event.EventService;
import org.cogaen.event.SimpleEvent;
import org.cogaen.lwjgl.input.MouseButtonPressedEvent;
import org.cogaen.lwjgl.input.MouseButtonReleasedEvent;
import org.cogaen.lwjgl.input.MouseUpdateEvent;
import org.cogaen.lwjgl.scene.SpriteVisual;
import org.cogaen.name.CogaenId;
import org.cogaen.resource.ResourceService;

public abstract class AbstractImageButton extends Gui implements EventListener {

	public static final CogaenId DEFAULT_PRESSED_EVENT_ID = new CogaenId("ButtonPressed");
	private static final double MOUSE_OVER_RESCALE = 1.3;
	private static final double PRESSED_EVENT_DELAY = 0.05;
	private String normalRes;
	private String selectedRes;
	private SpriteVisual normal;
	private SpriteVisual selected;
	private double origWidth;
	private double origHeight;
	private boolean mouseOver;
	
	private CogaenId pressedEventId = DEFAULT_PRESSED_EVENT_ID;

	public AbstractImageButton(Core core, double width, double height, String normalRes, String selectedRes) {
		super(core, width, height, Gui.DEFAULT_REFERENCE_RESOLUTION);
		this.normalRes = normalRes;
		this.selectedRes = selectedRes;
	}
	
	protected abstract boolean isHit(double x, double y);
	protected abstract double getScale();

	@Override
	public void engage() {
		super.engage();
		EventService evtSrv = EventService.getInstance(getCore());
		evtSrv.addListener(this, MouseButtonPressedEvent.TYPE_ID);
		evtSrv.addListener(this, MouseButtonReleasedEvent.TYPE_ID);
		evtSrv.addListener(this, MouseUpdateEvent.TYPE_ID);
		
		ResourceService resSrv = ResourceService.getInstance(getCore());
		this.normal = (SpriteVisual) resSrv.getResource(this.normalRes);
		this.selected = (SpriteVisual) resSrv.getResource(this.selectedRes);
		
		this.normal.setSize(getWidth(), getHeight());
		getBaseNode().addVisual(this.normal);
		this.selected.setSize(getWidth(), getHeight());
		getBaseNode().addVisual(this.selected);
		reset();
		this.mouseOver = false;
	}

	@Override
	public void disengage() {
		if (!isDisabled()) {
			EventService.getInstance(getCore()).removeListener(this);
		}
		
		super.disengage();
	}
	
	@Override
	public void handleEvent(Event event) {
		if (!isEngaged()) {
			return;
		}
		if (event.isOfType(MouseButtonPressedEvent.TYPE_ID)) {
			handleMouseButtonPressed((MouseButtonPressedEvent) event);
		} else if (event.isOfType(MouseButtonReleasedEvent.TYPE_ID)) {
			handleMouseButtonReleased((MouseButtonReleasedEvent) event);
		} else if (event.isOfType(MouseUpdateEvent.TYPE_ID)) {
			handleMouseUpdate((MouseUpdateEvent) event);
		}
	}

	private void handleMouseUpdate(MouseUpdateEvent event) {
		if (isHit(event.getPosX(), event.getPosY())) {
			if (!this.mouseOver) {
				this.mouseOver = true;
				this.origWidth = getWidth();
				this.origHeight = getHeight();
				setWidth(getWidth() * MOUSE_OVER_RESCALE);
				setHeight(getHeight() * MOUSE_OVER_RESCALE);
			}
		} else if (this.mouseOver){
			setWidth(this.origWidth);
			setHeight(this.origHeight);
			this.mouseOver = false;
		}
	}

	private void handleMouseButtonPressed(MouseButtonPressedEvent event) {
		if (isHit(event.getPosX(), event.getPosY())) {
			setSelected(true);
		}
	}

	private void handleMouseButtonReleased(MouseButtonReleasedEvent event) {
		if (isHit(event.getPosX(), event.getPosY())) {
			EventService.getInstance(getCore()).dispatchEvent(new SimpleEvent(this.pressedEventId), PRESSED_EVENT_DELAY);
		}
		setSelected(false);			
	}

	public CogaenId getPressedEventId() {
		return pressedEventId;
	}

	public void setPressedEventId(CogaenId pressedEventId) {
		this.pressedEventId = pressedEventId;
	}

	@Override
	public void setDisabled(boolean value) {
		if (isDisabled() == value) {
			return;
		}

		EventService evtSrv = EventService.getInstance(getCore());
		if (value) {
			evtSrv.removeListener(this);
			this.normal.getColor().setAlpha(0.5);
			if (this.mouseOver) {
				setWidth(this.origWidth);
				setHeight(this.origHeight);
				this.mouseOver = false;
			}
		} else {
			evtSrv.addListener(this, MouseButtonPressedEvent.TYPE_ID);
			evtSrv.addListener(this, MouseButtonReleasedEvent.TYPE_ID);
			evtSrv.addListener(this, MouseUpdateEvent.TYPE_ID);
			this.normal.getColor().setAlpha(1.0);
		}
		
		super.setDisabled(value);
	}

	@Override
	public void setVisible(boolean value) {
		if (isVisible() == value) {
			return;
		}
		
		super.setVisible(value);
		reset();
	}
	
	private void reset() {
		if (isVisible()) {
			if (!isSelected()) {
				this.normal.setMask(getMask());
				this.selected.setMask(0x000);
			} else {
				this.normal.setMask(0x000);
				this.selected.setMask(getMask());				
			}
		} else {
			this.normal.setMask(0x0000);
			this.selected.setMask(0x0000);
		}
	}
	
	@Override
	public void setMask(int mask) {
		super.setMask(mask);
		reset();
	}

	@Override
	public void setWidth(double width) {
		super.setWidth(width);
		this.normal.setSize(getWidth(), getHeight());
		this.selected.setSize(getWidth(), getHeight());
	}

	@Override
	public void setHeight(double height) {
		super.setHeight(height);
		this.normal.setSize(getWidth(), getHeight());
		this.selected.setSize(getWidth(), getHeight());
	}

	@Override
	public void setSelected(boolean value) {
		if (isSelected() == value) {
			return;
		}
		super.setSelected(value);
		reset();
	}	
	
}
