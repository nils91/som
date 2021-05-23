/**
 * 
 */
package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.dralle.som.BooleanArrayMemspace;

/**
 * @author Nils Dralle
 *
 */
class BooleanArrayMemspaceTests {

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
	void testSweepSet0() {
		BooleanArrayMemspace memSpace = new BooleanArrayMemspace(65535);
		for (int i = 0; i < 65535; i++) {
			memSpace.setBit(i, false);
		}
		for (int i = 0; i < 65535; i++) {
			assertFalse(memSpace.getBit(i));
		}
	}

	@Test
	void testSweepSet1() {
		BooleanArrayMemspace memSpace = new BooleanArrayMemspace(65535);
		for (int i = 0; i < 65535; i++) {
			memSpace.setBit(i, true);
		}
		for (int i = 0; i < 65535; i++) {
			assertTrue(memSpace.getBit(i));
		}
	}

	@Test
	void testSweepSetAlternating() {
		BooleanArrayMemspace memSpace = new BooleanArrayMemspace(65535);
		for (int i = 0; i < 65535; i++) {
			memSpace.setBit(i, i % 2 == 0);
		}
		for (int i = 0; i < 65535; i++) {
			assertEquals(i % 2 == 0, memSpace.getBit(i));
		}
	}

	@Test
	void testSweepSetAlternatingSporadic() {
		int alternatingDistance = 97;
		BooleanArrayMemspace memSpace = new BooleanArrayMemspace(65535);
		for (int i = 0; i < 65535; i++) {
			memSpace.setBit(i, i % alternatingDistance == 0);
		}
		for (int i = 0; i < 65535; i++) {
			assertEquals(i % alternatingDistance == 0, memSpace.getBit(i));
		}
	}

	@Test
	void testGetAccAddress() {
		BooleanArrayMemspace memSpace = new BooleanArrayMemspace(65535);
		assertEquals(0, memSpace.getAccumulatorAddress());
	}

	@Test
	void testAdrEvalAddress() {
		BooleanArrayMemspace memSpace = new BooleanArrayMemspace(65535);
		assertEquals(8, memSpace.getAdrEvalAddress());
	}

	@Test
	void testWhEnAddress() {
		int n = 7;
		BooleanArrayMemspace memSpace = new BooleanArrayMemspace((int) Math.pow(2, n));
		memSpace.setN(n);
		assertEquals(9 + n, memSpace.getWriteHookEnabledAddress());
	}

	@Test
	void testWhDirAddress() {
		int n = 7;
		BooleanArrayMemspace memSpace = new BooleanArrayMemspace((int) Math.pow(2, n));
		memSpace.setN(n);
		assertEquals(10 + n, memSpace.getWriteHookDirectionAddress());
	}

	@Test
	void testWhComAddress() {
		int n = 7;
		BooleanArrayMemspace memSpace = new BooleanArrayMemspace((int) Math.pow(2, n));
		memSpace.setN(n);
		assertEquals(11 + n, memSpace.getWriteHookCommunicationAddress());
	}

	@Test
	void testWhSelAddress() {
		int n = 7;
		BooleanArrayMemspace memSpace = new BooleanArrayMemspace((int) Math.pow(2, n));
		memSpace.setN(n);
		memSpace.setN(n);
		assertEquals(12 + n, memSpace.getWriteHookSelectAddress());
	}
}
