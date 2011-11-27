package org.cogaen.lwjgl.util;

import org.cogaen.core.CogaenBase;
import org.cogaen.core.Core;
import org.cogaen.event.Event;
import org.cogaen.event.EventListener;
import org.cogaen.event.EventService;
import org.cogaen.event.SimpleEvent;
import org.cogaen.lwjgl.scene.Color;
import org.cogaen.lwjgl.scene.RectangleVisual;
import org.cogaen.lwjgl.scene.SceneNode;
import org.cogaen.lwjgl.scene.SceneService;
import org.cogaen.lwjgl.scene.VisualFadeTask;
import org.cogaen.name.CogaenId;
import org.cogaen.task.TaskService;

public class Fader extends CogaenBase implements EventListener {

	private static final CogaenId FADE_IN_FINISHED = new CogaenId("FaderFadeInFinished");
	private static final CogaenId FADE_OUT_FINISHED = new CogaenId("FaderFadeOutFinished");
	private static final double DEFAULT_FADE_TIME = 1.0;
	private SceneNode baseNode;
	private RectangleVisual cover;
	private VisualFadeTask fadeIn;
	private VisualFadeTask fadeOut;
	private double fadeTime;
	private CogaenId fadeFinishedId;

	public Fader(Core core, double fadeTime) {
		super(core);
		this.fadeTime = fadeTime;
	}
	
	public Fader(Core core) {
		this(core, DEFAULT_FADE_TIME);
	}

	@Override
	public void engage() {
		super.engage();
		EventService evtSrv = EventService.getInstance(getCore());
		evtSrv.addListener(this, FADE_IN_FINISHED);
		evtSrv.addListener(this, FADE_OUT_FINISHED);
		
		SceneService scnSrv = SceneService.getInstance(getCore());
		this.baseNode = scnSrv.createNode();
		
		this.cover = new RectangleVisual(1.0, 1.0 / scnSrv.getAspectRatio());
		this.cover.setColor(Color.BLACK);
		this.cover.setFilled(true);
		this.baseNode.addVisual(this.cover);
		this.baseNode.setPose(0.5, 0.5 / scnSrv.getAspectRatio(), 0);
		
		scnSrv.getOverlayRoot().addNode(this.baseNode);
	}

	@Override
	public void disengage() {
		EventService.getInstance(getCore()).removeListener(this);
		SceneService.getInstance(getCore()).destroyNode(this.baseNode);
		super.disengage();
	}
	
	public void fadeIn() {
		if (this.fadeIn != null) {
			return;
		}
		
		if (this.fadeOut != null) {
			TaskService.getInstance(getCore()).destroyTask(this.fadeOut);
			this.fadeOut = null;
			this.fadeIn = new VisualFadeTask(getCore(), this.cover, this.fadeTime, 0.0);
		} else {
			this.cover.setMask(0xFFFF);
			this.fadeIn = new VisualFadeTask(getCore(), this.cover, this.fadeTime, 1.0, 0.0);
		}
		this.fadeIn.setFinishedEventId(FADE_IN_FINISHED);
		TaskService.getInstance(getCore()).attachTask(this.fadeIn);
	}

	
	public void fadeOut() {
		if (this.fadeOut != null) {
			return;
		}
		
		if (this.fadeIn != null) {
			TaskService.getInstance(getCore()).destroyTask(this.fadeIn);
			this.fadeIn = null;
			this.fadeOut = new VisualFadeTask(getCore(), this.cover, this.fadeTime, 1.0);
		} else {
			this.cover.setMask(0xFFFF);
			this.fadeOut = new VisualFadeTask(getCore(), this.cover, this.fadeTime, 0.0, 1.0);
		}
		this.fadeOut.setFinishedEventId(FADE_OUT_FINISHED);
		TaskService.getInstance(getCore()).attachTask(this.fadeOut);
	}

	@Override
	public void handleEvent(Event event) {
		if (event.isOfType(FADE_IN_FINISHED)) {
			this.fadeIn = null;
			if (this.fadeFinishedId != null) {
				EventService.getInstance(getCore()).dispatchEvent(new SimpleEvent(this.fadeFinishedId));
			}
			this.cover.setMask(0x0000);
		} else if (event.isOfType(FADE_OUT_FINISHED)) {
			this.fadeOut = null;
			if (this.fadeFinishedId != null) {
				EventService.getInstance(getCore()).dispatchEvent(new SimpleEvent(this.fadeFinishedId));
			}
			this.cover.setMask(0x0000);
		}
	}

	public CogaenId getFadeFinishedId() {
		return fadeFinishedId;
	}

	public void setFadeFinishedId(CogaenId fadeFinishedId) {
		this.fadeFinishedId = fadeFinishedId;
	}
	
}
