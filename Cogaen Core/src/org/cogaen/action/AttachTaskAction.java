package org.cogaen.action;

import org.cogaen.core.Core;
import org.cogaen.task.Task;
import org.cogaen.task.TaskService;

public class AttachTaskAction extends BasicAction {

	private Task task;
	
	public AttachTaskAction(Core core, Task task) {
		super(core);
		this.task = task;
	}

	@Override
	public void execute() {
		TaskService.getInstance(getCore()).attachTask(task);
	}

}
