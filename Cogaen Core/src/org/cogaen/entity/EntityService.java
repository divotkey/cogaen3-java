package org.cogaen.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cogaen.core.AbstractService;
import org.cogaen.core.Core;
import org.cogaen.core.ServiceException;
import org.cogaen.core.Updateable;
import org.cogaen.name.CogaenId;
import org.cogaen.util.Bag;

public class EntityService extends AbstractService implements Updateable{

	public static final CogaenId ID = new CogaenId("org.cogaen.entity.EntityService");
	public static final String NAME = "Cogaen Entity Service";
	
	private Map<CogaenId, Entity> entities = new HashMap<CogaenId, Entity>();
	private List<Entity> entitiesToRemove = new ArrayList<Entity>();
	private Bag<Updateable> updateables = new Bag<Updateable>();
//	private List<Updateable> updateables = new ArrayList<Updateable>();
//	private List<Entity> entititesToAdd = new ArrayList<Entity>();
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
			this.entities.remove(entity.getId());
			entity.disengage();
		}
		this.entitiesToRemove.clear();
		
		for (updateables.reset(); updateables.hasNext();) {
			updateables.next().update();
		}
	}
	
	@Override
	protected void doPause() {
		getCore().removeUpdateable(this);
		super.doPause();
	}

	@Override
	protected void doResume() {
		super.doResume();
		getCore().addUpdateable(this);
	}

	@Override
	protected void doStart() throws ServiceException {
		super.doStart();
		getCore().addUpdateable(this);
	}

	@Override
	protected void doStop() {
		if (getStatus() != Status.PAUSED) {
			getCore().removeUpdateable(this);
		}
		super.doStop();
	}
	
	public void addEntity(Entity entity) {
		CogaenId id = entity.getId();
		Entity old = this.entities.put(id, entity);
		if (old != null) {
			this.entities.put(id, old);
			throw new RuntimeException("ambiguous entity id " + id);
		}
		entity.engage();
	}
	
	public void removeEntity(CogaenId entityId) {
		this.entitiesToRemove.add(getEntity(entityId));
	}
	
	public boolean hasEntity(CogaenId entityId) {
		return this.entities.containsKey(entityId);
	}
	
	public void removeAllEntities() {
		for (Entity entity : this.entities.values()) {
			entity.disengage();
		}
		this.entities.clear();
	}
	
	public Entity getEntity(CogaenId entityId) {
		Entity entity = this.entities.get(entityId);
		
		if (entity == null) {
			throw new RuntimeException("unknown entity " + entityId);
		}
		
		return entity;
	}

	public void addUpdateable(Updateable updateable) {
		this.updateables.add(updateable);
	}
	
	public void removeUpdateable(Updateable updateable) {
		this.updateables.remove(updateable);
	}	
	
	public int numEntities() {
		return this.entities.size();
	}
}
