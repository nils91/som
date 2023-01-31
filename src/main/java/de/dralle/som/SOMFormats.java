/**
 * 
 */
package de.dralle.som;

import java.util.List;

/**
 * @author Nils
 *
 */
public enum SOMFormats {
	AB("ascii binary","ab"),BIN("binary","bin"),HRAS("human readable SOM Type A Simple","hras"),HRAC("human readable SOM Type A Complex","hrac",new String[]{"hra","hrac"});

	private String friendlyName;
	private String shortName;
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}


	private String[] fileExtensions;
	
	public String getFriendlyName() {
		return friendlyName;
	}

	public String[] getFileExtensionString() {
		return fileExtensions;
	}

	
	private SOMFormats(String friendlyName, String shortName) {
		this(friendlyName,shortName,shortName);
	}
	private SOMFormats(String friendlyName, String shortName,String fileExt) {
		this(friendlyName,shortName,new String[] {fileExt});
	}
	private SOMFormats(String friendlyName, String shortName,String[] fileExts) {
		this.fileExtensions=fileExts;
		this.friendlyName=friendlyName;
		this.shortName=shortName;
	}
}
