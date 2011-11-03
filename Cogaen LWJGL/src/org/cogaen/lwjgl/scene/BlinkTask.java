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
