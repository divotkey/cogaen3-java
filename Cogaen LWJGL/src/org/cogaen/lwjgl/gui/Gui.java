/* 
-----------------------------------------------------------------------------
                   Cogaen - Component-based Game Engine V3
-----------------------------------------------------------------------------
This software is developed by the Cogaen Development Team. Please have a 
look at our project home page for further details: http://www.cogaen.org
   
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
Copyright (c) 2010-2011 Roman Divotkey

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
*/

package org.cogaen.lwjgl.gui;

import org.cogaen.core.Core;
import org.cogaen.core.Engageable;
import org.cogaen.lwjgl.scene.SceneNode;
import org.cogaen.lwjgl.scene.SceneService;

// TODO use CogaenBase instead of implementing Engageable directly
public abstract class Gui implements Engageable {

	public static final int DEFAULT_REFERENCE_RESOLUTION = 1650;
	private SceneNode baseNode;
	private Core core;
	private boolean engaged;
	private double width;
	private double height;
	private int referenceResolution;
	private boolean visible;
	private boolean disabled;
	private boolean selected;
	private int mask = 0xFFFF;

	public Gui(Core core, double width, double height, int referenceResolution) {
		this.core = core;
		this.width = width;
		this.height = height;
		this.referenceResolution = referenceResolution;
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
		assert(isEngaged());
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

	public final int getReferenceResolution() {
		return referenceResolution;
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

	public final int getMask() {
		return mask;
	}

	public void setMask(int mask) {
		this.mask = mask;
	}
}
