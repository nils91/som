package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.dralle.som.SOMBitcodeRunner;

class BitcodeRunnerInitTest {

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
	void testInitMemspace() {
		SOMBitcodeRunner runner = new SOMBitcodeRunner(5, 11);
		assertNotNull(runner);
	}
	@Test
	void testInitMemspaceSize() {
		SOMBitcodeRunner runner = new SOMBitcodeRunner(5, 11);
		int expectedMemspaceSize=(int) Math.pow(2, 5);
		int actualSize = runner.getMemSpace().length*8;
		assertEquals(expectedMemspaceSize, actualSize);
	}
	@Test
	void testInitMemspaceContent() {
		SOMBitcodeRunner runner = new SOMBitcodeRunner(5, 11);
		byte[] expected=new byte[] {0x05,0x60,0x00,0x00};
		assertArrayEquals(expected, runner.getMemSpace());
	}
}
