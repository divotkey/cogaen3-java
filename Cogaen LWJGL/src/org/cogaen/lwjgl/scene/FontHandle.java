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

import java.awt.Font;

import org.cogaen.core.Core;
import org.cogaen.resource.ResourceException;
import org.cogaen.resource.ResourceHandle;
import org.newdawn.slick.TrueTypeFont;

@SuppressWarnings("deprecation")
public class FontHandle extends ResourceHandle {

	public static final int PLAIN = Font.PLAIN;
	public static final int ITALIC = Font.ITALIC;
	public static final int BOLD = Font.BOLD;
	
	private int size;
	private int style;
	private String name;
	private TrueTypeFont ttf;
	
	public FontHandle(String name, int size) {
		this(name, PLAIN, size);
	}
	
	public FontHandle(String name, int style, int size) {
		this.name = name;
		this.style = style;
		this.size = size;
	}
	
	@Override
	public boolean isLoaded() {
		return this.ttf != null;
	}

	@Override
	public void load(Core core) throws ResourceException {
		Font awtFont = new Font(this.name, this.style, this.size);
		this.ttf = new TrueTypeFont(awtFont, true);
	}

	@Override
	public void unload(Core core) {
		this.ttf = null;
	}

	@Override
	public Object getResource() {
		return this.ttf;
	}

}
