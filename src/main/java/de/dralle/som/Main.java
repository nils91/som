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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

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
		parseCli(args);
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
		br.close();
		int opcodeSize = 2;
		int commandSize = opcodeSize + memAddressSize;
		System.out.println("Opcode Size: " + opcodeSize);
		System.out.println("Command Size: " + commandSize);
		Boolean[] bitArray = bits.toArray(new Boolean[bits.size()]);
		int ACC = 0;// accumulator address
		int PC = 0;// program counter
		int step = 0;
		while (true) {
			if (PC > bitArray.length - 1) {
				break;
			}
			Boolean[] commandBits = Arrays.copyOfRange(bitArray, PC, PC + commandSize);
			Boolean[] opcodeBits = Arrays.copyOfRange(commandBits, 0, opcodeSize);
			Boolean[] addressBits = Arrays.copyOfRange(commandBits, opcodeSize, opcodeSize + memAddressSize);
			Opcode op = null;
			int address = 0;
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
				if (addressBits[i]) {
					address += Math.pow(2, memAddressSize - i - 1);
				}
			}
			System.out.println("Accumulator: " + bitArray[ACC]);
			System.out.println("Step: " + step);
			System.out.println("PC: " + PC);
			System.out.println("Opcode: " + op);
			System.out.println("Address: " + address);
			step++;
			PC += commandSize;
			switch (op) {
			case READ:
				bitArray[ACC] = bitArray[address];
				break;
			case WRITE:
				bitArray[address] = bitArray[ACC];
				break;
			case NAND:
				Boolean valueAtAddr = bitArray[address];
				Boolean valueAcc = bitArray[ACC];
				boolean newValueAcc = !(valueAtAddr && valueAcc);
				bitArray[ACC] = newValueAcc;
				break;
			case CJMP:
				if (bitArray[ACC]) {
					PC = address; // Can be done here because execution advance is performed before switch/case
				}
				break;
			default:
				break;
			}
		}
		for (int i = 0; i < bitArray.length; i++) {
			Boolean bit = bitArray[i];
			if(bit) {
				System.out.print("1");
			}else {
				System.out.print("0");
			}
			
		}
	}

	private static void parseCli(String[] args) {
		Options options= setupCliOptions();
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(cmd.hasOption("help")) {
			HelpFormatter formatter=new HelpFormatter();
			formatter.printHelp("som-java", options);
		}
		if(cmd.hasOption("verbose")) {
			System.out.println("verbose");
		}
		System.exit(1);
	}

	private static Options setupCliOptions() {
		Options options=new Options();
		options.addOption("v", "version", false, "Display the version");
		options.addOption("h", "help", false, "Display all available CLI options");
		options.addOption(null,"verbose",false,"Enable verbose mode");
		options.addOption("in","infile",true,"Specify input file");
		options.addOption("out","outfile",true,"Spacify output file");
		options.addOption("if","informat",true,"Input file format");
		options.addOption("of","outformat",true,"Output file format");
		return options;
	}
}
