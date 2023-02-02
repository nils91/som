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
			outfile = cmd.getOptionValue("outfile");
		}
		String informat = null;
		if (cmd.hasOption("informat")) {
			informat = cmd.getOptionValue("informat");
		}
		String outformat =null;
		if (cmd.hasOption("outformat")) {
			outformat = cmd.getOptionValue("outformat");
		}
		int to=0;
		if (cmd.hasOption("timeout")) {
			to=Integer.parseInt(cmd.getOptionValue("timeout"));
		}
		boolean compile=cmd.hasOption("compile");
		boolean run=cmd.hasOption("run");
		int heap=0;
		if (cmd.hasOption("heap")) {
			heap=Integer.parseInt(cmd.getOptionValue("heap"));
		}
		int n=-1;
		if (cmd.hasOption("n")) {
			n=Integer.parseInt(cmd.getOptionValue("n"));
		}
		SOMFormats inputFormat=new FileLoader().getFormatFromFilename(informat);
		SOMFormats outputFormat=new FileLoader().getFormatFromFilename(outformat);
		if(inputFormat==null) {
			inputFormat=new FileLoader().getFormatFromFilename(infile);
		}
		if(outputFormat==null) {
			outputFormat=new FileLoader().getFormatFromFilename(outfile);
		}
		if(run) {
			Object model = new FileLoader().loadFromFile(infile, inputFormat);
			if(model instanceof IMemspace) {
				//Can be executed
				int exitCode=0;
				SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) model);
				if(t)
				boolean execSuccess = runner.execute();
				if(verbose) {
					System.out.println("Program successfull: "+execSuccess);
				}
				if(execSuccess) {
					exitCode=0;
				}else {
					exitCode=1;
				}
				if(outfile!=null) {
					ISomMemspace memAfterExec = runner.getMemspace();
					new FileLoader().writeToFile(memAfterExec, outputFormat, outfile);
				}
				System.exit(exitCode);
			}
		}
		if(compile) {
			Object sourceModel = new FileLoader().loadFromFile(infile, inputFormat);
			if(n>-1&&sourceModel instanceof ISetN) {
				((ISetN)sourceModel).setN(n);
			}
			if(heap>0&&sourceModel instanceof IHeap) {
				((IHeap)sourceModel).setHeapSize(heap);
			}
			Object targetModel = new Compiler().compile(sourceModel, inputFormat, outputFormat);
			new FileLoader().writeToFile(targetModel, outputFormat, outfile);
		}
	}

	private static void printVersion(boolean verbose) {
		VersionHelper vh = new VersionHelper();
		System.out.println(vh.getVersion());
		if(verbose) {
			System.out.println(String.format("Repository: %s", vh.getRepositoryName()));
			System.out.println(String.format("Commit/Revision: %s", vh.getCommitHash()));
			System.out.println(String.format("Build system: %s", vh.getBuildSystemName()));
			System.out.println(String.format("Build type: %s", vh.getBuildType()));
			System.out.println(String.format("Time of build: %s", vh.getBuildTime()));
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
		Compiler c=new Compiler();
		IMemspace memspace = c.booleanListToMemspace(bits);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
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
		options.addOption(null, "heap", true, "Force the use of a heap");
		options.addOption("n"		, "n", true, "Set a minimum value for N");
		options.addOption(null		, "timeout", true, "Timeout after <value> milliseconds");
		return options;
	}
}
