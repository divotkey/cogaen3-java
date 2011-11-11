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
	private boolean useFrame = true;
	
	public FrameGui(Core core, double width, double height) {
		super(core, width, height);
	}

	@Override
	public void engage() {
		super.engage();
		
		RectangleVisual rec = new RectangleVisual(getWidth(), getHeight());
		rec.setFilled(true);
		rec.setColor(getBackColor());
		rec.setMask(getMask());
		getBaseNode().addVisual(rec);
		this.frameBackground = rec;

		rec = new RectangleVisual(getWidth(), getHeight());
		rec.setFilled(false);
		rec.setColor(getFrameColor());
		rec.setMask(getMask());
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
		
		getFrame().setMask(value && this.useFrame ? getMask() : 0x0000);
		getBackground().setMask(value && this.useFrame ? getMask() : 0x0000);
		
		super.setVisible(value);
	}

	public final void setUseFrame(boolean value) {
		if (this.useFrame == value) {
			return;
		}

		getFrame().setMask(value ? getMask() : 0x0000);
		getBackground().setMask(value ? getMask() : 0x0000);
		this.useFrame = value;
	}
	
	public final boolean isUseFrame() {
		return this.useFrame;
	}
	
	@Override
	public void setSelected(boolean value) {
		if (value == isSelected()) {
			return;
		}
		
		this.frame.setColor(value ? this.selectColor : this.frameColor);
		super.setSelected(value);
	}

	@Override
	public void setMask(int mask) {
		super.setMask(mask);
		if (this.frame != null) {
			this.frame.setMask(this.useFrame ? getMask() : 0x000);
		}
		if (this.frameBackground != null) {
			this.frameBackground.setMask(this.useFrame ? getMask() : 0x000);
		}
	}
	
}
