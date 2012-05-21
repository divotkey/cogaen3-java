package org.cogaen.resource;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.cogaen.core.AbstractService;
import org.cogaen.core.Core;
import org.cogaen.core.CoreListener;
import org.cogaen.core.ServiceException;
import org.cogaen.logging.LoggingService;
import org.cogaen.name.CogaenId;

public class XmlResourceDescriptionService extends AbstractService implements CoreListener {

	public static final CogaenId ID = new CogaenId(XmlResourceDescriptionService.class.getName());
	public static final String NAME = "Cogaen Resource Description Service (XML-Parser)";
	public static final String LOGGING_SOURCE = "RSDS";
	public static final String NAME_ATTR = "name";
	private static final String GROUP = "group";
	private static final String RESOURCES = "resources";

	private Map<String, XmlResourceParser> parserList = new HashMap<String, XmlResourceParser>();
	private ResourceService resSrv;
	private LoggingService logger;
	private String descriptionFile;
	private CogaenId groupId;
	private boolean insideResources;
	
	public static final XmlResourceDescriptionService getInstance(Core core) {
		return (XmlResourceDescriptionService) core.getService(ID);
	}
	
	public XmlResourceDescriptionService(String filename) {
		addDependency(LoggingService.ID);
		addDependency(ResourceService.ID);
		this.descriptionFile = filename;
	}
	
	public XmlResourceDescriptionService() {
		this(null);
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
		this.logger = LoggingService.getInstance(getCore());
		this.resSrv = ResourceService.getInstance(getCore());
		getCore().addListener(this);
	}

	@Override
	protected void doStop() {
		this.resSrv = null;
		this.logger = null;
		getCore().removeListener(this);
		super.doStop();
	}

	public void addResourceParser(String resourceType, XmlResourceParser parser) {
		XmlResourceParser old = this.parserList.put(resourceType, parser);
		if (old != null) {
			this.logger.logWarning(LOGGING_SOURCE, "parser of resource type " + resourceType + " has been redefined");
		}
	}
	
	public void removeResourceParser(String resourceType) {
		if (this.parserList.remove(resourceType) == null) {
			this.logger.logWarning(LOGGING_SOURCE, "attempt to remove unknown parser for unknown resource type " + resourceType);
		}
	}
	
	public boolean hasResourceParser(String resourceType) {
		return this.parserList.containsKey(resourceType);
	}
	
	public void parseResourceDescription(String filename) throws ServiceException {
		XMLInputFactory inputFactory = XMLInputFactory.newInstance();
		InputStream is = this.resSrv.getStream(filename);
		
		if (is == null) {
			throw new ServiceException("resource description file not found: " + filename);
		}
		
		XMLStreamReader xmlReader = null;
		try {
			xmlReader = inputFactory.createXMLStreamReader(is);
		} catch (XMLStreamException e) {
			throw new ServiceException("unable to read resource description file: " + filename, e);
		}
		
		try {
			this.logger.logInfo(LOGGING_SOURCE, "parsing resource description from file: " + filename);
			parseResourceDescription(xmlReader);
		} catch (XMLStreamException e) {
			throw new ServiceException("unable to parse resource description file: " + filename, e);
		} finally {
			try {
				xmlReader.close();
			} catch (XMLStreamException e) {
				this.logger.logWarning(LOGGING_SOURCE, "unable to close xml stream reader: " + e.getMessage());
			}
		}
	}
	
	private void parseResourceDescription(XMLStreamReader xmlReader) throws XMLStreamException {
		this.groupId = null;
		this.insideResources = false;
		int event = xmlReader.next();
		while (event != XMLStreamConstants.END_DOCUMENT) {
			switch (event) {
			case XMLStreamConstants.START_ELEMENT:
				parseStartElement(xmlReader);
				break;
				
			case XMLStreamConstants.END_ELEMENT:
				parseEndElement(xmlReader);
			}
			
			event = xmlReader.next();
		}
	}

	private void parseEndElement(XMLStreamReader xmlReader) {
		String name = xmlReader.getLocalName();
		
		if (RESOURCES.equals(name)) {
			this.insideResources = false;
		} else if (GROUP.equals(name)) {
			this.groupId = null;
		}
	}

	private void parseStartElement(XMLStreamReader xmlReader) throws XMLStreamException {
		String name = xmlReader.getLocalName();
		
		if (RESOURCES.equals(name)) {
			if (this.insideResources) {
				this.logger.logWarning(LOGGING_SOURCE, "invalid resource description file, 'resources' element must be root element");
			} else {
				this.insideResources = true;
			}
			return;
		} else if (!this.insideResources) {
			this.logger.logWarning(LOGGING_SOURCE, "invalid resource description file, 'resources' element must be root element");			
			return;
		}
		
		if (GROUP.equals(name)) {
			parseGroup(xmlReader, name);
		} else {
			parseResource(xmlReader);
		}
	}

	private void parseResource(XMLStreamReader xmlReader) throws XMLStreamException {
		String resourceType = xmlReader.getLocalName();
		XmlResourceParser parser = this.parserList.get(resourceType);
		if (parser == null) {
			logger.logWarning(LOGGING_SOURCE, "unknown resource type: " + resourceType);
			return;
		}
		
		try {
			parser.parseResource(xmlReader, this.groupId);
		} catch (ParsingException e) {
			this.logger.logWarning(LOGGING_SOURCE, e.getMessage());
		}
	}

	private void parseGroup(XMLStreamReader xmlReader, String name) {
		if (this.groupId != null) {
			this.logger.logWarning(LOGGING_SOURCE, "invalid resource description file, 'group' element must not be nested");
			return;
		}

		String groupName = xmlReader.getAttributeValue(null, NAME_ATTR);
		if (groupName == null) {
			this.logger.logWarning(LOGGING_SOURCE, "group name attribute must be specified");
			return;
		}
		
		this.groupId = new CogaenId(groupName);
		if (this.resSrv.hasGroup(this.groupId)) {
			this.logger.logWarning(LOGGING_SOURCE, "ambiguous group name: " + name);
		} else {
			this.resSrv.createGroup(groupId);
		}
	}

	@Override
	public void shutdownInitiated() {
		// intentionally left empty
	}

	@Override
	public void startupFinished() {
		if (this.descriptionFile == null) {
			return;
		}
		
		try {
			parseResourceDescription(this.descriptionFile);
		} catch (ServiceException e) {
			this.logger.logWarning(LOGGING_SOURCE, e.getMessage());
		}

	}	
}
