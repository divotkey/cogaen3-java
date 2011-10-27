package org.cogaen.view;

import java.util.HashMap;
import java.util.Map;

import org.cogaen.core.Core;
import org.cogaen.core.Engageable;
import org.cogaen.name.CogaenId;

public class View implements Engageable {

	private Core core;
	private boolean engaged;

	private Map<CogaenId, EntityRepresentation> representations = new HashMap<CogaenId, EntityRepresentation>();
	
	public View(Core core) {
		this.core = core;
	}
	
	public void registerResources(CogaenId groupId) {
		// intentionally left empty
	}
	
	public final void addRepresentation(CogaenId entityId, EntityRepresentation er) {
		EntityRepresentation old = this.representations.put(entityId, er);
		if (old != null) {
			this.representations.put(entityId, old);
			throw new RuntimeException("ambiguous entity id " + entityId);
		}
		
		er.engage();
	}
	
	public final void removeRepresentation(CogaenId entityId) {
		EntityRepresentation er = this.representations.remove(entityId);
		if (er == null) {
			throw new RuntimeException("unknown entity id" + entityId);			
		}
		er.disengage();
	}
	
	public final boolean hasRepresentation(CogaenId entityId) {
		return this.representations.containsKey(entityId);
	}
	
	public final EntityRepresentation getRepresentation(CogaenId entityId) {
		return this.representations.get(entityId);
	}
	
	public final void removeAllRepresentations() {
		for (EntityRepresentation er : this.representations.values()) {
			er.disengage();
		}
		this.representations.clear();
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
