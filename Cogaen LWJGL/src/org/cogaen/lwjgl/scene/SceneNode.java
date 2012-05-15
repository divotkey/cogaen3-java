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

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class SceneNode {
	
	private static final double DEG2RAD = Math.PI / 180.0;
	private static final double RAD2DEG = 180.0 / Math.PI;

	private List<Visual> visuals = new ArrayList<Visual>();
	private List<SceneNode> nodes = new ArrayList<SceneNode>();
	private SceneNode parent;
	private double posX;
	private double posY;
	private double angle;
	
	SceneNode() {
		
	}
	
	public void setPose(double x, double y, double angle) {
		this.posX = x;
		this.posY = y;
		this.angle = angle * RAD2DEG;
	}
	
	public double getPosX() {
		return this.posX;
	}
	
	public double getPosY() {
		return this.posY;
	}
	
	public double getAngle() {
		return this.angle * DEG2RAD;
	}
	
	public SceneNode getParent() {
		return this.parent;
	}
	
	public void addVisual(Visual visual) {
		this.visuals.add(visual);
	}
	
	public void removeVisual(Visual visual) {
		this.visuals.remove(visual);
	}
	
	public void addNode(SceneNode node) {
		this.nodes.add(node);
		node.parent = this;
	}
	
	public boolean removeNode(SceneNode node) {
		if (this.nodes.remove(node)) {
			node.parent = null;
			return true;
		}
		
		for (SceneNode child : this.nodes) {
			if (child.removeNode(node)) {
				return true;
			}
		}
		
		return false;
	}

	void renderWithAspectRatio(int mask, double ar) {
		GL11.glPushMatrix();
		GL11.glTranslated(this.posX, this.posY / ar, 0.0);
		GL11.glRotatef((float) this.angle, 0, 0, 1);
		
		for (Visual visual : this.visuals) {
			if ((visual.getMask() & mask) != 0) {
				visual.prolog();
				visual.render();
				visual.epilog();
			}
		}
		
		for (SceneNode node : this.nodes) {
			node.renderWithAspectRatio(mask, ar);
		}
		
		GL11.glPopMatrix();
	}
	
	void render(int mask) {
		GL11.glPushMatrix();
		GL11.glTranslated(this.posX, this.posY, 0.0);
		GL11.glRotatef((float) this.angle, 0, 0, 1);
		
		for (Visual visual : this.visuals) {
			if ((visual.getMask() & mask) != 0) {
				visual.prolog();
				visual.render();
				visual.epilog();
			}
		}
		
		for (SceneNode node : this.nodes) {
			node.render(mask);
		}
		
		GL11.glPopMatrix();
	}

	public void removeAllNodes() {
		this.nodes.clear();
	}

	public void removeAllVisuals() {
		this.visuals.clear();
	}
}
