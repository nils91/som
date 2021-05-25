package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.dralle.som.SOMBitcodeRunner;

class BitcodeRunnerInitTests {

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
		int expectedMemspaceSize = (int) Math.pow(2, 5);
		int actualSize = runner.getMemspace().getSize();
		assertTrue(expectedMemspaceSize<= actualSize);
	}

//	@Test
//	void testInitMemspaceContent() {
//		SOMBitcodeRunner runner = new SOMBitcodeRunner(5, 11);
//		byte[] expected = new byte[] { 0x05, 0x2C, 0x00, 0x00 };
//		assertArrayEquals(expected, runner.getMemSpace());
//	}
//
//	@Test
//	void testInitWithMemspaceContentFullMemspaceProvided() {
//		byte[] expected = new byte[] { 0x05, 0x60, 0x00, 0x00 };
//		SOMBitcodeRunner runner = new SOMBitcodeRunner(expected);
//		assertArrayEquals(expected, runner.getMemSpace());
//	}
//
//	@Test
//	void testInitWithMemspaceContentAsBitArrayFullMemspaceProvided() {
//		byte[] expected = new byte[] { 0x05, 0x60, 0x00, 0x00 };
//		SOMBitcodeRunner runner = new SOMBitcodeRunner(new boolean[] { false, false, false, false, false, true, false,
//				true, false, true, true, false, false, false, false, false, false, false, false, false, false, false,
//				false, false, false, false, false, false, false, false, false, false });
//		assertArrayEquals(expected, runner.getMemSpace());
//	}
//
//	@Test
//	void testInitWithMemspaceContentAsBitStringFullMemspaceProvided() {
//		byte[] expected = new byte[] { 0x05, 0x60, 0x00, 0x00 };
//		SOMBitcodeRunner runner = new SOMBitcodeRunner("00000101011000000000000000000000");
//		assertArrayEquals(expected, runner.getMemSpace());
//	}
//
//	@Test
//	void testInitWithMemspaceContentAsBitStringWithSpacesFullMemspaceProvided() {
//		byte[] expected = new byte[] { 0x05, 0x60, 0x00, 0x00 };
//		SOMBitcodeRunner runner = new SOMBitcodeRunner("0 00001 01011 00 00000 00 00000 00 00000");
//		assertArrayEquals(expected, runner.getMemSpace());
//	}
//
//	@Test
//	void testInitWithMemspaceContentNoRight0Bits() {
//		byte[] expected = new byte[] { 0x05, 0x60, 0x00, 0x00 };
//		SOMBitcodeRunner runner = new SOMBitcodeRunner(new byte[] { expected[0], expected[1] });
//		assertArrayEquals(expected, runner.getMemSpace());
//	}
//
//	@Test
//	void testInitWithMemspaceContentAsBitArrayNoRight0Bits() {
//		byte[] expected = new byte[] { 0x05, 0x60, 0x00, 0x00 };
//		SOMBitcodeRunner runner = new SOMBitcodeRunner(
//				new boolean[] { false, false, false, false, false, true, false, true, false, true, true });
//		assertArrayEquals(expected, runner.getMemSpace());
//	}
//
//	@Test
//	void testInitWithMemspaceContentAsBitStringNoRight0Bits() {
//		byte[] expected = new byte[] { 0x05, 0x60, 0x00, 0x00 };
//		SOMBitcodeRunner runner = new SOMBitcodeRunner("00000101011");
//		assertArrayEquals(expected, runner.getMemSpace());
//	}
//
//	@Test
//	void testInitWithMemspaceContentAsBitStringWithSpacesNoRight0Bits() {
//		byte[] expected = new byte[] { 0x05, 0x60, 0x00, 0x00 };
//		SOMBitcodeRunner runner = new SOMBitcodeRunner("0 00001 01011");
//		assertArrayEquals(expected, runner.getMemSpace());
//	}

	@Test
	void testInitRunnerGetN() {
		SOMBitcodeRunner runner = new SOMBitcodeRunner(5, 11);
		assertEquals(5, runner.getMemspace().getN());
	}

	@Test
	void testInitRunnerGetStartAddress() {
		SOMBitcodeRunner runner = new SOMBitcodeRunner(5, 11);
		assertEquals(11, runner.getMemspace().getNextAddress());
	}

