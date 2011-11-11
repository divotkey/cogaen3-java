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
import org.cogaen.lwjgl.scene.LocalToGlobal;
import org.cogaen.lwjgl.scene.ViewToOverlay;

public class OverlayTextInput extends AbstractTextInput {

	private ViewToOverlay viewToOverlay;
	private LocalToGlobal localToGlobal = new LocalToGlobal();
	
	public OverlayTextInput(Core core, String fontRes, double width, double height) {
		super(core, fontRes, width, height);
		this.viewToOverlay = new ViewToOverlay(core);
	}

	@Override
	protected boolean isHit(double x, double y) {
		this.viewToOverlay.transform(x, y);
		this.localToGlobal.transform(getBaseNode());
		
		if (this.viewToOverlay.getOverlayX() < this.localToGlobal.getGlobalX() - getWidth() / 2 
				|| this.viewToOverlay.getOverlayX() > this.localToGlobal.getGlobalX() + getWidth() / 2) {
			return false;
		}
		
		if (this.viewToOverlay.getOverlayY() < this.localToGlobal.getGlobalY() - getHeight() / 2 
				|| this.viewToOverlay.getOverlayY() > this.localToGlobal.getGlobalY() + getHeight() / 2) {
			return false;
		}
		
		return true;
	}

	@Override
	protected double getScale() {
		return 1.0 / getReferenceResolution();
	}

}
