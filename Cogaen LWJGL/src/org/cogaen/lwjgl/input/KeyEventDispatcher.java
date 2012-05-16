package org.cogaen.lwjgl.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cogaen.action.Action;
import org.cogaen.core.CogaenBase;
import org.cogaen.core.Core;
import org.cogaen.event.Event;
import org.cogaen.event.EventListener;
import org.cogaen.event.EventService;

public class KeyEventDispatcher extends CogaenBase implements EventListener {

	private Map<Integer, List<Action>> pressedActionMap = new HashMap<Integer, List<Action>>();
	private Map<Integer, List<Action>> releasedActionMap = new HashMap<Integer, List<Action>>();
	
	public KeyEventDispatcher(Core core) {
		super(core);
	}
	
	public void addPressedAction(int keyCode, Action action) {
		List<Action> actions = this.pressedActionMap.get(keyCode);
		if (actions == null) {
			actions = new ArrayList<Action>();
			this.pressedActionMap.put(keyCode, actions);
		}
		actions.add(action);
	}

	public void addReleasedAction(int keyCode, Action action) {
		List<Action> actions = this.releasedActionMap.get(keyCode);
		if (actions == null) {
			actions = new ArrayList<Action>();
			this.releasedActionMap.put(keyCode, actions);
		}
		actions.add(action);
	}
	
	@Override
	public void engage() {
		super.engage();
		EventService.getInstance(getCore()).addListener(this, KeyPressedEvent.TYPE_ID);
		EventService.getInstance(getCore()).addListener(this, KeyReleasedEvent.TYPE_ID);
	}

	@Override
	public void disengage() {
		EventService.getInstance(getCore()).removeListener(this, KeyPressedEvent.TYPE_ID);
		EventService.getInstance(getCore()).removeListener(this, KeyReleasedEvent.TYPE_ID);
		super.disengage();
	}

	@Override
	public void handleEvent(Event event) {
		if (event.isOfType(KeyPressedEvent.TYPE_ID)) {
			KeyPressedEvent keyPressed = (KeyPressedEvent) event;
			List<Action> actions = this.pressedActionMap.get(keyPressed.getKeyCode());
			if (actions == null) {
				return;
			}
			
			for (Action action : actions) {
				action.execute();
			}
		} else if (event.isOfType(KeyReleasedEvent.TYPE_ID)) {
			KeyReleasedEvent keyReleased = (KeyReleasedEvent) event;
			List<Action> actions = this.releasedActionMap.get(keyReleased.getKeyCode());
			if (actions == null) {
				return;
			}
			
			for (Action action : actions) {
				action.execute();
			}
		}
	}
}
