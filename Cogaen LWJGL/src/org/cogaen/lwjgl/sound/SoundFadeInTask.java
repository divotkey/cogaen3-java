package org.cogaen.lwjgl.sound;

import org.cogaen.core.Core;
import org.cogaen.event.EventService;
import org.cogaen.event.SimpleEvent;
import org.cogaen.name.CogaenId;
import org.cogaen.task.AbstractTask;
import org.cogaen.task.TaskService;
import org.cogaen.time.TimeService;
import org.cogaen.time.Timer;

public class SoundFadeInTask extends AbstractTask {
	
	public static final CogaenId FADE_IN_FINISHED_EVENT_ID = new CogaenId("SoundFadeInFinished");

	private Source source;
	private double fadeTime;
	private Timer timer;
	private double startTime;
	private CogaenId finishedEventId = FADE_IN_FINISHED_EVENT_ID;
	
	public SoundFadeInTask(Core core, Source source, double fadeTime) {
		super(core, "Sound Fade-in");
		this.source = source;
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
		
		double gain = elapsed / this.fadeTime;
		this.source.setGain(gain);
	}

	@Override
	public void destroy() {
		this.source.setGain(1.0);
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
