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

import org.cogaen.core.Core;
import org.cogaen.math.Vector2;

public class ViewToWorld {

	private Vector2 v = new Vector2();
	private int halfWidth;
	private int halfHeight;
	private SceneService scnSrv;
	
	public ViewToWorld(Core core) {
		this.scnSrv = SceneService.getInstance(core);
		this.halfWidth = this.scnSrv.getScreenWidth() / 2;
		this.halfHeight = this.scnSrv.getScreenHeight() / 2;
	}
	
	public void transform(double x, double y, int cameraIdx) {
		Camera camera = this.scnSrv.getCamera(cameraIdx);
		if (cameraIdx < 0 || cameraIdx >= this.scnSrv.numCameras()) {
			throw new IllegalArgumentException("camera index out of range: " + cameraIdx);
		}
		
		v.x = x - this.halfWidth;
		v.y = y - this.halfHeight;
		v.scale(1.0 / camera.getZoom());
		v.rotate(camera.getAngle());
		v.x += camera.getPosX();
		v.y += camera.getPosY();
	}
	
	public double getWorldX() {
		return v.x;
	}
	
	public double getWorldY() {
		return v.y;
	}
}
