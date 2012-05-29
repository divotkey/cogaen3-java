package org.cogaen.lwjgl.ngui;

import org.cogaen.core.Core;
import org.cogaen.lwjgl.scene.Color;
import org.cogaen.lwjgl.scene.NodeReceiver;
import org.cogaen.lwjgl.scene.ReadableColor;
import org.cogaen.lwjgl.scene.RectangleVisual;
import org.cogaen.lwjgl.scene.SceneNode;

public class Frame extends BaseGui implements NodeReceiver {

	private Color color = new Color(Color.CYAN);
	private double width = 1.0;
	private double height = 1.0;
	private RectangleVisual rec;
	
	public Frame(Core core) {
		super(core);
	}

	@Override
	public void engage() {
		super.engage();
		
		this.rec = new RectangleVisual(this.width, this.height);
		this.rec.setColor(this.color);
		
		getBaseNode().addVisual(this.rec);
	}

	@Override
	public void disengage() {
		this.rec = null;
		super.disengage();
	}
	

	public Frame color(ReadableColor color) {
		setColor(color);
		return this;
	}
	
	public void setColor(ReadableColor color) {
		this.color.setColor(color);
		if (isEngaged()) {
			this.rec.setColor(this.color);
		}
	}
	
	public Frame width(double width) {
		setWidth(width);
		return this;
	}
	
	public void setWidth(double width) {
		this.width = width;
		if (isEngaged()) {
			this.rec.setWidth(this.width);
		}
	}

	public Frame height(double height) {
		setHeight(height);
		return this;
	}
	
	public void setHeight(double height) {
		this.height = height;
		if (isEngaged()) {
			this.rec.setHeight(this.height);
		}
	}

	@Override
	public void setNode(SceneNode node) {
		getBaseNode().addNode(node);
	}
}
