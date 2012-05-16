package org.cogaen.lwjgl.task;

import org.cogaen.core.Core;
import org.cogaen.lwjgl.scene.SceneNode;
import org.cogaen.task.AbstractTask;
import org.cogaen.time.TimeService;
import org.cogaen.time.Timer;

public class RotatorTask extends AbstractTask {

	private SceneNode node;
	private Timer timer;
	private double speed;

	public RotatorTask(Core core, SceneNode node) {
		this(core, node, 1.0);
	}
	
	public RotatorTask(Core core, SceneNode node, double speed) {
		super(core, "Rotator");
		this.node = node;
		this.speed = speed;
		this.setTimer(TimeService.getInstance(getCore()).getTimer());
	}

	@Override
	public void update() {
		this.node.setPose(this.node.getPosX(), this.node.getPosY(), this.node.getAngle() + this.speed * this.timer.getDeltaTime());
	}

	@Override
	public void destroy() {
		// intentionally left empty
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

}
