package org.cogaen.task;

import org.cogaen.core.AbstractService;
import org.cogaen.core.Core;
import org.cogaen.core.ServiceException;
import org.cogaen.core.Updateable;
import org.cogaen.logging.LoggingService;
import org.cogaen.name.CogaenId;
import org.cogaen.util.Bag;

public class TaskService extends AbstractService implements Updateable {

	public static final CogaenId ID = new CogaenId("org.cogaen.tast.TaskService");
	public static final String NAME = "Cogaen Task Service";
	private static final String LOGGING_SOURCE = "TSKS";
	
	private Bag<Task> tasks = new Bag<Task>();
	private LoggingService logger;
	
	public static TaskService getInstance(Core core) {
		return (TaskService) core.getService(ID);
	}
	
	public TaskService() {
		addDependency(LoggingService.ID);
	}
	
	@Override
	public CogaenId getId() {
		return ID;
	}

	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	protected void doPause() {
		getCore().removeUpdateable(this);
		super.doPause();
	}

	@Override
	protected void doResume() {
		super.doResume();
		getCore().addUpdateable(this);
	}

	@Override
	protected void doStart() throws ServiceException {
		super.doStart();
		getCore().addUpdateable(this);
		this.logger = LoggingService.getInstance(getCore());
	}

	@Override
	protected void doStop() {
		this.logger = null;
		if (getStatus() != Status.PAUSED) {
			getCore().removeUpdateable(this);
		}
		super.doStop();
	}
	
	public void attachTask(Task task) {
		this.tasks.add(task);
		this.logger.logInfo(LOGGING_SOURCE, "task attached '" + task.getName() + "'");
	}
	
	public void destroyTask(Task task) {
		this.tasks.remove(task);
		task.destroy();
		String name = task.getName();
		this.logger.logInfo(LOGGING_SOURCE, "task destroyed '" + name + "'");
	}

	@Override
	public void update() {
		for (this.tasks.reset(); this.tasks.hasNext(); ) {
			this.tasks.next().update();
		}
	}

	public int numOfTasks() {
		return this.tasks.size();
	}

	public Task getTask(int i) {
		return this.tasks.get(i);
	}
}
