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
	private static Path testFixturesABPath;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		testFixturesABPath = Paths.get("test", "fixtures", "ab");
		tmpPath = Paths.get("test", "tmp", FormatHRASFileWriteTests.class.getName());
		tmpPathWithHRAS = Paths.get(tmpPath.toString(), "bin");
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
	@MethodSource("filesABFixturesInFolder")
	void testLoadSuccess(File file) throws IOException {
		String fileName = file.getName();
		if (fileName.endsWith("hras")) {
			HRASModel m = f.readHRAFile(file.getPath());
			assertNotNull(m);
		}
	}

	@ParameterizedTest
	@MethodSource("filesABFixturesInFolder")
	void testCompileSuccess(File file) throws IOException {
		String fileName = file.getName();
		if (fileName.endsWith("hras")) {
			HRASModel m = f.readHRAFile(file.getPath());
			IMemspace nm = m.compileToMemspace();
			assertNotNull(nm);
		}
	}

	@ParameterizedTest
	@MethodSource("filesABFixturesInFolder")
	void testConvertSuccessContentEqual(File file) throws IOException {
		String fileName = file.getName();
		if (fileName.endsWith("ab")) {
			IMemspace m = f.loadAsciiBinaryFile(file.getPath());
			byte[] ba = c.memspaceToByteArray(m);
			IMemspace nm = c.byteArrayToMemspace(ba);
			assertTrue(m.equalContent(nm));
		}
	}

	@ParameterizedTest
	@MethodSource("filesABFixturesInFolder")
	void testConvertAndWriteSuccess(File file) throws IOException {
		String fileName = file.getName();
		if (fileName.endsWith("ab")) {
			IMemspace m = f.loadAsciiBinaryFile(file.getPath());
			String newFileName = fileName + ".bin";
			f.writeBinaryFile(m, Paths.get(tmpPathWithHRAS.toString(), newFileName).toString());
			assertTrue(Paths.get(tmpPathWithHRAS.toString(), newFileName).toFile().exists());
		}
	}

	@ParameterizedTest
	@MethodSource("filesABFixturesInFolder")
	void testConvertAndWriteSuccessLoadSuccess(File file) throws IOException {
		String fileName = file.getName();
		if (fileName.endsWith("ab")) {
			IMemspace m = f.loadAsciiBinaryFile(file.getPath());
			String newFileName = fileName + ".bin";
			f.writeBinaryFile(m, Paths.get(tmpPathWithHRAS.toString(), newFileName).toString());
			IMemspace nm = f.loadBinaryFile(Paths.get(tmpPathWithHRAS.toString(), newFileName).toString());
			assertNotNull(nm);
		}
	}

	@ParameterizedTest
	@MethodSource("filesABFixturesInFolder")
	void testConvertAndWriteSuccessLoadSuccessContenEqual(File file) throws IOException {
		String fileName = file.getName();
		if (fileName.endsWith("ab")) {
			IMemspace m = f.loadAsciiBinaryFile(file.getPath());
			String newFileName = fileName + ".bin";
			f.writeBinaryFile(m, Paths.get(tmpPathWithHRAS.toString(), newFileName).toString());
			IMemspace nm = f.loadBinaryFile(Paths.get(tmpPathWithHRAS.toString(), newFileName).toString());
			assertTrue(m.equalContent(nm));
		}
	}

	@ParameterizedTest
	@MethodSource("filesABFixturesInFolder")
	@Timeout(300)
	void testConvertAndWriteSuccessLoadSuccessCanExecute(File file) throws IOException {
		String fileName = file.getName();
		if (fileName.endsWith("ab")) {
			IMemspace m = f.loadAsciiBinaryFile(file.getPath());
			String newFileName = fileName + ".bin";
			f.writeBinaryFile(m, Paths.get(tmpPathWithHRAS.toString(), newFileName).toString());
			IMemspace nm = f.loadBinaryFile(Paths.get(tmpPathWithHRAS.toString(), newFileName).toString());
			SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) nm);
			runner.execute();
		}
	}

	@ParameterizedTest
	@MethodSource("filesABFixturesInFolder")
	@Timeout(600)
	void testConvertAndWriteSuccessLoadSuccessCanExecuteSameResult(File file) throws IOException {
		String fileName = file.getName();
		if (fileName.endsWith("ab")) {
			IMemspace m = f.loadAsciiBinaryFile(file.getPath());
			String newFileName = fileName + ".bin";
			f.writeBinaryFile(m, Paths.get(tmpPathWithHRAS.toString(), newFileName).toString());
			IMemspace nm = f.loadBinaryFile(Paths.get(tmpPathWithHRAS.toString(), newFileName).toString());
			SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) nm);
			SOMBitcodeRunner runner2 = new SOMBitcodeRunner((ISomMemspace) m);
			assertEquals(runner.execute(), runner2.execute());
		}
	}

	private static Stream<File> filesABFixturesInFolder() {
		return Stream.of(testFixturesABPath.toFile().listFiles());
	}

}
