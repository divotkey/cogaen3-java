package org.cogaen.lwjgl.scene;

import org.cogaen.math.Vector2;

public class LocalToGlobal {

	private Vector2 v = new Vector2();
	
	public void transform(SceneNode node) {
		if (node == null) {
			v.set(0, 0);
			return;
		}

		transform(node.getParent());
		v.rotate(node.getAngle());
		this.v.x += node.getPosX();
		this.v.y += node.getPosY();
	}
	
	public double getGlobalX() {
		return this.v.x;
	}
	
	public double getGlobalY() {
		return this.v.y;
	}
}
