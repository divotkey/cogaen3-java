package org.cogaen.lwjgl.scene;

import org.cogaen.core.Core;
import org.cogaen.event.EventService;
import org.cogaen.event.SimpleEvent;
import org.cogaen.name.CogaenId;
import org.cogaen.task.AbstractTask;
import org.cogaen.task.TaskService;
import org.cogaen.time.TimeService;
import org.cogaen.time.Timer;

public class VisualFadeOutTask extends AbstractTask {

	public static final CogaenId FADE_OUT_FINISHED_EVENT_ID = new CogaenId("FadeOutFinished");
	
	private Visual visual;
	private double fadeTime;
	private Timer timer;
	private double startTime;
	private CogaenId finishedEventId = FADE_OUT_FINISHED_EVENT_ID;
	private double startAlpha;

	public VisualFadeOutTask(Core core, Visual visual, double fadeTime) {
		this(core, visual, fadeTime, 1.0);
	}
	
	public VisualFadeOutTask(Core core, Visual visual, double fadeTime, double startAlpha) {
		super(core, "Fade-out");
		this.visual = visual;
		this.fadeTime = fadeTime;
		this.startAlpha = startAlpha;
		this.visual.getColor().setAlpha(this.startAlpha);
		
		this.timer = TimeService.getInstance(core).getTimer();
		this.startTime = this.timer.getTime();
	}

	@Override
	public void update() {
		double elapsed = this.timer.getTime() - this.startTime;
		if (elapsed >= fadeTime) {
			this.visual.getColor().setAlpha(0.0);
			TaskService.getInstance(getCore()).destroyTask(this);
			if (this.finishedEventId != null) {
				EventService.getInstance(getCore()).dispatchEvent(new SimpleEvent(this.finishedEventId));
			}
			return;
		}
		
		double p = elapsed / this.fadeTime;
		this.visual.getColor().setAlpha(this.startAlpha + p * -this.startAlpha);
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

	public double getCurrentAlpha() {
		return this.visual.getColor().getAlpha();
	}
}
