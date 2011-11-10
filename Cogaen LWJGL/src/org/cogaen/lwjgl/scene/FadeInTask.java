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

public class FadeInTask extends AbstractTask {

	public static final CogaenId FADE_IN_FINISHED_EVENT_ID = new CogaenId("FadeInFinished");
	
	private Visual visual;
	private double fadeTime;
	private Timer timer;
	private double startTime;
	private CogaenId finishedEventId = FADE_IN_FINISHED_EVENT_ID;
	
	public FadeInTask(Core core, Visual visual, double fadeTime) {
		super(core, "Fade-in");
		this.visual = visual;
		this.visual.getColor().setAlpha(0.0);
		this.fadeTime = fadeTime;
		
		this.timer = TimeService.getInstance(core).getTimer();
		this.startTime = this.timer.getTime();
	}

	@Override
	public void update() {
		double elapsed = this.timer.getTime() - this.startTime;
		if (elapsed >= fadeTime) {
			TaskService.getInstance(getCore()).destroyTask(this);
			return;
		}
		
		double alpha = elapsed / this.fadeTime;
		this.visual.getColor().setAlpha(alpha);
	}

	@Override
	public void destroy() {
		this.visual.getColor().setAlpha(1.0);
		if (this.finishedEventId != null) {
			EventService.getInstance(getCore()).dispatchEvent(new SimpleEvent(this.finishedEventId));
		}
	}

	public CogaenId getFinishedEventId() {
		return finishedEventId;
	}

	public void setFinishedEventId(CogaenId finishedEventId) {
		this.finishedEventId = finishedEventId;
	}
}
