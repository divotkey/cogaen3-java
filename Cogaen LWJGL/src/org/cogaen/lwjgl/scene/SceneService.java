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

import java.awt.Canvas;
import java.util.ArrayList;
import java.util.List;

import org.cogaen.core.AbstractService;
import org.cogaen.core.Core;
import org.cogaen.core.ServiceException;
import org.cogaen.event.EventService;
import org.cogaen.event.SimpleEvent;
import org.cogaen.logging.LoggingService;
import org.cogaen.name.CogaenId;
import org.cogaen.property.PropertyService;
import org.cogaen.resource.ResourceService;
import org.cogaen.time.Clock;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.newdawn.slick.Font;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.Log;

@SuppressWarnings("deprecation")
public class SceneService extends AbstractService {

	public static final CogaenId ID = new CogaenId("org.cogaen.lwjgl.scene.SceneService");
	public static final String NAME = "Cogaen LWJGL Scene Service";
	public static final String LOGGING_SOURCE = "LWSC";
	public static final CogaenId WINDOW_CLOSE_REQUEST = new CogaenId("WindowCloseRequest");
	
	private static final ScreenConfig DEFAULT_CONFIG = new ScreenConfig.Builder().build();
	private static final String FS_PREFIX = "lwjgl.";
	private static final String CLONE_PROP = FS_PREFIX + "cloneDesktop";
	private static final String FULLSCREEN_PROP = FS_PREFIX + "fullscreen";
	private static final String VSYNC_PROP = FS_PREFIX + "vsync";
	private static final String WIDTH_PROP = FS_PREFIX + "screenwidth";
	private static final String HEIGHT_PROP = FS_PREFIX + "screenheight";
	private static final String TITLE_PROP = FS_PREFIX + "title";

	/** Determines if only screen resolutions available in full screen mode can be used. */
	private boolean useOnlyFullscreenModes = true;
	
	private EventService evtSrv;
	private boolean useProperties;
	private SceneNode overlayRoot;
	private List<Camera> cameras = new ArrayList<Camera>();
	private List<RenderSubsystem> subSystems = new ArrayList<RenderSubsystem>();
	private List<SceneNode> layers = new ArrayList<SceneNode>();
	private LoggingService logger;
	private Font font;
	private Clock clock = new Clock();
	private double fpsSum;
	private int fpsAvg = 300;
	private int fpsCnt = 0;
	private String fps = new String("FPS:");
	private Canvas parent;
	private ScreenConfig config;
	
	public static SceneService getInstance(Core core) {
		return (SceneService) core.getService(ID);
	}

	/** 
	 * Creates new instance with default screen configuration using property
	 * service.
	 * This instance will use property service to load and store screen
	 * configurations. If a screen configuration is found in the properties,
	 * this configuration will be used instead of default configuration.
	 * 
	 * @see PropertyService
	 */
	public SceneService() {
		this(true);
	}

	/**
	 * Creates new instance using default screen configuration.
	 * If {@code useProperties} is set to {@code true} this instance will
	 * use property service to load and store screen configurations, otherwise
	 * this service has no dependency to property service.
	 * 
	 * @param useProperties defines if property service should be used to load
	 * and store screen configuration
	 * 
	 * @see PropertyService
	 */
	public SceneService(boolean useProperties) {
		this(DEFAULT_CONFIG, useProperties);
	}
	
	/**
	 * Created new instance using specified default screen configuration using
	 * property service.
	 * @param defaultConfig screen configuration to be used if properties are
	 * not used or no stored configuration can be found
	 */
	public SceneService(ScreenConfig defaultConfig) {
		this(defaultConfig, true);
	}
	
	/**
	 * Creates new instance using specified default screen configuration.
	 * 
	 * @param defaultConfig screen configuration to be used if properties are
	 * not used or no stored configuration can be found
	 * @param useProperties defines if property service should be used to load
	 * and store screen configuration
	 */
	public SceneService(ScreenConfig defaultConfig, boolean useProperties) {
		this(null, defaultConfig, useProperties);
	}

