package org.cogaen.lwjgl.action;

import org.cogaen.action.BasicAction;
import org.cogaen.core.Core;
import org.cogaen.lwjgl.scene.NodeReceiver;
import org.cogaen.lwjgl.scene.SceneNode;
import org.cogaen.lwjgl.scene.SceneService;
import org.cogaen.lwjgl.scene.Visual;
import org.cogaen.resource.ResourceService;

public class AddOverlayVisualAction extends BasicAction {

	private double x;
	private double y;
	private double angle;
	private String resourceName;
	private NodeReceiver receiver;

	public AddOverlayVisualAction(Core core, String resourceName) {
		super(core);
		this.resourceName = resourceName;
	}
	
	public AddOverlayVisualAction position(double x, double y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public AddOverlayVisualAction angle(double angle) {
		this.angle = angle;
		return this;
	}

	public AddOverlayVisualAction receiver(NodeReceiver receiver) {
		this.receiver = receiver;
		return this;
	}
	
	@Override
	public void execute() {
		SceneService scnSrv = SceneService.getInstance(getCore());
		SceneNode node = scnSrv.createNode();
		
		node.addVisual((Visual) ResourceService.getInstance(getCore()).getResource(this.resourceName));
		node.setPose(this.x, this.y, this.angle);
		scnSrv.getOverlayRoot().addNode(node);
		if (this.receiver != null) {
			this.receiver.setNode(node);
		}
	}
}
