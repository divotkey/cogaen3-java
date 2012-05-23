package org.cogaen.lwjgl.ngui;

import org.cogaen.core.CogaenBase;
import org.cogaen.core.Core;
import org.cogaen.lwjgl.scene.NodeReceiver;
import org.cogaen.lwjgl.scene.Pose;
import org.cogaen.lwjgl.scene.SceneNode;
import org.cogaen.lwjgl.scene.SceneService;

public class BaseGui extends CogaenBase implements NodeReceiver {

	private NodeReceiver nodeReceiver;
	private SceneNode node;
	private Pose pose = new Pose();

	public BaseGui(Core core) {
		super(core);
		setNodeReceiver(this);
	}
	
	@Override
	public void engage() {
		super.engage();
		setBaseNode(SceneService.getInstance(getCore()).createNode());
		getBaseNode().setPose(this.pose);
	}

	@Override
	public void disengage() {
		SceneService.getInstance(getCore()).destroyNode(this.node);
		this.node = null;
		super.disengage();
	}

	public BaseGui nodeReceiver(NodeReceiver receiver) {
		setNodeReceiver(receiver);
		return this;
	}
	
	public final void setNodeReceiver(NodeReceiver receiver) {
		this.nodeReceiver = receiver;
	}

	public final void setBaseNode(SceneNode node) {
		this.node = node;
		if (this.nodeReceiver != null) {
			this.nodeReceiver.setNode(this.node);
		}
	}
	
	public final SceneNode getBaseNode() {
		return this.node;
	}
	
	public final void setPose(Pose pose) {
		setPose(pose.x, pose.y, pose.angle);
	}
	
	public final void setPose(double x, double y, double angle) {
		this.pose.set(x, y, angle);
		if (isEngaged()) {
			getBaseNode().setPose(this.pose);
		}
	}
	
	public BaseGui pose(Pose pose) {
		setPose(pose);
		return this;
	}
	
	public BaseGui pose(double x, double y, double angle) {
		setPose(x, y, angle);
		return this;
	}

	@Override
	public final void setNode(SceneNode node) {
		SceneService.getInstance(getCore()).getOverlayRoot().addNode(node);
	}
	
}
