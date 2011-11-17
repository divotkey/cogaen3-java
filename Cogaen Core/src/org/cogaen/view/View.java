package org.cogaen.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cogaen.core.Core;
import org.cogaen.core.Engageable;
import org.cogaen.name.CogaenId;

public class View implements Engageable {

	private Core core;
	private boolean engaged;

	private Map<CogaenId, EntityRepresentation> representationsMap = new HashMap<CogaenId, EntityRepresentation>();
	private List<EntityRepresentation> representations = new ArrayList<EntityRepresentation>();
	
	public View(Core core) {
		this.core = core;
	}
	
	public void registerResources(CogaenId groupId) {
		// intentionally left empty
	}
	
	public final void addRepresentation(CogaenId entityId, EntityRepresentation er) {
		EntityRepresentation old = this.representationsMap.put(entityId, er);
		if (old != null) {
			this.representationsMap.put(entityId, old);
			throw new RuntimeException("ambiguous entity id " + entityId);
		}
		assert(!this.representations.contains(er));
		this.representations.add(er);
		er.engage();
	}
	
	public final void removeRepresentation(CogaenId entityId) {
		EntityRepresentation er = this.representationsMap.remove(entityId);
		if (er == null) {
			throw new RuntimeException("unknown entity id" + entityId);			
		}
		this.representations.remove(er);
		er.disengage();
	}
	
	public final boolean hasRepresentation(CogaenId entityId) {
		return this.representationsMap.containsKey(entityId);
	}
	
	public final EntityRepresentation getRepresentation(CogaenId entityId) {
		return this.representationsMap.get(entityId);
	}
	
	public final void removeAllRepresentations() {
		for (EntityRepresentation er : this.representationsMap.values()) {
			er.disengage();
		}
		this.representationsMap.clear();
		this.representations.clear();
	}
		
	public final int numOfRepresentation() {
		return this.representations.size();
	}
	
	public final EntityRepresentation getRepresentation(int idx) {
		return this.representations.get(idx);
	}
	
	@Override
	public void engage() {
		this.engaged = true;
	}

	@Override
	public void disengage() {
		removeAllRepresentations();
		this.engaged = false;
	}

	@Override
	public final boolean isEngaged() {
		return this.engaged;
	}
	
	public final Core getCore() {
		return this.core;
	}

}
