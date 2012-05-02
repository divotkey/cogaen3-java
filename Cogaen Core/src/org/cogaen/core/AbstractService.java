/* 
 -----------------------------------------------------------------------------
                    Cogaen - Component-based Game Engine V3
 -----------------------------------------------------------------------------
 This software is developed by the Cogaen Development Team. Please have a 
 look at our project home page for further details: http://www.cogaen.org
    
 - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
 Copyright (c) 2010-2012 Roman Divotkey

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

package org.cogaen.core;

import java.util.ArrayList;
import java.util.List;

import org.cogaen.name.CogaenId;

/**
 * This class provides default implementation for the {@code Service}
 * interface. The status of this service and a reference to core is handled as
 * well as dependent services.
 * <p>Developer need only subclass this abstract class and optionally 
 * override the following methods:
 * <ul>
 * <li>{@code doStart}</li>
 * <li>{@code doPause}</li>
 * <li>{@code doResume}</li>
 * <li>{@code doStop}</li>
 * </ul>
 * </p> 
 */
public abstract class AbstractService implements Service {

	private List<CogaenId> dependencies = new ArrayList<CogaenId>();
	private Status status;
	private Core core;
	
	/**
	 * Defines a {@code Service} that is initially set to stopped state.
	 */
	public AbstractService() {
		this.status = Status.STOPPED;
	}
	
	/**
	 * Adds the specified service as dependency to this service.
	 * 
	 * @param serviceId identifier of the service this service depends on
	 */
	protected final void addDependency(CogaenId serviceId) {
		this.dependencies.add(serviceId);
	}

	/**
	 * Removes the specified service as dependency from this service.
	 * 
	 * @param serviceId identifier of the service to be removed as dependency
	 */
	protected final void removeDependency(CogaenId serviceId) {
		this.dependencies.remove(serviceId);
	}
	
	/**
	 * Returns the core that administers this service.
	 * 
	 * @return the core
	 */
	public final Core getCore() {
		if (getStatus() == Status.STOPPED) {
			throw new IllegalStateException();
		}
		return this.core;
	}

	/**
	 * Derived classes can override this method in order to do whatever
	 * is necessary to enter <strong>paused state</strong>. 
	 */
	protected void doPause() {
		// intentionally left empty
	}
	
	/**
	 * Derived classes can override this method in order to do whatever
	 * is necessary to resume <strong>started state</strong>. 
	 */
	protected void doResume() {
		// intentionally left empty		
	}
	
	/**
	 * Derived classes can override this method in order to do whatever
	 * is necessary to enter <strong>started state</strong>. 
	 */
	protected void doStart() throws ServiceException {
		// intentionally left empty
	}

	/**
	 * Derived classes can override this method in order to do whatever
	 * is necessary to enter <strong>stopped state</strong>. 
	 */
	protected void doStop() {
		// intentionally left empty
	}
	
	@Override
	public final void start(Core core) throws ServiceException {
		if (this.status != Status.STOPPED) {
			throw new IllegalStateException("service " + getName() + " is not in stopped state");
		}
		this.core = core;
		this.status = Status.STARTED;
		doStart();
	}

	@Override
	public final void stop() {
		if (this.status != Status.STARTED && this.status != Status.PAUSED) {
			throw new IllegalStateException("service " + getName() + " is not in started or paused state");
		}
		doStop();
		this.status = Status.STOPPED;
	}

	@Override
	public final void pause() {
		if (this.status != Status.STARTED) {
			throw new IllegalStateException("service " + getName() + " is not in started state");
		}
		doPause();
		this.status = Status.PAUSED;
	}

	@Override
	public final void resume() {
		if (this.status != Status.PAUSED) {
			throw new IllegalStateException("service " + getName() + " is not in paused state");
		}
		doResume();
		this.status = Status.STARTED;
	}
	
	@Override
	public final Status getStatus() {
		return this.status;
	}
	
	@Override
	public final int numOfDependencies() {
		return this.dependencies.size();
	}

	@Override
	public final CogaenId getDependency(int idx) {
		return this.dependencies.get(idx);
	}

}
