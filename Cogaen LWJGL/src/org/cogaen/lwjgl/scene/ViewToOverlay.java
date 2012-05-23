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

package org.cogaen.lwjgl.scene;

import org.cogaen.core.CogaenBase;
import org.cogaen.core.Core;

public class ViewToOverlay extends CogaenBase {

	private double width;
	private double height;
	private double xt;
	private double yt;
	
	public ViewToOverlay(Core core) {
		super(core);
		resetValues();
	}

	private void resetValues() {
		SceneService scnSrv = SceneService.getInstance(getCore());
		this.width = scnSrv.getScreenWidth();
		this.height = scnSrv.getScreenHeight();
	}
	
	public void transform(double x, double y) {
		this.xt = x / this.width;
		this.yt = y / this.height;
	}
	
	public double getOverlayX() {
		return this.xt;
	}
	
	public double getOverlayY() {
		return this.yt;
	}

	@Override
	public void engage() {
		super.engage();
		resetValues();
	}

	@Override
	public void disengage() {
		super.disengage();
	}
}
