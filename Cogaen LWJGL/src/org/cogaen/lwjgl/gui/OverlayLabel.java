package org.cogaen.lwjgl.gui;

import org.cogaen.core.Core;

public class OverlayLabel extends Label {

	public OverlayLabel(Core core, String fontRes, double width, double height) {
		super(core, fontRes, width, height);
	}

	@Override
	protected double getScale() {
		return 1.0 / getReferenceResolution();
	}

}
