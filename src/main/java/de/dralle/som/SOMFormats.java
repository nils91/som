/**
 * 
 */
package de.dralle.som;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.util.List;

import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hras.model.HRASModel;
import de.dralle.som.languages.hrav.model.HRAVModel;
import de.dralle.som.languages.hrbs.model.HRBSModel;

/**
 * @author Nils
 *
 */
public enum SOMFormats {
	AB(String.class,"ascii binary","ab"),BIN(IMemspace.class,"binary","bin"),HRAS(HRASModel.class,"human readable SOM Type A Simple","hras"),HRAP(HRACModel.class,"Human readable SOM type A complex precompiled","hrap",new String[] {".hrap"}),HRAC(HRACModel.class,"human readable SOM Type A Complex","hrac",new String[]{".hra",".hrac"}),HRBS(HRBSModel.class,"human readable som type b simple","hrbs"),HRAV(HRAVModel.class,"human readable som Type a very simple","hrav"),IMAGE(RenderedImage.class,"Binary as png","png"),CBIN(byte[].class,"compressed binary","cbin",new String[] {".cbin",".zip"}),B64(String.class,"KBase64 binary","b64");

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
	
	private Class<?> internalClazz;

	
	public Class<?> getInternalClazz() {
		return internalClazz;
	}

	private SOMFormats(Class<?> internalClazz,String friendlyName, String shortName) {
		this(internalClazz,friendlyName,shortName,"."+shortName);
	}
	private SOMFormats(Class<?> internalClazz,String friendlyName, String shortName,String fileExt) {
		this(internalClazz,friendlyName,shortName,new String[] {fileExt});
	}
	private SOMFormats(Class<?> internalClazz,String friendlyName, String shortName,String[] fileExts) {
		this.fileExtensions=fileExts;
		this.friendlyName=friendlyName;
		this.shortName=shortName;
		this.internalClazz=internalClazz;
	}
}