//	@Test
//	void testInitWithMemspaceContentFullMemspaceProvidedGetN() {
//		byte[] expected = new byte[] { 0x05, 0x60, 0x00, 0x00 };
//		SOMBitcodeRunner runner = new SOMBitcodeRunner(expected);
//		assertEquals(5, runner.getN());
//	}
//
//	@Test
//	void testInitWithMemspaceContentFullMemspaceProvidedGetStartAddress() {
//		byte[] expected = new byte[] { 0x05, 0x2C, 0x00, 0x00 };
//		SOMBitcodeRunner runner = new SOMBitcodeRunner(expected);
//		assertEquals(11, runner.getAddressBits());
//	}

	@Test
	void testInitRunnerContentAsBitArrayFullMemspaceProvidedGetN() {
		SOMBitcodeRunner runner = new SOMBitcodeRunner(new boolean[] { false, false, false, false, false, true, false,
				true, false, true, true, false, false, false, false, false, false, false, false, false, false, false,
				false, false, false, false, false, false, false, false, false, false });
		assertEquals(5, runner.getMemspace().getN());
	}

	@Test
	void testInitRunnerContentAsBitArrayFullMemspaceProvidedGetStartAddress() {
		SOMBitcodeRunner runner = new SOMBitcodeRunner(new boolean[] { false, false, false, false, false, true, false,		true, 
				true, false, true, false, true, true, false, false, false, false, false, false, false, false,
				false, false, false, false, false, false, false, false, false, false });
		assertEquals(11, runner.getMemspace().getNextAddress());
	}

	@Test
	void testInitRunnerContentAsBitStringFullMemspaceProvidedGetN() {
		SOMBitcodeRunner runner = new SOMBitcodeRunner("00000101011000000000000000000000");
		assertEquals(5, runner.getMemspace().getN());
	}

	@Test
	void testInitRunnerContentAsBitStringFullMemspaceProvidedGetStartAddress() {
		SOMBitcodeRunner runner = new SOMBitcodeRunner("00000101101011000000000000000000");
		assertEquals(11, runner.getMemspace().getNextAddress());
	}

	@Test
	void testInitRunnerContentAsBitStringWithSpacesFullMemspaceProvidedGetN() {
		SOMBitcodeRunner runner = new SOMBitcodeRunner("0 00001 01011 00 00000 00 00000 00 00000");
		assertEquals(5, runner.getMemspace().getN());
	}

	@Test
	void testInitRunnerContentAsBitStringWithSpacesFullMemspaceProvidedGetStartAddress() {
		SOMBitcodeRunner runner = new SOMBitcodeRunner("0 0000101 1 01011 0000 00 00000 00 00000");
		assertEquals(11, runner.getMemspace().getNextAddress());
	}

//	@Test
//	void testInitRunnerContentNoRight0BitsGetN() {
//		byte[] expected = new byte[] { 0x05, 0x60, 0x00, 0x00 };
//		SOMBitcodeRunner runner = new SOMBitcodeRunner(new byte[] { expected[0], expected[1] });
//		assertEquals(5, runner.getMemspace().getN());
//	}
//
//	@Test
//	void testInitRunnerContentNoRight0BitsGetStartAddress() {
//		// 0 000 0101 0 010 1100
//		byte[] expected = new byte[] { 0x05, 0x2C, 0x00, 0x00 };
//		SOMBitcodeRunner runner = new SOMBitcodeRunner(new byte[] { expected[0], expected[1] });
//		assertEquals(11, runner.getMemspace().getNextAddress());
//	}

	@Test
	void testInitRunnerContentAsBitArrayNoRight0BitsGetN() {
		SOMBitcodeRunner runner = new SOMBitcodeRunner(
				new boolean[] { false, false, false, false, false, true, false, true,true, false, false, false,false,false });
		assertEquals(5, runner.getMemspace().getN());
	}

	@Test
	void testInitRunnerContentAsBitArrayNoRight0BitsGetStartAddress() {
		SOMBitcodeRunner runner = new SOMBitcodeRunner(
				new boolean[] { false, false, false, false, false, true, false, true, true, false, true ,false,true,true});
		assertEquals(11, runner.getMemspace().getNextAddress());
	}

	@Test
	void testInitRunnerContentAsBitStringNoRight0BitsGetN() {
		SOMBitcodeRunner runner = new SOMBitcodeRunner("00000101100000");
		assertEquals(5, runner.getMemspace().getN());
	}

	@Test
	void testInitRunnerContentAsBitStringNoRight0BitsGetStartAddress() {
		SOMBitcodeRunner runner = new SOMBitcodeRunner("00000101101011");
		assertEquals(11, runner.getMemspace().getNextAddress());
	}

	@Test
	void testInitRunnerContentAsBitStringWithSpacesNoRight0BitsGetN() {
		SOMBitcodeRunner runner = new SOMBitcodeRunner("0 0000101 1 00000");
		assertEquals(5, runner.getMemspace().getN());
	}

	@Test
	void testInitRunnerContentAsBitStringWithSpacesNoRight0BitsGetStartAddress() {
		SOMBitcodeRunner runner = new SOMBitcodeRunner("0 000 0101 0 010 11");
		assertEquals(11, runner.getMemspace().getNextAddress());
	}
}
