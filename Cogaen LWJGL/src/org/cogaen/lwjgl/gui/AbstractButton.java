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


package org.cogaen.lwjgl.gui;

import org.cogaen.core.Core;
import org.cogaen.event.Event;
import org.cogaen.event.EventListener;
import org.cogaen.event.EventService;
import org.cogaen.event.SimpleEvent;
import org.cogaen.lwjgl.input.MouseButtonPressedEvent;
import org.cogaen.lwjgl.input.MouseButtonReleasedEvent;
import org.cogaen.lwjgl.scene.Color;
import org.cogaen.lwjgl.scene.ReadableColor;
import org.cogaen.lwjgl.scene.TextVisual;
import org.cogaen.lwjgl.scene.Alignment;
import org.cogaen.name.CogaenId;

public abstract class AbstractButton extends FrameGui implements EventListener {

	public static final String DEFAULT_TEXT = "Ok";
	public static final CogaenId DEFAULT_PRESSED_EVENT_ID = new CogaenId("ButtonPressed");
	private static final double PRESSED_EVENT_DELAY = 0.05;
	private TextVisual text;
	private String fontRes;
	private CogaenId pressedEventId;
	private Color textColor = new Color(Color.BLACK);

	public AbstractButton(Core core, String fontRes, double width, double height, int referenceResolution) {
		super(core, width, height, referenceResolution);
		this.fontRes = fontRes;
	}
	
	public AbstractButton(Core core, String fontRes, double width, double height) {
		this(core, fontRes, width, height, Gui.DEFAULT_REFERENCE_RESOLUTION);
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
		this.text.setColor(this.textColor);
		this.text.setMask(getMask());
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
			this.text.getColor().setAlpha(0.5);
		} else {
			evtSrv.addListener(this, MouseButtonPressedEvent.TYPE_ID);
			evtSrv.addListener(this, MouseButtonReleasedEvent.TYPE_ID);
			this.text.getColor().setAlpha(1.0);
		}
		
		super.setDisabled(value);
	}

	@Override
	public void setVisible(boolean value) {
		if (isVisible() == value) {
			return;
		}
		this.text.setMask(value ? getMask() : 0x0000);
		super.setVisible(value);
	}
	
	@Override
	public void setMask(int mask) {
		super.setMask(mask);
		if (this.text != null) {
			this.text.setMask(getMask());
		}
	}

	public void setTextColor(ReadableColor color) {
		this.textColor.setColor(color);
		if (this.text != null) {
			this.text.setColor(color);
		}
	}
	
}
