package org.cogaen.lwjgl.gui;

import org.cogaen.core.Core;
import org.cogaen.lwjgl.scene.LocalToGlobal;
import org.cogaen.lwjgl.scene.ViewToWorld;

public class WorldImageButton extends AbstractImageButton {

	private static final int DEFAULT_CAMERA_IDX = 0;
	private double worldWidth;
	private ViewToWorld viewToWorld;
	private LocalToGlobal localToGlobal = new LocalToGlobal();
	private int cameraIdx = DEFAULT_CAMERA_IDX;
	
	public WorldImageButton(Core core, double worldWidth, double width, double height, String normalRes, String selectedRes) {
		super(core, width, height, normalRes, selectedRes);
		this.worldWidth = worldWidth;
		this.viewToWorld = new ViewToWorld(core);
	}
	
	@Override
	protected boolean isHit(double x, double y) {
		this.viewToWorld.transform(x, y, this.cameraIdx);
		this.localToGlobal.transform(getBaseNode());
		
		if (this.viewToWorld.getWorldX() < this.localToGlobal.getGlobalX() - getWidth() / 2 
				|| this.viewToWorld.getWorldX() > this.localToGlobal.getGlobalX() + getWidth() / 2) {
			return false;
		}
		
		if (this.viewToWorld.getWorldY() < this.localToGlobal.getGlobalY() - getHeight() / 2 
				|| this.viewToWorld.getWorldY() > this.localToGlobal.getGlobalY() + getHeight() / 2) {
			return false;
		}
		
		return true;
	}

	@Override
	protected double getScale() {
		return this.worldWidth / getReferenceResolution();
	}

	public int getCameraIdx() {
		return cameraIdx;
	}

	public void setCameraIdx(int cameraIdx) {
		this.cameraIdx = cameraIdx;
	}

}
