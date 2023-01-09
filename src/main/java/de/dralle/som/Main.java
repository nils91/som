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
	public static String REPOSITORY="none";
	public static String COMMIT_HASH="";
	public static String BUILD_SYSTEM="Maven";
	public static String BUILD_TYPE="Manual";
	public static String TIME_OF_BUILD="";

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
		if (cmd.hasOption("help")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("som-java", options);
		}
		boolean verbose = false;
		if (cmd.hasOption("verbose")) {
			verbose = true;
		}
		if (cmd.hasOption("version")) {
			printVersion(verbose);
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

	private static void printVersion(boolean verbose) {
		System.out.println(VERSION);
		if(verbose) {
			System.out.println(String.format("Repository: %s", REPOSITORY));
			System.out.println(String.format("Commit/Revision: %s", COMMIT_HASH));
			System.out.println(String.format("Build system: %s", BUILD_SYSTEM));
			System.out.println(String.format("Build type: %s", BUILD_TYPE));
			System.out.println(String.format("Time of build: %s", TIME_OF_BUILD));
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
		SOMBitcodeRunner runner = new SOMBitcodeRunner(bits);
		boolean execSuccess = runner.execute();
		System.out.println("Program successfull: "+execSuccess);
		if(execSuccess) {
			System.exit(0);
		}else {
			System.exit(1);
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
