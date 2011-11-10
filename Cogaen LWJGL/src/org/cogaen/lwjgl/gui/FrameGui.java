package org.cogaen.lwjgl.gui;

import org.cogaen.core.Core;
import org.cogaen.lwjgl.scene.Color;
import org.cogaen.lwjgl.scene.ReadableColor;
import org.cogaen.lwjgl.scene.RectangleVisual;
import org.cogaen.lwjgl.scene.Visual;

public class FrameGui extends Gui {

	private Visual frame;
	private Visual frameBackground;
	private Color selectColor = new Color(Color.GREEN);
	private Color frameColor = new Color(Color.BLUE);
	private Color backColor = new Color(Color.RED);
	
	public FrameGui(Core core, double width, double height) {
		super(core, width, height);
	}

	@Override
	public void engage() {
		super.engage();
		
		RectangleVisual rec = new RectangleVisual(getWidth(), getHeight());
		rec.setFilled(true);
		rec.setColor(getBackColor());
		getBaseNode().addVisual(rec);
		this.frameBackground = rec;

		rec = new RectangleVisual(getWidth(), getHeight());
		rec.setFilled(false);
		rec.setColor(getFrameColor());
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

	public void setFrameColor(ReadableColor color) {
		this.frameColor.setColor(color);
		if (!isSelected() && this.frame != null) {
			this.frame.setColor(color);
		}
	}
	
	public ReadableColor getSelectColor() {
		return selectColor;
	}

	public void setSelectColor(ReadableColor color) {
		this.selectColor.setColor(color);
		if (isSelected() && this.frame != null) {
			this.frame.setColor(color);
		}
	}
	
	public ReadableColor getFrameColor() {
		return this.frameColor;
	}
	
	public void setBackColor(ReadableColor color) {
		this.backColor.setColor(color);
		if (this.frameBackground != null) {
			this.frameBackground.setColor(color);
		}
	}
	
	public ReadableColor getBackColor() {
		return this.backColor;
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

	@Override
	public void setSelected(boolean value) {
		if (value == isSelected()) {
			return;
		}
		
		this.frame.setColor(value ? this.selectColor : this.frameColor);
		super.setSelected(value);
	}
}
