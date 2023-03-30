package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import de.dralle.som.Compiler;
import de.dralle.som.FileLoader;
import de.dralle.som.IMemspace;
import de.dralle.som.ISomMemspace;
import de.dralle.som.SOMBitcodeRunner;
import de.dralle.som.SOMFormats;
import de.dralle.som.languages.hrac.HRACParser;
import de.dralle.som.languages.hrac.model.HRACModel;

/**
 * 
 */

/**
 * These tests will load all of the AB files from the test/fixtures/ab dir,
 * convert them to bin and check for content equality.
 * 
 * @author Nils
 *
 */
class FormatHRACFileWriteTests {
	private Compiler c;
	private FileLoader f;

	private static Path tmpPath;
	private static Path tmpPathWithHRAC;
	private static Path testFixturesHRACPath;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		testFixturesHRACPath = Paths.get("test", "fixtures", "hrac");
		tmpPath = Paths.get("test", "tmp", FormatHRACFileWriteTests.class.getName());
		tmpPathWithHRAC = Paths.get(tmpPath.toString(), "hrac");
		Files.createDirectories(tmpPathWithHRAC);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		tmpPathWithHRAC.toFile().delete();
	}

	@BeforeEach
	void setUp() throws Exception {
		c = new Compiler();
		f = new FileLoader();
	}

	@AfterEach
	void tearDown() throws Exception {
		File[] binFiles = tmpPathWithHRAC.toFile().listFiles();
		for (int i = 0; i < binFiles.length; i++) {
			File file = binFiles[i];
			file.delete();
		}
	}

	@ParameterizedTest
	@MethodSource("filesHRACFixturesInFolder")
	void testLoadSuccess(File file) throws IOException {
		String fileName = file.getName();
		if (fileName.endsWith("hrac")) {
			HRACModel m = f.readHRACFile(file.getPath());
			assertNotNull(m);
		}
	}

	@ParameterizedTest
	@MethodSource("filesHRACFixturesInFolder")
	void testCompileSuccess(File file) throws IOException {
		String fileName = file.getName();
		if (fileName.endsWith("hrac")) {
			HRACModel m = f.readHRACFile(file.getPath());
			IMemspace nm = c.compile(m, SOMFormats.HRAC, SOMFormats.BIN);
			assertNotNull(nm);
		}
	}
	@ParameterizedTest
	@MethodSource("filesHRACFixturesInFolder")
	void testCompileSuccessExecute(File file) throws IOException {
		String fileName = file.getName();
		if (fileName.endsWith("hrac")) {
			HRACModel m = f.readHRACFile(file.getPath());
			IMemspace nm = c.compile(m, SOMFormats.HRAC, SOMFormats.BIN);
			assertNotNull(nm);
			SOMBitcodeRunner runner=new SOMBitcodeRunner((ISomMemspace) nm);
			runner.execute();
		}
	}
	@ParameterizedTest
	@MethodSource("filesHRACFixturesInFolder")
	void testCompileTwiceContentEqual(File file) throws IOException {
		String fileName = file.getName();
		if (fileName.endsWith("hrac")) {
			HRACModel m = f.readHRACFile(file.getPath());
			IMemspace nm = c.compile(m, SOMFormats.HRAC, SOMFormats.BIN);
			IMemspace nm2 =  c.compile(m, SOMFormats.HRAC, SOMFormats.BIN);
			assertTrue(nm.equalContent(nm2));
		}
	}@ParameterizedTest
	@MethodSource("filesHRACFixturesInFolder")
	void testCompileTwiceFromFileContentEqual(File file) throws IOException {
		String fileName = file.getName();
		if (fileName.endsWith("hrac")) {
			HRACModel m = f.readHRACFile(file.getPath());
			HRACModel m2 = f.readHRACFile(file.getPath());
			IMemspace nm = c.compile(m, SOMFormats.HRAC, SOMFormats.BIN);
			IMemspace nm2 =  c.compile(m2, SOMFormats.HRAC, SOMFormats.BIN);
			assertTrue(nm.equalContent(nm2));
		}
	}
	@ParameterizedTest
	@MethodSource("filesHRACFixturesInFolder")
	void testCompileFromModelOutputContentEqual(File file) throws IOException {
		String fileName = file.getName();
		if (fileName.endsWith("hrac")) {
			HRACModel m = f.readHRACFile(file.getPath());
			String hracCode = m.asCode();
			HRACParser p = new HRACParser();
			HRACModel m2 = p.parse(hracCode);
			IMemspace nm =  c.compile(m, SOMFormats.HRAC, SOMFormats.BIN);
			IMemspace nm2 =  c.compile(m2, SOMFormats.HRAC, SOMFormats.BIN);
			assertTrue(nm.equalContent(nm2));
		}
	}
	@ParameterizedTest
	@MethodSource("filesHRACFixturesInFolder")
	void testCompileFromModelOutput(File file) throws IOException {
		String fileName = file.getName();
		if (fileName.endsWith("hrac")) {
			HRACModel m = f.readHRACFile(file.getPath());
			String hracCode = m.asCode();
			HRACParser p = new HRACParser();
			HRACModel m2 = p.parse(hracCode);
			IMemspace nm2 =  c.compile(m2, SOMFormats.HRAC, SOMFormats.BIN);
			assertNotNull(nm2);
		}
	}
	@ParameterizedTest
	@MethodSource("filesHRACFixturesInFolder")
	void testConvertAndWriteSuccess(File file) throws IOException {
		String fileName = file.getName();
		if (fileName.endsWith("hrac")) {
			HRACModel m = f.readHRACFile(file.getPath());
			IMemspace mem = c.compile(m,SOMFormats.HRAC,SOMFormats.BIN);
			String newFileName = fileName + ".bin";
			f.writeBinaryFile(mem, Paths.get(tmpPathWithHRAC.toString(), newFileName).toString());
			assertTrue(Paths.get(tmpPathWithHRAC.toString(), newFileName).toFile().exists());
		}
	}

	@ParameterizedTest
	@MethodSource("filesHRACFixturesInFolder")
	void testConvertAndWriteSuccessLoadSuccess(File file) throws IOException {
	
			String fileName = file.getName();
			if (fileName.endsWith("hrac")) {
				HRACModel m = f.readHRACFile(file.getPath());
				IMemspace mem = c.compile(m,SOMFormats.HRAC,SOMFormats.BIN);
				String newFileName = fileName + ".bin";
				f.writeBinaryFile(mem, Paths.get(tmpPathWithHRAC.toString(), newFileName).toString());
			IMemspace nm = f.loadBinaryFile(Paths.get(tmpPathWithHRAC.toString(), newFileName).toString());
			assertNotNull(nm);
		}
	}

	@ParameterizedTest
	@MethodSource("filesHRACFixturesInFolder")
	void testConvertAndWriteSuccessLoadSuccessContenEqual(File file) throws IOException {
		String fileName = file.getName();
		if (fileName.endsWith("hrac")) {
			HRACModel m = f.readHRACFile(file.getPath());
			IMemspace mem = c.compile(m,SOMFormats.HRAC,SOMFormats.BIN);
			String newFileName = fileName + ".bin";
			f.writeBinaryFile(mem, Paths.get(tmpPathWithHRAC.toString(), newFileName).toString());
			IMemspace nm = f.loadBinaryFile(Paths.get(tmpPathWithHRAC.toString(), newFileName).toString());
			assertTrue(mem.equalContent(nm));
		}
	}

	@ParameterizedTest
	@MethodSource("filesHRACFixturesInFolder")
	@Timeout(300)
	void testConvertAndWriteSuccessLoadSuccessCanExecute(File file) throws IOException {
		String fileName = file.getName();
		if (fileName.endsWith("hrac")) {
			HRACModel m = f.readHRACFile(file.getPath());
			IMemspace mem = c.compile(m,SOMFormats.HRAC,SOMFormats.BIN);
			String newFileName = fileName + ".bin";
			f.writeBinaryFile(mem, Paths.get(tmpPathWithHRAC.toString(), newFileName).toString());
			IMemspace nm = f.loadBinaryFile(Paths.get(tmpPathWithHRAC.toString(), newFileName).toString());
			SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) nm);
			runner.execute();
		}
	}

	@ParameterizedTest
	@MethodSource("filesHRACFixturesInFolder")
	@Timeout(600)
	void testConvertAndWriteSuccessLoadSuccessCanExecuteSameResult(File file) throws IOException {
		String fileName = file.getName();
		if (fileName.endsWith("hrac")) {
			HRACModel m = f.readHRACFile(file.getPath());
			IMemspace mem = c.compile(m,SOMFormats.HRAC,SOMFormats.BIN);
			String newFileName = fileName + ".bin";
			f.writeBinaryFile(mem, Paths.get(tmpPathWithHRAC.toString(), newFileName).toString());
			IMemspace nm = f.loadBinaryFile(Paths.get(tmpPathWithHRAC.toString(), newFileName).toString());
			SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) nm);
			SOMBitcodeRunner runner2 = new SOMBitcodeRunner((ISomMemspace) mem);
			assertEquals(runner.execute(), runner2.execute());
		}
	}

	private static Stream<File> filesHRACFixturesInFolder() {
		return Stream.of(testFixturesHRACPath.toFile().listFiles());
	}

}
