package org.cogaen.lwjgl.resource;

import javax.xml.stream.XMLStreamReader;

import org.cogaen.core.Core;
import org.cogaen.lwjgl.scene.FontHandle;
import org.cogaen.name.CogaenId;
import org.cogaen.resource.AbstractXmlResourceParser;
import org.cogaen.resource.ParsingException;
import org.cogaen.resource.ResourceHandle;
import org.cogaen.resource.ResourceService;

public class FontResourceParser extends AbstractXmlResourceParser {

	public static final String RESOURCE_TYPE = "font";
	private static final String FACE_ATTR = "face";
	private static final String STYLE_ATTR = "style";
	private static final String SIZE_ATTR = "size";
	
	public FontResourceParser(Core core) {
		super(core);
	}

	@Override
	public void parseResource(XMLStreamReader xmlReader, CogaenId groupId) throws ParsingException {
		ResourceService resSrv = ResourceService.getInstance(getCore());
		
		ResourceHandle handle = new FontHandle(parseFace(xmlReader), fontStyle(xmlReader), fontSize(xmlReader)); 
		resSrv.declareResource(parseName(xmlReader), groupId, handle);
	}
	
	private int fontStyle(XMLStreamReader xmlReader) throws ParsingException {
		String style = xmlReader.getAttributeValue(null, STYLE_ATTR);
		if (style == null) {
			return FontHandle.PLAIN;
		} else if ("plain".equalsIgnoreCase(style)) {
			return FontHandle.PLAIN;
		} else if ("italic".equalsIgnoreCase(style)) {
			return FontHandle.ITALIC;
		} else if ("bold".equalsIgnoreCase(style)) {
			return FontHandle.BOLD;
		}
		
		throw new ParsingException("invalid font style " + style);
	}

	private String parseFace(XMLStreamReader xmlReader) throws ParsingException {
		return parseAttribute(xmlReader, FACE_ATTR);
	}
	
	private int fontSize(XMLStreamReader xmlReader) throws ParsingException {
		return parseInt(xmlReader, SIZE_ATTR);
	}

}
