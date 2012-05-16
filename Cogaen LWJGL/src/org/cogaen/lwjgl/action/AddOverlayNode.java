package org.cogaen.lwjgl.action;

import org.cogaen.action.BasicAction;
import org.cogaen.core.Core;
import org.cogaen.lwjgl.scene.SceneNode;
import org.cogaen.lwjgl.scene.SceneService;

public class AddOverlayNode extends BasicAction {

	private SceneNode node;
	
	public AddOverlayNode(Core core, SceneNode node) {
		super(core);
		this.node = node;
	}

	@Override
	public void execute() {
		SceneService.getInstance(getCore()).getOverlayRoot().addNode(this.node);
	}

}
