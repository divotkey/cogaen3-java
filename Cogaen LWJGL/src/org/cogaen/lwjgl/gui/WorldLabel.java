package org.cogaen.lwjgl.gui;

import org.cogaen.core.Core;

public class WorldLabel extends Label {

	private double worldSize;

	public WorldLabel(Core core, double worldSize, String fontRes, double width, double height) {
		super(core, fontRes, width, height);
		this.worldSize = worldSize;
	}

	@Override
	protected double getScale() {
		return this.worldSize / getReferenceResolution();
	}

}
