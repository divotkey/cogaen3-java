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

package org.cogaen.lwjgl.input;

import org.cogaen.core.Core;
import org.cogaen.core.ServiceException;
import org.cogaen.core.UpdateableService;
import org.cogaen.event.EventService;
import org.cogaen.lwjgl.scene.SceneService;
import org.cogaen.name.CogaenId;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;

public class MouseService extends UpdateableService {

	public static final CogaenId ID = new CogaenId("org.cogaen.lwjgl.input.MouseService");
	public static final String NAME = "Cogaen LWJGL Mouse Service";
	
	private EventService evtSrv;
	private boolean buttons[];

	public static MouseService getInstance(Core core) {
		return (MouseService) core.getService(ID);
	}
	
	public MouseService() {
		super();
		addDependency(SceneService.ID);
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
			Mouse.create();
		} catch (LWJGLException e) {
			throw new ServiceException(e);
		}
		this.evtSrv = EventService.getInstance(getCore());
		this.buttons = new boolean[Mouse.getButtonCount()];
	}

	@Override
	protected void doStop() {
		super.doStop();
		this.evtSrv = null;
		Mouse.destroy();
	}

	@Override
	public void update() {
		this.evtSrv.dispatchEvent(new MouseUpdateEvent(Mouse.getX(), Mouse.getY()));

		for (int i = 0; i < this.buttons.length; ++i) {
			if (Mouse.isButtonDown(i)) {
				if (!this.buttons[i]) {
					this.buttons[i] = true;
					this.evtSrv.dispatchEvent(new MouseButtonPressedEvent(Mouse.getX(), Mouse.getY(), i));
				}
			} else if (this.buttons[i]) {
				this.buttons[i] = false;
				this.evtSrv.dispatchEvent(new MouseButtonReleasedEvent(Mouse.getX(), Mouse.getY(), i));
			}
		}
	}

	public void hideMouseCursor() {
		Mouse.setGrabbed(true);
	}
	
	public void showMouseCursor() {
		Mouse.setGrabbed(false);
	}
}
