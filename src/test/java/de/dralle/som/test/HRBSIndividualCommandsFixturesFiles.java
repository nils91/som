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

	@ParameterizedTest
	@MethodSource("provideTruthTableNOT1")
	@Timeout(10)
	void testNOT1(boolean inValueAcc, boolean inValueA, boolean finalValueAcc, boolean finalValueA) throws IOException {
		String hrbsCode = "import \"test/fixtures/hrbs/individual_commands/NOT1.hrbs\"\n\nMAIN:\n\tglobal A\n\tDEBUG: NOT1 A;";
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

	private static Stream<Arguments> provideTruthTableNOT1() {
		return Stream.of(Arguments.of(false, false, true, false), Arguments.of(false, true, false, true),
				Arguments.of(true, false, true, false), Arguments.of(true, true, false, true));
	}

	@ParameterizedTest
	@MethodSource("provideTruthTableOR1")
	@Timeout(10)
	void testOR1(boolean inValueAcc, boolean inValueA, boolean finalValueAcc, boolean finalValueA) throws IOException {
		String hrbsCode = "import \"test/fixtures/hrbs/individual_commands/OR1.hrbs\"\n\nMAIN:\n\tglobal A\n\tDEBUG: OR1 A;";
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

	private static Stream<Arguments> provideTruthTableOR1() {
		return Stream.of(Arguments.of(false, false, false, false), Arguments.of(false, true, true, true),
				Arguments.of(true, false, true, false), Arguments.of(true, true, true, true));
	}

	@ParameterizedTest
	@MethodSource("provideTruthTableREAD1")
	@Timeout(10)
	void testREAD1(boolean inValueAcc, boolean inValueA, boolean finalValueAcc, boolean finalValueA)
			throws IOException {
		String hrbsCode = "import \"test/fixtures/hrbs/individual_commands/READ1.hrbs\"\n\nMAIN:\n\tglobal A\n\tDEBUG: READ1 A;";
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

	private static Stream<Arguments> provideTruthTableREAD1() {
		return Stream.of(Arguments.of(false, false, false, false), Arguments.of(false, true, true, true),
				Arguments.of(true, false, false, false), Arguments.of(true, true, true, true));
	}

	@ParameterizedTest
	@MethodSource("provideTruthTableSET1")
	@Timeout(10)
	void testSET1(boolean inValueAcc, boolean inValueA, boolean finalValueAcc, boolean finalValueA) throws IOException {
		String hrbsCode = "import \"test/fixtures/hrbs/individual_commands/SET1.hrbs\"\n\nMAIN:\n\tglobal A\n\tDEBUG: SET1 A;";
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

	private static Stream<Arguments> provideTruthTableSET1() {
		return Stream.of(Arguments.of(false, false, false, true), Arguments.of(false, true, false, true),
				Arguments.of(true, false, true, true), Arguments.of(true, true, true, true));
	}

	@ParameterizedTest
	@MethodSource("provideTruthTableWRITE1")
	@Timeout(10)
	void testWRITE1(boolean inValueAcc, boolean inValueA, boolean finalValueAcc, boolean finalValueA)
			throws IOException {
		String hrbsCode = "import \"test/fixtures/hrbs/individual_commands/WRITE1.hrbs\"\n\nMAIN:\n\tglobal A\n\tDEBUG: WRITE1 A;";
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
		assertEquals(finalValueAcc, runner.getMemspace().getAccumulatorValue());
		assertEquals(finalValueA, runner.getMemspace().getBit(aAdr));
	}

	private static Stream<Arguments> provideTruthTableWRITE1() {
		return Stream.of(Arguments.of(false, false, false, false), Arguments.of(false, true, false, false),
				Arguments.of(true, false, true, true), Arguments.of(true, true, true, true));
	}
	@ParameterizedTest
	@MethodSource("provideTruthTableXOR1")
	@Timeout(10)
	void testXOR1(boolean inValueAcc, boolean inValueA, boolean finalValueAcc, boolean finalValueA)
			throws IOException {
		String hrbsCode = "import \"test/fixtures/hrbs/individual_commands/XOR1.hrbs\"\n\nMAIN:\n\tglobal A\n\tDEBUG: XOR1 A;";
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
		assertEquals(finalValueAcc, runner.getMemspace().getAccumulatorValue());
		assertEquals(finalValueA, runner.getMemspace().getBit(aAdr));
	}

	private static Stream<Arguments> provideTruthTableXOR1() {
		return Stream.of(Arguments.of(false, false, false, false), Arguments.of(false, true, true, true),
				Arguments.of(true, false, true, false), Arguments.of(true, true, false, true));
	}
	@ParameterizedTest
	@MethodSource("provideTruthTableTRIGGER1")
	@Timeout(10)
	void testTRIGGER1(boolean inValueAcc, boolean inValueA, boolean finalValueAcc, boolean finalValueA)
			throws IOException {
		String hrbsCode = "import \"test/fixtures/hrbs/individual_commands/TRIGGER1.hrbs\"\n\nMAIN:\n\tglobal A\n\tDEBUG: TRIGGER1 A;";
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
		assertEquals(finalValueAcc, runner.getMemspace().getAccumulatorValue());
		assertEquals(finalValueA, runner.getMemspace().getBit(aAdr));
	}

	private static Stream<Arguments> provideTruthTableTRIGGER1() {
		return Stream.of(Arguments.of(false, false, false, false),
				Arguments.of(true, false, true, false));
	}

	@ParameterizedTest
	@MethodSource("provideTruthTableCOPY2")
	@Timeout(10)
	void testCOPY2(boolean inValueAcc, boolean inValueA,boolean inValueB, boolean finalValueAcc, boolean finalValueA,boolean finalValueB)
			throws IOException {
		String hrbsCode = "import \"test/fixtures/hrbs/individual_commands/COPY2.hrbs\"\n\nMAIN:\n\tglobal A\n\tglobal B\n\tDEBUG: COPY2 A,B;";
		HRBSModel hrbsModel = (HRBSModel) f.loadFromString(hrbsCode, SOMFormats.HRBS);
		HRACModel hracModel = c.compile(hrbsModel, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hrasModel = c.compile(hrbsModel, SOMFormats.HRBS, SOMFormats.HRAS);
		IMemspace memspace = c.compile(hrbsModel, SOMFormats.HRBS, SOMFormats.BIN);
		int aAdr = hrasModel.resolveSymbolToAddress("A");
		int bAdr = hrasModel.resolveSymbolToAddress("B");
		int dbgAdr = hrasModel.resolveSymbolToAddress("MAIN_null_DEBUG");
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		runner.addDebugPoint(new AbstractCommandAddressListenerDP("DEBUG", dbgAdr) {

			@Override
			public boolean trigger(int cmdAddress, Opcode op, int tgtAddress, ISomMemspace memspace) {
				memspace.setAccumulatorValue(inValueAcc);
				memspace.setBit(aAdr, inValueA);
				memspace.setBit(bAdr, inValueB);
				return true;
			}
		});
		runner.execute();
		assertEquals(finalValueAcc, runner.getMemspace().getAccumulatorValue());
		assertEquals(finalValueA, runner.getMemspace().getBit(aAdr));assertEquals(finalValueB, runner.getMemspace().getBit(bAdr));
	}

	private static Stream<Arguments> provideTruthTableCOPY2() {
		return Stream.of(Arguments.of(false, false,  false, false, false, false),
				Arguments.of(false, false, true, false, false, false, false),
				Arguments.of(false, true, false,  false, true, true),
				Arguments.of(false, true, true,  false, true, true),
				Arguments.of(true, false, false,  true, false, false),
				Arguments.of(true, false, true,  true, false, false),
				Arguments.of(true, true, false,  true, true, true),
				Arguments.of(true, true, true,  true, true, true));
	}

	@ParameterizedTest
	@MethodSource("provideTruthTableCOMPARE1")
	@Timeout(10)
	void testCOMPARE1(boolean inValueAcc, boolean inValueA, boolean finalValueAcc, boolean finalValueA)
			throws IOException {
		String hrbsCode = "import \"test/fixtures/hrbs/individual_commands/COMPARE1.hrbs\"\n\nMAIN:\n\tglobal A\n\tDEBUG: COMPARE1 A;";
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
		assertEquals(finalValueAcc, runner.getMemspace().getAccumulatorValue());
		assertEquals(finalValueA, runner.getMemspace().getBit(aAdr));
	}

	private static Stream<Arguments> provideTruthTableCOMPARE1() {
		return Stream.of(Arguments.of(false, false, true, false), Arguments.of(false, true, false, true),
				Arguments.of(true, false, false, false), Arguments.of(true, true, true, true));
	}

	@ParameterizedTest
	@MethodSource("provideTruthTableCLEAR0")
	@Timeout(10)
	void testCLEAR0(boolean inValueAcc, boolean finalValueAcc) throws IOException {
		String hrbsCode = "import \"test/fixtures/hrbs/individual_commands/CLEAR0.hrbs\"\n\nMAIN:\n\tDEBUG: CLEAR0;";
		HRBSModel hrbsModel = (HRBSModel) f.loadFromString(hrbsCode, SOMFormats.HRBS);
		HRACModel hracModel = c.compile(hrbsModel, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hrasModel = c.compile(hrbsModel, SOMFormats.HRBS, SOMFormats.HRAS);
		IMemspace memspace = c.compile(hrbsModel, SOMFormats.HRBS, SOMFormats.BIN);
		int dbgAdr = hrasModel.resolveSymbolToAddress("MAIN_null_DEBUG");
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		runner.addDebugPoint(new AbstractCommandAddressListenerDP("DEBUG", dbgAdr) {

			@Override
			public boolean trigger(int cmdAddress, Opcode op, int tgtAddress, ISomMemspace memspace) {
				memspace.setAccumulatorValue(inValueAcc);
				return true;
			}
		});
		runner.execute();
		assertEquals(finalValueAcc, runner.getMemspace().getAccumulatorValue());
	}

	private static Stream<Arguments> provideTruthTableCLEAR0() {
		return Stream.of(Arguments.of(false, false), Arguments.of(true, false));
	}

	@ParameterizedTest
	@MethodSource("provideTruthTableAND1")
	@Timeout(10)
	void testAND1(boolean inValueAcc, boolean inValueA, boolean finalValueAcc, boolean finalValueA) throws IOException {
		String hrbsCode = "import \"test/fixtures/hrbs/individual_commands/AND1.hrbs\"\n\nMAIN:\n\tglobal A\n\tDEBUG: AND1 A;";
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
		assertEquals(finalValueAcc, runner.getMemspace().getAccumulatorValue());
		assertEquals(finalValueA, runner.getMemspace().getBit(aAdr));
	}

	private static Stream<Arguments> provideTruthTableAND1() {
		return Stream.of(Arguments.of(false, false, false, false), Arguments.of(false, true, false, true),
				Arguments.of(true, false, false, false), Arguments.of(true, true, true, true));
	}
	
	@ParameterizedTest
	@MethodSource("provideTruthTableSWAPY2")
	@Timeout(10)
	void testSWAP2(boolean inValueAcc, boolean inValueA,boolean inValueB, boolean finalValueAcc, boolean finalValueA,boolean finalValueB)
			throws IOException {
		String hrbsCode = "import \"test/fixtures/hrbs/individual_commands/SWAP2.hrbs\"\n\nMAIN:\n\tglobal A\n\tglobal B\n\tDEBUG: SWAP2 A,B;";
		HRBSModel hrbsModel = (HRBSModel) f.loadFromString(hrbsCode, SOMFormats.HRBS);
		HRACModel hracModel = c.compile(hrbsModel, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hrasModel = c.compile(hrbsModel, SOMFormats.HRBS, SOMFormats.HRAS);
		IMemspace memspace = c.compile(hrbsModel, SOMFormats.HRBS, SOMFormats.BIN);
		int aAdr = hrasModel.resolveSymbolToAddress("A");
		int bAdr = hrasModel.resolveSymbolToAddress("B");
		int dbgAdr = hrasModel.resolveSymbolToAddress("MAIN_null_DEBUG");
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) memspace);
		runner.addDebugPoint(new AbstractCommandAddressListenerDP("DEBUG", dbgAdr) {

			@Override
			public boolean trigger(int cmdAddress, Opcode op, int tgtAddress, ISomMemspace memspace) {
				memspace.setAccumulatorValue(inValueAcc);
				memspace.setBit(aAdr, inValueA);
				memspace.setBit(bAdr, inValueB);
				return true;
			}
		});
		runner.execute();
		assertEquals(finalValueAcc, runner.getMemspace().getAccumulatorValue());
		assertEquals(finalValueA, runner.getMemspace().getBit(aAdr));assertEquals(finalValueB, runner.getMemspace().getBit(bAdr));
	}

	private static Stream<Arguments> provideTruthTableSWAPY2() {
		return Stream.of(Arguments.of(false, false,  false, false, false, false),
				Arguments.of(false, false, true, false, true, false),
				Arguments.of(false, true, false,  false, false, true),
				Arguments.of(false, true, true,  false, true, true),
				Arguments.of(true, false, false,  true, false, false),
				Arguments.of(true, false, true,  true, true, false),
				Arguments.of(true, true, false,  true, false, true),
				Arguments.of(true, true, true,  true, true, true));
	}
	@ParameterizedTest
	@MethodSource("provideTruthTableNOR1")
	@Timeout(10)
	void testNOR1(boolean inValueAcc, boolean inValueA, boolean finalValueAcc, boolean finalValueA) throws IOException {
		String hrbsCode = "import \"test/fixtures/hrbs/individual_commands/NOR1.hrbs\"\n\nMAIN:\n\tglobal A\n\tDEBUG: NOR1 A;";
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
		assertEquals(finalValueAcc, runner.getMemspace().getAccumulatorValue());
		assertEquals(finalValueA, runner.getMemspace().getBit(aAdr));
	}
	@ParameterizedTest
	@MethodSource("provideTruthTableNAND1")
	@Timeout(10)
	void testNAND1(boolean inValueAcc, boolean inValueA, boolean finalValueAcc, boolean finalValueA) throws IOException {
		String hrbsCode = "import \"test/fixtures/hrbs/individual_commands/NAND1.hrbs\"\n\nMAIN:\n\tglobal A\n\tDEBUG: NAND1 A;";
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
		assertEquals(finalValueAcc, runner.getMemspace().getAccumulatorValue());
		assertEquals(finalValueA, runner.getMemspace().getBit(aAdr));
	}
	private static Stream<Arguments> provideTruthTableNAND1() {
		return Stream.of(Arguments.of(false, false, true, false), Arguments.of(false, true, true, true),
				Arguments.of(true, false, true, false), Arguments.of(true, true, false, true));
	}
	private static Stream<Arguments> provideTruthTableNOR1() {
		return Stream.of(Arguments.of(false, false, true, false), Arguments.of(false, true, false, true),
				Arguments.of(true, false, false, false), Arguments.of(true, true, false, true));
	}
	@ParameterizedTest
	@MethodSource("provideTruthTableSWAP1")
	@Timeout(10)
	void testSWAP1(boolean inValueAcc, boolean inValueA, boolean finalValueAcc, boolean finalValueA) throws IOException {
		String hrbsCode = "import \"test/fixtures/hrbs/individual_commands/SWAP1.hrbs\"\n\nMAIN:\n\tglobal A\n\tDEBUG: SWAP1 A;";
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
		assertEquals(finalValueAcc, runner.getMemspace().getAccumulatorValue());
		assertEquals(finalValueA, runner.getMemspace().getBit(aAdr));
	}
	private static Stream<Arguments> provideTruthTableSWAP1() {
		return Stream.of(Arguments.of(false, false, false, false), Arguments.of(false, true, true, false),
				Arguments.of(true, false, false, true), Arguments.of(true, true, true, true));
	}
	@ParameterizedTest
	@MethodSource("provideTruthTableCleAR1")
	@Timeout(10)
	void testCLEARP1(boolean inValueAcc, boolean inValueA, boolean finalValueAcc, boolean finalValueA) throws IOException {
		String hrbsCode = "import \"test/fixtures/hrbs/individual_commands/CLEAR1.hrbs\"\n\nMAIN:\n\tglobal A\n\tDEBUG: CLEAR1 A;";
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
		assertEquals(finalValueAcc, runner.getMemspace().getAccumulatorValue());
		assertEquals(finalValueA, runner.getMemspace().getBit(aAdr));
	}
	private static Stream<Arguments> provideTruthTableCleAR1() {
		return Stream.of(Arguments.of(false, false, false, false), Arguments.of(false, true, false, false),
				Arguments.of(true, false, true, false), Arguments.of(true, true, true, false));
	}
}
