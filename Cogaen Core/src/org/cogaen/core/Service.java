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

import org.cogaen.name.CogaenId;

/**
 * The interface for services to be administered by the service locator.
 * 
 * @see Core
 */
public interface Service {

	/**
	 * Represents the status of a service.
	 */
	public enum Status {
		/** Indicates that a service is not ready to be used and needs to be started. */
		STOPPED, 
		
		/** Indicates that a service has been started and is ready do be used. */
		STARTED, 
		
		/** Indicates that a service has been paused and can be resumed. */
		PAUSED};

	/**
	 * Returns the identifier of this service.
	 * 
	 * @return identifier of this service
	 */
	public CogaenId getId();
	
	/**
	 * Returns the name of this service.
	 * 
	 * @return human readable name of this service
	 */
	public String getName();
	
	/**
	 * Returns the status of this service.
	 * 
	 * @return the current status of this service
	 */
	public Status getStatus();
	
	/**
	 * Starts this service. After a successful call of this method the status
	 * of this service changes to {@code Status.STARTED}.
	 * This method should only be called if this service is in
	 * <strong>started state</strong>.
	 * 
	 * @param core service locator that administers this service
	 * @throws ServiceException in case this service could not be started
	 * @see #getStatus()
	 * @see Status
	 */
	public void start(Core core) throws ServiceException;
	
	/**
	 * Stops this service. After a successful call of this method the status of
	 * this service changes to {@code Status.STOPPED}
	 * This method should only be called if this service is in
	 * <strong>started state</strong>.
	 */
	public void stop();
	
	/**
	 * Pauses this service. After a successful call of this method the status
	 * of this service changes to {@code Status.PAUSED}.
	 * This method should only be called if this service is in 
	 * <strong>started state</strong>.
	 */
	public void pause();
	
	/**
	 * Resumes this service. After a successful call of this method the status
	 * of this service changes to {@code Status.STARTED}.
	 * This method should only be called if this service is in
	 * <strong>paused state</strong>.
	 */
	public void resume();
	
	/**
	 * Returns the number of services this service depends on.
	 * 
	 * @return number of dependencies
	 * @see #getDependency
	 */
	public int numOfDependencies();
	
	/**
	 * Returns the identifier of a service this services depends on.
	 * 
	 * <p>To query all services this service depends on, use the following
	 * code:
	 * <pre>
	 * for (int i = 0; i < service.numOfDependencies(); ++i) {
	 *     CogaenId serviceId = service.getDependency(i);
	 *     doSomeThing(serviceId);
	 * }
	 * </pre>
	 * </p>
	 * @param idx index of the dependency
	 * @return identifier of specified dependency
	 */
	public CogaenId getDependency(int idx);
}
