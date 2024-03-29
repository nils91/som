/**
 * 
 */
package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import de.dralle.som.WriteHookManager;
import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.test.util.TestWriteHook;

/**
 * @author Nils Dralle
 *
 */
class WriteHookHRACTests {
	private FileLoader f;
	private WriteHookManager testWriteHookManager;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	private TestWriteHook testWriteHook;
	private Compiler c;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		f = new FileLoader();
		c = new Compiler();
		testWriteHook = new TestWriteHook();
		testWriteHookManager = new WriteHookManager();
		testWriteHookManager.registerWriteHook(0, testWriteHook);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testWriteHookTriggerNoTrig() throws IOException {
		HRACModel model = f.readHRACFile("test/fixtures/hrac/test_write_hook_not_triggered.hrac");
		IMemspace m = c.compile(model,SOMFormats.HRAC,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) m);
		runner.setWriteHookManager(testWriteHookManager);
		assertTrue(runner.execute());
		assertEquals(0, testWriteHook.getReadTrgCnt());
		assertEquals(0, testWriteHook.getWriteTrgCnt());
	}

	@Test
	void testWriteHookTriggerWrite() throws IOException {
		HRACModel model = f.readHRACFile("test/fixtures/hrac/test_write_hook_triggered_write.hrac");
		IMemspace m = c.compile(model,SOMFormats.HRAC,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) m);
		runner.setWriteHookManager(testWriteHookManager);
		assertTrue(runner.execute());
		assertEquals(1, testWriteHook.getWriteTrgCnt());
		assertEquals(0, testWriteHook.getReadTrgCnt());
	}

	@Test
	void testWriteHookTriggerRead() throws IOException {
		HRACModel model = f.readHRACFile("test/fixtures/hrac/test_write_hook_triggered_read.hrac");
		IMemspace m = c.compile(model,SOMFormats.HRAC,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) m);
		runner.setWriteHookManager(testWriteHookManager);
		testWriteHook.setBitsProvidedForRead(new boolean[] { false });
		assertTrue(runner.execute());
		assertEquals(1, testWriteHook.getReadTrgCnt());
		assertEquals(0, testWriteHook.getWriteTrgCnt());
	}

	@Test
	void testWriteHookReceiveBit() throws IOException {
		HRACModel model = f.readHRACFile("test/fixtures/hrac/test_write_hook_triggered_write.hrac");
		IMemspace m = c.compile(model,SOMFormats.HRAC,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) m);
		runner.setWriteHookManager(testWriteHookManager);
		runner.execute();
		assertArrayEquals(new boolean[] { true }, testWriteHook.getWrittenBits());
	}

	@Test
	@Timeout(10)
	void testWriteHookReceiveBitSeveralBits() throws IOException {
		HRACModel model = f.readHRACFile("test/fixtures/hrac/test_write_hook_write_101.hrac");
		IMemspace m = c.compile(model,SOMFormats.HRAC,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) m);
		runner.setWriteHookManager(testWriteHookManager);
		runner.execute();
		assertArrayEquals(new boolean[] { true, false, true }, testWriteHook.getWrittenBits());
	}

	@Test
	@Timeout(10)
	void testWriteHookReadNoNewData() throws IOException {
		HRACModel model = f.readHRACFile("test/fixtures/hrac/test_write_hook_read_nonew.hrac");
		IMemspace m = c.compile(model,SOMFormats.HRAC,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) m);
		runner.setWriteHookManager(testWriteHookManager);
		assertTrue(runner.execute());
	}

	@Test
	@Timeout(10)
	void testWriteHookReadNoNewDataFail() throws IOException {
		HRACModel model = f.readHRACFile("test/fixtures/hrac/test_write_hook_read_nonew.hrac");
		IMemspace m = c.compile(model,SOMFormats.HRAC,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) m);
		runner.setWriteHookManager(testWriteHookManager);
		testWriteHook.setBitsProvidedForRead(new boolean[] { false });
		try {
			assertFalse(runner.execute());
		} catch (Exception e) {
		}
	}

	@Test
	void testWriteHookReadNewDataAvailable0() throws IOException {
		HRACModel model = f.readHRACFile("test/fixtures/hrac/test_write_hook_read_newdata.hrac");
		IMemspace m = c.compile(model,SOMFormats.HRAC,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) m);
		runner.setWriteHookManager(testWriteHookManager);
		testWriteHook.setBitsProvidedForRead(new boolean[] { false });
		assertTrue(runner.execute());
	}

	@Test
	void testWriteHookReadNewDataAvailable1() throws IOException {
		HRACModel model = f.readHRACFile("test/fixtures/hrac/test_write_hook_read_newdata.hrac");
		IMemspace m = c.compile(model,SOMFormats.HRAC,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) m);
		runner.setWriteHookManager(testWriteHookManager);
		testWriteHook.setBitsProvidedForRead(new boolean[] { true });
		assertTrue(runner.execute());
	}

	@Test
	void testWriteHookReadNewDataAvailableFail() throws IOException {
		HRACModel model = f.readHRACFile("test/fixtures/hrac/test_write_hook_read_newdata.hrac");
		IMemspace m = c.compile(model,SOMFormats.HRAC,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) m);
		runner.setWriteHookManager(testWriteHookManager);
		try {
			assertFalse(runner.execute());
		} catch (Exception e) {

		}
	}

	@Test
	void testWriteHookReadNewDataAvailable101() throws IOException {
		HRACModel model = f.readHRACFile("test/fixtures/hrac/test_write_hook_read_101.hrac");
		IMemspace m = c.compile(model,SOMFormats.HRAC,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) m);
		runner.setWriteHookManager(testWriteHookManager);
		testWriteHook.setBitsProvidedForRead(new boolean[] { true, false, true });
		assertTrue(runner.execute());
	}

	@Test
	void testWriteHookReadNewDataAvailable101WrongData100() throws IOException {
		HRACModel model = f.readHRACFile("test/fixtures/hrac/test_write_hook_read_101.hrac");
		IMemspace m = c.compile(model,SOMFormats.HRAC,SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) m);
		runner.setWriteHookManager(testWriteHookManager);
		testWriteHook.setBitsProvidedForRead(new boolean[] { true, false, false });
		assertFalse(runner.execute());
	}
}
