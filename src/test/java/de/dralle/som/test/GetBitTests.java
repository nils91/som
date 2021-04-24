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
class GetBitTests {

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
	void testGetBitByte0Bit0All1Bit0() {
		byte[] memSpace = new byte[2];
		for (int i = 0; i < memSpace.length; i++) {
			memSpace[i] = (byte) 0xFF;
		}
		memSpace[0] = (byte) 0x7F;
		boolean expected = false;
		boolean actual = SOMBitcodeRunner.getBit(0, memSpace);
		assertEquals(expected, actual);
	}

	@Test
	void testGetBitByte0Bit4All1Bit0() {
		byte[] memSpace = new byte[2];
		for (int i = 0; i < memSpace.length; i++) {
			memSpace[i] = (byte) 0xFF;
		}
		memSpace[0] = (byte) 0xF7;
		boolean expected = false;
		boolean actual = SOMBitcodeRunner.getBit(4, memSpace);
		assertEquals(expected, actual);
	}

	@Test
	void testGetBitByte0Bit7All1Bit0() {
		byte[] memSpace = new byte[2];
		for (int i = 0; i < memSpace.length; i++) {
			memSpace[i] = (byte) 0xFF;
		}
		memSpace[0] = (byte) 0xFE;
		boolean expected = false;
		boolean actual = SOMBitcodeRunner.getBit(7, memSpace);
		assertEquals(expected, actual);
	}

	@Test
	void testGetBitByte1Bit0All1Bit0() {
		byte[] memSpace = new byte[2];
		for (int i = 0; i < memSpace.length; i++) {
			memSpace[i] = (byte) 0xFF;
		}
		memSpace[1] = (byte) 0x7F;
		boolean expected = false;
		boolean actual = SOMBitcodeRunner.getBit(1 * 8 + 0, memSpace);
		assertEquals(expected, actual);
	}

	@Test
	void testGetBitByte1Bit4All1Bit0() {
		byte[] memSpace = new byte[2];
		for (int i = 0; i < memSpace.length; i++) {
			memSpace[i] = (byte) 0xFF;
		}
		memSpace[1] = (byte) 0xF7;
		boolean expected = false;
		boolean actual = SOMBitcodeRunner.getBit(1 * 8 + 4, memSpace);
		assertEquals(expected, actual);
	}

	@Test
	void testGetBitByte1Bit7All1Bit0() {
		byte[] memSpace = new byte[2];
		for (int i = 0; i < memSpace.length; i++) {
			memSpace[i] = (byte) 0xFF;
		}
		memSpace[1] = (byte) 0xFE;
		boolean expected = false;
		boolean actual = SOMBitcodeRunner.getBit(1 * 8 + 7, memSpace);
		assertEquals(expected, actual);
	}

	@Test
	void testGetBitByte0Bit0All0Bit1() {
		byte[] memSpace = new byte[2];
		for (int i = 0; i < memSpace.length; i++) {
			memSpace[i] = (byte) 0x00;
		}
		memSpace[0] = (byte) 0x80;
		boolean expected = true;
		boolean actual = SOMBitcodeRunner.getBit(0, memSpace);
		assertEquals(expected, actual);
	}

	@Test
	void testGetBitByte0Bit3All0Bit1() {
		byte[] memSpace = new byte[2];
		for (int i = 0; i < memSpace.length; i++) {
			memSpace[i] = (byte) 0x00;
		}
		memSpace[0] = (byte) 0x10;
		boolean expected = true;
		boolean actual = SOMBitcodeRunner.getBit(3, memSpace);
		assertEquals(expected, actual);
	}

	@Test
	void testGetBitByte0Bit4All0Bit1() {
		byte[] memSpace = new byte[2];
		for (int i = 0; i < memSpace.length; i++) {
			memSpace[i] = (byte) 0x00;
		}
		memSpace[0] = (byte) 0x08;
		boolean expected = true;
		boolean actual = SOMBitcodeRunner.getBit(4, memSpace);
		assertEquals(expected, actual);
	}

	@Test
	void testGetBitByte0Bit7All0Bit1() {
		byte[] memSpace = new byte[2];
		for (int i = 0; i < memSpace.length; i++) {
			memSpace[i] = (byte) 0x00;
		}
		memSpace[0] = (byte) 0x01;
		boolean expected = true;
		boolean actual = SOMBitcodeRunner.getBit(7, memSpace);
		assertEquals(expected, actual);
	}

