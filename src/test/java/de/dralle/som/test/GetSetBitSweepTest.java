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

import de.dralle.som.SOMBitcodeRunner;

/**
 * @author Nils Dralle
 *
 */
class GetSetBitSweepTest {

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
	void test255BitMemspaceSweepInit1Set0() {
		for (int i = 0; i < 255; i++) {
			byte[] memSpace=new byte[256/8];
			for (int j = 0; j < memSpace.length; j++) {
				memSpace[j]=(byte) 0xFF;
			}
			memSpace=SOMBitcodeRunner.setBit(i, false, memSpace);
			assertFalse(SOMBitcodeRunner.getBit(i, memSpace));
		}
	}

	@Test
	void test255BitMemspaceSweepInit0Set1() {
		for (int i = 0; i < 255; i++) {
			byte[] memSpace=new byte[256/8];
			for (int j = 0; j < memSpace.length; j++) {
				memSpace[j]=(byte) 0x00;
			}
			memSpace=SOMBitcodeRunner.setBit(i, true, memSpace);
			assertTrue(SOMBitcodeRunner.getBit(i, memSpace));
		}
	}
	@Test
	void test65535BitMemspaceSweepInit1Set0() {
		for (int i = 0; i < 65535; i++) {
			byte[] memSpace=new byte[65536/8];
			for (int j = 0; j < memSpace.length; j++) {
				memSpace[j]=(byte) 0xFF;
			}
			memSpace=SOMBitcodeRunner.setBit(i, false, memSpace);
			assertFalse(SOMBitcodeRunner.getBit(i, memSpace));
		}
	}

	@Test
	void test65535BitMemspaceSweepInit0Set1() {
		for (int i = 0; i < 65535; i++) {
			byte[] memSpace=new byte[65536/8];
			for (int j = 0; j < memSpace.length; j++) {
				memSpace[j]=(byte) 0x00;
			}
			memSpace=SOMBitcodeRunner.setBit(i, true, memSpace);
			assertTrue(SOMBitcodeRunner.getBit(i, memSpace));
		}
	}

}
