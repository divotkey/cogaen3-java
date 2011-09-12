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

import org.cogaen.core.AbstractService;
import org.cogaen.core.ServiceException;
import org.cogaen.core.Updateable;
import org.cogaen.core.Service.Status;
import org.cogaen.event.EventService;
import org.cogaen.lwjgl.scene.SceneService;
import org.cogaen.name.CogaenId;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

public class InputService extends AbstractService implements Updateable {

	public static final CogaenId ID = new CogaenId("org.cogaen.lwjgl.input.InputService");
	public static final String NAME = "Cogaen LWJGL Input Service";

	private EventService evtSrv;
	
	public InputService() {
		addDependency(EventService.ID);
		addDependency(SceneService.ID);
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
		this.evtSrv = EventService.getInstance(getCore());
		try {
			Keyboard.create();
		} catch (LWJGLException e) {
			throw new ServiceException(e);
		}
		
		getCore().addUpdateable(this);
	}

	@Override
	protected void doStop() {
		if (getStatus() != Status.PAUSED) {
			getCore().removeUpdateable(this);
		}
		
		Keyboard.destroy();
		this.evtSrv = null;
		super.doStop();
	}
	
	@Override
	protected void doPause() {
		getCore().removeUpdateable(this);
		super.doPause();
	}

	@Override
	protected void doResume() {
		getCore().addUpdateable(this);
		super.doResume();
	}

	@Override
	public void update() {
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				this.evtSrv.dispatchEvent(new KeyPressedEvent(Keyboard.getEventKey()));
			} else {
				this.evtSrv.dispatchEvent(new KeyReleasedEvent(Keyboard.getEventKey()));				
			}
		}
	}
	
}
