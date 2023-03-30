/**
 * 
 */
package de.dralle.som.test.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Nils Dralle
 *
 */
public class TestUtil {
	public static String readFileToString(String path) throws IOException {
		File f=new File(path);
		BufferedReader br = new BufferedReader(new FileReader(f));
		String entireFile="";
		String nextLine=null;
		do {
			nextLine=br.readLine();
			if(nextLine!=null) {
				entireFile+=nextLine;
			}			
		}while(nextLine!=null);
		br.close();
		return entireFile;
	}
}
