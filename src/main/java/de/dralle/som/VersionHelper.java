/**
 * 
 */
package de.dralle.som;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Nils
 *
 */
public class VersionHelper {
	private static final String APPLICATION_PROPERTIES_FN = "application.properties";
	private static final String MAVEN_PROPERTIES_FN = "maven.properties";
	public static final String VERSION_PROPERTY_NAME= "project.version";
	public static final String COMMIT_PROPERTY_NAME= "project.build.commit";
	public static final String BUILD_SYSTEM_PROPERTY_NAME= "project.build.system";
	public static final String BUILD_TYPE_PROPERTY_NAME= "project.build.type";
	public static final String PROJECT_REPOSITORY_PROPERTY_NAME= "project.repository";
	public static final String BUILD_TIME_PROPERTY_NAME= "project.build.timestamp";
	private Properties mavenProps;
	private Properties appProps;
	public VersionHelper() {
		loadProperties();
	}
	private void loadProperties() {
		InputStream mavenPropsFile = Main.class.getClassLoader().getResourceAsStream(MAVEN_PROPERTIES_FN);
		InputStream appPropsFile=Main.class.getClassLoader().getResourceAsStream(APPLICATION_PROPERTIES_FN);
		mavenProps = new Properties();
		appProps = new Properties();
		if(mavenPropsFile!=null) {			
			try {
				mavenProps.load(mavenPropsFile);
			} catch (IOException e) {
			}
			try {
				mavenPropsFile.close();
			} catch (IOException e) {
				
			}
		}
		if(appPropsFile!=null) {			
			try {
				appProps.load(appPropsFile);
			} catch (IOException e) {
			}
			try {
				appPropsFile.close();
			} catch (IOException e) {
				
			}
		}
	}
	private String getProperty(String propertyName) {
		String value=null;
		value=mavenProps.getProperty(propertyName);
		if(value==null) {
			value=appProps.getProperty(propertyName);
		}
		return value;
	}
	public String getVersion() {
		return getProperty(VERSION_PROPERTY_NAME);
	}
	public String getRepositoryName() {
		return getProperty(PROJECT_REPOSITORY_PROPERTY_NAME);
	}
	public String getBuildSystemName() {
		return getProperty(BUILD_SYSTEM_PROPERTY_NAME);
	}
	public String getBuildType() {
		return getProperty(BUILD_TYPE_PROPERTY_NAME);
	}
	public String getBuildTime() {
		return getProperty(BUILD_TIME_PROPERTY_NAME);
	}
	public String getCommitHash() {
		return getProperty(COMMIT_PROPERTY_NAME);
	}
}
