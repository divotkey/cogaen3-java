package org.cogaen.entity;

import org.cogaen.core.Updatable;

public abstract class UpdateableComponent extends Component implements Updatable {

	@Override
	public void engage() {
		super.engage();
		EntityService.getInstance(getCore()).addUpdatable(this);
	}

	@Override
	public void disengage() {
		EntityService.getInstance(getCore()).removeUpdatable(this);
		super.disengage();
	}

	@Override
	public abstract void update();

}
