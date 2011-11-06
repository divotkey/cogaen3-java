package org.cogaen.lwjgl.scene;

import org.cogaen.core.Core;

public class ViewToOverlay {

	private double width;
	private double height;
	private double ar;
	private double xt;
	private double yt;
	
	public ViewToOverlay(Core core) {
		SceneService scnSrv = SceneService.getInstance(core);
		this.width = scnSrv.getScreenWidth();
		this.height = scnSrv.getScreenHeight();
		this.ar = scnSrv.getAspectRatio();
	}
	
	public void transform(double x, double y) {
		this.xt = x / this.width;
		this.yt = y / this.height / this.ar;
	}
	
	public double getOverlayX() {
		return this.xt;
	}
	
	public double getOverlayY() {
		return this.yt;
	}
}
