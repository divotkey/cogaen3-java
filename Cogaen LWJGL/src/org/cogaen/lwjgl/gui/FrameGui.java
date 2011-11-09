package org.cogaen.lwjgl.gui;

import org.cogaen.core.Core;
import org.cogaen.lwjgl.scene.Color;
import org.cogaen.lwjgl.scene.RectangleVisual;
import org.cogaen.lwjgl.scene.Visual;

public abstract class FrameGui extends Gui {

	private Visual frame;
	private Visual frameBackground;
	
	public FrameGui(Core core, double width, double height) {
		super(core, width, height);
	}

	@Override
	public void engage() {
		super.engage();
		
		RectangleVisual rec = new RectangleVisual(getWidth(), getHeight());
		rec.setFilled(true);
		rec.setColor(getLightColor());
		getBaseNode().addVisual(rec);
		this.frameBackground = rec;

		rec = new RectangleVisual(getWidth(), getHeight());
		rec.setFilled(false);
		rec.setColor(getPrimaryColor());
		getBaseNode().addVisual(rec);
		this.frame = rec;
	}

	@Override
	public void disengage() {
		super.disengage();
	}

	public final Visual getFrame() {
		return this.frame;
	}
	
	public final Visual getBackground() {
		return this.frameBackground;
	}

	@Override
	public void setPrimaryColor(Color color) {
		super.setPrimaryColor(color);
		this.frame.setColor(getPrimaryColor());
	}

	@Override
	public void setLightColor(Color color) {
		super.setLightColor(color);
		this.frameBackground.setColor(getLightColor());
	}
	
	@Override
	public void setVisible(boolean value) {
		if (isVisible() == value) {
			return;
		}
		
		getFrame().setMask(value ? 0xFFFF : 0x0000);
		getBackground().setMask(value ? 0xFFFF : 0x0000);
		
		super.setVisible(value);
	}
	
}