	@Test
	void testGetBitByte1Bit0All0Bit1() {
		byte[] memSpace = new byte[2];
		for (int i = 0; i < memSpace.length; i++) {
			memSpace[i] = (byte) 0x00;
		}
		memSpace[1] = (byte) 0x80;
		boolean expected = true;
		boolean actual = SOMBitcodeRunner.getBit(1 * 8 + 0, memSpace);
		assertEquals(expected, actual);
	}

	@Test
	void testGetBitByte1Bit3All0Bit1() {
		byte[] memSpace = new byte[2];
		for (int i = 0; i < memSpace.length; i++) {
			memSpace[i] = (byte) 0x00;
		}
		memSpace[1] = (byte) 0x10;
		boolean expected = true;
		boolean actual = SOMBitcodeRunner.getBit(1 * 8 + 3, memSpace);
		assertEquals(expected, actual);
	}

	@Test
	void testGetBitByte1Bit4All0Bit1() {
		byte[] memSpace = new byte[2];
		for (int i = 0; i < memSpace.length; i++) {
			memSpace[i] = (byte) 0x00;
		}
		memSpace[1] = (byte) 0x08;
		boolean expected = true;
		boolean actual = SOMBitcodeRunner.getBit(1 * 8 + 4, memSpace);
		assertEquals(expected, actual);
	}

	@Test
	void testGetBitByte1Bit7All0Bit1() {
		byte[] memSpace = new byte[2];
		for (int i = 0; i < memSpace.length; i++) {
			memSpace[i] = (byte) 0x00;
		}
		memSpace[1] = (byte) 0x01;
		boolean expected = true;
		boolean actual = SOMBitcodeRunner.getBit(1 * 8 + 7, memSpace);
		assertEquals(expected, actual);
	}

	@Test
	void testGet8bitUnsignedIntByte0Val255() {
		byte[] memSpace = new byte[2];
		memSpace[0] = (byte) 0xFF;
		int actual = SOMBitcodeRunner.getBitsUnsigned(0, 8, memSpace);
		int expected = 255;
		assertEquals(expected, actual);
	}

	@Test
	void testGet8bitUnsignedIntByte0Val127() {
		byte[] memSpace = new byte[2];
		memSpace[0] = (byte) 0x7F;
		int actual = SOMBitcodeRunner.getBitsUnsigned(0, 8, memSpace);
		int expected = 127;
		assertEquals(expected, actual);
	}

	@Test
	void testGet8bitUnsignedIntByte0Val128() {
		byte[] memSpace = new byte[2];
		memSpace[0] = (byte) 0x80;
		int actual = SOMBitcodeRunner.getBitsUnsigned(0, 8, memSpace);
		int expected = 128;
		assertEquals(expected, actual);
	}

	@Test
	void testGet8bitUnsignedIntByte0Val170() { // Value selected because 10101010
		byte[] memSpace = new byte[2];
		memSpace[0] = (byte) 0xAA;
		int actual = SOMBitcodeRunner.getBitsUnsigned(0, 8, memSpace);
		int expected = 170;
		assertEquals(expected, actual);
	}

	@Test
	void testGet8bitUnsignedIntByte0Val85() {// Value selected because 01010101
		byte[] memSpace = new byte[2];
		memSpace[0] = (byte) 0x55;
		int actual = SOMBitcodeRunner.getBitsUnsigned(0, 8, memSpace);
		int expected = 85;
		assertEquals(expected, actual);
	}

	@Test
	void testGet8bitUnsignedIntByte1Val1() {
		byte[] memSpace = new byte[2];
		memSpace[1] = (byte) 0x01;
		int actual = SOMBitcodeRunner.getBitsUnsigned(8, 8, memSpace);
		int expected = 1;
		assertEquals(expected, actual);
	}

	@Test
	void testGet8bitUnsignedIntByte1Val255() {
		byte[] memSpace = new byte[2];
		memSpace[1] = (byte) 0xFF;
		int actual = SOMBitcodeRunner.getBitsUnsigned(8, 8, memSpace);
		int expected = 255;
		assertEquals(expected, actual);
	}

	@Test
	void testGet8bitUnsignedIntByte1Val127() {
		byte[] memSpace = new byte[2];
		memSpace[1] = (byte) 0x7F;
		int actual = SOMBitcodeRunner.getBitsUnsigned(8, 8, memSpace);
		int expected = 127;
		assertEquals(expected, actual);
	}

	@Test
	void testGet8bitUnsignedIntByte1Val128() {
		byte[] memSpace = new byte[2];
		memSpace[1] = (byte) 0x80;
		int actual = SOMBitcodeRunner.getBitsUnsigned(8, 8, memSpace);
		int expected = 128;
		assertEquals(expected, actual);
	}

