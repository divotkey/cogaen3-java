package org.cogaen.lwjgl.scene;

import org.cogaen.core.Core;
import org.cogaen.math.Vector2;

public class WorldToView {

	private int halfWidth;
	private int halfHeight;
	private double ar;
	private Vector2 v = new Vector2();
	private Camera camera;
	
	public WorldToView(Core core, Camera camera) {
		SceneService scnSrv = SceneService.getInstance(core);
		this.halfWidth = scnSrv.getScreenWidth() / 2;
		this.halfHeight = scnSrv.getScreenHeight() / 2;
		this.ar = scnSrv.getAspectRatio();
		this.camera = camera;
	}
	
	public void transform(double x, double y) {
		this.v.set(x, y);
		this.v.x -= this.camera.getPosX();
		this.v.y -= this.camera.getPosY();
		this.v.scale(this.camera.getZoom());
		this.v.x += this.halfWidth;
		this.v.y += this.halfHeight;
	}
	
	public void transform(SceneNode node) {
		nodeTransform(node);
		this.v.x -= this.camera.getPosX();
		this.v.y -= this.camera.getPosY();
		this.v.scale(this.camera.getZoom());
		this.v.x += this.halfWidth;
		this.v.y += this.halfHeight;
	}
	
	private void nodeTransform(SceneNode node) {
		if (node == null) {
			v.set(0, 0);
			return;
		}

		nodeTransform(node.getParent());
		v.rotate(node.getAngle());
		this.v.x += node.getPosX();
		this.v.y += node.getPosY();
	}
	
	public double getViewX() {
		return this.v.x;
	}
	
	public double getViewY() {
		return this.v.y;
	}
	
	public double getViewXRelativ() {
		return this.v.x / (this.halfWidth * 2);
	}
	
	public double getViewYRelativ() {
		return this.v.y / (this.halfHeight * 2) / this.ar;
	}
}
