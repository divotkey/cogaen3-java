package org.cogaen.state;

import java.util.ArrayList;
import java.util.List;

import org.cogaen.action.Action;

public class ActionState extends BasicState {

	private List<Action> enterActions = new ArrayList<Action>();
	private List<Action> exitActions = new ArrayList<Action>();
	
	public void addEnterAction(Action action) {
		this.enterActions.add(action);
	}
	
	public void addExitAction(Action action) {
		this.exitActions.add(action);
	}
	
	public void removeEnterAction(Action action) {
		this.enterActions.remove(action);
	}
	
	public void removeExitAction(Action action) {
		this.exitActions.remove(action);
	}
	
	public boolean hasEnterAction(Action action) {
		return this.enterActions.contains(action);
	}

	public boolean hasExitAction(Action action) {
		return this.exitActions.contains(action);
	}
	
	@Override
	public void onEnter() {
		super.onEnter();
		for (Action action : this.enterActions) {
			action.execute();
		}
	}

	@Override
	public void onExit() {
		for (Action action : this.exitActions) {
			action.execute();
		}
		super.onExit();
	}

}
