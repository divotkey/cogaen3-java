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

import org.cogaen.core.AbstractService;
import org.cogaen.core.Core;
import org.cogaen.core.ServiceException;
import org.cogaen.event.EventService;
import org.cogaen.event.SimpleEvent;
import org.cogaen.logging.LoggingService;
import org.cogaen.name.CogaenId;
import org.cogaen.property.PropertyService;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.util.Log;

public class SceneService extends AbstractService {

	public static final CogaenId ID = new CogaenId("org.cogaen.lwjgl.scene.SceneService");
	public static final String NAME = "Cogaen LWJGL Scene Service";
	public static final String LOGGING_SOURCE = "LWSC";
	public static final CogaenId WINDOW_CLOSE_REQUEST = new CogaenId("WindowCloseRequest");
	private static final String WIDTH_PROP = "lwjgl.screenwidth";
	private static final String HEIGHT_PROP = "lwjgl.screenheight";
	private static final String FS_PROP = "lwjgl.fullscreen";
	
	private EventService evtSrv;
	private int width;
	private int height;
	private boolean fullscreen;
	private boolean useProperties;
	private SceneNode root;
	private List<Camera> cameras = new ArrayList<Camera>();
	private LoggingService logger;
	
	public static SceneService getInstance(Core core) {
		return (SceneService) core.getService(ID);
	}
	
	public SceneService(int width, int height, boolean fs, boolean useProperties) {
		if (useProperties) {
			addDependency(PropertyService.ID);
		}
		addDependency(EventService.ID);
		addDependency(LoggingService.ID);
		
		this.useProperties = true;
		this.width = width;
		this.height = height;
		this.fullscreen = fs;
		
		this.root = new SceneNode();

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

	@Override
	protected void doStart() throws ServiceException {
		super.doStart();
		
		if (this.useProperties) {
			PropertyService prpSrv = PropertyService.getInstance(getCore());
			setDisplayMode(prpSrv.getIntProperty(WIDTH_PROP, this.width),
					prpSrv.getIntProperty(HEIGHT_PROP, this.height),
					prpSrv.getBoolProperty(FS_PROP, this.fullscreen));
		} else {
			setDisplayMode(this.width, this.height, this.fullscreen);
		}
		try {
			Display.create();
			this.evtSrv = EventService.getInstance(getCore());
		} catch (LWJGLException e) {
			throw new ServiceException(e);
		}		
		Display.setVSyncEnabled(true);
//		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	@Override
	protected void doStop() {
		this.evtSrv = null;
		Display.destroy();
		super.doStop();
	}
	
	public void renderScene() {

	    // Clear the screen and depth buffer
	    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);	
		
		for (Camera camera : cameras) {
			if (!camera.isActive()) {
				continue;
			}
			
			camera.applyTransform();
		    this.root.render();
		}
		
//		GL11.glMatrixMode(GL11.GL_PROJECTION);
//		GL11.glLoadIdentity();
//		GL11.glOrtho(0, 800, 600, 0, 1, -1);
//		GL11.glMatrixMode(GL11.GL_MODELVIEW);		
		
		
//	    Texture texture = (Texture) ResourceService.getInstance(getCore()).getResource(new CogaenId("test"));
//	    texture.bind();
	    
	    
	    // set the color of the quad (R,G,B,A)
//	    GL11.glColor3f(0.5f,0.5f,1.0f);
	    	
	    // draw quad
//	    GL11.glBegin(GL11.GL_QUADS);
//	    GL11.glTexCoord2f(0,0);
//        GL11.glVertex2f(100,100);
//
//        GL11.glTexCoord2f(1,0);
//        GL11.glVertex2f(100+200,100);
//	
//        GL11.glTexCoord2f(1,1);
//        GL11.glVertex2f(100+200,100+200);
//        
//        GL11.glTexCoord2f(0,1);
//		GL11.glVertex2f(100,100+200);
//	    GL11.glEnd();
 
	    Display.update();		
	
		if (Display.isCloseRequested()) {
			this.evtSrv.dispatchEvent(new SimpleEvent(WINDOW_CLOSE_REQUEST));
		}
	}

	public void setTitle(String windowTitle) {
		Display.setTitle(windowTitle);
	}

	public void setFullscreen(boolean fullscreen) throws ServiceException {
		try {
			Display.setFullscreen(fullscreen);
		} catch (LWJGLException e) {
			throw new ServiceException("unable to switch to fullscreen mode", e);
		}
	}
	
	public void setDisplayMode(int width, int height, boolean fullscreen) throws ServiceException {

	    // return if requested DisplayMode is already set
	    if ((Display.getDisplayMode().getWidth() == width) && 
	        (Display.getDisplayMode().getHeight() == height) && 
		(Display.isFullscreen() == fullscreen)) {
		    return;
	    }

	    try {
	        DisplayMode targetDisplayMode = null;
			
		if (fullscreen) {
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
	        } else {
	            targetDisplayMode = new DisplayMode(width,height);
	        }

	        if (targetDisplayMode == null) {
	            System.out.println("Failed to find value mode: "+width+"x"+height+" fs="+fullscreen);
	            return;
	        }

	        Display.setDisplayMode(targetDisplayMode);
	        Display.setFullscreen(fullscreen);
				
	    } catch (LWJGLException e) {
			throw new ServiceException("unable to setup display mode", e);
	    }
	}	
	
	public SceneNode getRootNode() {
		return this.root;
	}
	
	public SceneNode createNode() {
		SceneNode node = new SceneNode();
		
		return node;
	}
	
	public void destroyNode(SceneNode node) {
		if (node == this.root) {
			throw new IllegalArgumentException("root scenen node must not be destroyed");
		}
		if (!node.getParent().removeNode(node)) {
			this.logger.logWarning(LOGGING_SOURCE, "attempt to destroy non-existing scene node");
		}
	}
	
	public void destroyAllSceneNodes() {
		this.root.removeAllNodes();
	}
	
	public Camera createCamera() {
		DisplayMode mode = Display.getDisplayMode();
		Camera camera = new Camera(mode.getWidth(), mode.getHeight());
		this.cameras.add(camera);
		
		return camera;
	}

	public void destroyCamera(Camera camera) {
		if (this.cameras.remove(camera)) {
			this.logger.logWarning(LOGGING_SOURCE, "attempt to destroy non-existing camera");			
		}
	}

	public void destroyAllCameras() {
		this.cameras.clear();
	}
	
	public int getScreenWidth() {
		return Display.getDisplayMode().getWidth();
	}

	public int getScreenHeight() {
		return Display.getDisplayMode().getHeight();
	}

	
	public void destroyAll() {
		destroyAllCameras();
		destroyAllSceneNodes();
	}
}
