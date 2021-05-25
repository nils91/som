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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import de.dralle.som.BooleanArrayMemspace;
import de.dralle.som.ISomMemspace;

/**
 * @author Nils Dralle
 *
 */
class MemspaceTests {

	// argument source
	static int[] sweepN() {
		int[] allNForTesting = new int[15 - 4];
		for (int i = 0; i < allNForTesting.length; i++) {
			allNForTesting[i] = i + 4;
		}
		return allNForTesting;
	}

	static int[] sweepAddresses(int n) {
		int[] addressesForTesting = new int[(int) Math.pow(2, n)];
		for (int i = 0; i < addressesForTesting.length; i++) {
			addressesForTesting[i] = i;
		}
		return addressesForTesting;
	}

	/**
	 * Get Instances for all memspace types to be used with this test.
	 * 
	 * @return
	 */
	static ISomMemspace[] getMemspacesForTesting() {
		ISomMemspace[] memspacesForTesting = new ISomMemspace[] { new BooleanArrayMemspace() };
		return memspacesForTesting;
	}

	/**
	 * get memspace instances for all n tested.
	 * 
	 * @return
	 */
	static ISomMemspace[] getMemspacesSizePresetForTesting() {
		int[] allNForTesting = sweepN();
		ISomMemspace[] memspacesForTesting = new ISomMemspace[allNForTesting.length * getMemspacesForTesting().length];
		for (int i = 0; i < allNForTesting.length; i++) {
			ISomMemspace[] memSpaces = getMemspacesForTesting();
			for (int j = 0; j < memSpaces.length; j++) {
				memSpaces[j].resize((int) Math.pow(2, allNForTesting[i]), false);
				memspacesForTesting[i * memSpaces.length + j] = memSpaces[j];
			}
		}
		return memspacesForTesting;
	}

	static Arguments[] matrixMemSpaceAndN() {
		int[] allNForTesting = sweepN();
		Arguments[] testArguments = new Arguments[allNForTesting.length * getMemspacesForTesting().length];
		for (int i = 0; i < allNForTesting.length; i++) {
			ISomMemspace[] memspaces = getMemspacesForTesting();
			for (int j = 0; j < memspaces.length; j++) {
				memspaces[j].resize((int) Math.pow(2, allNForTesting[i]), false);
				testArguments[i * memspaces.length + j] = Arguments.of(memspaces[j], allNForTesting[i]);
			}
		}
		return testArguments;
	}

	static Arguments[] matrixMemSpaceAndNAndAddress() {
		Arguments[] memSpaceAndNArgStream = matrixMemSpaceAndN();
		List<Arguments> testArguments = new ArrayList<>();
		for (int i = 0; i < memSpaceAndNArgStream.length; i++) {
			Arguments testArgs = memSpaceAndNArgStream[i];
			ISomMemspace memspace = (ISomMemspace) testArgs.get()[0];
			int n = (int) testArgs.get()[1];
			int[] testAddresses = sweepAddresses(n);
			for (int j = 0; j < testAddresses.length; j++) {
				testArguments.add(Arguments.of(memspace, n, testAddresses[j]));
			}
		}
		return testArguments.toArray(new Arguments[testArguments.size()]);
	}

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

	@ParameterizedTest
	@MethodSource("matrixMemSpaceAndN")
	void testSweepSet0(ISomMemspace memSpace, int n) {
		int memSpaceSize = (int) Math.pow(2, n);
		for (int i = 0; i < memSpaceSize; i++) {
			memSpace.setBit(i, false);
		}
		for (int i = 0; i < memSpaceSize; i++) {
			assertFalse(memSpace.getBit(i));
		}
	}

	@ParameterizedTest
	@MethodSource("matrixMemSpaceAndN")
	void testSweepSet1(ISomMemspace memSpace, int n) {
		int memSpaceSize = (int) Math.pow(2, n);
		for (int i = 0; i < memSpaceSize; i++) {
			memSpace.setBit(i, true);
		}
		for (int i = 0; i < memSpaceSize; i++) {
			assertTrue(memSpace.getBit(i));
		}
	}

