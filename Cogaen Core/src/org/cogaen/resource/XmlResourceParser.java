package org.cogaen.resource;

import javax.xml.stream.XMLStreamReader;

import org.cogaen.name.CogaenId;

public interface XmlResourceParser {

	public void parseResource(XMLStreamReader xmlReader, CogaenId groupId) throws ParsingException;
	
}
