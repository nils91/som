package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import de.dralle.som.BooleanArrayMemspace;
import de.dralle.som.Compiler;
import de.dralle.som.FileLoader;
import de.dralle.som.IMemspace;
import de.dralle.som.ISomMemspace;
import de.dralle.som.SOMBitcodeRunner;
import de.dralle.som.languages.hra.model.HRAModel;
import de.dralle.som.test.util.TestUtil;

class BitcodeRunnerExecuteHRATests {

	private Compiler c;
	private FileLoader f;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		c = new Compiler();
		f=new FileLoader();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testReturnCode0() throws IOException {
		HRAModel model = f.readHRAFile("test/fixtures/hra/minimal_return0.hra");
		IMemspace memspace = c.compileHRAtoMemspace(model);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertTrue(runner.execute());
	}

	@Test
	void testReturnCode1() throws IOException {
		HRAModel model = f.readHRAFile("test/fixtures/hra/minimal_return1.hra");
		IMemspace memspace = c.compileHRAtoMemspace(model);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertFalse(runner.execute());
	}

	@Test
	@Timeout(10)
	void testOpcodeNAR() throws IOException {
		HRAModel model =f.readHRAFile("test/fixtures/hra/test_nar.hra");
		IMemspace memspace=c.compileHRAtoMemspace(model);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		runner.execute();
		// should have written accumulator to 1
		assertTrue(runner.getMemspace().getAccumulatorValue());
	}

	@Test
	@Timeout(10)
	void testOpcodeNAW() throws IOException {
		HRAModel model =f.readHRAFile("test/fixtures/hra/test_naw.hra");
		IMemspace memspace=c.compileHRAtoMemspace(model);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertTrue(runner.execute());
		// should have written accumulator to 1
		assertTrue(runner.getMemspace().getAccumulatorValue());
	}
	
	@Test
	@Timeout(10)
	void testNANDExampleInvert() throws IOException {
		HRAModel model =f.readHRAFile("test/fixtures/hra/test_invert_with_nand.hra");
		IMemspace memspace=c.compileHRAtoMemspace(model);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertTrue(runner.execute());
		// should have written accumulator to 1
		assertTrue(runner.getMemspace().getAccumulatorValue());
	}
	@Test
	@Timeout(10)
	void testNANDExampleRead() throws IOException {
		HRAModel model =f.readHRAFile("test/fixtures/hra/test_read_with_nand.hra");
		IMemspace memspace=c.compileHRAtoMemspace(model);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertTrue(runner.execute());
		// should have written accumulator to 1
		assertTrue(runner.getMemspace().getAccumulatorValue());
	}
	@Test
	@Timeout(10)
	void testNANDExampleWrite() throws IOException {
		HRAModel model =f.readHRAFile("test/fixtures/hra/test_write_with_nand.hra");
		IMemspace memspace=c.compileHRAtoMemspace(model);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertTrue(runner.execute());
		// should have written accumulator to 1
		assertTrue(runner.getMemspace().getAccumulatorValue());
	}
}
