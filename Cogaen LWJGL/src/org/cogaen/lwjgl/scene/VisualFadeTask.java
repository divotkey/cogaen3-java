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

public class VisualFadeTask extends AbstractTask {

	public static final CogaenId FADE_FINISHED_EVENT_ID = new CogaenId("FadeFinished");
	
	private Visual visual;
	private double fadeTime;
	private Timer timer;
	private double startTime;
	private CogaenId finishedEventId = FADE_FINISHED_EVENT_ID;
	private double targetAlpha = 1.0;
	private double startAlpha = 0.0;

	public VisualFadeTask(Core core, Visual visual, double fadeTime, double targetAlpha) {
		this(core, visual, fadeTime, visual.getColor().getAlpha(), targetAlpha);
	}
	
	public VisualFadeTask(Core core, Visual visual, double fadeTime, double startAlpha, double targetAlpha) {
		super(core, "Visual Fade");
		this.visual = visual;
		this.fadeTime = fadeTime;
		this.startAlpha = startAlpha;
		this.targetAlpha = targetAlpha;
		this.visual.getColor().setAlpha(this.startAlpha);
		
		this.timer = TimeService.getInstance(core).getTimer();
		this.startTime = this.timer.getTime();
	}

	@Override
	public void update() {
		double elapsed = this.timer.getTime() - this.startTime;
		if (elapsed >= fadeTime) {
			this.visual.getColor().setAlpha(this.targetAlpha);
			TaskService.getInstance(getCore()).destroyTask(this);
			if (this.finishedEventId != null) {
				EventService.getInstance(getCore()).dispatchEvent(new SimpleEvent(this.finishedEventId));
			}
			return;
		}

		double range =  this.targetAlpha - this.startAlpha;
		double p = elapsed / this.fadeTime;
		this.visual.getColor().setAlpha(this.startAlpha + range * p);
	}

	@Override
	public void destroy() {
		// intentionally left empty
	}

	public CogaenId getFinishedEventId() {
		return finishedEventId;
	}

	public void setFinishedEventId(CogaenId finishedEventId) {
		this.finishedEventId = finishedEventId;
	}

	public double getTargetAlpha() {
		return targetAlpha;
	}

	public void setTargetAlpha(double targetAlpha) {
		this.targetAlpha = targetAlpha;
	}

	public double getStartAlpha() {
		return startAlpha;
	}

	public void setStartAlpha(double startAlpha) {
		this.startAlpha = startAlpha;
	}
}
