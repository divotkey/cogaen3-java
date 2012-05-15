package org.cogaen.state;

import org.cogaen.action.DisengageAction;
import org.cogaen.action.EngageAction;
import org.cogaen.action.LoadResourceGroupAction;
import org.cogaen.action.UnloadResourceGroupAction;
import org.cogaen.core.Core;
import org.cogaen.name.CogaenId;
import org.cogaen.resource.ResourceService;
import org.cogaen.view.View;

public class ViewState extends ActionState {

	public ViewState(Core core, View view) {
		this(core, view, null);
	}
		
	public ViewState(Core core, View view, CogaenId resourceGroupId) {
		super(core);
		
		if (resourceGroupId != null) {
			ResourceService resSrv = ResourceService.getInstance(getCore());
			if (!resSrv.hasGroup(resourceGroupId)) {
				resSrv.createGroup(resourceGroupId);
			}
			view.registerResources(resourceGroupId);
			addEnterAction(new LoadResourceGroupAction(getCore(), resourceGroupId));
		}		
		addEnterAction(new EngageAction(view));
		
		if (resourceGroupId != null) {
			addExitAction(new UnloadResourceGroupAction(core, resourceGroupId));
		}
		addExitAction(new DisengageAction(view));
	}

}
