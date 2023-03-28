package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import de.dralle.som.Compiler;
import de.dralle.som.FileLoader;
import de.dralle.som.IMemspace;
import de.dralle.som.ISomMemspace;
import de.dralle.som.SOMBitcodeRunner;
import de.dralle.som.SOMFormats;

class SampleFilesTests {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@ParameterizedTest(name = "{index} - Test loading with file ''{0}''")
	@MethodSource("fileProvider")
	void testLoadFile(File file) throws IOException {
		// get format
		SOMFormats format = new FileLoader().getFormatFromFilename(file);
		if (format != null) {
			Object model = new FileLoader().loadFromFile(file, format);
			assertNotNull(model);
		}
	}

	@ParameterizedTest(name = "{index} - Test compilation with file ''{0}''")
	@MethodSource("fileProvider")
	void testCompileFile(File file) throws IOException {
		// get format
		SOMFormats format = new FileLoader().getFormatFromFilename(file);
		if (format != null) {
			Object model = new FileLoader().loadFromFile(file, format);
			Object compiled = new Compiler().compile(model, format, SOMFormats.BIN);
			assertNotNull(compiled);
		}
	}

	@ParameterizedTest(name = "{index} - Test execution with file ''{0}''")
	@MethodSource("fileProvider")
	void testExecuteFile(File file) throws IOException {
		// get format
		SOMFormats format = new FileLoader().getFormatFromFilename(file);
		if (format != null) {
			Object model = new FileLoader().loadFromFile(file, format);
			IMemspace compiled = new Compiler().compile(model, format, SOMFormats.BIN);
			SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) compiled);
			runner.execute();
		}
	}

	static List<File> fileProvider() {
		List<File> fileList = new ArrayList<>();
		getFiles(new File("sample/"), fileList);
		return fileList;
	}

	private static void getFiles(File folder, List<File> fileList) {
		File[] files = folder.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					SOMFormats format = new FileLoader().getFormatFromFilename(file);
					if(format!=null) {
						fileList.add(file);
					}
				} else if (file.isDirectory()) {
					getFiles(file, fileList);
				}
			}
		}
	}
}
