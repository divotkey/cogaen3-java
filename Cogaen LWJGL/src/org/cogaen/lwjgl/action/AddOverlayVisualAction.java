package org.cogaen.lwjgl.action;

import org.cogaen.action.BasicAction;
import org.cogaen.core.Core;
import org.cogaen.lwjgl.scene.SceneNode;
import org.cogaen.lwjgl.scene.SceneService;
import org.cogaen.lwjgl.scene.Visual;

public class AddOverlayVisualAction extends BasicAction {

	private Visual visual;
	private double x;
	private double y;
	private double angle;
	
	public AddOverlayVisualAction(Core core, Visual visual, double x, double y) {
		this(core, visual, x, y, 0);
	}

	public AddOverlayVisualAction(Core core, Visual visual, double x, double y, double angle) {
		super(core);
		this.visual = visual;
		this.x = x;
		this.y = y;
		this.angle = angle;
	}
	
	@Override
	public void execute() {
		SceneService scnSrv = SceneService.getInstance(getCore());
		SceneNode node = scnSrv.createNode();
		node.addVisual(this.visual);
		node.setPose(this.x, this.y, this.angle);
		scnSrv.getOverlayRoot().addNode(node);
	}
}
