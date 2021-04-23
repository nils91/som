/**
 * 
 */
package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;

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
class SetBitTests {

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
	void testSetBitByte0Bit0Init1Set0() {
		byte[] memSpace = new byte[2];
		for (int i = 0; i < memSpace.length; i++) {
			memSpace[i] = (byte) 0xFF;
		}
		byte expected = (byte) 0x7F;
		memSpace = SOMBitcodeRunner.setBit(0, false, memSpace);
		assertEquals(expected, memSpace[0]);
	}

	@Test
	void testSetBitByte0Bit4Init1Set0() {
		byte[] memSpace = new byte[2];
		for (int i = 0; i < memSpace.length; i++) {
			memSpace[i] = (byte) 0xFF;
		}
		byte expected = (byte) 0xF7;
		memSpace = SOMBitcodeRunner.setBit(4, false, memSpace);
		assertEquals(expected, memSpace[0]);
	}

	@Test
	void testSetBitByte0Bit7Init1Set0() {
		byte[] memSpace = new byte[2];
		for (int i = 0; i < memSpace.length; i++) {
			memSpace[i] = (byte) 0xFF;
		}
		byte expected = (byte) 0xFE;
		memSpace = SOMBitcodeRunner.setBit(7, false, memSpace);
		assertEquals(expected, memSpace[0]);
	}

	@Test
	void testSetBitByte1Bit0Init1Set0() {
		byte[] memSpace = new byte[2];
		for (int i = 0; i < memSpace.length; i++) {
			memSpace[i] = (byte) 0xFF;
		}
		byte expected = (byte) 0x7F;
		memSpace = SOMBitcodeRunner.setBit(1 * 8 + 0, false, memSpace);
		assertEquals(expected, memSpace[1]);
	}

	@Test
	void testSetBitByte1Bit4Init1Set0() {
		byte[] memSpace = new byte[2];
		for (int i = 0; i < memSpace.length; i++) {
			memSpace[i] = (byte) 0xFF;
		}
		byte expected = (byte) 0xF7;
		memSpace = SOMBitcodeRunner.setBit(1 * 8 + 4, false, memSpace);
		assertEquals(expected, memSpace[1]);
	}

	@Test
	void testSetBitByte1Bit7Init1Set0() {
		byte[] memSpace = new byte[2];
		for (int i = 0; i < memSpace.length; i++) {
			memSpace[i] = (byte) 0xFF;
		}
		byte expected = (byte) 0xFE;
		memSpace = SOMBitcodeRunner.setBit(1 * 8 + 7, false, memSpace);
		assertEquals(expected, memSpace[1]);
	}

	@Test
	void testSetBitByte0Bit0Init0Set1() {
		byte[] memSpace = new byte[2];
		for (int i = 0; i < memSpace.length; i++) {
			memSpace[i] = (byte) 0x00;
		}
		byte expected = (byte) 0x80;
		memSpace = SOMBitcodeRunner.setBit(0, true, memSpace);
		assertEquals(expected, memSpace[0]);
	}

	@Test
	void testSetBitByte0Bit3Init0Set1() {
		byte[] memSpace = new byte[2];
		for (int i = 0; i < memSpace.length; i++) {
			memSpace[i] = (byte) 0x00;
		}
		byte expected = (byte) 0x10;
		memSpace = SOMBitcodeRunner.setBit(3, true, memSpace);
		assertEquals(expected, memSpace[0]);
	}

	@Test
	void testSetBitByte0Bit4Init0Set1() {
		byte[] memSpace = new byte[2];
		for (int i = 0; i < memSpace.length; i++) {
			memSpace[i] = (byte) 0x00;
		}
		byte expected = (byte) 0x08;
		memSpace = SOMBitcodeRunner.setBit(4, true, memSpace);
		assertEquals(expected, memSpace[0]);
	}

	@Test
	void testSetBitByte0Bit7Init0Set1() {
		byte[] memSpace = new byte[2];
		for (int i = 0; i < memSpace.length; i++) {
			memSpace[i] = (byte) 0x00;
		}
		byte expected = (byte) 0x01;
		memSpace = SOMBitcodeRunner.setBit(7, true, memSpace);
		assertEquals(expected, memSpace[0]);
	}

	@Test
	void testSetBitByte1Bit0Init0Set1() {
		byte[] memSpace = new byte[2];
		for (int i = 0; i < memSpace.length; i++) {
			memSpace[i] = (byte) 0x00;
		}
		byte expected = (byte) 0x80;
		memSpace = SOMBitcodeRunner.setBit(1 * 8 + 0, true, memSpace);
		assertEquals(expected, memSpace[1]);
	}

	@Test
	void testSetBitByte1Bit3Init0Set1() {
		byte[] memSpace = new byte[2];
		for (int i = 0; i < memSpace.length; i++) {
			memSpace[i] = (byte) 0x00;
		}
		byte expected = (byte) 0x10;
		memSpace = SOMBitcodeRunner.setBit(1 * 8 + 3, true, memSpace);
		assertEquals(expected, memSpace[1]);
	}

	@Test
	void testSetBitByte1Bit4Init0Set1() {
		byte[] memSpace = new byte[2];
		for (int i = 0; i < memSpace.length; i++) {
			memSpace[i] = (byte) 0x00;
		}
		byte expected = (byte) 0x08;
		memSpace = SOMBitcodeRunner.setBit(1 * 8 + 4, true, memSpace);
		assertEquals(expected, memSpace[1]);
	}

	@Test
	void testSetBitByte1Bit7Init0Set1() {
		byte[] memSpace = new byte[2];
		for (int i = 0; i < memSpace.length; i++) {
			memSpace[i] = (byte) 0x00;
		}
		byte expected = (byte) 0x01;
		memSpace = SOMBitcodeRunner.setBit(1 * 8 + 7, true, memSpace);
		assertEquals(expected, memSpace[1]);
	}
	@Test
	void testSet8bitUnsignedIntByte0Val1() {
		byte[] memSpace = new byte[2];
		memSpace=SOMBitcodeRunner.setBitsUnsigned(0, 8, 1, memSpace);
		byte expected = (byte) 0x01;
		assertEquals(expected, memSpace[0]);
	}@Test
	void testSet8bitUnsignedIntByte0Val255() {
		byte[] memSpace = new byte[2];
		memSpace=SOMBitcodeRunner.setBitsUnsigned(0, 8, 255, memSpace);
		byte expected = (byte) 0xFF;
		assertEquals(expected, memSpace[0]);
	}@Test
	void testSet8bitUnsignedIntByte0Val127() {
		byte[] memSpace = new byte[2];
		memSpace=SOMBitcodeRunner.setBitsUnsigned(0, 8, 127, memSpace);
		byte expected = (byte) 0x0F;
		assertEquals(expected, memSpace[0]);
	}
	@Test
	void testSet8bitUnsignedIntByte0Val128() {
		byte[] memSpace = new byte[2];
		memSpace=SOMBitcodeRunner.setBitsUnsigned(0, 8, 128, memSpace);
		byte expected = (byte) 0x10;
		assertEquals(expected, memSpace[0]);
	}
}