	/**
	 * Creates new instance using specified default screen configuration and
	 * specified parent. If parent is {@code null}, the Display will appear as
	 * a top level window. If parent is not {@code null}, the Display is made
	 * a child of the parent. 
	 * 
	 * @param parent the parent canvas or {@code null} if used as top lebel window
	 * @param defaultConfig screen configuration to be used if properties are
	 * not used or no stored configuration can be found
	 * @param useProperties defines if property service should be used to load
	 * and store screen configuration
	 */
	public SceneService(Canvas parent, ScreenConfig defaultConfig, boolean useProperties) {
		if (useProperties) {
			addDependency(PropertyService.ID);
		}
		addDependency(EventService.ID);
		addDependency(LoggingService.ID);
		addDependency(ResourceService.ID);
		
		this.useProperties = useProperties;
		this.parent = parent;
		this.config = defaultConfig;
		
		this.layers.add(createNode());
		this.overlayRoot = new SceneNode();

		// initialize texture loader
		System.setProperty("org.newdawn.slick.pngloader", "true");
		
		// turn off verbose logging of slick library
		Log.setVerbose(false);		
	}

	
	@Override
	public CogaenId getId() {
		return ID;
	}

	@Override
	public String getName() {
		return NAME;
	}
	
	private ScreenConfig loadProperties(ScreenConfig defaultConfig) {
		PropertyService propSrv = PropertyService.getInstance(getCore());
		
		String title = propSrv.getProperty(TITLE_PROP, defaultConfig.getTitle());
		boolean fs = propSrv.getBoolProperty(FULLSCREEN_PROP, defaultConfig.isFullscreen());
		boolean vs = propSrv.getBoolProperty(VSYNC_PROP, defaultConfig.isVsync());
		boolean clone = propSrv.getBoolProperty(CLONE_PROP, defaultConfig.isCloneDesktop());
		int width = propSrv.getIntProperty(WIDTH_PROP, defaultConfig.getWidth());
		int height = propSrv.getIntProperty(HEIGHT_PROP, defaultConfig.getHeight());
		
		
		return new ScreenConfig.Builder().title(title).fullscreen(fs).
				vsync(vs).cloneDesktop(clone).resolution(width, height).build();
	}
	
	private void storeProperties(ScreenConfig config) {
		PropertyService propSrv = PropertyService.getInstance(getCore());

		propSrv.setProperty(TITLE_PROP, config.getTitle());
		propSrv.setBoolProperty(CLONE_PROP, config.isCloneDesktop());
		propSrv.setBoolProperty(VSYNC_PROP, config.isVsync());
		propSrv.setBoolProperty(FULLSCREEN_PROP, config.isFullscreen());
		propSrv.setIntProperty(WIDTH_PROP, config.getWidth());
		propSrv.setIntProperty(HEIGHT_PROP, config.getHeight());
	}
	
	@Override
	protected void doStart() throws ServiceException {
		this.logger = LoggingService.getInstance(getCore());

		ScreenConfig oldConfig = this.config;
		if (this.useProperties) {
			this.config = loadProperties(this.config);
		}
		
		setScreenMode(this.config);
		
		try {
			if (this.parent != null) {
				Display.setParent(this.parent);
			}
			Display.create();
			this.evtSrv = EventService.getInstance(getCore());
		} catch (LWJGLException e) {
			this.config = oldConfig;
			this.logger.logError(LOGGING_SOURCE, "unable to start scene service, requested screen mode not supported: " + this.config);
			throw new ServiceException("unable to initialize display", e);
		}		
		
		
		// font test
		java.awt.Font awtFont = new java.awt.Font("Arial", java.awt.Font.PLAIN, 16);
		this.font = new TrueTypeFont(awtFont, true);
		
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);		
		
