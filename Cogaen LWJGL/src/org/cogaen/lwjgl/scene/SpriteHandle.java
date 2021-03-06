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
import org.cogaen.resource.ResourceException;
import org.cogaen.resource.ResourceHandle;
import org.cogaen.resource.ResourceService;
import org.newdawn.slick.opengl.Texture;

public class SpriteHandle extends ResourceHandle {

	private SpriteVisual sprite;
	private double width;
	private double height;
	private String textureName;
	
	public SpriteHandle(String textureName) {
		this(textureName, 0, 0);
	}
	
	public SpriteHandle(String textureName, double width, double height) {
		this.textureName = textureName;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public boolean isLoaded() {
		return this.sprite != null;
	}

	@Override
	public void load(Core core) throws ResourceException {
		Texture texture = (Texture) ResourceService.getInstance(core).getResource(this.textureName);
		
		if (this.width == 0 || this.height == 0) {
			this.sprite = new SpriteVisual(core, this.textureName, texture.getImageWidth(), texture.getImageHeight());
		} else {
			this.sprite = new SpriteVisual(core, this.textureName, this.width, this.height);
		}
	}

	@Override
	public void unload(Core core) {
		this.sprite = null;
	}

	@Override
	public Object getResource() {
		return this.sprite.newInstance();
	}

}
