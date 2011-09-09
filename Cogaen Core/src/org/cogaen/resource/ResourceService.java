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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cogaen.core.AbstractService;
import org.cogaen.core.ServiceException;
import org.cogaen.name.CogaenId;

public class ResourceService extends AbstractService {

	public static final CogaenId ID = new CogaenId("org.cogaen.resource.ResourceService");
	public static final String NAME = "Cogaen Resource Service";
	
	private Map<CogaenId, List<ResourceHandle>> groups = new HashMap<CogaenId, List<ResourceHandle>>();
	private Map<CogaenId, ResourceHandle> resources = new HashMap<CogaenId, ResourceHandle>();
	
	
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
		// TODO Auto-generated method stub
		super.doStart();
	}

	@Override
	protected void doStop() {
		// TODO Auto-generated method stub
		super.doStop();
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
	
	public void declareResource(CogaenId resourceId, CogaenId groupId, ResourceHandle handle) {
		List<ResourceHandle> group = this.groups.get(groupId);
		if (group == null) {
			throw new RuntimeException("resource does not exist " + groupId);
		}
		
		group.add(handle);
	}
	
	public void loadGroup(CogaenId groupId) {
		
	}

	public void unloadGroup(CogaenId groupId) {
		
	}
	
	public boolean isDeclared(CogaenId resourceId) {
		return this.resources.containsKey(resourceId);
	}
	
	public Object getResource(CogaenId resourceId) {
		return null;
	}
}
