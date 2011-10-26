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

package org.cogaen.resource;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cogaen.core.AbstractService;
import org.cogaen.core.Core;
import org.cogaen.core.ServiceException;
import org.cogaen.logging.LoggingService;
import org.cogaen.name.CogaenId;

public class ResourceService extends AbstractService {

	public static final CogaenId ID = new CogaenId("org.cogaen.resource.ResourceService");
	public static final String NAME = "Cogaen Resource Service";
	public static final String LOGGING_SOURCE = "RSRC";
	
	private Map<CogaenId, List<ResourceHandle>> groups = new HashMap<CogaenId, List<ResourceHandle>>();
	private Map<String, ResourceHandle> resourceMap = new HashMap<String, ResourceHandle>();
	private Map<ResourceHandle, String> invResourceMap = new HashMap<ResourceHandle, String>();
	private LoggingService logger;
	
	public static ResourceService getInstance(Core core) {
		return (ResourceService) core.getService(ID);
	}
	
	public ResourceService() {
		addDependency(LoggingService.ID);
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
		this.logger = LoggingService.getInstance(getCore());
	}

	@Override
	protected void doStop() {
		unloadAll();
		this.logger = null;
		super.doStop();
	}
	
	public URL getUrl(String name) {
		URL url = getClass().getResource(name);
		if (url == null) {
			url = getClass().getResource("/" + name);
		}
		
		return url;
	}
	
	public InputStream getStream(String name) {
		InputStream is = getClass().getResourceAsStream(name);
		
		if (is == null) {
			is = getClass().getResourceAsStream("/" + name);
		}
		
		return is;
	}
	
	public boolean hasGroup(CogaenId groupId) {
		return this.groups.containsKey(groupId);
	}
	
	public void createGroup(CogaenId groupId) {
		if (hasGroup(groupId)) {
			throw new RuntimeException("ambiguous group id " + groupId);			
		}
		
		this.groups.put(groupId, new ArrayList<ResourceHandle>());
	}
	
	public void declareResource(String name, CogaenId groupId, ResourceHandle handle) {
		ResourceHandle old = this.resourceMap.put(name, handle);
		if (old != null) {
			this.resourceMap.put(name, old);
			throw new RuntimeException("ambiguous resource id " + groupId);
		}
		assert(!this.invResourceMap.containsKey(handle));
		this.invResourceMap.put(handle, name);
		
		List<ResourceHandle> group = this.groups.get(groupId);
		if (group == null) {
			this.resourceMap.remove(name);
			throw new RuntimeException("resource group does not exist " + groupId);
		}
		
		group.add(handle);
	}
	
	public boolean isDeclared(String resourceId) {
		return this.resourceMap.containsKey(resourceId);
	}
	
	public void loadGroup(CogaenId groupId) {
		List<ResourceHandle> group = this.groups.get(groupId);
		if (group == null) {
			throw new RuntimeException("unonkown resource group " + groupId);
		}
		
		this.logger.logNotice(LOGGING_SOURCE, "preloading resource group " + groupId);
		for (ResourceHandle handle : group) {
			if (!handle.isLoaded()) {
				try {
					handle.load(getCore());
					this.logger.logInfo(LOGGING_SOURCE, "loaded resource " + this.invResourceMap.get(handle));
				} catch (ResourceException e) {
					this.logger.logWarning(LOGGING_SOURCE, "unable to load resource: " + e.getMessage());
				}
			}
		}
	}

	public void unloadGroup(CogaenId groupId) {
		List<ResourceHandle> group = this.groups.get(groupId);
		if (group == null) {
			throw new RuntimeException("unonkown resource group " + groupId);
		}
		
		this.logger.logNotice(LOGGING_SOURCE, "unloading resource group " + groupId);
		for (ResourceHandle handle : group) {
			if (handle.isLoaded()) {
				handle.unload(getCore());
			}
		}
	}
		
	public void unloadAll() {
		this.logger.logNotice(LOGGING_SOURCE, "unloading resources");
		for (ResourceHandle handle : this.resourceMap.values()) {
			if (handle.isLoaded()) {
				handle.unload(getCore());
			}
		}
	}
	
	public Object getResource(String name) {
		ResourceHandle handle = this.resourceMap.get(name);
		if (handle == null) {
			throw new RuntimeException("unknown resource " + name);
		}
		
		if (!handle.isLoaded()) {
			try {
				handle.load(getCore());
			} catch (ResourceException e) {
				this.logger.logWarning(LOGGING_SOURCE, "unable to load resource: " + e.getMessage());
			}
		}
		
		return handle.getResource();
	}
}
