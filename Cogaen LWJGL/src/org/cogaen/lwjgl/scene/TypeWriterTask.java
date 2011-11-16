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

package org.cogaen.lwjgl.scene;

import org.cogaen.core.Core;
import org.cogaen.event.EventService;
import org.cogaen.event.SimpleEvent;
import org.cogaen.name.CogaenId;
import org.cogaen.task.AbstractTask;
import org.cogaen.task.TaskService;
import org.cogaen.time.TimeService;
import org.cogaen.time.Timer;

public class TypeWriterTask extends AbstractTask {
	
	public static final CogaenId TYPING_FINISHED_EVENT_ID = new CogaenId("TypingFinished");
	public static final double DEFAULT_TYPE_DELAY = 0.015;
	private Label label;
	private String text;
	private int idx;
	private Timer timer;
	private double timeStamp;
	private double typeDelay = DEFAULT_TYPE_DELAY;
	private CogaenId finishedEventId = TYPING_FINISHED_EVENT_ID;
	
	public TypeWriterTask(Core core, Label label, String text) {
		super(core, "Type Writer");
		this.label = label;
		this.text = text;
		this.idx = 0;
		this.timer = TimeService.getInstance(getCore()).getTimer();
		this.timeStamp = this.timer.getTime();
	}

	@Override
	public void update() {
		if (this.timeStamp <= this.timer.getTime()) {
			if (this.idx >= this.text.length()) {
				EventService.getInstance(getCore()).dispatchEvent(new SimpleEvent(this.finishedEventId));
				TaskService.getInstance(getCore()).destroyTask(this);
			} else {
				this.label.addChar(this.text.charAt(this.idx++));
				this.timeStamp = this.timer.getTime() + this.typeDelay;					
			}
		}
	}

	@Override
	public void destroy() {
		// intentionally left empty
	}

	public double getTypeDelay() {
		return typeDelay;
	}

	public void setTypeDelay(double typeDelay) {
		this.typeDelay = typeDelay;
	}

	public CogaenId getFinishedEventId() {
		return finishedEventId;
	}
	
	public void setFinishedEventId(CogaenId finishedEventId) {
		this.finishedEventId = finishedEventId;
	}

}
