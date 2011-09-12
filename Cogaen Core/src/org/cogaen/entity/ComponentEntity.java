package org.cogaen.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cogaen.core.Core;
import org.cogaen.name.CogaenId;

public class ComponentEntity extends Entity {

	private List<Component> components = new ArrayList<Component>();
	private Map<CogaenId, Object> attributes = new HashMap<CogaenId, Object>();
	
	public ComponentEntity(Core core, CogaenId id) {
		super(core, id);
	}

	public final void addComponent(Component component) {
		this.components.add(component);
		component.initialize(this);
	}
	
	public final boolean hasAttribute(CogaenId id) {
		return this.attributes.containsKey(id);
	}
	
	public final void addAttribute(CogaenId id, Object attribute) {
		Object old = this.attributes.put(id, attribute);
		if (old != null) {
			this.attributes.put(id,  old);
			throw new RuntimeException("ambiguous attribute id " + id);
		}
	}
	
	public final Object getAttribute(CogaenId id) {
		Object attribute = this.attributes.get(id);
		if (attribute == null) {
			throw new RuntimeException("unknown attribute " + id);
		}
		return attribute;
	}

	@Override
	public void engage() {
		super.engage();
		for (Component component : this.components) {
			component.engage();
		}
	}

	@Override
	public void disengage() {
		for (Component component : this.components) {
			component.disengage();
		}
		super.disengage();
	}
	
}
