package org.cogaen.lwjgl.gui;

import org.cogaen.core.Core;
import org.cogaen.core.Engageable;
import org.cogaen.lwjgl.scene.Color;
import org.cogaen.lwjgl.scene.SceneNode;
import org.cogaen.lwjgl.scene.SceneService;

public abstract class Gui implements Engageable {

	public static final int DEFAULT_REFERENCE_RESOLUTION = 1650;
	private SceneNode baseNode;
	private Core core;
	private boolean engaged;
	private double width;
	private double height;
	private Color primaryColor = new Color(Color.GRAY);
	private Color lightColor = new Color(Color.LIGHT_GRAY);
	private Color darkColor = new Color(Color.DARK_GRAY);
	private int referenceResolution = DEFAULT_REFERENCE_RESOLUTION;
	private boolean visible;
	private boolean disabled;
	
	public Gui(Core core, double width, double height) {
		this.core = core;
		this.width = width;
		this.height = height;
	}

	@Override
	public void engage() {
		this.baseNode = SceneService.getInstance(getCore()).createNode();
		this.engaged = true;
		this.visible = true;
		this.disabled = false;
	}

	@Override
	public void disengage() {
		SceneService.getInstance(getCore()).destroyNode(this.baseNode);
		this.engaged = false;
	}

	@Override
	public final boolean isEngaged() {
		return this.engaged;
	}

	public final SceneNode getBaseNode() {
		return this.baseNode;
	}
	
	public final Core getCore() {
		return this.core;
	}

	public final double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public final double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public Color getPrimaryColor() {
		return primaryColor;
	}

	public void setPrimaryColor(Color color) {
		this.primaryColor = color;
	}

	public Color getLightColor() {
		return lightColor;
	}

	public void setLightColor(Color color) {
		this.lightColor = color;
	}

	public Color getDarkColor() {
		return darkColor;
	}

	public void setDarkColor(Color color) {
		this.darkColor = color;
	}

	public int getReferenceResolution() {
		return referenceResolution;
	}

	public void setReferenceResolution(int referenceResolution) {
		this.referenceResolution = referenceResolution;
	}
	
	public void setDisabled(boolean value) {
		this.disabled = value;
	}
	public final boolean isDisabled() {
		return this.disabled;
	}
	
	public void setVisible(boolean value) {
		this.visible = value;
	}
	
	public final boolean isVisible() {
		return this.visible;
	}
}
