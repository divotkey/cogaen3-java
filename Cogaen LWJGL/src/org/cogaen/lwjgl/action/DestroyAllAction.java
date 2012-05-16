package org.cogaen.lwjgl.action;

import org.cogaen.action.BasicAction;
import org.cogaen.core.Core;
import org.cogaen.lwjgl.scene.SceneService;

public class DestroyAllAction extends BasicAction {

	public DestroyAllAction(Core core) {
		super(core);
	}

	@Override
	public void execute() {
		SceneService.getInstance(getCore()).destroyAll();
	}

}
