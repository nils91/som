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
import de.dralle.som.languages.hrbs.model.HRBSModel;

class BitcodeRunnerExecuteHRBsSameFileCommandsTests {

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
	@Timeout(10)
	void testInvert() throws IOException {
		HRBSModel model =f.readHRBSFile("test/fixtures/hrbs/commands_single_file/test_invert_acc.hrbs");
		IMemspace memspace=c.compile(model, SOMFormats.HRBS	, SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertTrue(runner.execute());
		// should have written accumulator to 1
		assertTrue(runner.getMemspace().getAccumulatorValue());
	}
	@Test
	@Timeout(10)
	void testSimpleRead() throws IOException {
		HRBSModel model =f.readHRBSFile("test/fixtures/hrbs/commands_single_file/test_read_simple.hrbs");
		IMemspace memspace=c.compile(model, SOMFormats.HRBS	, SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertTrue(runner.execute());
		// should have written accumulator to 1
		assertTrue(runner.getMemspace().getAccumulatorValue());
	}
	@Test
	@Timeout(10)
	void testSimpleWrite() throws IOException {
		HRBSModel model =f.readHRBSFile("test/fixtures/hrbs/commands_single_file/test_write_simple.hrbs");
		IMemspace memspace=c.compile(model, SOMFormats.HRBS	, SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertTrue(runner.execute());
		// should have written accumulator to 1
		assertTrue(runner.getMemspace().getAccumulatorValue());
	}
}
