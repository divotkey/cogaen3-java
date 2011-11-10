package org.cogaen.lwjgl.gui;

import org.cogaen.core.Core;
import org.cogaen.event.Event;
import org.cogaen.event.EventListener;
import org.cogaen.event.EventService;
import org.cogaen.event.SimpleEvent;
import org.cogaen.lwjgl.input.MouseButtonPressedEvent;
import org.cogaen.lwjgl.input.MouseButtonReleasedEvent;
import org.cogaen.lwjgl.scene.ReadableColor;
import org.cogaen.lwjgl.scene.TextVisual;
import org.cogaen.lwjgl.scene.Alignment;
import org.cogaen.name.CogaenId;

public abstract class AbstractButton extends FrameGui implements EventListener {

	public static final String DEFAULT_TEXT = "Ok";
	public static final CogaenId DEFAULT_PRESSED_EVENT_ID = new CogaenId("ButtonPressed");
	private static final double PRESSED_EVENT_DELAY = 0.1;
	private TextVisual text;
	private String fontRes;
	private CogaenId pressedEventId;
	
	public AbstractButton(Core core, String fontRes, double width, double height) {
		super(core, width, height);
		this.fontRes = fontRes;
	}

	protected abstract boolean isHit(double x, double y);
	protected abstract double getScale();
	
	@Override
	public void engage() {
		super.engage();
		EventService evtSrv = EventService.getInstance(getCore());
		evtSrv.addListener(this, MouseButtonPressedEvent.TYPE_ID);
		evtSrv.addListener(this, MouseButtonReleasedEvent.TYPE_ID);
		
		this.text = new TextVisual(getCore(), this.fontRes);
		this.text.setScale(getScale());
		this.text.setAllignment(Alignment.CENTER);
		this.text.setText(DEFAULT_TEXT);
		getBaseNode().addVisual(this.text);
	}

	@Override
	public void disengage() {
		if (!isDisabled()) {
			EventService.getInstance(getCore()).removeListener(this);
		}
		super.disengage();
	}
	
	public void setText(String text) {
		this.text.setText(text);
	}
	
	public void getText(String text) {
		this.text.getText();
	}

	@Override
	public void handleEvent(Event event) {
		if (event.isOfType(MouseButtonPressedEvent.TYPE_ID)) {
			handleMouseButtonPressed((MouseButtonPressedEvent) event);
		} else if (event.isOfType(MouseButtonReleasedEvent.TYPE_ID)) {
			handleMouseButtonReleased((MouseButtonReleasedEvent) event);
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
		} else {
			evtSrv.addListener(this, MouseButtonPressedEvent.TYPE_ID);
			evtSrv.addListener(this, MouseButtonReleasedEvent.TYPE_ID);
		}
		
		super.setDisabled(value);
	}

	@Override
	public void setVisible(boolean value) {
		if (isVisible() == value) {
			return;
		}
		this.text.setMask(value ? 0xFFFF : 0x0000);
		super.setVisible(value);
	}
	
	@Override
	public void setReferenceResolution(int referenceResolution) {
		super.setReferenceResolution(referenceResolution);
		if (this.text != null) {
			this.text.setScale(getScale());
		}
	}

	public void setTextColor(ReadableColor color) {
		this.text.setColor(color);
	}
	
	
	
}
