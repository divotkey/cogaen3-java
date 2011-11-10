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
import org.cogaen.task.AbstractTask;
import org.cogaen.time.TimeService;
import org.cogaen.time.Timer;

public class BlinkTask extends AbstractTask {

	private double interval;
	private Timer timer;
	private Visual visual;
	private int mask;
	private double timeStamp;
	
	public BlinkTask(Core core, Visual visual, double interval) {
		super(core, "Blink");
		this.interval = interval;
		this.timer = TimeService.getInstance(getCore()).getTimer();
		this.visual = visual;
		this.mask = this.visual.getMask();
		this.timeStamp = this.timer.getTime() + this.interval;
	}

	@Override
	public void update() {
		if (this.timeStamp <= this.timer.getTime()) {
			this.visual.setMask(this.visual.getMask() == this.mask ? 0x000 : this.mask);
			this.timeStamp = this.timer.getTime() + this.interval;
		}
	}

	@Override
	public void destroy() {
		// intentionally left empty
	}
	
}
