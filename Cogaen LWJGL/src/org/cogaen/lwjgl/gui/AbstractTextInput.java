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
import org.cogaen.lwjgl.input.KeyCode;
import org.cogaen.lwjgl.input.KeyPressedEvent;
import org.cogaen.lwjgl.input.KeyReleasedEvent;
import org.cogaen.lwjgl.input.MouseButtonPressedEvent;
import org.cogaen.lwjgl.scene.Color;
import org.cogaen.lwjgl.scene.ReadableColor;
import org.cogaen.lwjgl.scene.TextBlockVisual;
import org.cogaen.name.CogaenId;

public abstract class AbstractTextInput extends FrameGui implements EventListener {

	private static final double DEFAULT_GAP = 0.97;
	public static final CogaenId DEFAULT_PRESSED_EVENT_ID = new CogaenId("TextInputPressed");

	private TextBlockVisual tbv;
	private String fontRes;
	private double gap = DEFAULT_GAP;
	private CogaenId pressedEventId;

	public AbstractTextInput(Core core, String fontRes, double width, double height, int referenceResolution) {
		super(core, width, height, referenceResolution);
		this.fontRes = fontRes;
	}

	protected abstract double getScale();
	protected abstract boolean isHit(double x, double y);
	
	@Override
	public void engage() {
		super.engage();

		EventService evtSrv = EventService.getInstance(getCore());
		evtSrv.addListener(this, KeyPressedEvent.TYPE_ID);
		evtSrv.addListener(this, KeyReleasedEvent.TYPE_ID);
		evtSrv.addListener(this, MouseButtonPressedEvent.TYPE_ID);
		
		this.tbv = new TextBlockVisual(getCore(), fontRes, getWidth() * this.gap / getScale(), getHeight() * this.gap / getScale());
		this.tbv.setScale(getScale());
		this.tbv.setMask(getMask());
		
		getBaseNode().addVisual(this.tbv);
	}

	@Override
	public void disengage() {
		EventService.getInstance(getCore()).removeListener(this);
		super.disengage();
	}

	@Override
	public void setDisabled(boolean value) {
		if (isDisabled() == value) {
			return;
		}
		
		EventService evtSrv = EventService.getInstance(getCore());
		if (value) {
			evtSrv.removeListener(this, KeyPressedEvent.TYPE_ID);
			evtSrv.removeListener(this, KeyReleasedEvent.TYPE_ID);
			this.tbv.setShowCursor(false);
		} else {
			evtSrv.addListener(this, KeyPressedEvent.TYPE_ID);
			evtSrv.addListener(this, KeyReleasedEvent.TYPE_ID);
			this.tbv.setShowCursor(true);
		}
		super.setDisabled(value);
	}

	@Override
	public void setVisible(boolean value) {
		if (isVisible() == value) {
			return;
		}
		this.tbv.setMask(value ? 0xFFFF : 0x0000);
		super.setVisible(value);
	}
	
	public void setTextColor(ReadableColor color) {
		this.tbv.setColor(color);
	}
	
	public Color getTextColor() {
		return this.tbv.getColor();
	}
	
	@Override
	public void handleEvent(Event event) {
		if (!isEngaged()) {
			return;
		}
		if (event.isOfType(KeyPressedEvent.TYPE_ID)) {
			handleKeyPressed((KeyPressedEvent) event);
		} else if (event.isOfType(KeyReleasedEvent.TYPE_ID)) {
			handleKeyReleased((KeyReleasedEvent) event);
		} else if (event.isOfType(MouseButtonPressedEvent.TYPE_ID)) {
			handleMouseButtonPressed((MouseButtonPressedEvent) event);
		}
	}
	
	private void handleMouseButtonPressed(MouseButtonPressedEvent event) {
		if (isHit(event.getPosX(), event.getPosY())) {
			EventService.getInstance(getCore()).dispatchEvent(new SimpleEvent(this.pressedEventId));
		}
	}
	
	private void handleKeyReleased(KeyReleasedEvent event) {
		// intentionaly left empty
	}

	private void handleKeyPressed(KeyPressedEvent event) {
		if (event.getCharacter() != 0 && event.getKeyCode() != KeyCode.KEY_BACK && event.getKeyCode() != KeyCode.KEY_ESC) {
			this.tbv.addChar(event.getCharacter());
			return;
		}
		
		switch (event.getKeyCode()) {			
		case KeyCode.KEY_BACK:
			this.tbv.back();
			break;
			
		case KeyCode.KEY_LEFT:
			this.tbv.left();
			break;
			
		case KeyCode.KEY_RIGHT:
			this.tbv.right();
			break;
			
		case KeyCode.KEY_UP:
			this.tbv.up();
			break;
			
		case KeyCode.KEY_DOWN:
			this.tbv.down();
			break;
		}
	}

	public void setText(String text) {
		this.tbv.setText(text);
	}
	
	public String getText() {
		return this.tbv.getText();
	}

	public double getGap() {
		return gap;
	}

	public void setGap(double gap) {
		this.gap = gap;
	}

	public CogaenId getPressedEventId() {
		return pressedEventId;
	}

	public void setPressedEventId(CogaenId pressedEventId) {
		this.pressedEventId = pressedEventId;
	}	
}
