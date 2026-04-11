package de.dralle.som.util;

import java.io.File;

public class GenerateHRACDirectiveTreeVisitorInterface {
	public static void main(String... args) {
		File packageFolder=new File("src/main/java/de/dralle/som/languages/hrac/model/expressiontree");
		for (int i = 0; i < packageFolder.list().length; i++) {
			File string = packageFolder.listFiles()[i];
			if(string.isFile()) {
				String name=string.getName();
				if(name.contains(".")) {
					try {
					name=name.split("\\.")[0];}catch (Exception e)  {}
				}
				System.out.println("    default T visit("+name+" node){return null;}");
			}			
		}
	}
}
