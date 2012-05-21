package org.cogaen.resource;

import javax.xml.stream.XMLStreamReader;

import org.cogaen.core.Core;

public abstract class AbstractXmlResourceParser implements XmlResourceParser {

	private static final String NAME_ATTR = "name";
	private Core core;
	
	public AbstractXmlResourceParser(Core core) {
		this.core = core;
	}

	public final Core getCore() {
		return this.core;
	}
		
	protected String parseName(XMLStreamReader xmlReader) throws ParsingException {
		return parseAttribute(xmlReader, NAME_ATTR);
	}
	
	protected String parseAttribute(XMLStreamReader xmlReader, String attr) throws ParsingException {
		String value = xmlReader.getAttributeValue(null, attr);
		if (value == null) {
			throw new ParsingException("missing attribute '" + attr + "'");
		}
		
		return value;
	}
	
	protected int parseInt(XMLStreamReader xmlReader, String attr) throws ParsingException {
		String value = parseAttribute(xmlReader, attr);
		
		return parseInt(value);
	}

	private int parseInt(String value) throws ParsingException {
		try {
			return Integer.parseInt(value);
		}
		catch (NumberFormatException ex) {
			throw new ParsingException("invalid number format '" + value + "'");
		}
	}
	
	protected int parseInt(XMLStreamReader xmlReader, String attr, int defaultValue) throws ParsingException {
		String value = xmlReader.getAttributeValue(null, attr);
		if (value == null) {
			return defaultValue;
		}
		
		return parseInt(value);
	}

	protected double parseDouble(XMLStreamReader xmlReader, String attr, double defaultValue) throws ParsingException {		
		String value = xmlReader.getAttributeValue(null, attr);
		if (value == null) {
			return defaultValue;
		}
		
		return parseDouble(value);
	}
	
	protected double parseDouble(XMLStreamReader xmlReader, String attr) throws ParsingException {
		String value = parseAttribute(xmlReader, attr);
		return parseDouble(value);
	}

	private double parseDouble(String value) throws ParsingException {
		try {
			return Double.parseDouble(value);
		}
		catch (NumberFormatException ex) {
			throw new ParsingException("invalid number format '" + value + "'");
		}
	}
}
