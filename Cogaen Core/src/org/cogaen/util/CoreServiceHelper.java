package org.cogaen.util;

import java.io.File;

import org.cogaen.core.Core;
import org.cogaen.entity.EntityService;
import org.cogaen.event.EventService;
import org.cogaen.logging.ConsoleLogger;
import org.cogaen.logging.FileLogger;
import org.cogaen.name.IdService;
import org.cogaen.property.PropertyService;
import org.cogaen.resource.ResourceService;
import org.cogaen.resource.XmlResourceDescriptionService;
import org.cogaen.state.GameStateService;
import org.cogaen.task.TaskService;
import org.cogaen.time.TimeService;

public class CoreServiceHelper {

	private String proeprtyFile = "cogaen.cfg";
	private String logFile = null;
	private String resourceFile = "resources.xml";
	
	public void registerAllServices(Core core) {
		if (this.logFile == null) {
			core.addService(new ConsoleLogger());
		} else {
			core.addService(new FileLogger(new File(this.logFile)));
		}
		
		core.addService(new IdService());
		core.addService(new TimeService());
		core.addService(new PropertyService(this.proeprtyFile));
		core.addService(new EventService());
		core.addService(new GameStateService());
		core.addService(new TaskService());
		core.addService(new ResourceService());
		core.addService(new EntityService());
		core.addService(new XmlResourceDescriptionService(this.resourceFile));
	}

	public String getProeprtyFile() {
		return proeprtyFile;
	}

	public void setProeprtyFile(String proeprtyFile) {
		this.proeprtyFile = proeprtyFile;
	}

	public String getLogFile() {
		return logFile;
	}

	public void setLogFile(String logFile) {
		this.logFile = logFile;
	}

	public String getResourceFile() {
		return resourceFile;
	}

	public void setResourceFile(String resourceFile) {
		this.resourceFile = resourceFile;
	}
}
