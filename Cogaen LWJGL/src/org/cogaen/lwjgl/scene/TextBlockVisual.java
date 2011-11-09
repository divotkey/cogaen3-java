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
	private boolean showCursor = true;
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
		
		if (this.lines.length == 0) {
			throw new RuntimeException("dimension to small for text output");
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
		GL11.glPushMatrix();
		GL11.glScaled(getScale(), -getScale(), 1);
		getColor().apply();
		
		org.newdawn.slick.Color utilColor = new org.newdawn.slick.Color((int) (getColor().getRed() * 255), (int) (getColor().getGreen() * 255), (int) (getColor().getBlue() * 255), (int) (getColor().getAlpha() * 255));
		
		if (this.timeStamp < this.timer.getTime()) {
			this.timeStamp = this.timer.getTime() + BLINK_TIME;
			this.cursorOn = !this.cursorOn;
		}

		for (int i = 0; i < this.lines.length; ++i) {
			this.ttf.drawString((float) (-this.width / 2), (float) (-this.height / 2 + i * ttf.getHeight() * LINE_SPACE), this.lines[i].toString(), utilColor);						
		}
		
		if (this.cursorOn && this.showCursor) {
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
		GL11.glPopMatrix();
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
		instance.showCursor = this.showCursor;
		
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
	
	public void addString(String str) {
		for (int i = 0; i < str.length(); ++i) {
			addChar(str.charAt(i));
		}
	}
	
	private boolean adjust(int idx) {
		if(this.ttf.getWidth(this.lines[idx].toString()) > this.width) {
			if (idx + 1 >= this.lines.length) {
				return false;
			}
			
			int i = this.lines[idx].length() - 1;
			while (i > 0 && this.lines[idx].charAt(i) != ' ') {
				--i;
			}
			if (i == 0) {
				return false;
			}
			String word = this.lines[idx].substring(i + 1, this.lines[idx].length());
			this.lines[idx].delete(i, this.lines[idx].length());
			this.lines[idx + 1].insert(0, word);
			boolean needSpace = this.lines[idx + 1].length() > word.length();
			if (needSpace) {
				this.lines[idx + 1].insert(word.length(), ' ');
			}
			
			int oldCurX = -1;
			if (idx == this.curY && this.curX > this.lines[idx].length()) {
				this.curY++;
				oldCurX = this.curX;
				this.curX = word.length();
			}
			
			if (!adjust(idx + 1)) {
				this.lines[idx].append(word);
				this.lines[idx + 1].delete(0, word.length());
				if (needSpace) {
					this.lines[idx + 1].delete(0, 1);					
				}
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
			for (int i = this.curY + 1; i < this.lines.length; ++i) {
				if (this.lines[i].length() > 0) {
					this.lines[this.curY].append(' ');
					this.lines[this.curY].append(this.lines[i]);
					this.lines[i].setLength(0);
				}
			}
			adjust(this.curY);
		} else if (this.curY > 0) {
			this.lines[this.curY - 1].deleteCharAt(this.lines[this.curY - 1].length() - 1);
			this.curY--;
			this.curX = this.lines[this.curY].length();
			for (int i = this.curY + 1; i < this.lines.length; ++i) {
				this.lines[this.curY].append(this.lines[i]);
				this.lines[i].setLength(0);
			}
			adjust(this.curY);
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
	
	public void up() {
		if (this.curY > 0) {
			this.curY--;
		}
	}

	public void down() {
		if (this.curY < this.lines.length - 1 && this.lines[this.curY + 1].length() != 0) {
			this.curY++;
			if (this.curX > this.lines[this.curY].length()) {
				this.curX = this.lines[this.curY].length();
			}
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

	public String getText() {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < lines.length; ++i) {
			if (lines[i].length() > 0) {
				buf.append(' ');
				buf.append(lines[i]);
			}
		}
		return buf.toString().trim();
	}

	public void setText(String text) {
		for (int i = 0; i < this.lines.length; ++i) {
			this.lines[i].setLength(0);
		}
		this.curX = 0;
		this.curY = 0;
		addString(text);
	}

	public boolean isShowCursor() {
		return showCursor;
	}

	public void setShowCursor(boolean showCursor) {
		this.showCursor = showCursor;
	}
	
}
