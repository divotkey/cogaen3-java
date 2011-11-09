package org.cogaen.lwjgl.gui;

import org.cogaen.core.Core;
import org.cogaen.core.Engageable;
import org.cogaen.lwjgl.scene.SceneNode;
import org.cogaen.lwjgl.scene.SceneService;

public abstract class Gui implements Engageable {

	public static final int DEFAULT_REFERENCE_RESOLUTION = 1650;
	private SceneNode baseNode;
	private Core core;
	private boolean engaged;
	private double width;
	private double height;
	private int referenceResolution = DEFAULT_REFERENCE_RESOLUTION;
	private boolean visible;
	private boolean disabled;
	private boolean selected;
	
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
	
	public void setSelected(boolean value) {
		this.selected = value;
	}
	
	public final boolean isSelected() {
		return this.selected;
	}
}