		super.doStart();
	}
	
	@Override
	protected void doStop() {
		if (this.useProperties) {
			storeProperties(this.config);
		}
		
		this.evtSrv = null;
		Display.destroy();
		super.doStop();
	}
	
	public void addSubsystem(RenderSubsystem rs) {
		this.subSystems.add(rs);
	}
	
	public void removeSubsystem(RenderSubsystem rs) {
		this.subSystems.remove(rs);
	}
	
	public void renderScene() {
		
		// quite and dirty frame counter
		this.clock.tick();
		this.fpsSum += this.clock.getDelta();
		if (++this.fpsCnt >= this.fpsAvg) {
			this.fpsCnt = 0;
			this.fps = String.format("FPS: %2.2f", (1.0 / (this.fpsSum / this.fpsAvg)));
			this.fpsSum = 0.0;
		}
		
	    // Clear the screen and depth buffer
	    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);	
		
	    // render layers in each camera
		for (Camera camera : cameras) {
			if (!camera.isActive()) {
				continue;
			}
			
			camera.applyTransform();
			for (int i = this.layers.size() - 1; i >= 0; --i) {
				this.layers.get(i).render(camera.getMask());
			}
		    
			for (RenderSubsystem rs : this.subSystems) {
				rs.render(camera.getMask());
			}
		}
		
		// draw overlays
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 1.0, 0, 1.0 / getAspectRatio(), 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);		
		GL11.glViewport(0, 0, this.config.getWidth(), this.config.getHeight());
		
		this.overlayRoot.renderWithAspectRatio(0xFFFFFFFF, getAspectRatio());
