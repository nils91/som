/**
 * 
 */
package de.dralle.som;

/**
 * @author Nils
 *
 */
public enum SOMFormats {
	AB("ascii binary","ab");

	private String friendlyName;
	private String fileExtension;
	
	public String getFriendlyName() {
		return friendlyName;
	}

	public String getFileExtensionString() {
		return fileExtension;
	}

	
	private SOMFormats(String friendlyName, String fileExtension) {
		this.fileExtension=fileExtension;
		this.friendlyName=friendlyName;
	}
}
