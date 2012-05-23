package org.cogaen.lwjgl.ngui;

import org.cogaen.core.Core;
import org.cogaen.lwjgl.scene.Visual;
import org.cogaen.task.AbstractTask;
import org.cogaen.time.TimeService;
import org.cogaen.time.Timer;

public class HoverTask extends AbstractTask {

	private static final double SPEED = 6.0;
	private Visual visual;
	private Timer timer;
	private double startScale;
	private double pos = Math.PI * 1.5;
	private double percentage = 0.1;
	private boolean hovering;
	private boolean stopping;
	
	
	public HoverTask(Core core, Visual visual) {
		super(core);
		this.visual = visual;
		this.setTimer(TimeService.getInstance(getCore()).getTimer());
		this.startScale = visual.getScale();
		this.hovering = false;
		this.stopping = false;
	}

	@Override
	public void update() {
		if (!this.hovering) {
			return;
		}
		this.pos = this.pos + this.timer.getDeltaTime() * SPEED;
		double x = this.startScale * percentage + this.startScale * percentage * Math.sin(this.pos);
		this.visual.setScale(this.startScale + x);
		
		if (this.stopping) {
			if (Math.abs(x) < 0.0000001) {
				this.stopping = false;
				this.hovering = false;
				this.visual.setScale(this.startScale);
			}
		}
	}

	public void start() {
		this.hovering = true;
		this.stopping = false;
	}
	
	public void stop() {
		this.stopping = true;
	}
	
	@Override
	public void destroy() {
		this.visual.setScale(this.startScale);
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

}
