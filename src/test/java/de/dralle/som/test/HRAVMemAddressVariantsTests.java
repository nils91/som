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
import de.dralle.som.languages.hrav.model.HRAVModel;

class HRAVMemAddressVariantsTests {

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
		HRAVModel model = f.loadFromFile("test/fixtures/hrav/minimal_return0_decimal.hrav",SOMFormats.HRAV);
		IMemspace memspace = c.compile(model,SOMFormats.HRAV,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertTrue(runner.execute());
	}

	@Test
	void testMemAddressDecimalReturnCode1() throws IOException {
		HRAVModel model = f.loadFromFile("test/fixtures/hrav/minimal_return1_decimal.hrav",SOMFormats.HRAV);
		IMemspace memspace = c.compile(model,SOMFormats.HRAV,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertFalse(runner.execute());
	}

	@Test
	void testMemAddressHexadecimalhReturnCode0() throws IOException {
		HRAVModel model = f.loadFromFile("test/fixtures/hrav/minimal_return0_hexh.hrav",SOMFormats.HRAV);
		IMemspace memspace = c.compile(model,SOMFormats.HRAV,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertTrue(runner.execute());
	}

	@Test
	void testMemAddressHexadecimalhReturnCode1() throws IOException {
		HRAVModel model = f.loadFromFile("test/fixtures/hrav/minimal_return1_hexh.hrav",SOMFormats.HRAV);
		IMemspace memspace = c.compile(model,SOMFormats.HRAV,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertFalse(runner.execute());
	}
	@Test
	void testMemAddressHexadecimalxReturnCode0() throws IOException {
		HRAVModel model = f.loadFromFile("test/fixtures/hrav/minimal_return0_hexx.hrav",SOMFormats.HRAV);
		IMemspace memspace = c.compile(model,SOMFormats.HRAV,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertTrue(runner.execute());
	}

	@Test
	void testMemAddressHexadecimalxReturnCode1() throws IOException {
		HRAVModel model = f.loadFromFile("test/fixtures/hrav/minimal_return1_hexx.hrav",SOMFormats.HRAV);
		IMemspace memspace = c.compile(model,SOMFormats.HRAV,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertFalse(runner.execute());
	}
	@Test
	void testMemAddressBinaryReturnCode0() throws IOException {
		HRAVModel model = f.loadFromFile("test/fixtures/hrav/minimal_return0_bin.hrav",SOMFormats.HRAV);
		IMemspace memspace = c.compile(model,SOMFormats.HRAV,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertTrue(runner.execute());
	}

	@Test
	void testMemAddressBinaryReturnCode1() throws IOException {
		HRAVModel model = f.loadFromFile("test/fixtures/hrav/minimal_return1_bin.hrav",SOMFormats.HRAV);
		IMemspace memspace = c.compile(model,SOMFormats.HRAV,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertFalse(runner.execute());
	}
	@Test
	void testMemAddressOctalReturnCode0() throws IOException {
		HRAVModel model = f.loadFromFile("test/fixtures/hrav/minimal_return0_oct.hrav",SOMFormats.HRAV);
		IMemspace memspace = c.compile(model,SOMFormats.HRAV,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertTrue(runner.execute());
	}

	@Test
	void testMemAddressOctalReturnCode1() throws IOException {
		HRAVModel model = f.loadFromFile("test/fixtures/hrav/minimal_return1_oct.hrav",SOMFormats.HRAV);
		IMemspace memspace = c.compile(model,SOMFormats.HRAV,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertFalse(runner.execute());
	}
	@Test
	void testMemAddressDecimalExplicitBaseReturnCode0() throws IOException {
		HRAVModel model = f.loadFromFile("test/fixtures/hrav/minimal_return0_decimal_explicit_base.hrav",SOMFormats.HRAV);
		IMemspace memspace = c.compile(model,SOMFormats.HRAV,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertTrue(runner.execute());
	}

	@Test
	void testMemAddressDecimalExplicitBaseReturnCode1() throws IOException {
		HRAVModel model = f.loadFromFile("test/fixtures/hrav/minimal_return1_decimal_explicit_base.hrav",SOMFormats.HRAV);
		IMemspace memspace = c.compile(model,SOMFormats.HRAV,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertFalse(runner.execute());
	}
	@Test
	void testMemAddressDecimalExplicitReturnCode0() throws IOException {
		HRAVModel model = f.loadFromFile("test/fixtures/hrav/minimal_return0_decimal_explicit.hrav",SOMFormats.HRAV);
		IMemspace memspace = c.compile(model,SOMFormats.HRAV,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertTrue(runner.execute());
	}

	@Test
	void testMemAddressDecimalExplicitReturnCode1() throws IOException {
		HRAVModel model = f.loadFromFile("test/fixtures/hrav/minimal_return1_decimal_explicit.hrav",SOMFormats.HRAV);
		IMemspace memspace = c.compile(model,SOMFormats.HRAV,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		assertFalse(runner.execute());
	}
}
