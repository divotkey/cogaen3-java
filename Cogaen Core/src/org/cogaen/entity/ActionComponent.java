package org.cogaen.entity;

import java.util.ArrayList;
import java.util.List;

import org.cogaen.action.Action;

public class ActionComponent extends Component {

	private List<Action> engageActions = new ArrayList<Action>();
	private List<Action> disengageActions = new ArrayList<Action>();
	
	public ActionComponent() {
		this(null, null);
	}
	
	public ActionComponent(Action engageAction, Action disengageAction) {
		addEngageAction(engageAction);
		addDisengageAction(disengageAction);
	}
	
	@Override
	public void engage() {
		super.engage();
		for (Action action : this.engageActions) {
			action.execute();
		}
	}

	@Override
	public void disengage() {
		for (Action action : this.disengageActions) {
			action.execute();
		}
		super.disengage();
	}

	
	public void addEngageAction(Action action) {
		if (action != null) {
			this.engageActions.add(action);
		}
	}
	
	public void addDisengageAction(Action action) {
		if (action != null) {
			this.disengageActions.add(action);
		}
	}
}
