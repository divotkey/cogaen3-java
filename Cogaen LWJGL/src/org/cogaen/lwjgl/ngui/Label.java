package org.cogaen.lwjgl.ngui;

import org.cogaen.core.Core;
import org.cogaen.lwjgl.scene.Alignment;
import org.cogaen.lwjgl.scene.Color;
import org.cogaen.lwjgl.scene.NodeReceiver;
import org.cogaen.lwjgl.scene.Pose;
import org.cogaen.lwjgl.scene.ReadableColor;
import org.cogaen.lwjgl.scene.TextVisual;
import org.cogaen.resource.ResourceService;

public class Label extends BaseGui {

	private Color textColor = new Color(Color.CYAN);
	private TextVisual textVisual;
	private double scale = 1.0 / 512;
	private String fontRes;
	private String text;
	private Alignment alignment = Alignment.CENTER;
	private String colorResource;
	
	public Label(Core core, String fontRes) {
		super(core);
		this.fontRes = fontRes;
	}
	
	@Override
	public void engage() {
		super.engage();
		
		this.textVisual = new TextVisual(getCore(), this.fontRes);
		if (this.colorResource != null) {
			ResourceService resSrv = ResourceService.getInstance(getCore());
			setColor((ReadableColor) resSrv.getResource(this.colorResource));
		} else {
			this.textVisual.setColor(this.textColor);
		}
		this.textVisual.setScale(this.scale);
		this.textVisual.setAllignment(this.alignment);
		
		if (this.text != null) {
			this.textVisual.setText(this.text);
		}
		getBaseNode().addVisual(this.textVisual);
	}

	@Override
	public void disengage() {
		this.textVisual = null;
		super.disengage();
	}

	public Label text(String text) {
		setText(text);
		return this;
	}
	
	public void setText(String text) {
		this.text = text;
		if (isEngaged()) {
			this.textVisual.setText(this.text);
		}
	}

	public Label color(ReadableColor color) {
		setColor(color);
		return this;
	}
	
	public void setColor(ReadableColor color) {
		this.textColor.setColor(color);
		if (isEngaged()) {
			this.textVisual.setColor(this.textColor);
		}
	}
	
	public Label colorResource(String colorResource) {
		setColorResource(colorResource);
		return this;
	}
	
	public void setColorResource(String colorResource) {
		this.colorResource = colorResource;
		if (isEngaged()) {
			ResourceService resSrv = ResourceService.getInstance(getCore());
			setColor((Color) resSrv.getResource(this.colorResource));
		}
	}
	
	public Label scale(double scale) {
		setScale(scale);
		return this;
	}
	
	public void setScale(double scale) {
		this.scale = scale;
		if (isEngaged()) {
			this.textVisual.setScale(this.scale);
		}
	}

	public Label alignment(Alignment alignment) {
		setAlignment(alignment);
		return this;
	}
		
	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
		if (isEngaged()) {
			this.textVisual.setAllignment(this.alignment);
		}
	}

	@Override
	public Label nodeReceiver(NodeReceiver receiver) {
		super.nodeReceiver(receiver);
		return this;
	}
	
	@Override
	public Label pose(Pose pose) {
		super.pose(pose);
		return this;
	}

	@Override
	public Label pose(double x, double y, double angle) {
		super.pose(x, y, angle);
		return this;
	}
}
