/**
 * 
 */
package de.dralle.som;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

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

	public static String VERSION = "SNAPSHOT";

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		setupVersionInformation();
		parseCli(args);
	}

	private static void setupVersionInformation() {
		InputStream mavenPropsFile = Main.class.getClassLoader().getResourceAsStream("maven.properties");
		InputStream appPropsFile=Main.class.getClassLoader().getResourceAsStream("application.properties");
		Properties mavenProps = new Properties();
		Properties appProps = new Properties();
		if(mavenPropsFile!=null) {			
			try {
				mavenProps.load(mavenPropsFile);
			} catch (IOException e) {
			}
		}
		if(appPropsFile!=null) {			
			try {
				appProps.load(appPropsFile);
			} catch (IOException e) {
			}
		}
		String versionProp = mavenProps.getProperty("project.version");
		if(versionProp==null) {
			versionProp=appProps.getProperty("project.version");
		}if(versionProp!=null) {
			VERSION=versionProp;
		}
	}

	private static void parseCli(String[] args) throws IOException {
		Options options = setupCliOptions();
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (cmd.hasOption("version")) {
			System.out.println(VERSION);
		}
		if (cmd.hasOption("help")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("som-java", options);
		}
		boolean verbose = false;
		if (cmd.hasOption("verbose")) {
			verbose = true;
		}
		String infile = null;
		if (cmd.hasOption("infile")) {
			infile = cmd.getOptionValue("infile");
		}
		String outfile =null;
		if (cmd.hasOption("outfile")) {
			infile = cmd.getOptionValue("outfile");
		}
		String informat = null;
		if (cmd.hasOption("informat")) {
			informat = cmd.getOptionValue("informat");
		}
		String outformat =null;
		if (cmd.hasOption("outformat")) {
			outformat = cmd.getOptionValue("outformat");
		}
		if (cmd.hasOption("run")) {
			runProgramFromFile(infile, verbose);
		}
	}

	private static void runProgramFromFile(String inputFile, boolean verbose) throws IOException {
		File f = new File(inputFile);
		if (verbose) {
			System.out.println("File: " + f);
			System.out.println("File exist: " + f.exists());
		}
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
		runProgramFromBitList(bits,verbose);
	}

	private static void runProgramFromBitList(List<Boolean> bits, boolean verbose) {
		int n = getAsUnsignedInt(bits.subList(1, 6))+4;
		long memSpaceSize = (long) Math.pow(2, n);
		if (verbose) {
			System.out.println("Mem Address Size: " + n);
			System.out.println("Mem Space Size (Bits): " + memSpaceSize);
		}
		int startAddress=getAsUnsignedInt(bits.subList(6, 6+n));
		int opcodeSize = 2;
		int commandSize = opcodeSize + n;
		if (verbose) {
			System.out.println("Opcode Size: " + opcodeSize);
			System.out.println("Command Size: " + commandSize);
			System.out.println("Program starts at: "+startAddress);
		}
		Boolean[] bitArray = bits.toArray(new Boolean[bits.size()]);
		int ACC = 0;// accumulator address
		int PC = startAddress;// program counter
		int step = 0;
		while (true) {
			if (PC > bitArray.length - 1) {
				break;
			}
			Boolean[] commandBits = Arrays.copyOfRange(bitArray, PC, PC + commandSize);
			Boolean[] opcodeBits = Arrays.copyOfRange(commandBits, 0, opcodeSize);
			Boolean[] addressBits = Arrays.copyOfRange(commandBits, opcodeSize, opcodeSize + n);
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
			address = getAsUnsignedInt(addressBits);
			if (verbose) {
				System.out.println("Accumulator: " + bitArray[ACC]);
				System.out.println("Step: " + step);
				System.out.println("PC: " + PC);
				System.out.println("Opcode: " + op);
				System.out.println("Address: " + address);
			}
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
			if (bit) {
				System.out.print("1");
			} else {
				System.out.print("0");
			}
		}		
	}

	private static int getAsUnsignedInt(List<Boolean> subList) {
		return getAsUnsignedInt(subList.toArray(new Boolean[subList.size()]));
	}

	private static int getAsUnsignedInt(Boolean[] array) {
		int n = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i]) {
				n += Math.pow(2, array.length - i - 1);
			}
		}
		return n;
	}

	private static Options setupCliOptions() {
		Options options = new Options();
		options.addOption("v", "version", false, "Display the version");
		options.addOption("h", "help", false, "Display all available CLI options");
		options.addOption(null, "verbose", false, "Enable verbose mode");
		options.addOption("in", "infile", true, "Specify input file");
		options.addOption("out", "outfile", true, "Spacify output file");
		options.addOption("if", "informat", true, "Input file format");
		options.addOption("of", "outformat", true, "Output file format");
		options.addOption(null, "run", false, "Run a file");
		options.addOption(null, "compile", false, "Compile a file");
		return options;
	}
}