	@Test
	void testGet8bitUnsignedIntByte1Val170() { // Value selected because 10101010
		byte[] memSpace = new byte[2];
		memSpace[1] = (byte) 0xAA;
		int actual = SOMBitcodeRunner.getBitsUnsigned(8, 8, memSpace);
		int expected = 170;
		assertEquals(expected, actual);
	}

	@Test
	void testGet8bitUnsignedIntByte1Val85() {// Value selected because 01010101
		byte[] memSpace = new byte[2];
		memSpace[1] = (byte) 0x55;
		int actual = SOMBitcodeRunner.getBitsUnsigned(8, 8, memSpace);
		int expected = 85;
		assertEquals(expected, actual);
	}

	@Test
	void testGet16bitUnsignedIntAccross2BytesVal1() {
		byte[] memSpace = new byte[2];
		memSpace[1] = (byte) 0x01;
		int actual = SOMBitcodeRunner.getBitsUnsigned(0, 16, memSpace);
		int expected = 1;
		assertEquals(expected, actual);
	}

	@Test
	void testGet16bitUnsignedIntAccross2BytesVal255() {
		byte[] memSpace = new byte[2];
		memSpace[1] = (byte) 0xFF;
		int actual = SOMBitcodeRunner.getBitsUnsigned(0, 16, memSpace);
		int expected = 255;
		assertEquals(expected, actual);
	}

	@Test
	void testGet16bitUnsignedIntAccross2BytesVal127() {
		byte[] memSpace = new byte[2];
		memSpace[1] = (byte) 0x7F;
		int actual = SOMBitcodeRunner.getBitsUnsigned(0, 16, memSpace);
		int expected = 127;
		assertEquals(expected, actual);
	}

	@Test
	void testGet16bitUnsignedIntAccross2BytesVal128() {
		byte[] memSpace = new byte[2];
		memSpace[1] = (byte) 0x80;
		int actual = SOMBitcodeRunner.getBitsUnsigned(0, 16, memSpace);
		int expected = 128;
		assertEquals(expected, actual);
	}

	@Test
	void testGet16bitUnsignedIntAccross2BytesVal170() { // Value selected because 10101010
		byte[] memSpace = new byte[2];
		memSpace[1] = (byte) 0xAA;
		int actual = SOMBitcodeRunner.getBitsUnsigned(0, 16, memSpace);
		int expected = 170;
		assertEquals(expected, actual);
	}

	@Test
	void testGet16bitUnsignedIntAccross2BytesVal85() {// Value selected because 01010101
		byte[] memSpace = new byte[2];
		memSpace[1] = (byte) 0x55;
		int actual = SOMBitcodeRunner.getBitsUnsigned(0, 16, memSpace);
		int expected = 85;
		assertEquals(expected, actual);
	}

	@Test
	void testGet8bitUnsignedIntAccross2BytesVal16() {
		byte[] memSpace = new byte[2];
		memSpace[0] = (byte) 0x01;
		int actual = SOMBitcodeRunner.getBitsUnsigned(4, 8, memSpace);
		int expected = 16;
		assertEquals(expected, actual);
	}

	@Test
	void testGet7bitUnsignedIntByte0Val127() {
		byte[] memSpace = new byte[2];
		memSpace[0] = (byte) 0x7F;
		int actual = SOMBitcodeRunner.getBitsUnsigned(1, 7, memSpace);
		int expected = 127;
		assertEquals(expected, actual);
	}

	@Test
	void testGet7bitUnsignedIntByte0Val85() {// Value selected because 1010101
		byte[] memSpace = new byte[2];
		memSpace[0] = (byte) 0x55;
		int actual = SOMBitcodeRunner.getBitsUnsigned(1, 7, memSpace);
		int expected = 85;
		assertEquals(expected, actual);
	}

	@Test
	void testGet7bitUnsignedIntByte0Val63() {
		byte[] memSpace = new byte[2];
		memSpace[0] = (byte) 0x3F;
		int actual = SOMBitcodeRunner.getBitsUnsigned(1, 7, memSpace);
		int expected = 63;
		assertEquals(expected, actual);
	}

	@Test
	void testGet7bitUnsignedIntByte0Val64() {
		byte[] memSpace = new byte[2];
		memSpace[0] = (byte) 0x40;
		int actual = SOMBitcodeRunner.getBitsUnsigned(1, 7, memSpace);
		int expected = 64;
		assertEquals(expected, actual);
	}
}
