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
 * The core class is the central service locator used to retrieve services.
 * 
 * The main purpose of this class is to provide a centralized access to
 * services. Beside this it keeps track about the absolute game time and
 * updates registered updatable objects.
 * 
 * <p>An instance of a core can be in two differnt states:
 * <strong>stopped</strong> and <strong>running</strong>.
 * A newly created core will be initially in <strong>stopped state</strong>. Before
 * any of the added services can be used the core (and thus its services) must
 * be started by a call to {@code startup}.</p> 
 * 
 * @see Service
 * @see Updatable
 */
public class Core {

	private static final Version VERSION = new Version(3, 1, 1);
	
	private Map<CogaenId, Service> servicesMap = new HashMap<CogaenId, Service>();
	private List<Service> services = new ArrayList<Service>();
	private List<CoreListener> listeners = new ArrayList<CoreListener>();
	private Bag<Updatable> updatables = new Bag<Updatable>();
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
	 * @param serviceId identifier of the service that should be queried
	 * @return {@code true} if the specified service exists, {@code false}
	 * otherwise
	 */
	public boolean hasService(CogaenId serviceId) {
		return this.servicesMap.containsKey(serviceId);
	}
	
	/**
	 * Retrieves a service.
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
	
	/**
	 * Adds the specified core listener.
	 * 
	 * @param listener the core listener to be added.
	 */
	public void addListener(CoreListener listener) {
		if (this.listeners.contains(listener)) {
			throw new RuntimeException("listener already in list: " + listener.getClass().getName());
		}
		this.listeners.add(listener);
	}
	
	/**
	 * Removes the specified core listener.
	 * 
	 * @param listener the core listener to be removed
	 */
	public void removeListener(CoreListener listener) {
		if ( !this.listeners.remove(listener) ) {
			throw new RuntimeException("listener not registered: " + listener.getClass().getName());
		}
	}
	
	/**
	 * Adds a service.  New services can only be added if this
	 * core is not in <strong>running state</strong>.
	 * 
	 * @param service the service to be added
	 * @throws IllegalStateException if this core is in running state
	 * @throws RuntimeException if the identifier of the specified service is
	 * ambiguous
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
	 * Removes a service. Services can only be removed before if this core is
	 * not in <strong>running state</strong>.
	 * 
	 * @param serviceId identifier of the service to be removed.
	 * @throws IllegalStateException if this core is running state.
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
	 * After a successful call of this method this core is in
	 * <strong>running state</strong>.
	 * 
	 * @throws ServiceException in case at least one service could not be
	 * started
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
	 * Stops all services.  After a successful call to this method this core is
	 * in <strong>stopped state</strong>.
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
	 * The game time and all updatable objects will be updated.
	 * 
	 * @param dt elapsed time in seconds
	 * @see #addUpdatable
	 * @see Updatable
	 */
	public void update(double dt) {
		this.deltaTime = dt;
		this.time += dt;
		
		if (!this.running) {
			throw new IllegalStateException();
		}

		for (this.updatables.reset(); this.updatables.hasNext();) {
			this.updatables.next().update();
		}
	}
	
	/**
	 * Adds the specified updatable object. All updatable objects will be 
	 * updated whenever the method {@code update} of this core is called.
	 * 
	 * <p>In most cases services that need to be updated within the game loop
	 * cycle implement the interface
	 * {@code Updatable}. When such a service is started it adds itself as 
	 * updatable object and removes itself when it is stopped.</p>
	 * 
	 * @param updatable the updatable object to be added
	 * @see Updatable
	 */
	public void addUpdatable(Updatable updatable) {
		this.updatables.add(updatable);
	}
	
	/**
	 * Removes the specified updatable object.
	 * @param updatable the updatable object to be removed
	 */
	public void removeUpdateable(Updatable updatable) {
		this.updatables.remove(updatable);
	}

	/**
	 * Returns the last elapsed time in seconds.
	 * @return elapsed time in seconds
	 */
	public double getDeltaTime() {
		return this.deltaTime;
	}
	
	/**
	 * Retrieves the current absolute game time.
	 * 
	 * @return absolute game time in seconds
	 */
	public double getTime() {
		return this.time;
	}

	/**
	 * Retrieves the version of this core.
	 * 
	 * @return version of this core
	 */
	public Version getVersion() {
		return VERSION;
	}
	
	/**
	 * Returns the number of services. 
	 * 
	 * @return number of services.
	 */
	public int numServices() {
		return this.servicesMap.size();
	}

	/**
	 * Retrieves the specified service.
	 * 
	 * @param idx index of service to Retrieve
	 * @return service with specified index
	 */
	public Service getService(int idx) {
		return this.services.get(idx);
	}

	/**
	 * Queries this core if it is in running state.
	 * @return {@code true} if this core is in <strong>running state</strong>,
	 * {@code false} otherwise
	 */
	public boolean isRunning() {
		return this.running;
	}
}
