package org.cogaen.lwjgl.scene;

import org.cogaen.core.AbstractService;
import org.cogaen.core.Core;
import org.cogaen.core.ServiceException;
import org.cogaen.event.EventService;
import org.cogaen.event.SimpleEvent;
import org.cogaen.name.CogaenId;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class SceneService extends AbstractService {

	public static final CogaenId ID = new CogaenId("org.cogaen.lwjgl.scene.SceneService");
	public static final String NAME = "Cogaen LWJGL Scene Service";
	public static final String LOGGING_SOURCE = "LWSC";
	public static final CogaenId WINDOW_CLOSE_REQUEST = new CogaenId("WindowCloseRequest");
	
	private EventService evtSrv;
	
	public static SceneService getInstance(Core core) {
		return (SceneService) core.getService(ID);
	}
	
	public SceneService(boolean fullscreen) throws ServiceException {
		setFullscreen(fullscreen);
	}
	
	public SceneService(int width, int height) throws ServiceException {
		setResolution(width, height);
	}
	
	public SceneService(int width, int height, boolean fullscreen) throws ServiceException {
		setResolution(width, height);
		setFullscreen(fullscreen);
	}
	
	public SceneService() {
		addDependency(EventService.ID);
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
		
		
		try {
			Display.create();
			this.evtSrv = EventService.getInstance(getCore());
		} catch (LWJGLException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	protected void doStop() {
		this.evtSrv = null;
		Display.destroy();
		super.doStop();
	}

	public void setResolution(int width, int height) throws ServiceException {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
		} catch (LWJGLException e) {
			throw new ServiceException("invalid resolution " + width + "x" + height, e);
		}
	}

	public void renderScene() {
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

}
