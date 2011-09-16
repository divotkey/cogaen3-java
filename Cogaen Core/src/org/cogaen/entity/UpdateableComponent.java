package org.cogaen.entity;

import org.cogaen.core.Updateable;

public abstract class UpdateableComponent extends Component implements Updateable {

	@Override
	public void engage() {
		super.engage();
		EntityService.getInstance(getCore()).addUpdateable(this);
	}

	@Override
	public void disengage() {
		EntityService.getInstance(getCore()).removeUpdateable(this);
		super.disengage();
	}

	@Override
	public abstract void update();

}
