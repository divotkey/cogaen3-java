package org.cogaen.lwjgl.action;

import org.cogaen.action.BasicAction;
import org.cogaen.core.Core;
import org.cogaen.lwjgl.scene.NodeReceiver;
import org.cogaen.lwjgl.scene.SceneService;

public class CreateNodeAction extends BasicAction {

	private NodeReceiver receiver;
	
	public CreateNodeAction(Core core, NodeReceiver receiver) {
		super(core);
		this.receiver = receiver;
	}
	
	public CreateNodeAction nodeReceiver(NodeReceiver receiver) {
		this.receiver = receiver;
		return this;
	}

	@Override
	public void execute() {
		SceneService scnSrv = SceneService.getInstance(getCore());
		this.receiver.setNode(scnSrv.createNode());
	}

}
