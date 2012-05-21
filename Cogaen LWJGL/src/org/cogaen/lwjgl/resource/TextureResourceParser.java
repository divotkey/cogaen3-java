package org.cogaen.lwjgl.resource;

import javax.xml.stream.XMLStreamReader;

import org.cogaen.core.Core;
import org.cogaen.lwjgl.scene.TextureHandle;
import org.cogaen.name.CogaenId;
import org.cogaen.resource.AbstractXmlResourceParser;
import org.cogaen.resource.ParsingException;
import org.cogaen.resource.ResourceHandle;
import org.cogaen.resource.ResourceService;

public class TextureResourceParser extends AbstractXmlResourceParser {

	public static final String RESOURCE_TYPE = "texture";
	private static final String TYPE_ATTR = "type";
	private static final String FILE_ATTR = "file";
	
	public TextureResourceParser(Core core) {
		super(core);
	}
	
	@Override
	public void parseResource(XMLStreamReader xmlReader, CogaenId groupId) throws ParsingException {
		ResourceService resSrv = ResourceService.getInstance(getCore());
		
		ResourceHandle handle = new TextureHandle(parseType(xmlReader), parseFile(xmlReader)); 
		resSrv.declareResource(parseName(xmlReader), groupId, handle);
		
	}

	private String parseType(XMLStreamReader xmlReader) throws ParsingException {
		return parseAttribute(xmlReader, TYPE_ATTR);
	}

	private String parseFile(XMLStreamReader xmlReader) throws ParsingException {
		return parseAttribute(xmlReader, FILE_ATTR);
	}

}