	@ParameterizedTest
	@MethodSource("matrixMemSpaceAndN")
	void testSweepSetAlternating(ISomMemspace memSpace, int n) {
		int memSpaceSize = (int) Math.pow(2, n);
		for (int i = 0; i < memSpaceSize; i++) {
			memSpace.setBit(i, i % 2 == 0);
		}
		for (int i = 0; i < memSpaceSize; i++) {
			assertEquals(i % 2 == 0, memSpace.getBit(i));
		}
	}

	@ParameterizedTest
	@MethodSource("matrixMemSpaceAndN")
	void testSweepSetAlternatingSporadic(ISomMemspace memSpace, int n) {
		int memSpaceSize = (int) Math.pow(2, n);
		int alternatingDistance = 97;
		for (int i = 0; i < memSpaceSize; i++) {
			memSpace.setBit(i, i % alternatingDistance == 0);
		}
		for (int i = 0; i < memSpaceSize; i++) {
			assertEquals(i % alternatingDistance == 0, memSpace.getBit(i));
		}
	}

	@ParameterizedTest
	@MethodSource("getMemspacesForTesting")
	void testGetAccAddress(ISomMemspace memSpace) {
		assertEquals(0, memSpace.getAccumulatorAddress());
	}

	@ParameterizedTest
	@MethodSource("getMemspacesForTesting")
	void testAdrEvalAddress(ISomMemspace memSpace) {
		assertEquals(8, memSpace.getAdrEvalAddress());
	}

	@ParameterizedTest
	@MethodSource("matrixMemSpaceAndN")
	void testWhEnAddress(ISomMemspace memSpace, int n) {
		memSpace.setN(n);
		assertEquals(9 + n, memSpace.getWriteHookEnabledAddress());
	}

	@ParameterizedTest
	@MethodSource("matrixMemSpaceAndN")
	void testWhDirAddress(ISomMemspace memSpace, int n) {
		memSpace.setN(n);
		assertEquals(10 + n, memSpace.getWriteHookDirectionAddress());
	}

	@ParameterizedTest
	@MethodSource("matrixMemSpaceAndN")
	void testWhComAddress(ISomMemspace memSpace, int n) {
		memSpace.setN(n);
		assertEquals(11 + n, memSpace.getWriteHookCommunicationAddress());
	}

	@ParameterizedTest
	@MethodSource("matrixMemSpaceAndN")
	void testWhSelAddress(ISomMemspace memSpace, int n) {
		memSpace.setN(n);
		memSpace.setN(n);
		assertEquals(12 + n, memSpace.getWriteHookSelectAddress());
	}

	@ParameterizedTest
	@MethodSource("matrixMemSpaceAndN")
	void testSetN(ISomMemspace memSpace, int n) {
		memSpace.setN(n);
		String nBin = Integer.toBinaryString(n);
		while (nBin.length() < 7) {
			nBin = '0' + nBin;
		}
		for (int i = 0; i < nBin.toCharArray().length; i++) {
			switch (nBin.toCharArray()[i]) {
			case '0':
				assertFalse(memSpace.getBit(1 + i));
				break;
			case '1':
				assertTrue(memSpace.getBit(1 + i));
				break;
			default:
				fail();
				break;
			}
		}
	}

	@ParameterizedTest
	@MethodSource("matrixMemSpaceAndN")
	void testGetN(ISomMemspace memSpace, int n) {
		String nBin = Integer.toBinaryString(n);
		while (nBin.length() < 7) {
			nBin = '0' + nBin;
		}
		for (int i = 0; i < nBin.toCharArray().length; i++) {
			switch (nBin.toCharArray()[i]) {
			case '0':
				memSpace.setBit(1 + i, false);
				break;
			case '1':
				memSpace.setBit(1 + i, true);
				break;
			default:
				break;
			}
		}
		assertEquals(n, memSpace.getN());
	}

