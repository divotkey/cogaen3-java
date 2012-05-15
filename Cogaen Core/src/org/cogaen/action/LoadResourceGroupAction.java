package org.cogaen.action;

import org.cogaen.core.Core;
import org.cogaen.name.CogaenId;
import org.cogaen.resource.ResourceService;

public class LoadResourceGroupAction implements Action {

	private ResourceService resSrv;
	private CogaenId groupId;

	public LoadResourceGroupAction(Core core, CogaenId groupId) {
		this.resSrv = ResourceService.getInstance(core);
		this.groupId = groupId;
	}

	@Override
	public void execute() {
		this.resSrv.loadGroup(this.groupId);
	}

}
