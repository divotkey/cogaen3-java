package org.cogaen.lwjgl.gui;

import org.cogaen.core.Core;
import org.cogaen.lwjgl.scene.Alignment;
import org.cogaen.lwjgl.scene.Color;
import org.cogaen.lwjgl.scene.MultiLineLabelVisual;
import org.cogaen.lwjgl.scene.ReadableColor;

public abstract class AbstractLabel extends FrameGui {

	private static final double DEFAULT_GAP = 0.97;
	private MultiLineLabelVisual mll;
	private String fontRes;
	private Alignment alignment = Alignment.LEFT;
	private double gap = DEFAULT_GAP;
	
	public AbstractLabel(Core core, String fontRes, double width, double height) {
		super(core, width, height);
		this.fontRes = fontRes;
	}

	protected abstract double getScale();
	
	@Override
	public void engage() {
		super.engage();
		
		this.mll = new MultiLineLabelVisual(getCore(), this.fontRes, getWidth() * this.gap / getScale(), getHeight() * this.gap / getScale());
		this.mll.setScale(getScale());
		this.mll.setAlignment(this.alignment);
		getBaseNode().addVisual(this.mll);
	}

	public void setTextColor(ReadableColor color) {
		this.mll.setColor(color);
	}
	
	public Color getTextColor() {
		return this.mll.getColor();
	}
	
	@Override
	public void setVisible(boolean value) {
		this.mll.setMask(value ? getMask() : 0x0000);
		super.setVisible(value);
	}
	
	@Override
	public void setMask(int mask) {
		super.setMask(mask);
		if (this.mll != null) {
			this.mll.setMask(getMask());
		}
	}

	public void setText(String text) {
		this.mll.setText(text);
	}

	public double getGap() {
		return gap;
	}

	public void setGap(double gap) {
		this.gap = gap;
	}
	
	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
		if (this.mll != null) {
			this.mll.setAlignment(alignment);
		}
	}
	
}