	@ParameterizedTest
	@MethodSource("matrixMemSpaceAndNAndAddress")
	void testSetNAddress(ISomMemspace memSpace, int n, int address) {
		memSpace.setN(n);
		memSpace.setNextAddress(address);
		String addressBin = Integer.toBinaryString(address);
		while (addressBin.length() < n) {
			addressBin = '0' + addressBin;
		}
		for (int i = 0; i < addressBin.toCharArray().length; i++) {
			switch (addressBin.toCharArray()[i]) {
			case '0':
				assertFalse(memSpace.getBit(memSpace.getAdrEvalAddress() + 1 + i));
				break;
			case '1':
				assertTrue(memSpace.getBit(memSpace.getAdrEvalAddress() + 1 + i));
				break;
			default:
				fail();
				break;
			}
		}
	}

	@ParameterizedTest
	@MethodSource("matrixMemSpaceAndNAndAddress")
	void testGetNAddress(ISomMemspace memSpace, int n, int address) {
		memSpace.setN(n);
		String nAdressBin = Integer.toBinaryString(address);
		while (nAdressBin.length() < n) {
			nAdressBin = '0' + nAdressBin;
		}
		for (int i = 0; i < nAdressBin.toCharArray().length; i++) {
			switch (nAdressBin.toCharArray()[i]) {
			case '0':
				memSpace.setBit(memSpace.getAdrEvalAddress() + 1 + i, false);
				break;
			case '1':
				memSpace.setBit(memSpace.getAdrEvalAddress() + 1 + i, true);
				break;
			default:
				break;
			}
		}
		assertEquals(address, memSpace.getNextAddress());
	}

	@ParameterizedTest
	@MethodSource("matrixMemSpaceAndN")
	void testMemspaceCloneSameContent(ISomMemspace memSpace, int n) {
		Random r = new Random();
		memSpace.setN(n);
		// fill with random crap
		for (int i = 8; i < Math.pow(2, n); i++) {
			memSpace.setBit(i, r.nextBoolean());
		}
		ISomMemspace clone = memSpace.clone();
		for (int i = 0; i < memSpace.getSize(); i++) {
			assertEquals(memSpace.getBit(i), clone.getBit(i));
		}
	}

	@ParameterizedTest
	@MethodSource("matrixMemSpaceAndN")
	void testMemspaceCloneSameContentWithEqualContent(ISomMemspace memSpace, int n) {
		Random r = new Random();
		memSpace.setN(n);
		// fill with random crap
		for (int i = 8; i < Math.pow(2, n); i++) {
			memSpace.setBit(i, r.nextBoolean());
		}
		ISomMemspace clone = memSpace.clone();
		assertTrue(memSpace.equalContent(clone));
	}

	@ParameterizedTest
	@MethodSource("matrixMemSpaceAndN")
	void testMemspaceCloneSameContentWithEquals(ISomMemspace memSpace, int n) {
		Random r = new Random();
		memSpace.setN(n);
		// fill with random crap
		for (int i = 8; i < Math.pow(2, n); i++) {
			memSpace.setBit(i, r.nextBoolean());
		}
		ISomMemspace clone = memSpace.clone();
		assertEquals(memSpace, clone);
	}

	@ParameterizedTest
	@MethodSource("matrixMemSpaceAndN")
	void testMemspaceClone(ISomMemspace memSpace, int n) {
		memSpace.setN(n);
		ISomMemspace clone = memSpace.clone();
		assertNotNull(clone);
	}
	@ParameterizedTest
	@MethodSource("matrixMemSpaceAndN")
	void testMemspaceCloneNotSameRef(ISomMemspace memSpace, int n) {
		memSpace.setN(n);
		ISomMemspace clone = memSpace.clone();
		assertFalse(memSpace==clone);
	}
	@ParameterizedTest
	@MethodSource("matrixMemSpaceAndN")
	void testMemspaceCloneNotSameRefBitSet(ISomMemspace memSpace, int n) {
		memSpace.setN(n);
		ISomMemspace clone = memSpace.clone();
		memSpace.setBit(0, false);
		clone.setBit(0, true);
		assertNotEquals(memSpace.getBit(0), clone.getBit(0));
	}
	@ParameterizedTest
	@MethodSource("matrixMemSpaceAndN")
	void testMemspaceCloneSameSize(ISomMemspace memSpace, int n) {
		memSpace.setN(n);
		ISomMemspace clone = memSpace.clone();
		assertEquals(memSpace.getSize(), clone.getSize());
	}

