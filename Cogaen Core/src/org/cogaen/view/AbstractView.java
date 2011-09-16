package org.cogaen.view;

import java.util.HashMap;
import java.util.Map;

import org.cogaen.core.Core;
import org.cogaen.core.Engageable;
import org.cogaen.name.CogaenId;

public class AbstractView implements Engageable {

	private Core core;
	private boolean engaged;

	private Map<CogaenId, EntityRepresentation> representations = new HashMap<CogaenId, EntityRepresentation>();
	
	public AbstractView(Core core) {
		this.core = core;
	}
	
	public void addRepresentation(CogaenId entityId, EntityRepresentation er) {
		EntityRepresentation old = this.representations.put(entityId, er);
		if (old != null) {
			this.representations.put(entityId, old);
			throw new RuntimeException("ambiguous entity id " + entityId);
		}
		
		er.engage();
	}
	
	public void removeRepresentation(CogaenId entityId) {
		EntityRepresentation er = this.representations.remove(entityId);
		if (er == null) {
			throw new RuntimeException("unknown entity id" + entityId);			
		}
		er.disengage();
	}
	
	public boolean hasRepresentation(CogaenId entityId) {
		return this.representations.containsKey(entityId);
	}
	
	public EntityRepresentation getRepresentation(CogaenId entityId) {
		return this.representations.get(entityId);
	}
	
	public void removeAllRepresentations() {
		for (EntityRepresentation er : this.representations.values()) {
			er.disengage();
		}
		this.representations.clear();
	}
	
	public void purgeRepresentation() {
		this.representations.clear();
	}
	
	@Override
	public void engage() {
		this.engaged = true;
	}

	@Override
	public void disengage() {
		purgeRepresentation();
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
