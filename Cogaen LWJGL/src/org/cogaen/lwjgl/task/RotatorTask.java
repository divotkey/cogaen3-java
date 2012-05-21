package org.cogaen.lwjgl.task;

import org.cogaen.core.Core;
import org.cogaen.lwjgl.scene.NodeReceiver;
import org.cogaen.lwjgl.scene.SceneNode;
import org.cogaen.task.AbstractTask;
import org.cogaen.time.TimeService;
import org.cogaen.time.Timer;

public class RotatorTask extends AbstractTask implements NodeReceiver {

	private SceneNode node;
	private Timer timer;
	private double speed = 1.0;

	public RotatorTask(Core core) {
		this(core, null);
	}
	
	public RotatorTask(Core core, SceneNode node) {
		super(core, "Rotator");
		setNode(node);
		setTimer(TimeService.getInstance(getCore()).getTimer());
	}
	
	public RotatorTask speed(double speed) {
		setSpeed(speed);
		return this;
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

	public double getSpeed() {
		return speed;
	}

	public final void setSpeed(double speed) {
		this.speed = speed;
	}

	@Override
	public final void setNode(SceneNode node) {
		this.node = node;
	}
	
	public SceneNode getNode() {
		return this.node;
	}

}
