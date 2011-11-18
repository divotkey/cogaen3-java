package org.cogaen.lwjgl.util;

import org.cogaen.core.Core;
import org.cogaen.event.Event;
import org.cogaen.event.EventListener;
import org.cogaen.event.EventService;
import org.cogaen.event.SimpleEvent;
import org.cogaen.lwjgl.input.KeyPressedEvent;
import org.cogaen.lwjgl.input.MouseButtonPressedEvent;
import org.cogaen.lwjgl.scene.Color;
import org.cogaen.lwjgl.scene.RectangleVisual;
import org.cogaen.lwjgl.scene.SceneNode;
import org.cogaen.lwjgl.scene.SceneService;
import org.cogaen.lwjgl.scene.SpriteHandle;
import org.cogaen.lwjgl.scene.SpriteVisual;
import org.cogaen.lwjgl.scene.TextureHandle;
import org.cogaen.lwjgl.scene.Visual;
import org.cogaen.lwjgl.scene.VisualFadeTask;
import org.cogaen.name.CogaenId;
import org.cogaen.resource.ResourceService;
import org.cogaen.task.TaskService;
import org.cogaen.view.View;

public class CogaenSplashView extends View implements EventListener {

	private static final double FADE_TIME = 1.0;
	private static final double DISPLAY_TIME = 10.0;
	private static final CogaenId FADE_IN_FINISHED = new CogaenId("SplashFadeInFinished");
	private static final CogaenId FADE_OUT_FINISHED = new CogaenId("SplashFadeOutFinished");
	private static final CogaenId END_OF_SPLASH = new CogaenId("EndOfSplashDisplay");;
	private SpriteVisual splash;
	private VisualFadeTask fadeIn;
	private Visual fade;
	private boolean busy;
	
	public CogaenSplashView(Core core) {
		super(core);
	}

	@Override
	public void registerResources(CogaenId groupId) {
		super.registerResources(groupId);
		ResourceService resSrv = ResourceService.getInstance(getCore());
		resSrv.declareResource("CogaenSplashTex", groupId, new TextureHandle("PNG", "cogaen_mit_800x600.jpg"));
		resSrv.declareResource("CogaenSplashSpr", groupId, new SpriteHandle("CogaenSplashTex"));
	}

	@Override
	public void engage() {
		super.engage();
		EventService evtSrv = EventService.getInstance(getCore());
		evtSrv.addListener(this, KeyPressedEvent.TYPE_ID);
		evtSrv.addListener(this, MouseButtonPressedEvent.TYPE_ID);
		evtSrv.addListener(this, FADE_IN_FINISHED);
		evtSrv.addListener(this, FADE_OUT_FINISHED);
		evtSrv.addListener(this, END_OF_SPLASH);
		
		SceneService scnSrv = SceneService.getInstance(getCore());		
		SceneNode node = scnSrv.createNode();
		node.setPose(0.5, 0.5 / scnSrv.getAspectRatio(), 0);
		this.splash = (SpriteVisual) ResourceService.getInstance(getCore()).getResource("CogaenSplashSpr");
		if (scnSrv.getAspectRatio() >= 1.0) {
			double height = 1.0 / scnSrv.getAspectRatio();
			this.splash.setSize(height * 4.0 / 3.0, height);
		} else {
			this.splash.setSize(1.0, 0.75);
		}
		node.addVisual(splash);
		scnSrv.getOverlayRoot().addNode(node);
		
		
		RectangleVisual rec = new RectangleVisual(1.0, 1.0 / scnSrv.getAspectRatio());
		this.fade = rec;
		this.fade.setColor(Color.BLACK);
		node.addVisual(this.fade);
		this.fadeIn = new VisualFadeTask(getCore(), this.fade, FADE_TIME, 1.0, 0.0);
		this.fadeIn.setFinishedEventId(FADE_IN_FINISHED);
		TaskService.getInstance(getCore()).attachTask(this.fadeIn);
		this.busy = false;
	}

	@Override
	public void disengage() {
		EventService.getInstance(getCore()).removeListener(this);
		SceneService.getInstance(getCore()).destroyAll();
		super.disengage();
	}

	@Override
	public void handleEvent(Event event) {		
		if (event.isOfType(KeyPressedEvent.TYPE_ID)) {
			fadeOut();
		} else if (event.isOfType(MouseButtonPressedEvent.TYPE_ID)) {
			fadeOut();
		} else if (event.isOfType(FADE_IN_FINISHED)) {
			EventService.getInstance(getCore()).dispatchEvent(new SimpleEvent(END_OF_SPLASH), DISPLAY_TIME);
		} else if (event.isOfType(FADE_OUT_FINISHED)) {
			System.out.println("fade out finished");
			EventService.getInstance(getCore()).dispatchEvent(new SimpleEvent(CogaenSplashState.END_OF_COGAEN_SPLASH));
		} else if (event.isOfType(END_OF_SPLASH)) {
			fadeOut();
		}
	}
	
	private void fadeOut() {
		if (this.busy) {
			return;
		}
		VisualFadeTask fadeOut = new VisualFadeTask(getCore(), this.fade, FADE_TIME, 1.0);
		fadeOut.setFinishedEventId(FADE_OUT_FINISHED);
		if (this.fadeIn != null) {
			TaskService.getInstance(getCore()).destroyTask(this.fadeIn);
		}
		TaskService.getInstance(getCore()).attachTask(fadeOut);
		this.busy = true;
	}

}
