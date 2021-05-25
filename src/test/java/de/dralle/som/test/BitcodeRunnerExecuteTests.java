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

import de.dralle.som.SOMBitcodeRunner;
import de.dralle.som.test.util.TestUtil;

class BitcodeRunnerExecuteTests {

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

	@Test
	void testReturnCode0() throws IOException {
		String entireFile=TestUtil.readFileToString("test/fixtures/ab/minimal_return0.ab");
		SOMBitcodeRunner runner=new SOMBitcodeRunner(entireFile);
		assertTrue(runner.execute());
	}
	@Test
	void testReturnCode1() throws IOException {
		String entireFile=TestUtil.readFileToString("test/fixtures/ab/minimal_return1.ab");
		SOMBitcodeRunner runner=new SOMBitcodeRunner(entireFile);
		assertFalse(runner.execute());
	}
	@Test
	@Timeout(10)
	void testOpcodeNAR() throws IOException {
		String entireFile=TestUtil.readFileToString("test/fixtures/ab/test_nar.ab");
		SOMBitcodeRunner runner=new SOMBitcodeRunner(entireFile);
		runner.execute();
		//should have written accumulator to 1
		assertTrue(runner.getMemspace().getAccumulatorValue());
	}
	@Test
	@Timeout(10)
	void testOpcodeNAW() throws IOException {
		String entireFile=TestUtil.readFileToString("test/fixtures/ab/test_naw.ab");
		SOMBitcodeRunner runner=new SOMBitcodeRunner(entireFile);
		assertTrue(runner.execute());
		//should have written accumulator to 1
		assertTrue(runner.getMemspace().getAccumulatorValue());
	}
}
