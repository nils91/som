package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

import de.dralle.som.AbstractUnconditionalDebugPoint;
import de.dralle.som.Compiler;
import de.dralle.som.FileLoader;
import de.dralle.som.IMemspace;
import de.dralle.som.ISomMemspace;
import de.dralle.som.Opcode;
import de.dralle.som.SOMBitcodeRunner;
import de.dralle.som.SOMFormats;
import de.dralle.som.languages.hrac.model.AbstractHRACMemoryAddress;
import de.dralle.som.languages.hrac.model.FixedHRACMemoryAddress;
import de.dralle.som.languages.hrac.model.HRACForDup;
import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hrac.model.HRACSymbol;
import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractDirectiveExpressionTreeNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACDirectiveNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACIntegerNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACMultiplicationExpressionNode;
import de.dralle.som.languages.hras.model.AbstractHRASMemoryAddress;
import de.dralle.som.languages.hras.model.HRASCommand;
import de.dralle.som.languages.hras.model.HRASModel;
import de.dralle.som.languages.hrav.model.HRAVModel;
import de.dralle.som.languages.hrbs.model.HRBSFixedMemoryAddress;
import de.dralle.som.languages.hrbs.model.HRBSModel;
import de.dralle.som.languages.hrbs.model.HRBSSymbol;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSDirectiveNode;

class HRBSCompileTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	static Stream<String> testfileFor145Provider() {
		return Stream.of(
				"test/fixtures/hrbs/issue/145_no_atomic_childs_label_compile/test_label_compile_no_atomic_child_2nd.hrbs",
				"test/fixtures/hrbs/issue/145_no_atomic_childs_label_compile/test_label_compile_no_atomic_child.hrbs",
				"test/fixtures/hrbs/issue/145_no_atomic_childs_label_compile/test_label_compile_no_atomic_childs_child_2nd.hrbs",
				"test/fixtures/hrbs/issue/145_no_atomic_childs_label_compile/test_label_compile_no_atomic_childs_child_label_defer_gen.hrbs",
				"test/fixtures/hrbs/issue/145_no_atomic_childs_label_compile/test_label_compile_no_atomic_childs_child_label_defer.hrbs",
				"test/fixtures/hrbs/issue/145_no_atomic_childs_label_compile/test_label_compile_no_atomic_childs_child.hrbs",
				// The next 4 files are not issue 145
				"test/fixtures/hrbs/features/lbl_bump/test_label_bump.hrbs",
				"test/fixtures/hrbs/features/lbl_bump/test_label_bump_no_more.hrbs",
				"test/fixtures/hrbs/features/lbl_bump/test_label_bump_no_overwrite.hrbs",
				"test/fixtures/hrbs/test_label_child_no_overwrite.hrbs");
	}

	static Stream<Arguments> testfileForLabelCommandAdditionProvider() {
		return Stream.of(Arguments.of("test/fixtures/hrbs/features/lbl_bump/test_label_bump.hrbs", 1),
				Arguments.of("test/fixtures/hrbs/features/lbl_bump/test_label_bump_no_more.hrbs", 2),
				Arguments.of("test/fixtures/hrbs/features/lbl_bump/test_label_bump_no_overwrite.hrbs", 1));
	}

	static Stream<String> testfileForNotOverwritingLabelProvider() {
		return Stream.of("test/fixtures/hrbs/features/lbl_bump/test_label_bump_no_overwrite.hrbs",
				"test/fixtures/hrbs/test_label_child_no_overwrite.hrbs");
	}

	private Compiler c;

	private FileLoader f;

	@BeforeEach
	void setUp() throws Exception {
		c = new Compiler();
		f = new FileLoader();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test4bitAddCompileForDup() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_4bit_add.hrbs", SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		HRAVModel hrav = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAV);
		IMemspace bin = c.compile(model, SOMFormats.HRBS, SOMFormats.BIN);
		assertNotNull(hrac);
		assertNotNull(hras);
		assertNotNull(hrav);
		assertNotNull(bin);
	}

	@Test
	void test4bitAddExecute() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_4bit_add.hrbs", SOMFormats.HRBS);
		IMemspace bin = c.compile(model, SOMFormats.HRBS, SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) bin);
		assertTrue(runner.execute());
	}

	@Timeout(30)
	@Test
	void testAdrSetToLabelAfterExec() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_jump.hrbs", SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		int labelArd = hras.resolveSymbolToAddress("LABEL");
		HRAVModel hrav = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAV);
		IMemspace bin = c.compile(model, SOMFormats.HRBS, SOMFormats.BIN);
		assertNotEquals(((ISomMemspace) bin).getNextAddress(), labelArd);// no change before exec
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) bin);
		runner.execute();
		bin = runner.getMemspace();
		assertEquals(labelArd, ((ISomMemspace) bin).getNextAddress());// written to label expectesd after exec
	}

	@Test
	void testCompileFixedAdressOnCommand() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_fixed_adr_on_command.hrbs", SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		IMemspace m = c.compile(model, SOMFormats.HRBS, SOMFormats.BIN);
		assertNotNull(m);
	}

	@Test
	void testCompileFixedAdressOnMirrorSymbol() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_fixed_adr_on_mirror_symbol.hrbs", SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		IMemspace m = c.compile(model, SOMFormats.HRBS, SOMFormats.BIN);
		assertNotNull(m);
	}

	@Test
	void testConditionalJumpExecutePositive() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_conditionaljump_simple.hrbs", SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		HRAVModel hrav = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAV);
		IMemspace bin = c.compile(model, SOMFormats.HRBS, SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) bin);
		assertTrue(runner.execute());
	}

	@Test
	void testConditionalJumpOut() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_conditionaljump.hrbs", SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		int labelArd = hras.resolveSymbolToAddress("LABEL");
		int aAdr = hras.resolveSymbolToAddress("ACTUALTARGET");
		int cAdr = hras.resolveSymbolToAddress("CONTLABEL");
		HRAVModel hrav = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAV);
		IMemspace bin = c.compile(model, SOMFormats.HRBS, SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) bin);
		runner.execute();
		bin = runner.getMemspace();
		assertEquals(labelArd, ((ISomMemspace) bin).getNextAddress());// written to label expectesd after exec
	}

	@Test
	void testCopyAdrCompile() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_copy_address.hrbs", SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		HRAVModel hrav = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAV);
		IMemspace bin = c.compile(model, SOMFormats.HRBS, SOMFormats.BIN);
		assertNotNull(hrac);
		assertNotNull(hras);
		assertNotNull(hrav);
		assertNotNull(bin);
	}

	@Test
	@Timeout(10)
	void testCopyAdrExecutePossible() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_copy_address.hrbs", SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		HRAVModel hrav = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAV);
		IMemspace bin = c.compile(model, SOMFormats.HRBS, SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) bin);
		runner.execute();
	}

	@Test
	@Timeout(10)
	void testCopyAdrLabelCopyCmpAfterExec() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_copy_address.hrbs", SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		HRAVModel hrav = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAV);
		IMemspace bin = c.compile(model, SOMFormats.HRBS, SOMFormats.BIN);
		int lblAR = hras.resolveSymbolToAddress("LABEL");
		int copyADr = hras.resolveSymbolToAddress("COPYINHERE");
		int n = hrav.getN();
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) bin);
		runner.execute();
		bin = runner.getMemspace();
		int copyVal = ((ISomMemspace) bin).getBitsUnsigned(copyADr, n);
		assertEquals(lblAR, copyVal);
	}

	@Test
	void testCopyAdrLabelCopyCmpBeforeExec() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_copy_address.hrbs", SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		HRAVModel hrav = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAV);
		IMemspace bin = c.compile(model, SOMFormats.HRBS, SOMFormats.BIN);
		int lblAR = hras.resolveSymbolToAddress("LABEL");
		int copyADr = hras.resolveSymbolToAddress("COPYINHERE");
		int n = hrav.getN();
		int copyVal = ((ISomMemspace) bin).getBitsUnsigned(copyADr, n);
		assertNotEquals(lblAR, copyVal);
	}

	@Test
	void testDerefCompile() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_deref.hrbs", SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		HRAVModel hrav = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAV);
		IMemspace bin = c.compile(model, SOMFormats.HRBS, SOMFormats.BIN);
		assertNotNull(hrac);
		assertNotNull(hras);
		assertNotNull(hrav);
		assertNotNull(bin);
	}

	@Test
	void testDerefFixValueInsertionCompile() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/deref_fix_value_insertion.hrbs", SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		HRAVModel hrav = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAV);
		IMemspace bin = c.compile(model, SOMFormats.HRBS, SOMFormats.BIN);
		assertNotNull(hrac);
		assertNotNull(hras);
		assertNotNull(hrav);
		assertNotNull(bin);
	}

	@Test
	void testDerefFixValueInsertionOutCompile() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/deref_fix_vi_out.hrbs", SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		HRAVModel hrav = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAV);
		IMemspace bin = c.compile(model, SOMFormats.HRBS, SOMFormats.BIN);
		assertNotNull(hrac);
		assertNotNull(hras);
		assertNotNull(hrav);
		assertNotNull(bin);
	}

	@Test
	void testDerefIsDeref() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_deref_global.hrbs", SOMFormats.HRBS);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		int aAdr = 0;
		int bAdr = 0;
		for (Entry<String, AbstractHRASMemoryAddress> entry : hras.getSymbols().entrySet()) {
			String key = entry.getKey();
			AbstractHRASMemoryAddress val = entry.getValue();
			if ("A".equals(key)) {
				aAdr = val.resolve(hras);
			}
			if ("B".equals(key)) {
				bAdr = val.resolve(hras);
			}

		}
		assertNotEquals(aAdr, bAdr);
	}

	@Test
	void testDerefParamCompile() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_df_param.hrbs", SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		HRAVModel hrav = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAV);
		IMemspace bin = c.compile(model, SOMFormats.HRBS, SOMFormats.BIN);
		assertNotNull(hrac);
		assertNotNull(hras);
		assertNotNull(hrav);
		assertNotNull(bin);
	}

	@Test
	void testForDupCompileHBRSAtomic() throws IOException { // see also issue 142 on github and issueTests
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_fd_compile_atomic.hrbs", SOMFormats.HRBS);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		assertEquals(3, hras.getCommandCount()); // 2 from loop, 1 added by hrac compiler
	}

	@Test
	void testForDupCompileHBRSNonAtomic() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_fd_compile_nonatomic.hrbs", SOMFormats.HRBS);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		assertEquals(3, hras.getCommandCount()); // 2 from loop, 1 added by hrac compiler
	}

	@Test
	void testForDupCompileHBRSAtomicOffsetResolve() throws IOException { // also in issuetests as 152
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_fd_compile_atomic.hrbs", SOMFormats.HRBS);
		//HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC); //This problem lies with the precompiler, so enable hrac and hrap (with these 2 lines) for debugging
		//HRACModel hrap = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAP);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		int hrbsStartAddress = hras.resolveSymbolToAddress("HRBS_START");
		int secCmdAddress = hrbsStartAddress + hras.getN() + 1;
		HRASCommand[] cmds = new HRASCommand[2];
		cmds[0] = hras.getCommandAtAddress(hrbsStartAddress);
		cmds[1] = hras.getCommandAtAddress(secCmdAddress);
		int[] cmdTgtAdr = new int[cmds.length];
		for (int i = 0; i < cmds.length; i++) {
			HRASCommand j = cmds[i];
			assertNotNull(j);
			cmdTgtAdr[i] = j.getAddress().resolve(hras);

		}
		assertNotEquals(cmdTgtAdr[0], cmdTgtAdr[1]);
	}

	@Test
	void testForDupCompileHBRSNonAtomicOffsetResolve() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_fd_compile_nonatomic.hrbs", SOMFormats.HRBS);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		int hrbsStartAddress = hras.resolveSymbolToAddress("HRBS_START");
		int secCmdAddress = hrbsStartAddress + hras.getN() + 1;
		HRASCommand[] cmds = new HRASCommand[2];
		cmds[0] = hras.getCommandAtAddress(hrbsStartAddress);
		cmds[1] = hras.getCommandAtAddress(secCmdAddress);
		int[] cmdTgtAdr = new int[cmds.length];
		for (int i = 0; i < cmds.length; i++) {
			HRASCommand j = cmds[i];
			assertNotNull(j);
			cmdTgtAdr[i] = j.getAddress().resolve(hras);

		}
		assertNotEquals(cmdTgtAdr[0], cmdTgtAdr[1]);
	}

	@Test
	void testIfDirectiveAccessCompile() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_if_da.hrbs", SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		HRAVModel hrav = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAV);
		IMemspace bin = c.compile(model, SOMFormats.HRBS, SOMFormats.BIN);
		assertNotNull(hrac);
		assertNotNull(hras);
		assertNotNull(hrav);
		assertNotNull(bin);
	}

	@Test
	void testIfElseCompile() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_ifelse_debug.hrbs", SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		HRAVModel hrav = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAV);
		IMemspace bin = c.compile(model, SOMFormats.HRBS, SOMFormats.BIN);
		assertNotNull(hrac);
		assertNotNull(hras);
		assertNotNull(hrav);
		assertNotNull(bin);
	}

	@Test
	void testIfElseExecute() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_ifelse_debug.hrbs", SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		int n = hras.getN();
		int IFADR = hras.resolveSymbolToAddress("IF");
		int ELSEADR = hras.resolveSymbolToAddress("ELSE");
		int ENDIFADR = hras.resolveSymbolToAddress("ENDIF");
		int IF2ADR = hras.resolveSymbolToAddress("IF2");
		int ELSE2ADR = hras.resolveSymbolToAddress("ELSE2");
		int ENDIF2ADR = hras.resolveSymbolToAddress("ENDIF2");
		System.out.println(String.format("IFADR: %d", IFADR));
		System.out.println(String.format("ELSEADR: %d", ELSEADR));
		System.out.println(String.format("ENDIFADR: %d", ENDIFADR));
		System.out.println(String.format("IF2ADR: %d", IF2ADR));
		System.out.println(String.format("ELSE2ADR: %d", ELSE2ADR));
		System.out.println(String.format("ENDIF2ADR: %d", ENDIF2ADR));
		HRAVModel hrav = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAV);
		IMemspace bin = c.compile(model, SOMFormats.HRBS, SOMFormats.BIN);
		SOMBitcodeRunner runner = new SOMBitcodeRunner(bin);
		final ISomMemspace sbin = runner.getMemspace();
		runner.addDebugPoint(new AbstractUnconditionalDebugPoint("JUMPCATCH") {

			@Override
			public boolean trigger(int cmdAddress, Opcode op, int tgtAddress, ISomMemspace memspace) {
				boolean jump = sbin.isAdrEvalSet();
				if (jump) {
					int tgt = sbin.getNextAddress();
					System.out.println(String.format("jump target: %d", tgt));
				}
				return true;
			}
		});
		runner.execute();
	}

	@Timeout(30)
	@ParameterizedTest
	@MethodSource("testfileFor145Provider")
	void testIssue145NoAtomicChildsLabelGen(String testFile) throws IOException {
		HRBSModel model = f.loadFromFile(testFile, SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		Collection<String> labels = hrac.getAllLabelsRecursive(true);
		assertTrue(labels.contains("LABEL"));
	}

	@Timeout(30)
	@ParameterizedTest
	@MethodSource("testfileFor145Provider")
	void testIssue145NoAtomicChildsLabelGenToHRASSymbol(String testFile) throws IOException {
		HRBSModel model = f.loadFromFile(testFile, SOMFormats.HRBS);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		Map<String, AbstractHRASMemoryAddress> labels = hras.getSymbols();
		assertTrue(labels.containsKey("LABEL"));
	}

	@Test
	void testJumpCompile() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_jump.hrbs", SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		HRAVModel hrav = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAV);
		IMemspace bin = c.compile(model, SOMFormats.HRBS, SOMFormats.BIN);
		assertNotNull(hrac);
		assertNotNull(hras);
		assertNotNull(hrav);
		assertNotNull(bin);
	}

	@Timeout(30)
	@ParameterizedTest
	@MethodSource("testfileForLabelCommandAdditionProvider")
	void testLabelCommandAddition(String testFile, int expectedAtomicCommandGen) throws IOException {
		HRBSModel model = f.loadFromFile(testFile, SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		assertEquals(expectedAtomicCommandGen, hrac.getCommandCountSimple());
	}

	@Timeout(30)
	@ParameterizedTest
	@MethodSource("testfileForNotOverwritingLabelProvider")
	void testLabelNotOverwriteLabelExist(String testFile) throws IOException {
		HRBSModel model = f.loadFromFile(testFile, SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		Collection<String> labels = hrac.getAllLabelsRecursive(true);
		// Label could exist as mirrorsymbol, that would be ok to
		HRACSymbol symbol = hrac.getSymbolByName("LABEL");
		assertTrue(labels.contains("LABEL") || symbol != null);
	}

	@Timeout(30)
	@ParameterizedTest
	@MethodSource("testfileForNotOverwritingLabelProvider")
	void testLabelNotOverwriteLabelExist2(String testFile) throws IOException {
		HRBSModel model = f.loadFromFile(testFile, SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		Collection<String> labels = hrac.getAllLabelsRecursive(true);
		// Label could exist as mirrorsymbol, that would be ok to
		HRACSymbol symbol = hrac.getSymbolByName("OVERWRITING_LABEL");
		assertTrue(labels.contains("OVERWRITING_LABEL") || symbol != null);
	}

	@Test
	void testLblExist() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_lbl_on_rng.hrbs", SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		boolean exists = false;
		try {
			hras.resolveSymbolToAddress("LBL");
			exists = true;
		} catch (Exception e) {

		}
		assertTrue(exists);
	}

	@Test
	void testLblExistSimple() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_lbl.hrbs", SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		boolean exists = false;
		try {
			hras.resolveSymbolToAddress("LBL");
			exists = true;
		} catch (Exception e) {

		}
		assertTrue(exists);
	}

	@Test
	void testLblOnRngCompile() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_lbl_on_rng.hrbs", SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		HRAVModel hrav = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAV);
		IMemspace bin = c.compile(model, SOMFormats.HRBS, SOMFormats.BIN);
		assertNotNull(bin);
	}

	@Test
	void testLblOnRngGenOnce() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_lbl_on_rng.hrbs", SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		int cnt = 0;
		for (String entry : hras.getSymbols().keySet()) {
			if (entry.equals("LBL")) {
				cnt++;
			}

		}
		assertEquals(1, cnt);
	}

	@Test
	void testMSOfsCompile() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_ms_ofs.hrbs", SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		HRAVModel hrav = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAV);
		IMemspace bin = c.compile(model, SOMFormats.HRBS, SOMFormats.BIN);
		assertNotNull(hrac);
		assertNotNull(hras);
		assertNotNull(hrav);
		assertNotNull(bin);
	}

	@Test
	void testNAllocEnoughAllocated() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_alloc_n_passdown.hrbs", SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		HRAVModel hrav = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAV);
		IMemspace bin = c.compile(model, SOMFormats.HRBS, SOMFormats.BIN);
		int aDR = hras.resolveSymbolToAddress("A");
		int bAdr = hras.resolveSymbolToAddress("B");
		assertEquals(hrav.getN(), bAdr - aDR);
	}

	@Test
	void testNAllocPassdownMArk() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_alloc_n_passdown.hrbs", SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		HRAVModel hrav = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAV);
		IMemspace bin = c.compile(model, SOMFormats.HRBS, SOMFormats.BIN);
		List<HRBSSymbol> hrbsS = model.getSymbols();
		List<HRACSymbol> hracS = hrac.getSymbols();
		HRBSSymbol hrbsSA = null;
		HRACSymbol hracSA = null;
		for (HRACSymbol hracSymbol : hracS) {
			if (hracSymbol.getName().equals("A")) {
				hracSA = hracSymbol;
			}
		}
		for (HRBSSymbol hracSymbol : hrbsS) {
			if (hracSymbol.getName().equals("A")) {
				hrbsSA = hracSymbol;
			}
		}
		assertEquals(hrbsSA.getBitCnt() instanceof HRBSDirectiveNode, hracSA.getBitCnt() instanceof HRACDirectiveNode);
	}

	@Test
	void testNAllocPassdownName() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_alloc_n_passdown.hrbs", SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		HRAVModel hrav = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAV);
		IMemspace bin = c.compile(model, SOMFormats.HRBS, SOMFormats.BIN);
		List<HRBSSymbol> hrbsS = model.getSymbols();
		List<HRACSymbol> hracS = hrac.getSymbols();
		HRBSSymbol hrbsSA = null;
		HRACSymbol hracSA = null;
		for (HRACSymbol hracSymbol : hracS) {
			if (hracSymbol.getName().equals("A")) {
				hracSA = hracSymbol;
			}
		}
		for (HRBSSymbol hracSymbol : hrbsS) {
			if (hracSymbol.getName().equals("A")) {
				hrbsSA = hracSymbol;
			}
		}
		assertEquals(((HRBSDirectiveNode) hrbsSA.getBitCnt()).getDirectiveName(),
				((HRACDirectiveNode) hracSA.getBitCnt()).getDirectiveName());
	}

	@Test
	void testExpressionTreePassdownDirectives() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/features/expression-tree/test_et_correct_compile.hrbs",
				SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		List<HRACSymbol> hracS = hrac.getSymbols();
		List<HRACForDup> hracC = hrac.getCommands();
		HRACSymbol hracSA = null;
		for (HRACSymbol hracSymbol : hracS) {
			if (hracSymbol.getName().equals("A")) {
				hracSA = hracSymbol;
			}
		}
		HRACForDup hrbsS = hracC.get(0);
		HRACAbstractDirectiveExpressionTreeNode address = ((FixedHRACMemoryAddress) hracSA.getTargetSymbol()).getAddress();
		assertTrue(address instanceof HRACMultiplicationExpressionNode);
		HRACMultiplicationExpressionNode multiNode = (HRACMultiplicationExpressionNode) address;
		assertTrue(multiNode.getChilds()[0] instanceof HRACDirectiveNode);
		assertTrue(multiNode.getChilds()[1] instanceof HRACDirectiveNode);

		AbstractHRACMemoryAddress cmdT = hrbsS.getCmd().getTarget();
		address = ((FixedHRACMemoryAddress) cmdT).getAddress();
		assertTrue(address instanceof HRACMultiplicationExpressionNode);
		multiNode = (HRACMultiplicationExpressionNode) address;
		assertTrue(multiNode.getChilds()[0] instanceof HRACDirectiveNode);
		assertTrue(multiNode.getChilds()[1] instanceof HRACDirectiveNode);
	}

	@Test
	void testExpressionTreePassdown() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/features/expression-tree/test_et_correct_compile.hrbs",
				SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		List<HRACSymbol> hracS = hrac.getSymbols();
		List<HRACForDup> hracC = hrac.getCommands();
		HRACSymbol hracSA = null;
		for (HRACSymbol hracSymbol : hracS) {
			if (hracSymbol.getName().equals("B")) {
				hracSA = hracSymbol;
			}
		}
		HRACForDup hrbsS = hracC.get(1);
		HRACAbstractDirectiveExpressionTreeNode address = ((FixedHRACMemoryAddress) hracSA.getTargetSymbol()).getAddress();
		assertTrue(address instanceof HRACMultiplicationExpressionNode);
		HRACMultiplicationExpressionNode multiNode = (HRACMultiplicationExpressionNode) address;
		assertTrue(multiNode.getChilds()[0] instanceof HRACIntegerNode);
		assertTrue(multiNode.getChilds()[1] instanceof HRACIntegerNode);

		AbstractHRACMemoryAddress cmdT = hrbsS.getCmd().getTarget();
		address = ((FixedHRACMemoryAddress) cmdT).getAddress();
		assertTrue(address instanceof HRACMultiplicationExpressionNode);
		multiNode = (HRACMultiplicationExpressionNode) address;
		assertTrue(multiNode.getChilds()[0] instanceof HRACIntegerNode);
		assertTrue(multiNode.getChilds()[1] instanceof HRACIntegerNode);
	}

	@Test
	void testNAllocPassdownParse() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_alloc_n_passdown.hrbs", SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		HRAVModel hrav = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAV);
		IMemspace bin = c.compile(model, SOMFormats.HRBS, SOMFormats.BIN);
		List<HRBSSymbol> hrbsS = model.getSymbols();
		HRBSSymbol hrbsSA = null;
		for (HRBSSymbol hracSymbol : hrbsS) {
			if (hracSymbol.getName().equals("A")) {
				hrbsSA = hracSymbol;
			}
		}
		HRBSAbstractExpressionNode bitcnt = hrbsSA.getBitCnt();
		assertEquals("N", ((HRBSDirectiveNode) bitcnt).getDirectiveName());
	}

	@Test
	void testNAllocPassdownParseIsRecognized() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_alloc_n_passdown.hrbs", SOMFormats.HRBS);
		List<HRBSSymbol> hrbsS = model.getSymbols();
		HRBSSymbol hrbsSA = null;
		for (HRBSSymbol hracSymbol : hrbsS) {
			if (hracSymbol.getName().equals("A")) {
				hrbsSA = hracSymbol;
			}
		}
		assertTrue(hrbsSA.getBitCnt() instanceof HRBSDirectiveNode);
	}
}
