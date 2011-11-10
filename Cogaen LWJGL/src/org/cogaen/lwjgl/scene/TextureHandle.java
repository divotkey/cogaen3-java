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

import java.io.IOException;
import java.io.InputStream;

import org.cogaen.core.Core;
import org.cogaen.resource.ResourceException;
import org.cogaen.resource.ResourceHandle;
import org.cogaen.resource.ResourceService;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class TextureHandle extends ResourceHandle {

	public static final int LINEAR_FILTER = GL11.GL_LINEAR;
	public static final int NEAREST_FILTER = GL11.GL_NEAREST;
	private String filename;
	private String format;
	private int filter;
	private Texture texture;
	
	public TextureHandle(String format, String filename) {
		this(format, filename, LINEAR_FILTER);
	}
	
	public TextureHandle(String format, String filename, int filter) {
		this.format = format;
		this.filename = filename;
		this.filter = filter;
	}
	
	@Override
	public boolean isLoaded() {
		return this.texture != null;
	}

	@Override
	public void load(Core core) throws ResourceException {
		InputStream in = ResourceService.getInstance(core).getStream(this.filename);
		if (in == null) {
			throw new ResourceException("resource not found " + this.filename);
		}
		try {
			this.texture = TextureLoader.getTexture(this.format, in, this.filter);
			in.close();
		} catch (IOException e) {
			throw new ResourceException("unable to load texture", e);
		}
	}

	@Override
	public void unload(Core core) {
		if (SceneService.getInstance(core).isValid()) {
			this.texture.release();
			this.texture = null;
		}
	}

	@Override
	public Object getResource() {
		return this.texture;
	}

}
