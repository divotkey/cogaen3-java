package org.cogaen.action;

import org.cogaen.core.Core;
import org.cogaen.task.Task;
import org.cogaen.task.TaskService;

public class DestroyTaskAction extends BasicAction {

	private Task task;
	
	public DestroyTaskAction(Core core, Task task) {
		super(core);
		this.task = task;
	}

	@Override
	public void execute() {
		TaskService.getInstance(getCore()).destroyTask(task);
	}

}
