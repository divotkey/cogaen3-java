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
	
	public AbstractLabel(Core core, String fontRes, double width, double height, int referenceResolution) {
		super(core, width, height, referenceResolution);
		this.fontRes = fontRes;
	}

	protected abstract double getScale();
	
	@Override
	public void engage() {
		super.engage();
		
		this.mll = new MultiLineLabelVisual(getCore(), this.fontRes, getWidth() * this.gap / getScale(), getHeight() * this.gap / getScale());
		this.mll.setScale(getScale());
		this.mll.setAlignment(this.alignment);
		this.mll.setMask(getMask());
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
