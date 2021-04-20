/**
 * 
 */
package de.dralle.som;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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
		int opcodeSize = 2;
		int commandSize = opcodeSize + memAddressSize;
		System.out.println("Opcode Size: " + opcodeSize);
		System.out.println("Command Size: " + commandSize);
		Boolean[] bitArray = bits.toArray(new Boolean[bits.size()]);
		int ACC = 0;// accumulator address
		int PC = 0;// program counter
		int step=0;
		while (true) {
			if(PC>bitArray.length-1) {
				break;
			}
			Boolean[] commandBits = Arrays.copyOfRange(bitArray, PC, PC + commandSize);
			Boolean[] opcodeBits = Arrays.copyOfRange(commandBits, 0, opcodeSize);
			Boolean[] addressBits = Arrays.copyOfRange(commandBits, opcodeSize, opcodeSize + memAddressSize);
			Opcode op = null;
			long address = 0;
			if (!opcodeBits[0] && !opcodeBits[1]) {// 00
				op = Opcode.READ;
			} else if (!opcodeBits[0] && opcodeBits[1]) {// 01
				op = Opcode.WRITE;
			} else if (opcodeBits[0] && !opcodeBits[1]) {// 10
				op = Opcode.NAND;
			} else if (opcodeBits[0] && opcodeBits[1]) {// 11
				op = Opcode.CJMP;
			}
			for (int i = 0; i < addressBits.length; i++) {
				if(addressBits[i]) {
					address+=Math.pow(2, memAddressSize-i-1);
				}
			}
			System.out.println("Step: "+step);
			System.out.println("PC: "+PC);
			System.out.println("Opcode: "+op);
			System.out.println("Address: "+address);
			step++;
			PC+=commandSize;
		}
	}
}
