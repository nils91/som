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
import de.dralle.som.languages.hras.model.HRASModel;

class HRASMemAddressVariantsTests {

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
	void testMemAddressDecimalReturnCode0() throws IOException {
		HRASModel model = f.loadFromFile("test/fixtures/hras/minimal_return0_decimal.hras",SOMFormats.HRAS);
		IMemspace memspace = c.compile(model,SOMFormats.HRAS,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertTrue(runner.execute());
	}

	@Test
	void testMemAddressDecimalReturnCode1() throws IOException {
		HRASModel model = f.loadFromFile("test/fixtures/hras/minimal_return1_decimal.hras",SOMFormats.HRAS);
		IMemspace memspace = c.compile(model,SOMFormats.HRAS,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertFalse(runner.execute());
	}

	@Test
	void testMemAddressHexadecimalhReturnCode0() throws IOException {
		HRASModel model = f.loadFromFile("test/fixtures/hras/minimal_return0_hexh.hras",SOMFormats.HRAS);
		IMemspace memspace = c.compile(model,SOMFormats.HRAS,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertTrue(runner.execute());
	}

	@Test
	void testMemAddressHexadecimalhReturnCode1() throws IOException {
		HRASModel model = f.loadFromFile("test/fixtures/hras/minimal_return1_hexh.hras",SOMFormats.HRAS);
		IMemspace memspace = c.compile(model,SOMFormats.HRAS,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertFalse(runner.execute());
	}
	@Test
	void testMemAddressHexadecimalxReturnCode0() throws IOException {
		HRASModel model = f.loadFromFile("test/fixtures/hras/minimal_return0_hexx.hras",SOMFormats.HRAS);
		IMemspace memspace = c.compile(model,SOMFormats.HRAS,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertTrue(runner.execute());
	}

	@Test
	void testMemAddressHexadecimalxReturnCode1() throws IOException {
		HRASModel model = f.loadFromFile("test/fixtures/hras/minimal_return1_hexx.hras",SOMFormats.HRAS);
		IMemspace memspace = c.compile(model,SOMFormats.HRAS,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertFalse(runner.execute());
	}
	@Test
	void testMemAddressBinaryReturnCode0() throws IOException {
		HRASModel model = f.loadFromFile("test/fixtures/hras/minimal_return0_bin.hras",SOMFormats.HRAS);
		IMemspace memspace = c.compile(model,SOMFormats.HRAS,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertTrue(runner.execute());
	}

	@Test
	void testMemAddressBinaryReturnCode1() throws IOException {
		HRASModel model = f.loadFromFile("test/fixtures/hras/minimal_return1_bin.hras",SOMFormats.HRAS);
		IMemspace memspace = c.compile(model,SOMFormats.HRAS,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertFalse(runner.execute());
	}
	@Test
	void testMemAddressOctalReturnCode0() throws IOException {
		HRASModel model = f.loadFromFile("test/fixtures/hras/minimal_return0_oct.hras",SOMFormats.HRAS);
		IMemspace memspace = c.compile(model,SOMFormats.HRAS,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertTrue(runner.execute());
	}

	@Test
	void testMemAddressOctalReturnCode1() throws IOException {
		HRASModel model = f.loadFromFile("test/fixtures/hras/minimal_return1_oct.hras",SOMFormats.HRAS);
		IMemspace memspace = c.compile(model,SOMFormats.HRAS,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertFalse(runner.execute());
	}
	@Test
	void testMemAddressDecimalExplicitBaseReturnCode0() throws IOException {
		HRASModel model = f.loadFromFile("test/fixtures/hras/minimal_return0_decimal_base_explicit.hras",SOMFormats.HRAS);
		IMemspace memspace = c.compile(model,SOMFormats.HRAS,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertTrue(runner.execute());
	}

	@Test
	void testMemAddressDecimalExplicitBaseReturnCode1() throws IOException {
		HRASModel model = f.loadFromFile("test/fixtures/hras/minimal_return1_decimal_base_explicit.hras",SOMFormats.HRAS);
		IMemspace memspace = c.compile(model,SOMFormats.HRAS,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertFalse(runner.execute());
	}
	@Test
	void testMemAddressDecimalExplicitReturnCode0() throws IOException {
		HRASModel model = f.loadFromFile("test/fixtures/hras/minimal_return0_decimal_explicit.hras",SOMFormats.HRAS);
		IMemspace memspace = c.compile(model,SOMFormats.HRAS,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertTrue(runner.execute());
	}

	@Test
	void testMemAddressDecimalExplicitReturnCode1() throws IOException {
		HRASModel model = f.loadFromFile("test/fixtures/hras/minimal_return1_decimal_explicit.hras",SOMFormats.HRAS);
		IMemspace memspace = c.compile(model,SOMFormats.HRAS,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertFalse(runner.execute());
	}
}
