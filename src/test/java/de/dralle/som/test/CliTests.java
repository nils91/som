/**
 * 
 */
package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.dralle.som.Main;

/**
 * @author Nils
 *
 */
class CliTests {

	private static Path tmpPath;
	private static Path testFixturesHRACPath;
	private static Path tmpPathWithHRAC;
	private static Path testFixturesHRASPath;
	private static Path testFixturesABPath;
	private static Path testFixturesBINPath;
	private static Path tmpPathWithHRAS;
	private static Path tmpPathWithAB;
	private static Path tmpPathWithBIN;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		testFixturesHRACPath = Paths.get("test", "fixtures", "hrac");
		testFixturesHRASPath = Paths.get("test", "fixtures", "hras");
		testFixturesABPath = Paths.get("test", "fixtures", "ab");
		testFixturesBINPath = Paths.get("test", "fixtures", "bin");
		tmpPath = Paths.get("test", "tmp", CliTests.class.getName());
		tmpPathWithHRAC = Paths.get(tmpPath.toString(), "hrac");
		tmpPathWithHRAS = Paths.get(tmpPath.toString(), "hras");
		tmpPathWithAB = Paths.get(tmpPath.toString(), "ab");
		tmpPathWithBIN = Paths.get(tmpPath.toString(), "bin");
		Files.createDirectories(tmpPathWithHRAC);
		Files.createDirectories(tmpPathWithHRAS);
		Files.createDirectories(tmpPathWithBIN);
		Files.createDirectories(tmpPathWithAB);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
		tmpPathWithHRAC.toFile().delete();
		tmpPathWithHRAS.toFile().delete();
		tmpPathWithAB.toFile().delete();
		tmpPathWithBIN.toFile().delete();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testCompileHRASToBIN() throws IOException {
		Main.main(new String[] { "--compile",
				"--infile=" + Paths.get(testFixturesHRASPath.toString(), "test_write_with_nand.hras").toString(),
				"--informat==hras",
				"--outfile=" + Paths.get(tmpPathWithBIN.toString(), "test_write_with_nand.bin").toString(),
				"--outformat=bin" });
		assertTrue(new File(Paths.get(tmpPathWithBIN.toString(), "test_write_with_nand.bin").toString()).exists());
	}
}
