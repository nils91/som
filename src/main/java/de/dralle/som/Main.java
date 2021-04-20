/**
 * 
 */
package de.dralle.som;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nils Dralle
 *
 */
public class Main {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		for (int i = 0; i < args.length; i++) {
			String string = args[i];
			System.out.println("Argument[" + i + "]: " + string);
		}
		String argN = args[0];
		String filename = args[1];
		int memAddressSize = Integer.parseInt(argN);
		long memSpaceSize = (long) Math.pow(2, memAddressSize);
		File f = new File(filename);
		System.out.println("Mem Address Size: " + memAddressSize);
		System.out.println("Mem Space Size (Bits): " + memSpaceSize);
		System.out.println("File: " + filename);
		System.out.println("File exist: " + f.exists());
		List<Boolean> bits = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(f));
		int r = 0;
		while ((r = br.read()) != -1) {
			switch ((char) r) {
			case '0':
				bits.add(false);
				break;
			case '1':
				bits.add(true);
				break;
			default:
				break;
			}
		}
		int opcodeSize=2;
		int commandSize=opcodeSize+memAddressSize;
		System.out.println("Opcode Size: "+opcodeSize);
		System.out.println("Command Size: "+commandSize);
	}
}
