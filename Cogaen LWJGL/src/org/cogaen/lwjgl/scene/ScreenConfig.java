package org.cogaen.lwjgl.scene;

public class ScreenConfig {

	private int width;
	private int height;
	private boolean vsync;
	private boolean fullscreen;
	private boolean cloneDesktop;
	private String title;
	
	public static class Builder {
		private int width = 800;
		private int height = 600;
		private boolean fullscreen = false;
		private boolean vsync = true;
		private boolean cloneDesktop = false;
		private String title = "[untitled]";
		
		public Builder fullscreen(boolean fullscreen) {
			this.fullscreen = fullscreen;
			return this;
		}
		
		public Builder vsync(boolean vsync) {
			this.vsync = vsync;
			return this;
		}
		
		public Builder title(String title) {
			this.title = title;
			return this;
		}
		
		public Builder cloneDesktop(boolean cloneDesktop) {
			this.cloneDesktop = cloneDesktop;
			return this;
		}
		
		public Builder resolution(int width, int height) {
			this.width = width;
			this.height = height;
			return this;
		}
		
		public ScreenConfig build() {
			return new ScreenConfig(this);
		}
	}
	
	private ScreenConfig(Builder builder) {
		setWidth(builder.width);
		setHeight(builder.height);
		setFullscreen(builder.fullscreen);
		setCloneDesktop(builder.cloneDesktop);
		setVsync(builder.vsync);
		setTitle(builder.title);
	}

	public ScreenConfig(ScreenConfig config) {
		setWidth(config.getWidth());
		setHeight(config.getHeight());
		setFullscreen(config.isFullscreen());
		setVsync(config.isVsync());
		setCloneDesktop(config.isCloneDesktop());
		setTitle(config.getTitle());
	}

	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		if (width <= 0) {
			throw new IllegalArgumentException("width must be greater zero");
		}
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		if (height <= 0) {
			throw new IllegalArgumentException("height must be greater zero");
		}
		this.height = height;
	}
	
	public boolean isVsync() {
		return vsync;
	}

	public void setVsync(boolean vsync) {
		this.vsync = vsync;
	}
	
	public boolean isFullscreen() {
		return fullscreen;
	}

	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}
	
	public boolean isCloneDesktop() {
		return cloneDesktop;
	}
	
	public void setCloneDesktop(boolean cloneDesktop) {
		this.cloneDesktop = cloneDesktop;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (cloneDesktop ? 1231 : 1237);
		result = prime * result + (fullscreen ? 1231 : 1237);
		result = prime * result + height;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + (vsync ? 1231 : 1237);
		result = prime * result + width;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScreenConfig other = (ScreenConfig) obj;
		if (cloneDesktop != other.cloneDesktop)
			return false;
		if (fullscreen != other.fullscreen)
			return false;
		if (height != other.height)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (vsync != other.vsync)
			return false;
		if (width != other.width)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer(getTitle());
		buf.append(": ");
				
		if (isCloneDesktop()) {
			buf.append("desktop resolution");
		} else {			
			buf.append(getWidth());
			buf.append(" x ");
			buf.append(getHeight());
		}
		buf.append(" | fullscreen = ");
		buf.append(isFullscreen());

		buf.append(" | vsync = ");
		buf.append(isVsync());
		
		return buf.toString();
	}	
	
	
}
