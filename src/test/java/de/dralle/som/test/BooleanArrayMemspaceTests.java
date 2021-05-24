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
import java.util.stream.Stream;

import de.dralle.som.BooleanArrayMemspace;
import de.dralle.som.ISomMemspace;

/**
 * @author Nils Dralle
 *
 */
class BooleanArrayMemspaceTests {

	// argument source
	static int[] sweepN() {
		int[] allNForTesting = new int[12 - 4];
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

	static ISomMemspace[] getMemspacesForTesting(int size) {
		ISomMemspace[] memspacesForTesting = new ISomMemspace[] {new BooleanArrayMemspace(size)};
		return memspacesForTesting;
	}

	/**
	 * get memspace instances for all n tested.
	 * 
	 * @return
	 */
	static ISomMemspace[] getMemspacesForTesting() {
		int[] allNForTesting = sweepN();
		ISomMemspace[] memspacesForTesting = new ISomMemspace[allNForTesting.length*getMemspacesForTesting(0).length];
		for (int i = 0; i < allNForTesting.length; i++) {
			ISomMemspace[] memSpaces = getMemspacesForTesting((int) Math.pow(2, allNForTesting[i]));
			for (int j = 0; j < memSpaces.length; j++) {
				memspacesForTesting[i*memSpaces.length+j]=memSpaces[j];
			}
		}
		return memspacesForTesting;
	}

	static Arguments[] matrixMemSpaceAndN() {
		int[] allNForTesting = sweepN();
		Arguments[] testArguments = new Arguments[allNForTesting.length*getMemspacesForTesting(0).length];		
		for (int i = 0; i < allNForTesting.length; i++) {
			ISomMemspace[] memspaces = getMemspacesForTesting((int) Math.pow(2, allNForTesting[i]));
			for (int j = 0; j < memspaces.length; j++) {
				testArguments[i*memspaces.length+j]=Arguments.of(memspaces[j],allNForTesting[i]);				
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
				testArguments.add(Arguments.of(memspace, n, testAddresses[i]));
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
}
