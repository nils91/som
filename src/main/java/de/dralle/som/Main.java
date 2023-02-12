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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
				if(n>-1&&model instanceof ISetN) {
					((ISetN)model).setN(n);
				}
				SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) model);
				boolean execSuccess = false;
				if(to>0) {
					ExecutorService runnerExecutor = Executors.newSingleThreadExecutor();
					Callable<Boolean> runnerCallable=() ->{
						return runner.execute();
					};
					Future<Boolean> runnerFuture=runnerExecutor.submit(runnerCallable);
					try {
						execSuccess=runnerFuture.get(to, TimeUnit.SECONDS);
					} catch (InterruptedException | ExecutionException | TimeoutException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					execSuccess=runner.execute();
				}
				
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
			Object targetModel = new Compiler().compileDirect(sourceModel, inputFormat, outputFormat);
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
		options.addOption(null		, "timeout", true, "Timeout after <value> in seconds");
		return options;
	}
}
