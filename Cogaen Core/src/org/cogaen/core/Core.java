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

package org.cogaen.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cogaen.name.CogaenId;
import org.cogaen.util.Bag;

/**
 * The core is the central service locator in Cogaen used to retrieve all kind
 * of registered services.
 */
public class Core {

	private static final Version VERSION = new Version(3, 1, 1);
	
	private Map<CogaenId, Service> servicesMap = new HashMap<CogaenId, Service>();
	private List<Service> services = new ArrayList<Service>();
	private List<CoreListener> listeners = new ArrayList<CoreListener>();
	private Bag<Updateable> updateables = new Bag<Updateable>();
	private boolean running = false;
	private double deltaTime;
	private double time;
	
	/**
	 * Creates a new instance.
	 */
	public Core() {
		// intentionally left empty
	}

	/**
	 * Queries if the specified service exists.
	 * 
	 * @param serviceId {@link CogaenId} of the service that should be queried.
	 * @return {@code true} if the specified service exists, {@code false} otherwise.
	 */
	public boolean hasService(CogaenId serviceId) {
		return this.servicesMap.containsKey(serviceId);
	}
	
	/**
	 * Retrieves the service with the specified identifier.
	 * 
	 * @param serviceId identifier of the service that should be retrieved
	 * @return service specified by the given identifier
	 * @throws RuntimeException if no service with the specified identifier 
	 * is available
	 */
	public Service getService(CogaenId serviceId) {
		Service srv = this.servicesMap.get(serviceId);
		if (srv == null) {
			throw new RuntimeException("unknown service: " + serviceId);
		}
		
		return srv;
	}
	
	public void addListener(CoreListener listener) {
		if (this.listeners.contains(listener)) {
			throw new RuntimeException("listener already in list: " + listener.getClass().getName());
		}
		this.listeners.add(listener);
	}
	
	public void removeListener(CoreListener listener) {
		if ( !this.listeners.remove(listener) ) {
			throw new RuntimeException("listener not registered: " + listener.getClass().getName());
		}
	}
	
	/**
	 * Adds a service. 
	 * 
	 * @param service Service that should be added.
	 * throws IllegalStateException if this core has been started.
	 * throws RuntimeException if the id of the given service is ambiguous.
	 */
	public void addService(Service service) {
		if (this.running) {
			throw new IllegalStateException();
		}
		
		Service oldService = this.servicesMap.put(service.getId(), service);
		if (oldService != null) {
			this.servicesMap.put(oldService.getId(), oldService);
			throw new RuntimeException("service with id " + oldService.getId() + " already added");
		}
		this.services.add(service);
	}
	
	/**
	 * Removes the specified service. Services can only be removed before a call to {@link startup} or
	 * after a call of {@link shutdown}.
	 * 
	 * @param serviceId {@link CogaenId} of the service to be removed.
	 * throws IllegalStateException if this core is running state.
	 */
	public void removeService(CogaenId serviceId) {
		if (this.running) {
			throw new IllegalStateException();
		}		
		
		Service service = this.servicesMap.remove(serviceId);
		this.services.remove(service);
	}
	
	/**
	 * Starts all services.
	 * After a successful call of this method this core is in 'running' state.
	 */
	public void startup() throws ServiceException {
		for (Service service : this.servicesMap.values()) {
			if (service.getStatus() != Service.Status.STARTED) {
				startService(service);
			}
		}
		this.running = true;
	}
	
	private void startService(Service service) throws ServiceException {
		for (int i = 0; i < service.numOfDependencies(); ++i) {
			Service dependency = this.servicesMap.get(service.getDependency(i));
			if (dependency == null) {
				throw new RuntimeException("unresolved dependency: " + service.getDependency(i));
			}
			if (dependency.getStatus() != Service.Status.STARTED) {
				startService(dependency);
			}
		}
		
		service.start(this);
	}
	
	private void stopService(Service service) {
		for (Service srv : this.servicesMap.values()) {
			if (srv != service) {
				if (isDependent(srv, service.getId()) && srv.getStatus() != Service.Status.STOPPED) {
					stopService(srv);
				}
			}
		}
		service.stop();
	}
	
	private boolean isDependent(Service s1, CogaenId serviceId) {
		for (int i = 0; i < s1.numOfDependencies(); ++i) {
			if (s1.getDependency(i).equals(serviceId)) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Stops all services.
	 * After a successful call to this method this core is in 'stopped' state.
	 */
	public void shutdown() {
		for (CoreListener listener : this.listeners) {
			listener.shutdownInitiated();
		}
		
		for (Service service : this.servicesMap.values()) {
			if (service.getStatus() != Service.Status.STOPPED) {
				stopService(service);
			}
		}
		this.running = false;
	}
	
	/**
	 * Updates this core. 
	 * The game time and all registered {@code Updateable} objects will be updated.
	 * 
	 * @param dt elapsed time in seconds.
	 * @see addUpdateable
	 */
	public void update(double dt) {
		this.deltaTime = dt;
		this.time += dt;
		
		if (!this.running) {
			throw new IllegalStateException();
		}

		for (this.updateables.reset(); this.updateables.hasNext();) {
			this.updateables.next().update();
		}
	}
	
	/**
	 * Adds an Updateable to the list of objects to be updated when {@link update} is called.
	 * In most cases a Service will implement the interface {@code Updateable} and 
	 * register itself as as updatable, if the services needs to be updated each game loop cycle.
	 * 
	 * @param updateable {@code Updateable} to be added
	 * @see Updateable
	 */
	public void addUpdateable(Updateable updateable) {
		this.updateables.add(updateable);
	}
	
	/**
	 * Removes the given updateable from the list of objects to be updated.
	 * @param updateable {@code Updateable} to be removed
	 */
	public void removeUpdateable(Updateable updateable) {
		this.updateables.remove(updateable);
	}

	/**
	 * Returns the last elapsed time in secondes.
	 * @return elapsed time in seconds.
	 */
	public double getDeltaTime() {
		return this.deltaTime;
	}
	
	/**
	 * Retrieves the current absolute game time.
	 * 
	 * @return game time in seconds.
	 */
	public double getTime() {
		return this.time;
	}

	/**
	 * Retrieves the {@link Version} number of this core.
	 * 
	 * @return version number of this core.
	 */
	public Version getVersion() {
		return VERSION;
	}
	
	/**
	 * Returns the number of added services. 
	 * 
	 * @return number of services.
	 */
	public int numServices() {
		return this.servicesMap.size();
	}

	/**
	 * Retrieves the specified service.
	 * 
	 * @param idx index of service to Retrieve.
	 * @return service with specified index.
	 */
	public Service getService(int idx) {
		return this.services.get(idx);
	}
}
