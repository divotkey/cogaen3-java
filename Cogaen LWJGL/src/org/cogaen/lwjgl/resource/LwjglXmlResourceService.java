package org.cogaen.lwjgl.resource;

import java.util.ArrayList;
import java.util.List;

import org.cogaen.core.AbstractService;
import org.cogaen.core.Core;
import org.cogaen.core.ServiceException;
import org.cogaen.logging.LoggingService;
import org.cogaen.name.CogaenId;
import org.cogaen.resource.XmlResourceDescriptionService;
import org.cogaen.resource.XmlResourceParser;

public class LwjglXmlResourceService extends AbstractService {

	public static final CogaenId ID = new CogaenId(LwjglXmlResourceService.class.getName());
	public static final String NAME = "Cogaen LWJGL XML Resource Service";
	
	private List<String> resourceTypes = new ArrayList<String>();
	private XmlResourceDescriptionService resDescSrv;
	
	public static LwjglXmlResourceService getInstance(Core core) {
		return (LwjglXmlResourceService) core.getService(ID);
	}
	
	public LwjglXmlResourceService() {
		addDependency(LoggingService.ID);
		addDependency(XmlResourceDescriptionService.ID);
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
	protected void doStart() throws ServiceException {
		super.doStart();
		this.resDescSrv = XmlResourceDescriptionService.getInstance(getCore());
		addResourceParser(FontResourceParser.RESOURCE_TYPE, new FontResourceParser(getCore()));
		addResourceParser(TextureResourceParser.RESOURCE_TYPE, new TextureResourceParser(getCore()));
		addResourceParser(ColorResourceParser.RESOURCE_TYPE, new ColorResourceParser(getCore()));
	}

	@Override
	protected void doStop() {
		for (String resourceType : this.resourceTypes) {
			this.resDescSrv.removeResourceParser(resourceType);
		}
		resDescSrv = null;
		super.doStop();
	}
	
	private void addResourceParser(String resourceType, XmlResourceParser parser) {
		this.resDescSrv.addResourceParser(resourceType, parser);
		this.resourceTypes.add(resourceType);
	}
}
