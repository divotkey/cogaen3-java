package org.cogaen.property;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.cogaen.core.AbstractService;
import org.cogaen.core.Core;
import org.cogaen.core.ServiceException;
import org.cogaen.logging.LoggingService;
import org.cogaen.name.CogaenId;

public class PropertyService extends AbstractService {

	
	public static final CogaenId ID = new CogaenId("org.cogaen.property.PropertyService");
	public static final String NAME = "Cogaen Property Service";
	public static final String DEFAULT_PROPERTY_FILE = "cogaen.cfg";
	public static final String LOGGING_SOURCE = "PRPT";
	private static final String DEFAULT_FILE_COMMENT = "Cogaen Properties File";

	private String filename;
	private Properties properties;
	private LoggingService logger;
	private String fileComment = DEFAULT_FILE_COMMENT;
	
	public static PropertyService getInstance(Core core) {
		return (PropertyService) core.getService(ID);
	}
	
	public PropertyService() {
		this(DEFAULT_PROPERTY_FILE);
	}
	
	public PropertyService(String filename) {
		addDependency(LoggingService.ID);
		this.filename = filename;
	}
	
	@Override
	public CogaenId getId() {
		return ID;
	}

	@Override
	public String getName() {
		return NAME;
	}

	public String getFileComment() {
		return fileComment;
	}

	public void setFileComment(String fileComment) {
		this.fileComment = fileComment;
	}

	@Override
	protected void doStart() throws ServiceException {
		super.doStart();
		
		this.logger = LoggingService.getInstance(getCore());

		File file = new File(this.filename);
		if (!file.exists() || file.isDirectory()) {
			this.logger.logWarning(LOGGING_SOURCE, "unable to locate properties file " + file.getAbsolutePath());			
			this.properties = new Properties();
			return;
		}
		
		try {
			FileInputStream fis = new FileInputStream(file);
			this.properties = new Properties();
			this.properties.load(fis);
			fis.close();
		} catch (Exception e) {
			this.logger.logError(LOGGING_SOURCE, "unable to load properties from " + file.getAbsolutePath());
			throw new ServiceException("unable to load properties file", e);
		}
		
		this.logger.logNotice(LOGGING_SOURCE, "properties loaded from file " + file.getAbsolutePath());
	}

	@Override
	protected void doStop() {
		
		File file = new File(this.filename);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			this.properties.store(fos, this.fileComment);
			fos.close();
		} catch (FileNotFoundException e) {
			this.logger.logError(LOGGING_SOURCE, "unable to create properties file " + file.getAbsolutePath());
		} catch (IOException e) {
			this.logger.logError(LOGGING_SOURCE, "unable to store properties file " + file.getAbsolutePath());
		}
		
		this.logger.logNotice(LOGGING_SOURCE, "properties stored to file " + file.getAbsolutePath());
		
		super.doStop();
	}
	
	public String getProperty(String key) {
		return this.properties.getProperty(key);
	}
	
	public String getProperty(String key, String defaultValue) {
		String value = getProperty(key);
		if (value == null) {
			setProperty(key, defaultValue);
			value = defaultValue;
		}

		return value;
	}
	
	public void setProperty(String key, String value) {
		this.properties.setProperty(key, value);
	}
	
	public void setIntProperty(String key, int value) {
		setProperty(key, Integer.toString(value));
	}
		
	public boolean hasProperty(String key) {
		return this.properties.getProperty(key) != null;
	}
	
	public boolean getBoolProperty(String key, boolean defaultValue) {
		String value = getProperty(key);
		if (value == null) {
			setProperty(key, Boolean.toString(defaultValue));
			return defaultValue;
		}

		return Boolean.parseBoolean(value);
	}

	public boolean getBoolProperty(String key) {
		String value = getProperty(key);
		if (value == null) {
			return false;
		}

		return Boolean.parseBoolean(value);
	}
	
	public void setBoolProperty(String key, boolean value) {
		setProperty(key, Boolean.toString(value));
	}
	
	public int getIntProperty(String key, int defaultValue) {
		String value = getProperty(key);
		if (value == null) {
			setIntProperty(key, defaultValue);
			return defaultValue;
		}
		
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			this.logger.logError(LOGGING_SOURCE, "unable to read property value " + key + " as integer");
			return defaultValue;
		}
	}

	public int getIntProperty(String key) {
		String value = getProperty(key);
		if (value == null) {
			return 0;
		}
		
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			this.logger.logError(LOGGING_SOURCE, "unable to read property value " + key + " as integer");
			return 0;
		}
	}
	
	private void setDoubleProperty(String key, double value) {
		setProperty(key, Double.toString(value));
	}
	
	public double getDoubleProperty(String key, double defaultValue) {
		String value = getProperty(key);
		if (value == null) {
			setDoubleProperty(key, defaultValue);
			return defaultValue;
		}
		
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			this.logger.logError(LOGGING_SOURCE, "unable to read property value " + key + " as double");
			return defaultValue;
		}
	}

	public double getDoubleProperty(String key) {
		String value = getProperty(key);
		if (value == null) {
			return 0.0;
		}
		
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			this.logger.logError(LOGGING_SOURCE, "unable to read property value " + key + " as double");
			return 0;
		}
	}
			
}
