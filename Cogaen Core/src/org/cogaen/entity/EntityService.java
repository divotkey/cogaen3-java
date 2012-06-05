package org.cogaen.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cogaen.core.Core;
import org.cogaen.core.Updatable;
import org.cogaen.core.UpdateableService;
import org.cogaen.name.CogaenId;
import org.cogaen.util.Bag;

public class EntityService extends UpdateableService {

	public static final CogaenId ID = new CogaenId("org.cogaen.entity.EntityService");
	public static final String NAME = "Cogaen Entity Service";
	
	private Map<CogaenId, Entity> entitiesMap = new HashMap<CogaenId, Entity>();
	private List<Entity> entities = new ArrayList<Entity>();
	private List<Entity> entitiesToRemove = new ArrayList<Entity>();
	private Bag<Updatable> updateables = new Bag<Updatable>();
	public static EntityService getInstance(Core core) {
		return (EntityService) core.getService(ID);
	}
	
	public EntityService() {
		// intentionally left empty
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
	public void update() {
		for (Entity entity : this.entitiesToRemove) {
			this.entitiesMap.remove(entity.getId());
			this.entities.remove(entity);
			entity.disengage();
		}
		this.entitiesToRemove.clear();
		
		for (updateables.reset(); updateables.hasNext();) {
			updateables.next().update();
		}
	}
	
	public void addEntity(Entity entity) {
		CogaenId id = entity.getId();
		Entity old = this.entitiesMap.put(id, entity);
		if (old != null) {
			this.entitiesMap.put(id, old);
			throw new RuntimeException("ambiguous entity id " + id);
		}
		this.entities.add(entity);
		entity.engage();
	}
	
	public void removeEntity(CogaenId entityId) {
		this.entitiesToRemove.add(getEntity(entityId));
	}
	
	public boolean hasEntity(CogaenId entityId) {
		return this.entitiesMap.containsKey(entityId);
	}
	
	public void removeAllEntities() {
		for (Entity entity : this.entitiesMap.values()) {
			entity.disengage();
		}
		this.entitiesMap.clear();
		this.entities.clear();
	}
	
	public Entity getEntity(CogaenId entityId) {
		Entity entity = this.entitiesMap.get(entityId);
		
		if (entity == null) {
			throw new RuntimeException("unknown entity " + entityId);
		}
		
		return entity;
	}
	
	public Entity getEntity(int idx) {
		return this.entities.get(idx);
	}

	public void addUpdatable(Updatable updateable) {
		this.updateables.add(updateable);
	}
	
	public void removeUpdatable(Updatable updateable) {
		this.updateables.remove(updateable);
	}	
	
	public int numEntities() {
		return this.entitiesMap.size();
	}
}
