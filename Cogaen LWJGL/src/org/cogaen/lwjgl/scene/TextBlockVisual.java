package org.cogaen.lwjgl.scene;

import org.cogaen.core.Core;
import org.cogaen.resource.ResourceService;
import org.cogaen.time.TimeService;
import org.cogaen.time.Timer;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;

@SuppressWarnings("deprecation")
public class TextBlockVisual extends Visual {

	private static final double LINE_SPACE = 1.0;
	private static final double BLINK_TIME = 0.5;

	private TrueTypeFont ttf;
	private StringBuffer lines[];
	private double width;
	private double height;
	private Timer timer;
	private double timeStamp;
	private boolean cursorOn;
	private int curX;
	private int curY;
	
	public TextBlockVisual(Core core, String fontRes, double width, double height) {
		this.ttf = (TrueTypeFont) ResourceService.getInstance(core).getResource(fontRes);
		this.width = width;
		this.height = height;
		
		this.timer = TimeService.getInstance(core).getTimer();
		this.timeStamp = this.timer.getTime() + BLINK_TIME;
		this.cursorOn = true;

		int nLines = (int) (this.height / (ttf.getHeight() * LINE_SPACE));
		this.lines = new StringBuffer[nLines];
		for (int i = 0; i < nLines; ++i) {
			this.lines[i] = new StringBuffer();
		}
		
		this.curX = 0;
		this.curY = 0;
	}
	
	TextBlockVisual() {
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
		GL11.glScaled(getScale(), getScale(), 1);
		getColor().apply();
		
		if (this.timeStamp < this.timer.getTime()) {
			this.timeStamp = this.timer.getTime() + BLINK_TIME;
			this.cursorOn = !this.cursorOn;
		}

		for (int i = 0; i < this.lines.length; ++i) {
			this.ttf.drawString((float) (-this.width / 2), (float) (-this.height / 2 + i * ttf.getHeight() * LINE_SPACE), this.lines[i].toString());						
		}
		
		if (this.cursorOn) {
			GL11.glDisable(GL11.GL_TEXTURE_2D);
	    	GL11.glEnable(GL11.GL_BLEND);
	    	
			double offsetX;
			if (this.curX == 0) {
				offsetX = 0;
			} else if (this.curX >= this.lines[this.curY].length()){
				offsetX = this.ttf.getWidth(this.lines[this.curY].substring(0, this.lines[this.curY].length()));
			} else {
				offsetX = this.ttf.getWidth(this.lines[this.curY].substring(0, this.curX));				
			}
			double offsetY = this.curY * this.ttf.getHeight() * LINE_SPACE;
			
			double x = -this.width / 2 + offsetX;
			double y = -this.height / 2 + offsetY + this.ttf.getHeight() / 2;
			double curHeight = this.ttf.getHeight() / 2 * 0.8;
			
		    GL11.glBegin(GL11.GL_LINES);
	        GL11.glVertex2d(x, y + curHeight);
	        GL11.glVertex2d(x, y - curHeight);
		    GL11.glEnd();
		}
	}

	@Override
	public void epilog() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TextBlockVisual newInstance() {
		TextBlockVisual instance = new TextBlockVisual();
		super.copyFields(instance);
		instance.ttf = ttf;
		instance.width = this.width;
		instance.height = this.height;
		instance.timer = this.timer;
		instance.timeStamp = this.timeStamp;
		
		for (int i = 0; i < this.lines.length; ++i) {
			instance.lines[i] = new StringBuffer(this.lines[i]);
		}
		
		return instance;
	}

	public void addChar(char ch) {
		if (this.lines[this.curY].length() <= this.curX) {
			this.lines[this.curY].append(ch);
		} else {
			this.lines[this.curY].insert(this.curX, ch);
		}
		this.curX++;
		
		if (!adjust(this.curY)) {
			this.lines[this.curY].deleteCharAt(this.curX - 1);
			this.curX--;
		}
	}
	
	private boolean adjust(int idx) {
		if(this.ttf.getWidth(this.lines[idx].toString()) > this.width) {
			if (idx + 1 >= this.lines.length) {
				return false;
			}
			
			char ch = this.lines[idx].charAt(this.lines[idx].length() - 1);
			this.lines[idx].deleteCharAt(this.lines[idx].length() - 1);
			this.lines[idx + 1].insert(0, ch);

			int oldCurX = -1;
			if (idx == this.curY && this.curX > this.lines[idx].length()) {
				this.curY++;
				oldCurX = this.curX;
				this.curX = 1;
			}
			
			if (!adjust(idx + 1)) {
				this.lines[idx].append(ch);
				if (oldCurX != -1) {
					this.curY--;
					this.curX = oldCurX;
				}
				return false;
			}
			return adjust(idx);
		}
		
		return true;
	}

	public void back() {
		if (this.curX - 1 >= 0) {
			this.lines[this.curY].deleteCharAt(this.curX - 1);
			this.curX--;
		} else if (this.curY - 1 >= 0){
			this.lines[this.curY - 1].deleteCharAt(this.lines[this.curY - 1].length() - 1);
			this.curY--;
			this.curX = this.lines[this.curY].length();
		}
	}
	
	public void left() {
		if (this.curX > 0) {
			this.curX--;
		} else if (this.curY > 0){
			this.curY--;
			this.curX = this.lines[this.curY].length();
		}
	}
	
	public void right() {
		if (this.curX < this.lines[this.curY].length()) {
			this.curX++;
		} else if (this.curY < this.lines.length - 1 && this.lines[this.curY + 1].length() > 0) {
			this.curY++;
			this.curX = 0;
		}
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
	
	public void setHeight(double height) {
		this.height = height;
	}
	
	public void setTimer(Timer timer) {
		this.timer = timer;
	}
	
	public Timer getTimer() {
		return this.timer;
	}
}
