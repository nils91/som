/**
 * 
 */
package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.dralle.som.SOMBitcodeRunner;
import de.dralle.som.test.util.TestUtil;
import de.dralle.som.test.util.TestWriteHook;

/**
 * @author Nils Dralle
 *
 */
class WriteHookTests {

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

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		testWriteHook = new TestWriteHook();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testWriteHookTriggerNoTrig() throws IOException {
		String fContent = TestUtil.readFileToString("test/fixtures/ab/test_write_hook_not_triggered.ab");
		SOMBitcodeRunner runner = new SOMBitcodeRunner(fContent);
		runner.addWriteHook(testWriteHook);
		assertTrue(runner.execute());
		assertEquals(0, testWriteHook.getReadTrgCnt());
		assertEquals(0, testWriteHook.getWriteTrgCnt());
	}

	@Test
	void testWriteHookTriggerWrite() throws IOException {
		String fContent = TestUtil.readFileToString("test/fixtures/ab/test_write_hook_triggered_write.ab");
		SOMBitcodeRunner runner = new SOMBitcodeRunner(fContent);
		runner.addWriteHook(testWriteHook);
		assertTrue(runner.execute());
		assertEquals(1, testWriteHook.getWriteTrgCnt());
		assertEquals(0, testWriteHook.getReadTrgCnt());
	}

	@Test
	void testWriteHookTriggerRead() throws IOException {
		String fContent = TestUtil.readFileToString("test/fixtures/ab/test_write_hook_triggered_read.ab");
		SOMBitcodeRunner runner = new SOMBitcodeRunner(fContent);
		runner.addWriteHook(testWriteHook);
		assertTrue(runner.execute());
		assertEquals(1, testWriteHook.getReadTrgCnt());
		assertEquals(0, testWriteHook.getWriteTrgCnt());
	}

	@Test
	void testWriteHookReceiveBit() throws IOException {
		String fContent = TestUtil.readFileToString("test/fixtures/ab/test_write_hook_triggered_write.ab");
		SOMBitcodeRunner runner = new SOMBitcodeRunner(fContent);
		runner.addWriteHook(testWriteHook);
		runner.execute();
		assertArrayEquals(new boolean[] { true }, testWriteHook.getWrittenBits());
	}

	@Test
	void testWriteHookReceiveBitSeveralBits() throws IOException {
		String fContent = TestUtil.readFileToString("test/fixtures/ab/test_write_hook_write_101.ab");
		SOMBitcodeRunner runner = new SOMBitcodeRunner(fContent);
		runner.addWriteHook(testWriteHook);
		runner.execute();
		assertArrayEquals(new boolean[] { true, false, true }, testWriteHook.getWrittenBits());
	}

	@Test
	void testWriteHookReadNoNewData() throws IOException {
		String fContent = TestUtil.readFileToString("test/fixtures/ab/test_write_hook_read_nonew.ab");
		SOMBitcodeRunner runner = new SOMBitcodeRunner(fContent);
		runner.addWriteHook(testWriteHook);
		assertTrue(runner.execute());
	}

	@Test
	void testWriteHookReadNoNewDataFail() throws IOException {
		String fContent = TestUtil.readFileToString("test/fixtures/ab/test_write_hook_read_nonew.ab");
		SOMBitcodeRunner runner = new SOMBitcodeRunner(fContent);
		runner.addWriteHook(testWriteHook);
		testWriteHook.setBitsProvidedForRead(new boolean[] { false });
		try {
			assertFalse(runner.execute());
		} catch (Exception e) {			
		}
	}

	@Test
	void testWriteHookReadNewDataAvailable0() throws IOException {
		String fContent = TestUtil.readFileToString("test/fixtures/ab/test_write_hook_read_newdata.ab");
		SOMBitcodeRunner runner = new SOMBitcodeRunner(fContent);
		runner.addWriteHook(testWriteHook);
		testWriteHook.setBitsProvidedForRead(new boolean[] { false });
		assertTrue(runner.execute());
	}

	@Test
	void testWriteHookReadNewDataAvailable1() throws IOException {
		String fContent = TestUtil.readFileToString("test/fixtures/ab/test_write_hook_read_newdata.ab");
		SOMBitcodeRunner runner = new SOMBitcodeRunner(fContent);
		runner.addWriteHook(testWriteHook);
		testWriteHook.setBitsProvidedForRead(new boolean[] { true });
		assertTrue(runner.execute());
	}
}
