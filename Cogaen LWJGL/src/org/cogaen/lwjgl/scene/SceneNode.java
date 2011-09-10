package org.cogaen.lwjgl.scene;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class SceneNode {

	private List<Visual> visuals = new ArrayList<Visual>();
	private List<SceneNode> nodes = new ArrayList<SceneNode>();
	private SceneNode parent;
	private double posX;
	private double posY;
	private double angle;
	
	public void setPose(double x, double y, double angle) {
		this.posX = x;
		this.posY = y;
		this.angle = angle;
	}
	
	public SceneNode getParent() {
		return this.parent;
	}
	
	public void addVisual(Visual visual) {
		this.visuals.add(visual);
	}
	
	public void removeVisual(Visual visual) {
		this.visuals.remove(visual);
	}
	
	public void addNode(SceneNode node) {
		this.nodes.add(node);
		node.parent = this;
	}
	
	public boolean removeNode(SceneNode node) {
		if (this.nodes.remove(node)) {
			node.parent = null;
			return true;
		}
		
		for (SceneNode child : this.nodes) {
			if (child.removeNode(node)) {
				return true;
			}
		}
		
		return false;
	}
	
	public void render() {
		
		GL11.glPushMatrix();
		GL11.glTranslated(this.posX, this.posY, 0.0);
		GL11.glRotatef((float) this.angle, 0, 0, 1);
		
		for (Visual visual : this.visuals) {
			visual.render();
		}
		
		for (SceneNode node : this.nodes) {
			node.render();
		}
		
		GL11.glPopMatrix();
	}

	public void removeAllNodes() {
		this.nodes.clear();
	}
}
