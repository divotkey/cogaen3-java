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

import java.util.HashMap;
import java.util.Map;

import org.cogaen.name.CogaenId;
import org.cogaen.util.Bag;

public class Core {

	private static final Version VERSION = new Version(3, 1, 0);
	
	private Map<CogaenId, Service> services = new HashMap<CogaenId, Service>();
	private Bag<Updateable> updateables = new Bag<Updateable>();
	private boolean running = false;
	private double deltaTime;
	private double time;
	
	public Core() {
		// intentionally left empty
	}
	
	public boolean hasService(CogaenId serviceId) {
		return this.services.containsKey(serviceId);
	}
	
	public Service getService(CogaenId serviceId) {
		Service srv = this.services.get(serviceId);
		if (srv == null) {
			throw new RuntimeException("unknown service: " + serviceId);
		}
		
		return srv;
	}
	
	public void addService(Service service) {
		if (this.running) {
			throw new IllegalStateException();
		}
		
		Service oldService = this.services.put(service.getId(), service);
		if (oldService != null) {
			this.services.put(oldService.getId(), oldService);
			throw new RuntimeException("service with id " + oldService.getId() + " already added");
		}
	}
	
	public void removeService(CogaenId serviceId) {
		if (this.running) {
			throw new IllegalStateException();
		}		
		this.services.remove(serviceId);
	}
	
	public void startup() {
		for (Service service : this.services.values()) {
			if (service.getStatus() != Service.Status.STARTED) {
				startService(service);
			}
		}
		this.running = true;
	}
	
	private void startService(Service service) {
		for (int i = 0; i < service.numOfDependencies(); ++i) {
			Service dependency = this.services.get(service.getDependency(i));
			if (dependency == null) {
				throw new RuntimeException("unresolved dependency: " + service.getDependency(i));
			}
			if (dependency.getStatus() != Service.Status.STARTED) {
				startService(dependency);
			}
		}
		
		try {
			service.start(this);
		} catch (ServiceException e) {
			throw new RuntimeException("unable to start " + service.getName(), e);
		}
	}
	
	private void stopService(Service service) {
		for (Service srv : this.services.values()) {
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

	public void shutdown() {
		for (Service service : this.services.values()) {
			if (service.getStatus() != Service.Status.STOPPED) {
				stopService(service);
			}
		}
		this.running = false;
	}
	
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
	
	public void addUpdateable(Updateable updateable) {
		this.updateables.add(updateable);
	}
	
	public void removeUpdateable(Updateable updateable) {
		this.updateables.remove(updateable);
	}

	public double getDeltaTime() {
		return this.deltaTime;
	}
	
	public double getTime() {
		return this.time;
	}
	
	public Version getVersion() {
		return VERSION;
	}
	
}