//		this.overlayRoot.render(0xFFFFFFFF);
		
		// render frame counter
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, this.config.getWidth(), this.config.getHeight(), 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);	
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
    	GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4d(1, 0, 1, 1);		
		font.drawString(5, this.config.getHeight() - 20, this.fps);		
		
	    Display.update();		
	
		if (Display.isCloseRequested()) {
			this.evtSrv.dispatchEvent(new SimpleEvent(WINDOW_CLOSE_REQUEST));
		}
	}
	
	public void setTitle(String title) {
		assert(Display.getTitle().equals(this.config.getTitle()));
		
		Display.setTitle(title);
		this.config.setTitle(title);
		
		assert(Display.getTitle().equals(this.config.getTitle()));
		this.logger.logInfo(LOGGING_SOURCE, "changed window title to '" + title + "'");
	}
	
	public String getTitle() {
		assert(Display.getTitle().equals(this.config.getTitle()));	
		return this.config.getTitle();
	}

	public void setFullscreen(boolean fullscreen) throws ServiceException {
		assert(Display.isFullscreen() == this.config.isFullscreen());
		
		try {
			Display.setFullscreen(fullscreen);
			this.config.setFullscreen(fullscreen);
		} catch (LWJGLException e) {
			throw new ServiceException("unable to switch to fullscreen mode", e);
		}
		assert(Display.isFullscreen() == this.config.isFullscreen());
		
		this.logger.logInfo(LOGGING_SOURCE, "screen mode switch to " + 
				(this.config.isFullscreen() ? "fullscreen" : "windowed"));
	}
	
	public boolean isFullscreen() {
		assert(Display.isFullscreen() == this.config.isFullscreen());
		return this.config.isFullscreen();
	}

	/**
	 * Enables of disables vertical synchronization (vsync).
	 * @param vsync {@code true} to enable vsync, otherwise {@code false}
	 */
	public void setVSync(boolean vsync) {
		Display.setVSyncEnabled(vsync);
		this.config.setVsync(vsync);
		
		this.logger.logInfo(LOGGING_SOURCE, "vertical synchronization (vsync) "
				+ (this.config.isVsync() ? "enabled" : "disabled"));
	}	
	
	/**
	 * Returns if vertical synchronization (vsync) is enabled.
	 * @return {@code true} if vsync is enabled, otherwise {@code false}
	 */
	public boolean isVSync() {
		return this.config.isVsync();
	}
	
	public ScreenConfig getScreenMode() {
		return new ScreenConfig(this.config);
	}
	
	public ScreenConfig[] getScreenModes() throws ServiceException {

		try {
			DisplayMode desktopMode = Display.getDesktopDisplayMode();
			DisplayMode[] modes = Display.getAvailableDisplayModes();
			ArrayList<ScreenConfig> result = new ArrayList<ScreenConfig>();
			ScreenConfig.Builder builder = new ScreenConfig.Builder();
			builder.cloneDesktop(false);
			builder.fullscreen(this.config.isFullscreen());
			builder.vsync(this.config.isVsync());
			builder.title(this.config.getTitle());
			
			for (DisplayMode mode : modes) {
				if (mode.getFrequency() != desktopMode.getFrequency()) {
					continue;
				}
				if (mode.getBitsPerPixel() != desktopMode.getBitsPerPixel()) {
					continue;
				}

				insertConfig(result, builder.resolution(mode.getWidth(), mode.getHeight()).build());
			}			
			return result.toArray(new ScreenConfig[0]);
			
		} catch (LWJGLException e) {
			throw new ServiceException("unable to query display modes", e);
		}
	}
	
	private void insertConfig(List<ScreenConfig> configList, ScreenConfig config) {
		int resolution = config.getWidth() * config.getHeight();
		for (int i = 0; i < configList.size(); ++i) {
			ScreenConfig current = configList.get(i);
			if (current.getWidth() * current.getHeight() > resolution) {
				configList.add(i, config);
				return;
			}
		}
		configList.add(config);
	}
	
	public void setScreenMode(ScreenConfig mode) throws ServiceException {
		if (mode.isCloneDesktop()) {
			DisplayMode desktop = Display.getDesktopDisplayMode();
			setScreenMode(desktop.getWidth(), desktop.getHeight(), mode.isFullscreen());
		} else {
			setScreenMode(mode.getWidth(), mode.getHeight(), mode.isFullscreen());			
		}
		setTitle(mode.getTitle());
		setVSync(mode.isVsync());
	}
	
	public void setScreenMode(int width, int height, boolean fullscreen) throws ServiceException {

        this.logger.logDebug(LOGGING_SOURCE, "trying to switched to " 
        		+ width + " x " + height + " (" +  windowMode(fullscreen) + ")");
		
	    // return if requested DisplayMode is already set
	    if ((Display.getDisplayMode().getWidth() == width) && 
	    		(Display.getDisplayMode().getHeight() == height) && 
	    		(Display.isFullscreen() == fullscreen)) {
	    	
		    return;
	    }
		
	    try {
			DisplayMode targetDisplayMode = findDisplayMode(width, height);
			if (targetDisplayMode == null) {
				logger.logWarning(LOGGING_SOURCE, "unable to find compatible mode for " + 
						width + " x " + height + " (" + windowMode(fullscreen) + ")");
				
				throw new ServiceException("unable to find compatible display mode for " 
						+ width + " x " + height + " (" + windowMode(fullscreen) + ")");
			}
			
	        Display.setDisplayMode(targetDisplayMode);
	        Display.setFullscreen(fullscreen);
	        
	        this.config.setWidth(targetDisplayMode.getWidth());
	        this.config.setHeight(targetDisplayMode.getHeight());
	        this.config.setFullscreen(fullscreen);
	        assert(Display.getWidth() == this.config.getWidth());
	        assert(Display.getHeight() == this.config.getHeight());
	        assert(Display.isFullscreen() == this.config.isFullscreen());
	        
	        this.logger.logInfo(LOGGING_SOURCE, "switched to " 
	        		+ targetDisplayMode + " (" 
	        		+ (fullscreen ? "fullscreen" : "windowed") + ")");
	        
		} catch (LWJGLException e) {
			throw new ServiceException("unable to switch to requested display mode " 
						+ width + " x " + height + "( " + windowMode(fullscreen) + ")", e);
		}
	}

	private String windowMode(boolean fullscreen) {
		return fullscreen ? "fullscreen" : "windowed";
	}
	
	private DisplayMode findDisplayMode(int width, int height) throws LWJGLException {
    	DisplayMode targetDisplayMode = null;

		DisplayMode[] modes = Display.getAvailableDisplayModes();
		int freq = 0;

		for (int i=0;i<modes.length;i++) {
			DisplayMode current = modes[i];

			if ((current.getWidth() == width) && (current.getHeight() == height)) {
				if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
					if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
						targetDisplayMode = current;
						freq = targetDisplayMode.getFrequency();
					}
				}

				// if we've found a match for bpp and frequence against the 
				// original display mode then it's probably best to go for this one
				// since it's most likely compatible with the monitor
				if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
						(current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
					targetDisplayMode = current;
					break;
				}
			}
		}
		
		return targetDisplayMode;
	}
	
	@Deprecated
	public void setDisplayMode(int width, int height, boolean fullscreen) throws ServiceException {

	    // return if requested DisplayMode is already set
	    if ((Display.getDisplayMode().getWidth() == width) && 
	        (Display.getDisplayMode().getHeight() == height) && 
		(Display.isFullscreen() == fullscreen)) {
		    return;
	    }

	    try {
	    	DisplayMode targetDisplayMode = null;
			
	    	if (this.useOnlyFullscreenModes || fullscreen) {
	    		DisplayMode[] modes = Display.getAvailableDisplayModes();
	    		int freq = 0;

	    		for (int i=0;i<modes.length;i++) {
	    			DisplayMode current = modes[i];

	    			if ((current.getWidth() == width) && (current.getHeight() == height)) {
	    				if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
	    					if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
	    						targetDisplayMode = current;
	    						freq = targetDisplayMode.getFrequency();
	    					}
	    				}

	    				// if we've found a match for bpp and frequence against the 
	    				// original display mode then it's probably best to go for this one
	    				// since it's most likely compatible with the monitor
	    				if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
	    						(current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
	    					targetDisplayMode = current;
	    					break;
	    				}
	    			}
	    		}
	    	}
	    	else {
	    		targetDisplayMode = new DisplayMode(width,height);
	    	}

	        if (targetDisplayMode == null) {
	        	this.logger.logWarning("LOGGING_SOURCE", "Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen);
	            return;
	        }

	        Display.setDisplayMode(targetDisplayMode);
	        this.config.setWidth(targetDisplayMode.getWidth());
	        this.config.setHeight(targetDisplayMode.getHeight());
	        Display.setFullscreen(fullscreen);
	        this.config.setFullscreen(fullscreen);
	        this.logger.logNotice(LOGGING_SOURCE, "switched to " 
	        		+ targetDisplayMode + " (" 
	        		+ (fullscreen ? "fullscreen" : "windowed") + ")");
	    } catch (LWJGLException e) {
			throw new ServiceException("unable to setup display mode", e);
	    }
	}	

	public void setBackgroundColor(ReadableColor color) {
		GL11.glClearColor((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), (float) color.getAlpha());
	}
	
	public int createLayer() {
		this.layers.add(createNode());
		return this.layers.size() - 1;
	}
	
	public void ensuerNumOfLayers(int i) {
		while (numLayers() < i) {
			createLayer();
		}
	}
	
	public int numLayers() {
		return this.layers.size();
	}
	
	public SceneNode getLayer(int idx) {
		return this.layers.get(idx);
	}
	
	public SceneNode getRootNode() {
		return getLayer(0);
	}
	
	public SceneNode createNode() {
		SceneNode node = new SceneNode();
		
		return node;
	}
	
	public void destroyNode(SceneNode node) {
		if (this.layers.contains(node) || node == this.overlayRoot) {
			throw new IllegalArgumentException("root scene node must not be destroyed");
		}
		
		if (!node.getParent().removeNode(node)) {
			this.logger.logWarning(LOGGING_SOURCE, "attempt to destroy non-existing scene node");
		}
	}
	
	public void destroyAllSceneNodes() {
		for (SceneNode root : this.layers) {
			root.removeAllNodes();
		}
		this.overlayRoot.removeAllNodes();
	}
	
	public SceneNode getOverlayRoot() {
		return this.overlayRoot;
	}
	
	public Camera createCamera() {
		Camera camera = new Camera(getScreenWidth(),getScreenHeight());
		this.cameras.add(camera);
		
		return camera;
	}

	public void destroyCamera(Camera camera) {
		if (!this.cameras.remove(camera)) {
			this.logger.logWarning(LOGGING_SOURCE, "attempt to destroy non-existing camera");			
		}
	}

	public void destroyAllCameras() {
		this.cameras.clear();
	}
	
	public int numCameras() {
		return this.cameras.size();
	}
	
	public Camera getCamera(int idx) {
		return this.cameras.get(idx);
	}
	
	public int getScreenWidth() {
		assert(Display.getWidth() == this.config.getWidth());
		return this.config.getWidth();
	}

	public int getScreenHeight() {
		assert(Display.getHeight() == this.config.getHeight());
		return this.config.getHeight();
	}

	public double getAspectRatio() {
		assert(Display.getWidth() == this.config.getWidth());
		assert(Display.getHeight() == this.config.getHeight());
		return (double) this.config.getWidth() / (double) this.config.getHeight();
	}
	
	public void destroyAll() {
		destroyAllCameras();
		destroyAllSceneNodes();
		for (SceneNode node : this.layers) {
			node.removeAllVisuals();
		}
	}
		
	public boolean isValid() {
		return Display.isCreated() && (this.parent == null || GLContext.getCapabilities() != null);
	}

}
