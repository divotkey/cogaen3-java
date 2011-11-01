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
	private static final double DEFAULT_TYPE_DELAY = 0.015;
	private MultiLineLabelVisual mll;
	private String text;
	private int idx;
	private Timer timer;
	private double timeStamp;
	private double typeDelay = DEFAULT_TYPE_DELAY;
	private CogaenId finishedEventId = TYPING_FINISHED_EVENT_ID;
	
	public TypeWriterTask(Core core, MultiLineLabelVisual mll, String text) {
		super(core, "Type Writer");
		this.mll = mll;
		this.text = text;
		this.idx = 0;
		this.timer = TimeService.getInstance(getCore()).getTimer();
		this.timeStamp = this.timer.getTime();
	}

	@Override
	public void update() {
		if (this.timeStamp <= this.timer.getTime()) {
			this.mll.addChar(this.text.charAt(this.idx++));
			if (this.idx >= this.text.length()) {
				EventService.getInstance(getCore()).dispatchEvent(new SimpleEvent(this.finishedEventId));
				TaskService.getInstance(getCore()).destroyTask(this);
			} else {
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
