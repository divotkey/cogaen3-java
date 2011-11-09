package org.cogaen.lwjgl.scene;

import org.cogaen.core.Core;
import org.cogaen.math.Vector2;

public class ViewToWorld {

	private Vector2 v = new Vector2();
	private int halfWidth;
	private int halfHeight;
	private SceneService scnSrv;
	
	public ViewToWorld(Core core) {
		this.scnSrv = SceneService.getInstance(core);
		this.halfWidth = this.scnSrv.getScreenWidth() / 2;
		this.halfHeight = this.scnSrv.getScreenHeight() / 2;
	}
	
	public void transform(double x, double y, int cameraIdx) {
		Camera camera = this.scnSrv.getCamera(cameraIdx);
		if (cameraIdx < 0 || cameraIdx >= this.scnSrv.numCameras()) {
			throw new IllegalArgumentException("camera index out of range: " + cameraIdx);
		}
		
		v.x = x - this.halfWidth;
		v.y = y - this.halfHeight;
		v.scale(1.0 / camera.getZoom());
		v.rotate(camera.getAngle());
		v.x += camera.getPosX();
		v.y += camera.getPosY();
	}
	
	public double getWorldX() {
		return v.x;
	}
	
	public double getWorldY() {
		return v.y;
	}
}
