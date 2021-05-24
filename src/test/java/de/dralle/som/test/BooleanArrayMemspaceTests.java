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
import java.util.List;
import java.util.stream.Stream;

import de.dralle.som.BooleanArrayMemspace;
import de.dralle.som.ISomMemspace;

/**
 * @author Nils Dralle
 *
 */
class BooleanArrayMemspaceTests {

	//argument source
	static int[] sweepN() {
		int[] allNForTesting=new int[8-4];
		for (int i = 0; i < allNForTesting.length; i++) {
			allNForTesting[i]=i+4;
		}
		return allNForTesting;
	}
	static int[] sweepAddresses(int n) {
		int[] addressesForTesting=new int[(int) Math.pow(2, n)];
		for (int i = 0; i < addressesForTesting.length; i++) {
			addressesForTesting[i]=i;
		}
		return addressesForTesting;
	}
	static List<ISomMemspace> getMemspacesForTesting(int size){
		List<ISomMemspace> memspacesForTesting = new ArrayList<>();
		memspacesForTesting.add(new BooleanArrayMemspace(size));
		return memspacesForTesting;
	}
	/**
	 * get memspace instances for all n tested.
	 * @return
	 */
	static List<ISomMemspace> getMemspacesForTesting(){
		int[] allNForTesting = sweepN();
		List<ISomMemspace> memspacesForTesting = new ArrayList<>();
		for (int i = 0; i < allNForTesting.length; i++) {
			memspacesForTesting.addAll(getMemspacesForTesting((int) Math.pow(2, allNForTesting[i])));
		}
		return memspacesForTesting;
	}
	static Stream<Arguments> matrixMemSpaceAndN(){
		Stream<Arguments> testArguments=Stream.empty();
		int[] allNForTesting = sweepN();
		for (int i = 0; i < allNForTesting.length; i++) {
			List<ISomMemspace> memspaces = getMemspacesForTesting((int) Math.pow(2, allNForTesting[i]));
			for (ISomMemspace memspace : memspaces) {
				testArguments=Stream.concat(testArguments, Stream.of(Arguments.of(memspace,allNForTesting[i])));
			}			
		}
		return testArguments;
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
	void testSweepSet0(ISomMemspace memSpace,int n) {
		int memSpaceSize=(int) Math.pow(2, n);
		for (int i = 0; i < memSpaceSize; i++) {
			memSpace.setBit(i, false);
		}
		for (int i = 0; i < memSpaceSize; i++) {
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

	@Test
	void testSetN() {
		int n = 7;
		BooleanArrayMemspace memSpace = new BooleanArrayMemspace((int) Math.pow(2, n));
		memSpace.setN(n);
		String nBin = Integer.toBinaryString(n);
		while(nBin.length()<7) {
			nBin='0'+nBin;
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
	@Test
	void testGetN() {
		int n = 7;
		BooleanArrayMemspace memSpace = new BooleanArrayMemspace((int) Math.pow(2, n));
		String nBin = Integer.toBinaryString(n);
		while(nBin.length()<7) {
			nBin='0'+nBin;
		}
		for (int i = 0; i < nBin.toCharArray().length; i++) {
			switch (nBin.toCharArray()[i]) {
			case '0':
				memSpace.setBit(1+i, false);
				break;
			case '1':
				memSpace.setBit(1+i, true);
				break;
			default:
				break;
			}
		}
		assertEquals(n, memSpace.getN());
	}
	@Test
	void testSetNAddress() {
		int n = 7;
		int address=7;
		BooleanArrayMemspace memSpace = new BooleanArrayMemspace((int) Math.pow(2, n));
		memSpace.setN(n);
		memSpace.setNextAddress(address);
		String addressBin = Integer.toBinaryString(address);
		while(addressBin.length()<n) {
			addressBin='0'+addressBin;
		}
		for (int i = 0; i < addressBin.toCharArray().length; i++) {
			switch (addressBin.toCharArray()[i]) {
			case '0':
				assertFalse(memSpace.getBit(memSpace.getAdrEvalAddress()+1+i));
				break;
			case '1':
				assertTrue(memSpace.getBit(memSpace.getAdrEvalAddress()+1+i));
				break;
			default:
				fail();
				break;
			}
		}
	}
	@Test
	void testGetNAddress() {
		int n = 7;
		int address=7;
		BooleanArrayMemspace memSpace = new BooleanArrayMemspace((int) Math.pow(2, n));
		memSpace.setN(n);
		
		String nAdressBin = Integer.toBinaryString(n);
		while(nAdressBin.length()<n) {
			nAdressBin='0'+nAdressBin;
		}
		for (int i = 0; i < nAdressBin.toCharArray().length; i++) {
			switch (nAdressBin.toCharArray()[i]) {
			case '0':
				memSpace.setBit(memSpace.getAdrEvalAddress()+1+i, false);
				break;
			case '1':
				memSpace.setBit(memSpace.getAdrEvalAddress()+1+i, true);
				break;
			default:
				break;
			}
		}
		assertEquals(address, memSpace.getNextAddress());
	}
}
