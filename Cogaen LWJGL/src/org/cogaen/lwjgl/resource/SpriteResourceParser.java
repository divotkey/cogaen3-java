package org.cogaen.lwjgl.resource;

import javax.xml.stream.XMLStreamReader;

import org.cogaen.core.Core;
import org.cogaen.lwjgl.scene.SpriteHandle;
import org.cogaen.name.CogaenId;
import org.cogaen.resource.AbstractXmlResourceParser;
import org.cogaen.resource.ParsingException;
import org.cogaen.resource.ResourceHandle;
import org.cogaen.resource.ResourceService;

public class SpriteResourceParser extends AbstractXmlResourceParser {

	public static final String RESOURCE_TYPE = "sprite";
	private static final String WIDTH_ATTR = "width";
	private static final String HEIGHT_ATTR = "height";
	private static final String TEXTURE_ATTR = "texture";

	public SpriteResourceParser(Core core) {
		super(core);
	}

	@Override
	public void parseResource(XMLStreamReader xmlReader, CogaenId groupId) throws ParsingException {
		ResourceService resSrv = ResourceService.getInstance(getCore());
		
		ResourceHandle handle = new SpriteHandle(parseTexture(xmlReader),
				parseWidth(xmlReader), parseHeight(xmlReader));
				
		resSrv.declareResource(parseName(xmlReader), groupId, handle);
	}

	private String parseTexture(XMLStreamReader xmlReader) throws ParsingException {
		return parseAttribute(xmlReader, TEXTURE_ATTR);
	}
	
	private double parseWidth(XMLStreamReader xmlReader) throws ParsingException {
		return parseDouble(xmlReader, WIDTH_ATTR);
	}
	
	private double parseHeight(XMLStreamReader xmlReader) throws ParsingException {
		return parseDouble(xmlReader, HEIGHT_ATTR);
	}
	
	
}
