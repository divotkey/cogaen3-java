package org.cogaen.lwjgl.scene;

import org.cogaen.core.Core;
import org.cogaen.math.Vector2;

public class ViewToWorld {

	private Vector2 v = new Vector2();
	private int halfWidth;
	private int halfHeight;
	private Camera camera;
	
	public ViewToWorld(Core core, Camera camera) {
		SceneService scnSrv = SceneService.getInstance(core);
		this.halfWidth = scnSrv.getScreenWidth() / 2;
		this.halfHeight = scnSrv.getScreenHeight() / 2;
		this.camera = camera;
	}
	
	public void transform(double x, double y) {
		v.x = x - this.halfWidth;
		v.y = y - this.halfHeight;
		v.scale(1.0 / this.camera.getZoom());
		v.rotate(this.camera.getAngle());
		v.x += this.camera.getPosX();
		v.y += this.camera.getPosY();
	}
	
	public double getWorldX() {
		return v.x;
	}
	
	public double getWorldY() {
		return v.y;
	}
}
