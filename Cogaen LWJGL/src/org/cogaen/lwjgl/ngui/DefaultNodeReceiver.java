package org.cogaen.lwjgl.ngui;

import org.cogaen.core.Core;
import org.cogaen.lwjgl.scene.NodeReceiver;
import org.cogaen.lwjgl.scene.SceneNode;
import org.cogaen.lwjgl.scene.SceneService;

public class DefaultNodeReceiver implements NodeReceiver {

	private Core core;
	
	public DefaultNodeReceiver(Core core) {
		super();
		this.core = core;
	}

	@Override
	public void setNode(SceneNode node) {
		SceneService.getInstance(this.core).getOverlayRoot().addNode(node);
	}

}
