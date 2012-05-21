package org.cogaen.lwjgl.resource;

import javax.xml.stream.XMLStreamReader;

import org.cogaen.core.Core;
import org.cogaen.lwjgl.scene.ColorHandle;
import org.cogaen.name.CogaenId;
import org.cogaen.resource.AbstractXmlResourceParser;
import org.cogaen.resource.ParsingException;
import org.cogaen.resource.ResourceHandle;
import org.cogaen.resource.ResourceService;

public class ColorResourceParser extends AbstractXmlResourceParser {

	public static final String RESOURCE_TYPE = "color";
	private static final String RED_ATTR = "red";
	private static final String BLUE_ATTR = "blue";
	private static final String GREEN_ATTR = "green";

	public ColorResourceParser(Core core) {
		super(core);
	}

	@Override
	public void parseResource(XMLStreamReader xmlReader, CogaenId groupId) throws ParsingException {
		ResourceService resSrv = ResourceService.getInstance(getCore());
		
		ResourceHandle handle = new ColorHandle(
				parseDouble(xmlReader, RED_ATTR),
				parseDouble(xmlReader, GREEN_ATTR),
				parseDouble(xmlReader, BLUE_ATTR));
				
		resSrv.declareResource(parseName(xmlReader), groupId, handle);
	}

}
