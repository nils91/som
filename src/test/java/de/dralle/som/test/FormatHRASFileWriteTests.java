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
import de.dralle.som.languages.hras.HRASParser;
import de.dralle.som.languages.hras.model.HRASModel;

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
class FormatHRASFileWriteTests {
	private Compiler c;
	private FileLoader f;

	private static Path tmpPath;
	private static Path tmpPathWithHRAS;
	private static Path testFixturesHRASPath;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		testFixturesHRASPath = Paths.get("test", "fixtures", "hras");
		tmpPath = Paths.get("test", "tmp", FormatHRASFileWriteTests.class.getName());
		tmpPathWithHRAS = Paths.get(tmpPath.toString(), "hras");
		Files.createDirectories(tmpPathWithHRAS);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		tmpPathWithHRAS.toFile().delete();
	}

	@BeforeEach
	void setUp() throws Exception {
		c = new Compiler();
		f = new FileLoader();
	}

	@AfterEach
	void tearDown() throws Exception {
		File[] binFiles = tmpPathWithHRAS.toFile().listFiles();
		for (int i = 0; i < binFiles.length; i++) {
			File file = binFiles[i];
			file.delete();
		}
	}

	@ParameterizedTest
	@MethodSource("filesHRASFixturesInFolder")
	void testLoadSuccess(File file) throws IOException {
		String fileName = file.getName();
		if (fileName.endsWith("hras")) {
			HRASModel m = f.readHRASFile(file.getPath());
			assertNotNull(m);
		}
	}

	@ParameterizedTest
	@MethodSource("filesHRASFixturesInFolder")
	void testCompileSuccess(File file) throws IOException {
		String fileName = file.getName();
		if (fileName.endsWith("hras")) {
			HRASModel m = f.readHRASFile(file.getPath());
			IMemspace nm =  c.compile(m, SOMFormats.HRAS, SOMFormats.BIN);
			assertNotNull(nm);
		}
	}
	@ParameterizedTest
	@MethodSource("filesHRASFixturesInFolder")
	void testCompileSuccessExecute(File file) throws IOException {
		String fileName = file.getName();
		if (fileName.endsWith("hras")) {
			HRASModel m = f.readHRASFile(file.getPath());
			IMemspace nm =   c.compile(m, SOMFormats.HRAS, SOMFormats.BIN);
			assertNotNull(nm);
			SOMBitcodeRunner runner=new SOMBitcodeRunner((ISomMemspace) nm);
			runner.execute();
		}
	}
	@ParameterizedTest
	@MethodSource("filesHRASFixturesInFolder")
	void testCompileTwiceContentEqual(File file) throws IOException {
		String fileName = file.getName();
		if (fileName.endsWith("hras")) {
			HRASModel m = f.readHRASFile(file.getPath());
			IMemspace nm  = c.compile(m, SOMFormats.HRAS, SOMFormats.BIN);
			IMemspace nm2 =  c.compile(m, SOMFormats.HRAS, SOMFormats.BIN);
			assertTrue(nm.equalContent(nm2));
		}
	}@ParameterizedTest
	@MethodSource("filesHRASFixturesInFolder")
	void testCompileTwiceFromFileContentEqual(File file) throws IOException {
		String fileName = file.getName();
		if (fileName.endsWith("hras")) {
			HRASModel m = f.readHRASFile(file.getPath());
			HRASModel m2 = f.readHRASFile(file.getPath());
			IMemspace nm =  c.compile(m, SOMFormats.HRAS, SOMFormats.BIN);
			IMemspace nm2 =  c.compile(m2, SOMFormats.HRAS, SOMFormats.BIN);
			assertTrue(nm.equalContent(nm2));
		}
	}
	@ParameterizedTest
	@MethodSource("filesHRASFixturesInFolder")
	void testCompileFromModelOutputContentEqual(File file) throws IOException {
		String fileName = file.getName();
		if (fileName.endsWith("hras")) {
			HRASModel m = f.readHRASFile(file.getPath());
			String hrasCode = m.asCode();
			HRASParser p = new HRASParser();
			HRASModel m2 = p.parse(hrasCode);
			IMemspace nm =   c.compile(m, SOMFormats.HRAS, SOMFormats.BIN);
			IMemspace nm2 =  c.compile(m2, SOMFormats.HRAS, SOMFormats.BIN);
			assertTrue(nm.equalContent(nm2));
		}
	}
	@ParameterizedTest
	@MethodSource("filesHRASFixturesInFolder")
	void testCompileFromModelOutput(File file) throws IOException {
		String fileName = file.getName();
		if (fileName.endsWith("hras")) {
			HRASModel m = f.readHRASFile(file.getPath());
			String hrasCode = m.asCode();
			HRASParser p = new HRASParser();
			HRASModel m2 = p.parse(hrasCode);
			IMemspace nm2 =  c.compile(m2, SOMFormats.HRAS, SOMFormats.BIN);
			assertNotNull(nm2);
		}
	}
	@ParameterizedTest
	@MethodSource("filesHRASFixturesInFolder")
	void testConvertAndWriteSuccess(File file) throws IOException {
		String fileName = file.getName();
		if (fileName.endsWith("hras")) {
			HRASModel m = f.readHRASFile(file.getPath());
			IMemspace mem = c.compile(m,SOMFormats.HRAS,SOMFormats.BIN);
			String newFileName = fileName + ".bin";
			f.writeBinaryFile(mem, Paths.get(tmpPathWithHRAS.toString(), newFileName).toString());
			assertTrue(Paths.get(tmpPathWithHRAS.toString(), newFileName).toFile().exists());
		}
	}

	@ParameterizedTest
	@MethodSource("filesHRASFixturesInFolder")
	void testConvertAndWriteSuccessLoadSuccess(File file) throws IOException {
	
			String fileName = file.getName();
			if (fileName.endsWith("hras")) {
				HRASModel m = f.readHRASFile(file.getPath());
				IMemspace mem = c.compile(m,SOMFormats.HRAS,SOMFormats.BIN);
				String newFileName = fileName + ".bin";
				f.writeBinaryFile(mem, Paths.get(tmpPathWithHRAS.toString(), newFileName).toString());
			IMemspace nm = f.loadBinaryFile(Paths.get(tmpPathWithHRAS.toString(), newFileName).toString());
			assertNotNull(nm);
		}
	}

	@ParameterizedTest
	@MethodSource("filesHRASFixturesInFolder")
	void testConvertAndWriteSuccessLoadSuccessContenEqual(File file) throws IOException {
		String fileName = file.getName();
		if (fileName.endsWith("hras")) {
			HRASModel m = f.readHRASFile(file.getPath());
			IMemspace mem = c.compile(m,SOMFormats.HRAS,SOMFormats.BIN);
			String newFileName = fileName + ".bin";
			f.writeBinaryFile(mem, Paths.get(tmpPathWithHRAS.toString(), newFileName).toString());
			IMemspace nm = f.loadBinaryFile(Paths.get(tmpPathWithHRAS.toString(), newFileName).toString());
			assertTrue(mem.equalContent(nm));
		}
	}

	@ParameterizedTest
	@MethodSource("filesHRASFixturesInFolder")
	@Timeout(300)
	void testConvertAndWriteSuccessLoadSuccessCanExecute(File file) throws IOException {
		String fileName = file.getName();
		if (fileName.endsWith("hras")) {
			HRASModel m = f.readHRASFile(file.getPath());
			IMemspace mem = c.compile(m,SOMFormats.HRAS,SOMFormats.BIN);
			String newFileName = fileName + ".bin";
			f.writeBinaryFile(mem, Paths.get(tmpPathWithHRAS.toString(), newFileName).toString());
			IMemspace nm = f.loadBinaryFile(Paths.get(tmpPathWithHRAS.toString(), newFileName).toString());
			SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) nm);
			runner.execute();
		}
	}

	@ParameterizedTest
	@MethodSource("filesHRASFixturesInFolder")
	@Timeout(600)
	void testConvertAndWriteSuccessLoadSuccessCanExecuteSameResult(File file) throws IOException {
		String fileName = file.getName();
		if (fileName.endsWith("hras")) {
			HRASModel m = f.readHRASFile(file.getPath());
			IMemspace mem = c.compile(m,SOMFormats.HRAS,SOMFormats.BIN);
			String newFileName = fileName + ".bin";
			f.writeBinaryFile(mem, Paths.get(tmpPathWithHRAS.toString(), newFileName).toString());
			IMemspace nm = f.loadBinaryFile(Paths.get(tmpPathWithHRAS.toString(), newFileName).toString());
			SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) nm);
			SOMBitcodeRunner runner2 = new SOMBitcodeRunner((ISomMemspace) mem);
			assertEquals(runner.execute(), runner2.execute());
		}
	}

	private static Stream<File> filesHRASFixturesInFolder() {
		return Stream.of(testFixturesHRASPath.toFile().listFiles());
	}

}
