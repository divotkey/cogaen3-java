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

public class MouseEventDispatcher extends CogaenBase implements EventListener {

	private Map<Integer, List<Action>> pressedActionMap = new HashMap<Integer, List<Action>>();
	private Map<Integer, List<Action>> releasedActionMap = new HashMap<Integer, List<Action>>();
	private List<Action> pressedList = new ArrayList<Action>();
	private List<Action> releasedList = new ArrayList<Action>();
	private List<Action> updateList = new ArrayList<Action>();
	private double x;
	private double y;
	private int button;
	
	public MouseEventDispatcher(Core core) {
		super(core);
	}
	
	public void addUpdateAction(Action action) {
		this.updateList.add(action);
	}
	
	public void addButtonPressedAction(Action action) {
		this.pressedList.add(action);
	}
	
	public void addButtonPressedAction(int button, Action action) {
		addButtonAction(button, action, this.pressedActionMap);
	}

	public void addButtonReleasedAction(Action action) {
		this.releasedList.add(action);
	}
	
	public void addButtonReleasedAction(int button, Action action) {
		addButtonAction(button, action, this.releasedActionMap);
	}
	
	private void addButtonAction(int button, Action action, Map<Integer, List<Action>> map) {
		List<Action> actions = map.get(button);
		if (actions == null) {
			actions = new ArrayList<Action>();
			map.put(button, actions);
		}
		actions.add(action);		
	}
		
	@Override
	public void engage() {
		super.engage();
		EventService.getInstance(getCore()).addListener(this, MouseButtonPressedEvent.TYPE_ID);
		EventService.getInstance(getCore()).addListener(this, MouseButtonReleasedEvent.TYPE_ID);
		EventService.getInstance(getCore()).addListener(this, MouseUpdateEvent.TYPE_ID);
	}

	@Override
	public void disengage() {
		EventService.getInstance(getCore()).removeListener(this, MouseButtonPressedEvent.TYPE_ID);
		EventService.getInstance(getCore()).removeListener(this, MouseButtonReleasedEvent.TYPE_ID);
		EventService.getInstance(getCore()).removeListener(this, MouseUpdateEvent.TYPE_ID);
		super.disengage();
	}

	@Override
	public void handleEvent(Event event) {
		if (event.isOfType(MouseButtonPressedEvent.TYPE_ID)) {
			handleMouseButtonPressed((MouseButtonPressedEvent) event);
		} else if (event.isOfType(MouseButtonReleasedEvent.TYPE_ID)) {
			handleMouseButtonReleased((MouseButtonReleasedEvent) event);
		}  else if (event.isOfType(MouseUpdateEvent.TYPE_ID)) {
			handleMouseUpdate((MouseUpdateEvent) event);
		}
	}
	
	private void handleMouseUpdate(MouseUpdateEvent event) {
		this.x = event.getPosX();
		this.y = event.getPosY();
		this.button = -1;
		
		for (Action action : this.updateList) {
			action.execute();
		}
	}

	
	private void handleButton(int button, Map<Integer, List<Action>> map, List<Action> list) {
		// execute actions registered for pressed AND released events
		for (Action action : list) {
			action.execute();
		}

		// execute actions handled for pressed OR released events
		List<Action> actions = map.get(button);
		if (actions == null) {
			return;
		}

		for (Action action : actions) {
			action.execute();
		}
	}
	
	private void handleMouseButtonReleased(MouseButtonReleasedEvent event) {
		this.x = event.getPosX();
		this.y = event.getPosY();
		this.button = event.getButton();
		
		handleButton(event.getButton(), this.releasedActionMap, this.releasedList);
	}

	
	private void handleMouseButtonPressed(MouseButtonPressedEvent event) {
		this.x = event.getPosX();
		this.y = event.getPosY();
		this.button = event.getButton();
		
		handleButton(event.getButton(), this.pressedActionMap, this.pressedList);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getButton() {
		return button;
	}
}
