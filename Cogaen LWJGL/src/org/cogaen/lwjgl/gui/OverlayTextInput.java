package org.cogaen.lwjgl.gui;

import org.cogaen.core.Core;
import org.cogaen.lwjgl.scene.LocalToGlobal;
import org.cogaen.lwjgl.scene.ViewToOverlay;

public class OverlayTextInput extends AbstractTextInput {

	private ViewToOverlay viewToOverlay;
	private LocalToGlobal localToGlobal = new LocalToGlobal();
	
	public OverlayTextInput(Core core, String fontRes, double width, double height) {
		super(core, fontRes, width, height);
		this.viewToOverlay = new ViewToOverlay(core);
	}

	@Override
	protected boolean isHit(double x, double y) {
		this.viewToOverlay.transform(x, y);
		this.localToGlobal.transform(getBaseNode());
		
		if (this.viewToOverlay.getOverlayX() < this.localToGlobal.getGlobalX() - getWidth() / 2 
				|| this.viewToOverlay.getOverlayX() > this.localToGlobal.getGlobalX() + getWidth() / 2) {
			return false;
		}
		
		if (this.viewToOverlay.getOverlayY() < this.localToGlobal.getGlobalY() - getHeight() / 2 
				|| this.viewToOverlay.getOverlayY() > this.localToGlobal.getGlobalY() + getHeight() / 2) {
			return false;
		}
		
		return true;
	}

	@Override
	protected double getScale() {
		return 1.0 / getReferenceResolution();
	}

}
