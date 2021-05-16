package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.dralle.som.SOMBitcodeRunner;

class BitcodeRunnerInitWithWriteHookBitsTests {

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
		SOMBitcodeRunner runner = new SOMBitcodeRunner(5, 13);
		assertNotNull(runner);
	}

	@Test
	void testInitMemspaceSize() {
		SOMBitcodeRunner runner = new SOMBitcodeRunner(5, 13);
		int expectedMemspaceSize = (int) Math.pow(2, 5);
		int actualSize = runner.getMemSpace().length * 8;
		assertEquals(expectedMemspaceSize, actualSize);
	}

	@Test
	void testInitMemspaceContent() {
		SOMBitcodeRunner runner = new SOMBitcodeRunner(5, 13);
		byte[] expected = new byte[] { 0x05, (byte) 0xA0, 0x00, 0x00 };
		assertArrayEquals(expected, runner.getMemSpace());
	}

	@Test
	void testInitWithMemspaceContentFullMemspaceProvided() {
		byte[] expected = new byte[] { 0x05, (byte) 0xA0, 0x00, 0x00 };
		SOMBitcodeRunner runner = new SOMBitcodeRunner(expected);
		assertArrayEquals(expected, runner.getMemSpace());
	}

	@Test
	void testInitWithMemspaceContentAsBitArrayFullMemspaceProvided() {
		byte[] expected = new byte[] { 0x05, (byte) 0xA0, 0x00, 0x00 };
		SOMBitcodeRunner runner = new SOMBitcodeRunner(new boolean[] { false, false, false, false, false, true, false,
				true, true, false, true, false, false, false, false, false, false, false, false, false, false, false,
				false, false, false, false, false, false, false, false, false, false });
		assertArrayEquals(expected, runner.getMemSpace());
	}

	@Test
	void testInitWithMemspaceContentAsBitStringFullMemspaceProvided() {
		byte[] expected = new byte[] { 0x05, (byte) 0xA0, 0x00, 0x00 };
		SOMBitcodeRunner runner = new SOMBitcodeRunner("00000101101000000000000000000000");
		assertArrayEquals(expected, runner.getMemSpace());
	}

	@Test
	void testInitWithMemspaceContentAsBitStringWithSpacesFullMemspaceProvided() {
		byte[] expected = new byte[] { 0x05, (byte) 0xA0, 0x00, 0x00 };
		SOMBitcodeRunner runner = new SOMBitcodeRunner("0 00001 01101 00 00000 00 00000 00 00000");
		assertArrayEquals(expected, runner.getMemSpace());
	}

	@Test
	void testInitWithMemspaceContentNoRight0Bits() {
		byte[] expected = new byte[] { 0x05, (byte) 0xA0, 0x00, 0x00 };
		SOMBitcodeRunner runner = new SOMBitcodeRunner(new byte[] { expected[0], expected[1] });
		assertArrayEquals(expected, runner.getMemSpace());
	}

	@Test
	void testInitWithMemspaceContentAsBitArrayNoRight0Bits() {
		byte[] expected = new byte[] { 0x05, (byte) 0xA0, 0x00, 0x00 };
		SOMBitcodeRunner runner = new SOMBitcodeRunner(
				new boolean[] { false, false, false, false, false, true, false, true, true, false, true });
		assertArrayEquals(expected, runner.getMemSpace());
	}

	@Test
	void testInitWithMemspaceContentAsBitStringNoRight0Bits() {
		byte[] expected = new byte[] { 0x05, (byte) 0xA0, 0x00, 0x00 };
		SOMBitcodeRunner runner = new SOMBitcodeRunner("00000101101");
		assertArrayEquals(expected, runner.getMemSpace());
	}

	@Test
	void testInitWithMemspaceContentAsBitStringWithSpacesNoRight0Bits() {
		byte[] expected = new byte[] { 0x05, (byte) 0xA0, 0x00, 0x00 };
		SOMBitcodeRunner runner = new SOMBitcodeRunner("0 00001 01101");
		assertArrayEquals(expected, runner.getMemSpace());
	}

	@Test
	void testInitRunnerGetN() {
		SOMBitcodeRunner runner = new SOMBitcodeRunner(5, 13);
		assertEquals(5, runner.getN());
	}

	@Test
	void testInitRunnerGetStartAddress() {
		SOMBitcodeRunner runner = new SOMBitcodeRunner(5, 13);
		assertEquals(13, runner.getAddressBits());
	}

	@Test
	void testInitWithMemspaceContentFullMemspaceProvidedGetN() {
		byte[] expected = new byte[] { 0x05, (byte) 0xA0, 0x00, 0x00 };
		SOMBitcodeRunner runner = new SOMBitcodeRunner(expected);
		assertEquals(5, runner.getN());
	}

	@Test
	void testInitWithMemspaceContentFullMemspaceProvidedGetStartAddress() {
		byte[] expected = new byte[] { 0x05, (byte) 0xA0, 0x00, 0x00 };
		SOMBitcodeRunner runner = new SOMBitcodeRunner(expected);
		assertEquals(13, runner.getAddressBits());
	}

	@Test
	void testInitRunnerContentAsBitArrayFullMemspaceProvidedGetN() {
		byte[] expected = new byte[] { 0x05, (byte) 0xA0, 0x00, 0x00 };
		SOMBitcodeRunner runner = new SOMBitcodeRunner(new boolean[] { false, false, false, false, false, true, false,
				true, true, false, true, false, false, false, false, false, false, false, false, false, false, false,
				false, false, false, false, false, false, false, false, false, false });
		assertEquals(5, runner.getN());
	}

	@Test
	void testInitRunnerContentAsBitArrayFullMemspaceProvidedGetStartAddress() {
		byte[] expected = new byte[] { 0x05, (byte) 0xA0, 0x00, 0x00 };
		SOMBitcodeRunner runner = new SOMBitcodeRunner(new boolean[] { false, false, false, false, false, true, false,
				true, true, false, true, false, false, false, false, false, false, false, false, false, false, false,
				false, false, false, false, false, false, false, false, false, false });
		assertEquals(13, runner.getAddressBits());
	}

	@Test
	void testInitRunnerContentAsBitStringFullMemspaceProvidedGetN() {
		byte[] expected = new byte[] { 0x05, (byte) 0xA0, 0x00, 0x00 };
		SOMBitcodeRunner runner = new SOMBitcodeRunner("00000101101000000000000000000000");
		assertEquals(5, runner.getN());
	}

	@Test
	void testInitRunnerContentAsBitStringFullMemspaceProvidedGetStartAddress() {
		byte[] expected = new byte[] { 0x05, (byte) 0xA0, 0x00, 0x00 };
		SOMBitcodeRunner runner = new SOMBitcodeRunner("00000101101000000000000000000000");
		assertEquals(13, runner.getAddressBits());
	}

	@Test
	void testInitRunnerContentAsBitStringWithSpacesFullMemspaceProvidedGetN() {
		byte[] expected = new byte[] { 0x05, (byte) 0xA0, 0x00, 0x00 };
		SOMBitcodeRunner runner = new SOMBitcodeRunner("0 00001 01101 00 00000 00 00000 00 00000");
		assertEquals(5, runner.getN());
	}

	@Test
	void testInitRunnerContentAsBitStringWithSpacesFullMemspaceProvidedGetStartAddress() {
		byte[] expected = new byte[] { 0x05, (byte) 0xA0, 0x00, 0x00 };
		SOMBitcodeRunner runner = new SOMBitcodeRunner("0 00001 01101 00 00000 00 00000 00 00000");
		assertEquals(13, runner.getAddressBits());
	}

	@Test
	void testInitRunnerContentNoRight0BitsGetN() {
		byte[] expected = new byte[] { 0x05, (byte) 0xA0, 0x00, 0x00 };
		SOMBitcodeRunner runner = new SOMBitcodeRunner(new byte[] { expected[0], expected[1] });
		assertEquals(5, runner.getN());
	}

	@Test
	void testInitRunnerContentNoRight0BitsGetStartAddress() {
		byte[] expected = new byte[] { 0x05, (byte) 0xA0, 0x00, 0x00 };
		SOMBitcodeRunner runner = new SOMBitcodeRunner(new byte[] { expected[0], expected[1] });
		assertEquals(13, runner.getAddressBits());
	}

	@Test
	void testInitRunnerContentAsBitArrayNoRight0BitsGetN() {
		byte[] expected = new byte[] { 0x05, (byte) 0xA0, 0x00, 0x00 };
		SOMBitcodeRunner runner = new SOMBitcodeRunner(
				new boolean[] { false, false, false, false, false, true, false, true, true, false, true });
		assertEquals(5, runner.getN());
	}

	@Test
	void testInitRunnerContentAsBitArrayNoRight0BitsGetStartAddress() {
		byte[] expected = new byte[] { 0x05, (byte) 0xA0, 0x00, 0x00 };
		SOMBitcodeRunner runner = new SOMBitcodeRunner(
				new boolean[] { false, false, false, false, false, true, false, true, true, false, true });
		assertEquals(13, runner.getAddressBits());
	}

	@Test
	void testInitRunnerContentAsBitStringNoRight0BitsGetN() {
		byte[] expected = new byte[] { 0x05, (byte) 0xA0, 0x00, 0x00 };
		SOMBitcodeRunner runner = new SOMBitcodeRunner("00000101101");
		assertEquals(5, runner.getN());
	}

	@Test
	void testInitRunnerContentAsBitStringNoRight0BitsGetStartAddress() {
		byte[] expected = new byte[] { 0x05, (byte) 0xA0, 0x00, 0x00 };
		SOMBitcodeRunner runner = new SOMBitcodeRunner("00000101101");
		assertEquals(13, runner.getAddressBits());
	}

	@Test
	void testInitRunnerContentAsBitStringWithSpacesNoRight0BitsGetN() {
		byte[] expected = new byte[] { 0x05, (byte) 0xA0, 0x00, 0x00 };
		SOMBitcodeRunner runner = new SOMBitcodeRunner("0 00001 01101");
		assertEquals(5, runner.getN());
	}

	@Test
	void testInitRunnerContentAsBitStringWithSpacesNoRight0BitsGetStartAddress() {
		byte[] expected = new byte[] { 0x05, (byte) 0xA0, 0x00, 0x00 };
		SOMBitcodeRunner runner = new SOMBitcodeRunner("0 00001 01101");
		assertEquals(13, runner.getAddressBits());
	}
}
