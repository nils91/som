/**
 * 
 */
package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.dralle.som.AbstractCommandAddressListenerDP;
import de.dralle.som.AbstractUnconditionalDebugPoint;
import de.dralle.som.Compiler;
import de.dralle.som.FileLoader;
import de.dralle.som.IMemspace;
import de.dralle.som.ISomMemspace;
import de.dralle.som.Opcode;
import de.dralle.som.SOMBitcodeRunner;
import de.dralle.som.SOMFormats;
import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hras.model.HRASModel;
import de.dralle.som.languages.hrbs.model.HRBSModel;

/**
 * @author Nils
 *
 */
class HRBSIndividualCommandsFixturesFiles {

	private Compiler c;
	private FileLoader f;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		c = new Compiler();
		f = new FileLoader();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@ParameterizedTest
	@MethodSource("provideTruthTableINV0")
	@Timeout(10)
	void testINV0(boolean inAcc, boolean finalAcc) throws IOException {
		String hrbsCode = "import \"test/fixtures/hrbs/individual_commands/INV0.hrbs\"\n\nMAIN:\n\tDEBUG: INV0;";
		HRBSModel hrbsModel = (HRBSModel) f.loadFromString(hrbsCode, SOMFormats.HRBS);
		HRACModel hracModel = c.compile(hrbsModel, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hrasModel = c.compile(hrbsModel, SOMFormats.HRBS, SOMFormats.HRAS);
		IMemspace memspace = c.compile(hrbsModel, SOMFormats.HRBS, SOMFormats.BIN);
		int dbgAdr = hrasModel.resolveSymbolToAddress("MAIN_null_DEBUG");
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		runner.addDebugPoint(new AbstractCommandAddressListenerDP("DEBUG", dbgAdr) {

			@Override
			public boolean trigger(int cmdAddress, Opcode op, int tgtAddress, ISomMemspace memspace) {
				memspace.setBit(0, inAcc);
				return true;
			}
		});
		runner.execute();
		assertTrue(runner.getMemspace().getAccumulatorValue() == finalAcc);
	}

	private static Stream<Arguments> provideTruthTableINV0() {
		return Stream.of(Arguments.of(false, true), Arguments.of(true, false));
	}

	@ParameterizedTest
	@MethodSource("provideTruthTableNOOP0")
	@Timeout(10)
	void testNOOP0(boolean inAcc, boolean finalAcc) throws IOException {
		String hrbsCode = "import \"test/fixtures/hrbs/individual_commands/NOOP0.hrbs\"\n\nMAIN:\n\tDEBUG: NOOP0;";
		HRBSModel hrbsModel = (HRBSModel) f.loadFromString(hrbsCode, SOMFormats.HRBS);
		HRACModel hracModel = c.compile(hrbsModel, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hrasModel = c.compile(hrbsModel, SOMFormats.HRBS, SOMFormats.HRAS);
		IMemspace memspace = c.compile(hrbsModel, SOMFormats.HRBS, SOMFormats.BIN);
		int dbgAdr = hrasModel.resolveSymbolToAddress("MAIN_null_DEBUG");
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		runner.addDebugPoint(new AbstractCommandAddressListenerDP("DEBUG", dbgAdr) {

			@Override
			public boolean trigger(int cmdAddress, Opcode op, int tgtAddress, ISomMemspace memspace) {
				memspace.setBit(0, inAcc);
				return true;
			}
		});
		runner.execute();
		assertTrue(runner.getMemspace().getAccumulatorValue() == finalAcc);
	}

	private static Stream<Arguments> provideTruthTableNOOP0() {
		return Stream.of(Arguments.of(false, false), Arguments.of(true, true));
	}

	@ParameterizedTest
	@MethodSource("provideTruthTableSET0")
	@Timeout(10)
	void testSET0(boolean inAcc, boolean finalAcc) throws IOException {
		String hrbsCode = "import \"test/fixtures/hrbs/individual_commands/SET0.hrbs\"\n\nMAIN:\n\tDEBUG: SET0;";
		HRBSModel hrbsModel = (HRBSModel) f.loadFromString(hrbsCode, SOMFormats.HRBS);
		HRACModel hracModel = c.compile(hrbsModel, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hrasModel = c.compile(hrbsModel, SOMFormats.HRBS, SOMFormats.HRAS);
		IMemspace memspace = c.compile(hrbsModel, SOMFormats.HRBS, SOMFormats.BIN);
		int dbgAdr = hrasModel.resolveSymbolToAddress("MAIN_null_DEBUG");
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		runner.addDebugPoint(new AbstractCommandAddressListenerDP("DEBUG", dbgAdr) {

			@Override
			public boolean trigger(int cmdAddress, Opcode op, int tgtAddress, ISomMemspace memspace) {
				memspace.setBit(0, inAcc);
				return true;
			}
		});
		runner.execute();
		assertTrue(runner.getMemspace().getAccumulatorValue() == finalAcc);
	}

	private static Stream<Arguments> provideTruthTableSET0() {
		return Stream.of(Arguments.of(false, true), Arguments.of(true, true));
	}

	@ParameterizedTest
	@MethodSource("provideTruthTableINV1")
	@Timeout(10)
	void testINV1(boolean inValueAcc, boolean inValueA, boolean finalValueAcc, boolean finalValueA) throws IOException {
		String hrbsCode = "import \"test/fixtures/hrbs/individual_commands/INV1.hrbs\"\n\nMAIN:\n\tglobal A\n\tDEBUG: INV1 A;";
		HRBSModel hrbsModel = (HRBSModel) f.loadFromString(hrbsCode, SOMFormats.HRBS);
		HRACModel hracModel = c.compile(hrbsModel, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hrasModel = c.compile(hrbsModel, SOMFormats.HRBS, SOMFormats.HRAS);
		IMemspace memspace = c.compile(hrbsModel, SOMFormats.HRBS, SOMFormats.BIN);
		int aAdr = hrasModel.resolveSymbolToAddress("A");
		int dbgAdr = hrasModel.resolveSymbolToAddress("MAIN_null_DEBUG");
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		runner.addDebugPoint(new AbstractCommandAddressListenerDP("DEBUG", dbgAdr) {

			@Override
			public boolean trigger(int cmdAddress, Opcode op, int tgtAddress, ISomMemspace memspace) {
				memspace.setAccumulatorValue(inValueAcc);
				memspace.setBit(aAdr, inValueA);
				return true;
			}
		});
		runner.execute();
		assertTrue(runner.getMemspace().getAccumulatorValue() == finalValueAcc);
		assertTrue(runner.getMemspace().getBit(aAdr) == finalValueA);
	}

	private static Stream<Arguments> provideTruthTableINV1() {
		return Stream.of(Arguments.of(false, false, false, true), Arguments.of(false, true, false, false),
				Arguments.of(true, false, true, true), Arguments.of(true, true, true, false));
	}
}
