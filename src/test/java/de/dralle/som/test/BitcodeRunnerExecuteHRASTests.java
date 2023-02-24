package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import de.dralle.som.Compiler;
import de.dralle.som.FileLoader;
import de.dralle.som.IMemspace;
import de.dralle.som.ISomMemspace;
import de.dralle.som.SOMBitcodeRunner;
import de.dralle.som.SOMFormats;
import de.dralle.som.languages.hras.model.HRASModel;

class BitcodeRunnerExecuteHRASTests {

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
		HRASModel model = f.loadFromFile("test/fixtures/hras/minimal_return0.hras",SOMFormats.HRAS);
		IMemspace memspace = c.compile(model,SOMFormats.HRAS,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertTrue(runner.execute());
	}

	@Test
	void testReturnCode1() throws IOException {
		HRASModel model = f.loadFromFile("test/fixtures/hras/minimal_return1.hras",SOMFormats.HRAS);
		IMemspace memspace = c.compile(model,SOMFormats.HRAS,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertFalse(runner.execute());
	}

	@Test
	@Timeout(10)
	void testOpcodeNAR() throws IOException {
		HRASModel model =f.loadFromFile("test/fixtures/hras/test_nar.hras",SOMFormats.HRAS);
		IMemspace memspace=c.compile(model,SOMFormats.HRAS,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		runner.execute();
		// should have written accumulator to 1
		assertTrue(runner.getMemspace().getAccumulatorValue());
	}

	@Test
	@Timeout(10)
	void testOpcodeNAW() throws IOException {
		HRASModel model =f.loadFromFile("test/fixtures/hras/test_naw.hras",SOMFormats.HRAS);
		IMemspace memspace=c.compile(model,SOMFormats.HRAS,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertTrue(runner.execute());
		// should have written accumulator to 1
		assertTrue(runner.getMemspace().getAccumulatorValue());
	}
	
	@Test
	@Timeout(10)
	void testNANDExampleInvert() throws IOException {
		HRASModel model =f.loadFromFile("test/fixtures/hras/test_invert_with_nand.hras",SOMFormats.HRAS);
		IMemspace memspace=c.compile(model,SOMFormats.HRAS,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertTrue(runner.execute());
		// should have written accumulator to 1
		assertTrue(runner.getMemspace().getAccumulatorValue());
	}
	@Test
	@Timeout(10)
	void testNANDExampleRead() throws IOException {
		HRASModel model =f.loadFromFile("test/fixtures/hras/test_read_with_nand.hras",SOMFormats.HRAS);
		IMemspace memspace=c.compile(model,SOMFormats.HRAS,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertTrue(runner.execute());
		// should have written accumulator to 1
		assertTrue(runner.getMemspace().getAccumulatorValue());
	}
	@Test
	@Timeout(10)
	void testNANDExampleWrite() throws IOException {
		HRASModel model =f.loadFromFile("test/fixtures/hras/test_write_with_nand.hras",SOMFormats.HRAS);
		IMemspace memspace=c.compile(model,SOMFormats.HRAS,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertTrue(runner.execute());
		// should have written accumulator to 1
		assertTrue(runner.getMemspace().getAccumulatorValue());
	}
}