	@ParameterizedTest
	@MethodSource("matrixMemSpaceAndN")
	void testMemspaceResizeMakeSmaller(ISomMemspace memSpace, int n) {
		memSpace.setN(n);
		if (n > 4) {
			int newSize = (int) Math.pow(2, n - 1);
			memSpace.resize(newSize, false);
			assertTrue(newSize<= memSpace.getSize());
		}
	}

	@ParameterizedTest
	@MethodSource("matrixMemSpaceAndN")
	void testMemspaceResizeMakeBigger(ISomMemspace memSpace, int n) {
		memSpace.setN(n);
		int newSize = (int) Math.pow(2, n + 1);
		memSpace.resize(newSize, false);
		assertTrue(newSize<= memSpace.getSize());
	}

	@ParameterizedTest
	@MethodSource("matrixMemSpaceAndN")
	void testMemspaceResizeMakeSmallerSetBits(ISomMemspace memSpace, int n) {
		memSpace.setN(n);
		if (n > 4) {
			int newSize = (int) Math.pow(2, n - 1);
			memSpace.resize(newSize, false);
			for (int i = 8; i < newSize; i++) {
				memSpace.setBit(i, true);
			}
		}
	}

	@ParameterizedTest
	@MethodSource("matrixMemSpaceAndN")
	void testMemspaceResizeMakeBiggerSetBits(ISomMemspace memSpace, int n) {
		memSpace.setN(n);
		int newSize = (int) Math.pow(2, n + 1);
		memSpace.resize(newSize, false);
		for (int i = 8; i < newSize; i++) {
			memSpace.setBit(i, true);
		}
	}

	@ParameterizedTest
	@MethodSource("matrixMemSpaceAndN")
	void testMemspaceResizeMakeSmallerRetainContent(ISomMemspace memSpace, int n) {
		Random r = new Random();
		memSpace.setN(n);
		// fill with random crap
		for (int i = 8; i < Math.pow(2, n); i++) {
			memSpace.setBit(i, r.nextBoolean());
		}
		ISomMemspace oldMemspace = memSpace.clone();
		if (n > 4) {
			int newSize = (int) Math.pow(2, n - 1);
			memSpace.resize(newSize, true);
			for (int i = 0; i < newSize; i++) {
				assertEquals(oldMemspace.getBit(i), memSpace.getBit(i));
			}
		}
	}

	@ParameterizedTest
	@MethodSource("matrixMemSpaceAndN")
	void testMemspaceResizeMakeBiggerRetainContent(ISomMemspace memSpace, int n) {
		Random r = new Random();
		memSpace.setN(n);
		// fill with random crap
		for (int i = 8; i < Math.pow(2, n); i++) {
			memSpace.setBit(i, r.nextBoolean());
		}
		ISomMemspace oldMemspace = memSpace.clone();
		int newSize = (int) Math.pow(2, n + 1);
		memSpace.resize(newSize, true);
		for (int i = 0; i < newSize; i++) {
			if(i<oldMemspace.getSize()) {
				assertEquals(oldMemspace.getBit(i), memSpace.getBit(i));
			}
		}
	}
	
	@ParameterizedTest
	@MethodSource("matrixMemSpaceAndN")
	void testMemspaceCopy(ISomMemspace memSpace, int n) {
		ISomMemspace clone = memSpace.clone();
		Random r = new Random();
		memSpace.setN(n);
		// fill with random crap
		for (int i = 8; i < Math.pow(2, n); i++) {
			memSpace.setBit(i, r.nextBoolean());
		}
		clone.copy(memSpace);
		assertTrue(memSpace.equalContent(clone));
	}
	@ParameterizedTest
	@MethodSource("matrixMemSpaceAndN")
	void testMemspaceNoCopy(ISomMemspace memSpace, int n) {
		ISomMemspace clone = memSpace.clone();
		Random r = new Random();
		memSpace.setN(n);
		// fill with random crap
		for (int i = 8; i < Math.pow(2, n); i++) {
			memSpace.setBit(i, r.nextBoolean());
		}
		assertFalse(memSpace.equalContent(clone));
	}
}
