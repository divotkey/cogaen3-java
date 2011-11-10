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
import org.cogaen.resource.ResourceService;
import org.cogaen.time.TimeService;
import org.cogaen.time.Timer;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;

@SuppressWarnings("deprecation")
public class CommandLineVisual extends Visual {

	private static final double DEFAULT_WIDTH = 500;
	private static final double LINE_SPACE = 1.0;
	private static final double BLINK_TIME = 0.5;

	private TrueTypeFont ttf;
	private StringBuffer lines[];
	private int curLine;
	private double width = DEFAULT_WIDTH;
	private double height;
	private Timer timer;
	private double timeStamp;
	private boolean cursorOn;
	
	public CommandLineVisual(Core core, String fontRes, int numOfLines) {
		this.ttf = (TrueTypeFont) ResourceService.getInstance(core).getResource(fontRes);
		this.lines = new StringBuffer[numOfLines];
		this.curLine = 0;
		for (int i = 0; i < this.lines.length; ++i) {
			this.lines[i] = new StringBuffer("");
		}
		this.height = this.ttf.getHeight() * this.lines.length * LINE_SPACE;
		this.timer = TimeService.getInstance(core).getTimer();
		this.timeStamp = this.timer.getTime() + BLINK_TIME;
		this.cursorOn = true;
	}
	
	CommandLineVisual() {
		// intentionally left empty
	}
	
	@Override
	public void prolog() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
    	GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void render() {
		GL11.glScaled(getScale(), -getScale(), 1);
		getColor().apply();
		
		if (this.timeStamp < this.timer.getTime()) {
			this.timeStamp = this.timer.getTime() + BLINK_TIME;
			this.cursorOn = !this.cursorOn;
		}
		
		for (int i = 0; i < this.lines.length; ++i) {
			if (i == this.curLine && this.cursorOn) {
				this.ttf.drawString((float) (-this.width / 2), (float) (-this.height / 2 + i * ttf.getHeight() * LINE_SPACE), this.lines[i].toString() + "_");		
			} else {
				this.ttf.drawString((float) (-this.width / 2), (float) (-this.height / 2 + i * ttf.getHeight() * LINE_SPACE), this.lines[i].toString());						
			}
		}
	}

	@Override
	public void epilog() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CommandLineVisual newInstance() {
		CommandLineVisual instance = new CommandLineVisual();
		super.copyFields(instance);
		instance.ttf = ttf;
		instance.lines = new StringBuffer[this.lines.length];
		for (int i = 0; i < this.lines.length; ++i) {
			instance.lines[i] = new StringBuffer(this.lines[i]);
		}
		instance.curLine = this.curLine;
		
		return instance;
	}

	public void addChar(char ch) {
		this.lines[this.curLine].append(ch);
		
		if (this.ttf.getWidth(this.lines[this.curLine].toString()) > this.width) {
			this.lines[this.curLine].deleteCharAt(this.lines[this.curLine].length() - 1);
			newLine();
			addChar(ch);
		}
	}
	
	public void addString(String str) {
		for (int i = 0; i < str.length(); ++i) {
			addChar(str.charAt(i));
		}
	}
		
	public void newLine() {
		if (this.curLine >= this.lines.length - 1) {
			
			StringBuffer first = this.lines[0];
			for (int i = 0; i < this.lines.length; ++i) {
				if (i + 1 >= this.lines.length) {
					this.lines[i] = first;
					first.setLength(0);
				} else {
					this.lines[i] = this.lines[i + 1];					
				}
			}
		} else {
			this.curLine++;
		}
	}
	
	public void deleteLastChar() {
		if (this.lines[this.curLine].length() > 0) {
			this.lines[this.curLine].deleteCharAt(this.lines[this.curLine].length() - 1);
		}
	}
	
	public void clearLine() {
		this.lines[this.curLine].setLength(0);
	}
	
	public int curLineLengh() {
		return this.lines[this.curLine].length();
	}
	
	public double getWidth() {
		return this.width;
	}
	
	public void setWidth(double width) {
		this.width = width;
	}
	
	public double getHeight() {
		return this.height;
	}
	
	public void setTimer(Timer timer) {
		this.timer = timer;
	}
	
	public Timer getTimer() {
		return this.timer;
	}
}
