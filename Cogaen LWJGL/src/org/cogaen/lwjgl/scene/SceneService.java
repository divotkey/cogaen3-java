package org.cogaen.lwjgl.scene;

import org.cogaen.core.AbstractService;
import org.cogaen.core.Core;
import org.cogaen.name.CogaenId;

public class SceneService extends AbstractService {

	public static final CogaenId ID = new CogaenId("org.cogaen.lwjgl.scene.SceneService");
	public static final String NAME = "Cogaen LWJGL Scene Service";
	public static final String LOGGING_SOURCE = "LWSC";
	
	public static SceneService getInstance(Core core) {
		return (SceneService) core.getService(ID);
	}
	
	@Override
	public CogaenId getId() {
		return ID;
	}

	@Override
	public String getName() {
		return NAME;
	}

}
