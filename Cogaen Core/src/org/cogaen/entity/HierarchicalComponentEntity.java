package org.cogaen.entity;

import java.util.ArrayList;
import java.util.List;

import org.cogaen.core.Core;
import org.cogaen.event.Event;
import org.cogaen.name.CogaenId;

public class HierarchicalComponentEntity extends ComponentEntity {

	private List<ComponentEntity> subEntities = new ArrayList<ComponentEntity>();
	
	public HierarchicalComponentEntity(Core core, CogaenId id, CogaenId typeId) {
		super(core, id, typeId);
	}

	@Override
	public void engage() {
		super.engage();
		for (ComponentEntity entity : this.subEntities) {
			entity.engage();
		}
	}

	@Override
	public void disengage() {
		for (ComponentEntity entity : this.subEntities) {
			entity.disengage();
		}
		super.disengage();
	}

	@Override
	public void handleEvent(Event event) {
		super.handleEvent(event);
		for (ComponentEntity entity : this.subEntities) {
			entity.handleEvent(event);
		}
	}
	
	public void addEntity(ComponentEntity entity) {
		this.subEntities.add(entity);
	}
}
