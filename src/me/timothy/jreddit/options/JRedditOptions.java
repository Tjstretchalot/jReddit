package me.timothy.jreddit.options;

import java.util.Properties;

/**
 * Holds all of the options that jreddit has, stored in a Properties
 * file. Method names are exactly the same as the keys in the properties.
 * 
 * @author Timothy
 */
public class JRedditOptions {
	public static final Properties DEFAULTS;
	
	static {
		DEFAULTS = new Properties();
		
		DEFAULTS.setProperty("maxLogFilesPerZip", "30");
	}
	
	protected Properties realProperties;
	
	public JRedditOptions(Properties props) {
		realProperties = props;
	}
	
	/**
	 * Defines the maximum number of log files contained in logs.zip, before 
	 * it is moved to logs (n).zip, to increase shutdown time in most instances.
	 * 
	 * @return
	 */
	public int maxLogFilesPerZip() {
		return Integer.parseInt(realProperties.getProperty("maxLogFilesPerZip"));
	}
}
