/**
 * 
 */
package de.dralle.som;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
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
		if (cmd.hasOption("regenerate-gitignore")) {
			regenerateGitignire();
		}
		String infile = null;
		if (cmd.hasOption("infile")) {
			infile = cmd.getOptionValue("infile");
		}
		String outfile = null;
		if (cmd.hasOption("outfile")) {
			outfile = cmd.getOptionValue("outfile");
		}
		String informat = null;
		if (cmd.hasOption("informat")) {
			informat = cmd.getOptionValue("informat");
		}
		String outformat = null;
		if (cmd.hasOption("outformat")) {
			outformat = cmd.getOptionValue("outformat");
		}
		int to = 0;
		if (cmd.hasOption("timeout")) {
			to = Integer.parseInt(cmd.getOptionValue("timeout"));
		}
		boolean compile = cmd.hasOption("compile");
		boolean run = cmd.hasOption("run");
		int heap = 0;
		if (cmd.hasOption("heap")) {
			heap = Integer.parseInt(cmd.getOptionValue("heap"));
		}
		int n = -1;
		if (cmd.hasOption("n")) {
			n = Integer.parseInt(cmd.getOptionValue("n"));
		}
		SOMFormats inputFormat = new FileLoader().getFormatFromFilename(informat);
		SOMFormats outputFormat = new FileLoader().getFormatFromFilename(outformat);
		if (inputFormat == null) {
			inputFormat = new FileLoader().getFormatFromFilename(infile);
		}
		if (outputFormat == null) {
			outputFormat = new FileLoader().getFormatFromFilename(outfile);
		}
		if (run) {
			Object model = new FileLoader().loadFromFile(infile, inputFormat);
			if (model instanceof IMemspace) {
				// Can be executed
				int exitCode = 0;
				if (n > -1 && model instanceof ISetN) {
					((ISetN) model).setN(n);
				}
				SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) model);
				boolean execSuccess = false;
				if (to > 0) {
					ExecutorService runnerExecutor = Executors.newSingleThreadExecutor();
					Callable<Boolean> runnerCallable = () -> {
						return runner.execute();
					};
					Future<Boolean> runnerFuture = runnerExecutor.submit(runnerCallable);
					try {
						execSuccess = runnerFuture.get(to, TimeUnit.SECONDS);
					} catch (InterruptedException | ExecutionException | TimeoutException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					execSuccess = runner.execute();
				}

				if (verbose) {
					System.out.println("Program successfull: " + execSuccess);
				}
				if (execSuccess) {
					exitCode = 0;
				} else {
					exitCode = 1;
				}
				if (outfile != null) {
					ISomMemspace memAfterExec = runner.getMemspace();
					new FileLoader().writeToFile(memAfterExec, outputFormat, outfile);
				}
				System.exit(exitCode);
			}
		}
		if (compile) {
			Object sourceModel = new FileLoader().loadFromFile(infile, inputFormat);
			if (n > -1 && sourceModel instanceof ISetN) {
				((ISetN) sourceModel).setN(n);
			}
			if (heap > 0 && sourceModel instanceof IHeap) {
				((IHeap) sourceModel).setHeapSize(heap);
			}
			Object targetModel = new Compiler().compile(sourceModel, inputFormat, outputFormat);
			new FileLoader().writeToFile(targetModel, outputFormat, outfile);
		}
	}

	private static void regenerateGitignire() throws IOException {
		// (try) load prototype gitignore file
		File proto = new File(".gitignore.prototype");
		File gitign = new File(".gitignore");
		File bu = new File(".gitignore.backup");
		File timestampedBu = new File(".gitignore." + (System.currentTimeMillis() / 1000) + ".backup");
		// if an old gitignore exists, backup
		if (gitign.exists()) {
			gitign.renameTo(timestampedBu);
			try {
				Files.copy(timestampedBu.toPath(), bu.toPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(proto));
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		List<String> gitignLines = new ArrayList<>();
		BufferedWriter writer = new BufferedWriter(new FileWriter(gitign));
		if (reader != null) {
			String line;
			try {
				while ((line = reader.readLine()) != null) {
					gitignLines.add(line);
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Add new lines for somformats, except in test/ sample/ and src/
		String[] excludeFolders = new String[] { "test/", "sample/", "src/", "notes/" };
		gitignLines.add("");
		gitignLines.add("#GENERATED START");
		for (int i = 0; i < SOMFormats.values().length; i++) {
			SOMFormats string = SOMFormats.values()[i];
			gitignLines.add("#Format " + i);
			gitignLines.add("#Name (internal) " + string.name());
			for (int j = 0; j < string.getFileExtensionString().length; j++) {
				String string1 = string.getFileExtensionString()[j];
				gitignLines.add("#File extension " + string1 + " (" + i + ")");
				if (!string1.startsWith(".")) {
					string1 = "." + string1;
				}
				gitignLines.add("*" + string1);
				for (int k = 0; k < excludeFolders.length; k++) {
					String string2 = excludeFolders[k];
					gitignLines.add("#Exclude folder " + string2 + " (" + i + ")");
					gitignLines.add("!" + string2 + "**/*" + string1);
				}
			}
			gitignLines.add("");
		}
		gitignLines.add("#GENERATED END");
		for (String string : gitignLines) {
			writer.write(string);
			writer.newLine();
		}
		writer.close();
	}

	private static void printVersion(boolean verbose) {
		VersionHelper vh = new VersionHelper();
		System.out.println(vh.getVersion());
		if (verbose) {
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
		options.addOption("n", "n", true, "Set a minimum value for N");
		options.addOption(null, "timeout", true, "Timeout after <value> in seconds");
		options.addOption(null, "regenerate-gitignore", false,
				"Regenerate the gitignore file with all the SOM file formats");
		return options;
	}
}
