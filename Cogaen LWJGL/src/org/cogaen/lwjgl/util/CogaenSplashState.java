package org.cogaen.lwjgl.util;

import org.cogaen.core.Core;
import org.cogaen.name.CogaenId;
import org.cogaen.resource.ResourceService;
import org.cogaen.state.BasicState;
import org.cogaen.view.View;

public class CogaenSplashState extends BasicState {

	public static final CogaenId ID = new CogaenId("CogaenSplash");
	public static final CogaenId END_OF_COGAEN_SPLASH = new CogaenId("EndOfCogaenSplash");
	private View view;
	
	public CogaenSplashState(Core core) {
		super(core);
		this.view = new CogaenSplashView(core);
		
		ResourceService resSrv = ResourceService.getInstance(getCore());
		resSrv.createGroup(ID);
		this.view.registerResources(ID);
	}

	@Override
	public void onEnter() {
		super.onEnter();
		ResourceService.getInstance(getCore()).loadGroup(ID);
		this.view.engage();
	}

	@Override
	public void onExit() {
		this.view.disengage();
		ResourceService.getInstance(getCore()).unloadGroup(ID);
		super.onExit();
	}

}
