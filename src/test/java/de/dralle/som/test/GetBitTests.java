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

}
