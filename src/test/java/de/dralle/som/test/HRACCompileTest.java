package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.dralle.som.Compiler;
import de.dralle.som.FileLoader;
import de.dralle.som.IMemspace;
import de.dralle.som.ISomMemspace;
import de.dralle.som.SOMBitcodeRunner;
import de.dralle.som.SOMFormats;
import de.dralle.som.languages.hrac.model.AbstractHRACMemoryAddress;
import de.dralle.som.languages.hrac.model.FixedHRACMemoryAddress;
import de.dralle.som.languages.hrac.model.HRACForDup;
import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hrac.model.HRACSymbol;
import de.dralle.som.languages.hrac.model.NamedHRACMemoryAddress;
import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACDirectiveNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACIntegerNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACMultiplicationExpressionNode;
import de.dralle.som.languages.hras.model.AbstractHRASMemoryAddress;
import de.dralle.som.languages.hras.model.ExpressionHRASMemoryAddress;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hras.model.HRASCommand;
import de.dralle.som.languages.hras.model.HRASIntegerNode;
import de.dralle.som.languages.hras.model.HRASModel;
import de.dralle.som.languages.hras.model.HRASMultiplicationExpression;
import de.dralle.som.languages.hrav.model.HRAVModel;
import de.dralle.som.languages.hrbs.model.HRBSModel;

class HRACCompileTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	private Compiler c;

	private FileLoader f;

	private int getSum(int n) {
		if (n == 0) {
			return 0;

		}
		return n + getSum(n - 1);
	}

	@BeforeEach
	void setUp() throws Exception {
		c = new Compiler();
		f = new FileLoader();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testCompileFixedAdressOnCommand() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_fixed_adr_on_command.hrac", SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRAC, SOMFormats.HRAS);
		IMemspace m = c.compile(model, SOMFormats.HRAC, SOMFormats.BIN);
		assertNotNull(m);
	}

	@Test
	void testCompileFixedAdressOnMirrorSymbol() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_fixed_adr_on_mirror_symbol.hrac", SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRAC, SOMFormats.HRAS);
		IMemspace m = c.compile(model, SOMFormats.HRAC, SOMFormats.BIN);
		assertNotNull(m);
	}

	@Test
	void testFDCorrectAllocNum() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_fd_smbol_gen2.hrac", SOMFormats.HRAC);
		HRASModel m = c.compile(model, SOMFormats.HRAC, SOMFormats.HRAS);
		assertEquals(1 + 3 + 5 + 1, m.getSymbolCount());
	}// 3+1 (2 start markers+1 heap marker+ADR_EVAL for now) symbols should be added
		// by
		// compiler, 5 from input, 1 from input retaining original symbol name

	@Test
	void testFDIndependentAlloc() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_fd_smbol_gen2.hrac", SOMFormats.HRAC);
		IMemspace m = c.compile(model, SOMFormats.HRAC, SOMFormats.BIN);
		assertNotNull(new SOMBitcodeRunner((ISomMemspace) m).execute());
	}

	@Test
	void testFDSymbolGen() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_fd_smbol_gen.hrac", SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRAC, SOMFormats.HRAS);
		IMemspace m = c.compile(model, SOMFormats.HRAC, SOMFormats.BIN);
		assertNotNull(m);
	}

	@Test
	void testFDSymbolGenNested() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_fd_smbol_gen_nested_rep5.hrac", SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRAC, SOMFormats.HRAS);
		IMemspace m = c.compile(model, SOMFormats.HRAC, SOMFormats.BIN);
		assertNotNull(m);
	}
	@Test
	void testFDSymbolGenNestedRefCorrectLevelR5() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_fd_smbol_gen_nested_rep5.hrac", SOMFormats.HRAC);
		HRACModel hrap=c.compile(model, SOMFormats.HRAC, SOMFormats.HRAP);
		HRASModel hras = c.compile(model, SOMFormats.HRAC, SOMFormats.HRAS);
		HRAVModel hrav=c.compile(model, SOMFormats.HRAC, SOMFormats.HRAV);
		int a0=hras.resolveSymbolToAddress("A0");
		int a1=hras.resolveSymbolToAddress("A1");
		int a2=hras.resolveSymbolToAddress("A2");
		int l0=hras.resolveSymbolToAddress("L0");
		int l1=hras.resolveSymbolToAddress("L1");
		int l2=hras.resolveSymbolToAddress("L2");
		HRASCommand l0c=hras.getCommandAtAddress(l0);
		HRASCommand l1c=hras.getCommandAtAddress(l1);
		HRASCommand l2c=hras.getCommandAtAddress(l2);
		assertNotNull(l0c);
		assertNotNull(l1c);
		assertNotNull(l2c);
		assertEquals(a0, l0c.getAddress().resolve(hras));
		assertEquals(a1, l1c.getAddress().resolve(hras));
		assertEquals(a2, l2c.getAddress().resolve(hras));
	}
	@Test
	void testFDSymbolGenNestedRefCorrectLevelR1() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_fd_smbol_gen_nested_rep1.hrac", SOMFormats.HRAC);
		HRACModel hrap=c.compile(model, SOMFormats.HRAC, SOMFormats.HRAP);
		HRASModel hras = c.compile(model, SOMFormats.HRAC, SOMFormats.HRAS);
		HRAVModel hrav=c.compile(model, SOMFormats.HRAC, SOMFormats.HRAV);
		int a0=hras.resolveSymbolToAddress("A0");
		int a1=hras.resolveSymbolToAddress("A1");
		int a2=hras.resolveSymbolToAddress("A2");
		int l0=hras.resolveSymbolToAddress("L0");
		int l1=hras.resolveSymbolToAddress("L1");
		int l2=hras.resolveSymbolToAddress("L2");
		HRASCommand l0c=hras.getCommandAtAddress(l0);
		HRASCommand l1c=hras.getCommandAtAddress(l1);
		HRASCommand l2c=hras.getCommandAtAddress(l2);
		assertNotNull(l0c);
		assertNotNull(l1c);
		assertNotNull(l2c);
		assertEquals(a0, l0c.getAddress().resolve(hras));
		assertEquals(a1, l1c.getAddress().resolve(hras));
		assertEquals(a2, l2c.getAddress().resolve(hras));
	}
	@Test
	void testBLKSymbolGenNestedRefCorrectLevel() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_blk_smbol_gen_nested.hrac", SOMFormats.HRAC);
		HRACModel hrap=c.compile(model, SOMFormats.HRAC, SOMFormats.HRAP);
		HRASModel hras = c.compile(model, SOMFormats.HRAC, SOMFormats.HRAS);
		HRAVModel hrav=c.compile(model, SOMFormats.HRAC, SOMFormats.HRAV);
		int a0=hras.resolveSymbolToAddress("A0");
		int a1=hras.resolveSymbolToAddress("A1");
		int a2=hras.resolveSymbolToAddress("A2");
		int l0=hras.resolveSymbolToAddress("L0");
		int l1=hras.resolveSymbolToAddress("L1");
		int l2=hras.resolveSymbolToAddress("L2");
		HRASCommand l0c=hras.getCommandAtAddress(l0);
		HRASCommand l1c=hras.getCommandAtAddress(l1);
		HRASCommand l2c=hras.getCommandAtAddress(l2);
		assertNotNull(l0c);
		assertNotNull(l1c);
		assertNotNull(l2c);
		assertEquals(a0, l0c.getAddress().resolve(hras));
		assertEquals(a1, l1c.getAddress().resolve(hras));
		assertEquals(a2, l2c.getAddress().resolve(hras));
	}
	@Test
	void testBLKSymbolGenOp() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_blk_smbol_gen_op.hrac", SOMFormats.HRAC);
		HRACModel hrap=c.compile(model, SOMFormats.HRAC, SOMFormats.HRAP);
		HRASModel hras = c.compile(model, SOMFormats.HRAC, SOMFormats.HRAS);
		HRAVModel hrav=c.compile(model, SOMFormats.HRAC, SOMFormats.HRAV);
		int a0=hras.resolveSymbolToAddress("A0");
		int a1=hras.resolveSymbolToAddress("A1");
		int l0=hras.resolveSymbolToAddress("L0");
		int l1=hras.resolveSymbolToAddress("L1");
		HRASCommand l0c=hras.getCommandAtAddress(l0);
		HRASCommand l1c=hras.getCommandAtAddress(l1);
		assertNotNull(l0c);
		assertNotNull(l1c);
		assertEquals(a1, l0c.getAddress().resolve(hras));
		assertEquals(a1, l1c.getAddress().resolve(hras));
	}
	@Test
	void testBLKSymbolGenNotOp() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_blk_smbol_gen_notop.hrac", SOMFormats.HRAC);
		HRACModel hrap=c.compile(model, SOMFormats.HRAC, SOMFormats.HRAP);
		HRASModel hras = c.compile(model, SOMFormats.HRAC, SOMFormats.HRAS);
		HRAVModel hrav=c.compile(model, SOMFormats.HRAC, SOMFormats.HRAV);
		int a0=hras.resolveSymbolToAddress("A0");
		int a1=hras.resolveSymbolToAddress("A1");
		int l0=hras.resolveSymbolToAddress("L0");
		int l1=hras.resolveSymbolToAddress("L1");
		HRASCommand l0c=hras.getCommandAtAddress(l0);
		HRASCommand l1c=hras.getCommandAtAddress(l1);
		assertNotNull(l0c);
		assertNotNull(l1c);
		assertEquals(a0, l0c.getAddress().resolve(hras));
		assertEquals(a1, l1c.getAddress().resolve(hras));
	}
	@Test
	void testFDSymbolGenPrecompiledNaming() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_fd_smbol_gen.hrac", SOMFormats.HRAC);
		model.precompile("", null, true);
		assertEquals(5, model.getCommands().size());
		int eval = 0;
		for (HRACForDup s1 : model.getCommands()) {
			for (HRACForDup s2 : model.getCommands()) {
				if (s1 != s2) {
					AbstractHRACMemoryAddress s1CmdTgt = s1.getCmd().getTarget();
					AbstractHRACMemoryAddress s2CmdTgt = s2.getCmd().getTarget();
					if (s1CmdTgt instanceof NamedHRACMemoryAddress && s2CmdTgt instanceof NamedHRACMemoryAddress) {
						HRACAbstractExpressionNode s1CmdTgTOfs = null;
						s1CmdTgTOfs = s1CmdTgt.getOffset();
						assertNotEquals(s1CmdTgTOfs, ((NamedHRACMemoryAddress) s2CmdTgt).getName());
						eval++;
					} else {
						// fail
						assertTrue(false);
					}

				}
			}
		}
		assertTrue(eval > 0);
	}

	@Test
	void testExpressionTreePassdownDirectivesResolveHRAC2HRAP() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/features/expression-tree/test_et_correct_compile.hrac",
				SOMFormats.HRAC);
		HRACModel hrac = c.compile(model, SOMFormats.HRAC, SOMFormats.HRAP);
		List<HRACSymbol> hracS = hrac.getSymbols();
		List<HRACForDup> hracC = hrac.getCommands();
		HRACSymbol hracSA = null;
		for (HRACSymbol hracSymbol : hracS) {
			if (hracSymbol.getName().equals("A")) {
				hracSA = hracSymbol;
			}
		}
		HRACForDup hrbsS = hracC.get(0);
		HRACAbstractExpressionNode address = ((FixedHRACMemoryAddress) hracSA.getTargetSymbol()).getAddress();
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
	void testExpressionTreePassdownHRAP2HRAS() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/features/expression-tree/test_et_correct_compile.hrap",
				SOMFormats.HRAP);
		HRASModel hrac = c.compile(model, SOMFormats.HRAP, SOMFormats.HRAS);
		Map<String, AbstractHRASMemoryAddress> hracS = hrac.getSymbols();
		Map<AbstractHRASMemoryAddress, HRASCommand> hracC = hrac.getCommands();
		AbstractHRASMemoryAddress hracSA = hracS.get("B");
		assertNotNull(hracSA);
		assertInstanceOf(ExpressionHRASMemoryAddress.class, hracSA);
		ExpressionHRASMemoryAddress ma = (ExpressionHRASMemoryAddress) hracSA;
		HRASAbstractExpressionNode ex = ma.getExpression();
		assertTrue(ex instanceof HRASMultiplicationExpression);
		HRASMultiplicationExpression multiNode = (HRASMultiplicationExpression) ex;
		assertTrue(multiNode.getChilds()[0] instanceof HRASIntegerNode);
		assertTrue(multiNode.getChilds()[1] instanceof HRASIntegerNode);

		HRASCommand compiledCmd = null;
		for (Entry<AbstractHRASMemoryAddress, HRASCommand> entry : hracC.entrySet()) {
			AbstractHRASMemoryAddress key = entry.getKey();
			HRASCommand val = entry.getValue();
			if (val.getAddress() instanceof ExpressionHRASMemoryAddress) {
				compiledCmd = val;
			}

		}
		assertNotNull(compiledCmd);
		AbstractHRASMemoryAddress address = compiledCmd.getAddress();
		assertInstanceOf(ExpressionHRASMemoryAddress.class, address);
		ma = (ExpressionHRASMemoryAddress) address;
		ex = ma.getExpression();
		assertTrue(ex instanceof HRASMultiplicationExpression);
		multiNode = (HRASMultiplicationExpression) ex;
		assertTrue(multiNode.getChilds()[0] instanceof HRASIntegerNode);
		assertTrue(multiNode.getChilds()[1] instanceof HRASIntegerNode);
	}

	@Test
	void testExpressionTreePassdownHRAC2HRAP() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/features/expression-tree/test_et_correct_compile.hrac",
				SOMFormats.HRAC);
		HRACModel hrac = c.compile(model, SOMFormats.HRAC, SOMFormats.HRAP);
		List<HRACSymbol> hracS = hrac.getSymbols();
		List<HRACForDup> hracC = hrac.getCommands();
		HRACSymbol hracSA = null;
		for (HRACSymbol hracSymbol : hracS) {
			if (hracSymbol.getName().equals("B")) {
				hracSA = hracSymbol;
			}
		}
		HRACForDup hrbsS = hracC.get(1);
		HRACAbstractExpressionNode address = ((FixedHRACMemoryAddress) hracSA.getTargetSymbol()).getAddress();
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
	void testNCorrectCalcNotPrec() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_n_repl_fordup.hrac", SOMFormats.HRAC);
		int nBeforePrec = model.getN();
		model.precompile("", null, true);
		int nAfterPrec = model.getN();
		assertEquals(nAfterPrec, nBeforePrec);
	}

	@Test
	void testNestedRunningForDup() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_for_running_nested.hrac", SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRAC, SOMFormats.HRAS);
		assertEquals(1 + hras.getN() + getSum(hras.getN()), hras.getCommandCount());// 1 added by compiler, N in loop
																					// (upper and lower are included)
																					// and 2 at the end
	}

	@Test
	void testNestedRunningForDupPrecompiler() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_for_running_nested.hrac", SOMFormats.HRAC);
		HRACModel prec = model.clone();
		prec.precompile("", new HashMap<>(), true);
		System.out.println(prec);
	}

	@Test
	void testNReplAlloc() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_n_repl_alloc.hrac", SOMFormats.HRAC);
		model.precompile("", null, true);
		assertEquals(model.getN(), model.getSymbolByName("A").getBitCntAsInt(model));
	}

	@Test
	void testNReplForD() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_n_repl_fordup.hrac", SOMFormats.HRAC);
		model.precompile("", null, true);
		assertEquals(model.getN(), model.getCommandCount(model.getN()));
	}

	@Test
	void testRunningForDup() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_for_running.hrac", SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRAC, SOMFormats.HRAS);
		assertEquals(1 + hras.getN() + 2, hras.getCommandCount());// 1 added by compiler, N in loop (upper and lower are
																	// included) and 2 at the end
	}

	@Test
	void testRunningForDupRunningVarRepl() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_for_running_var_repl.hrac", SOMFormats.HRAC);
		model.precompile("", null, true);
		// command suffixes should increase by 1 each time
		for (int i = 0; i < 4; i++) {
			HRACForDup curCommand = model.getCommands().get(i);
			HRACForDup nxtCommand = model.getCommands().get(i + 1);
			assertEquals(
					curCommand.getCmd().getTarget().getOffset().getResolvedExpressionTree(model)
							.calculateNumericalValue() + 1,
					nxtCommand.getCmd().getTarget().getOffset().getResolvedExpressionTree(model)
							.calculateNumericalValue());
		}

	}

	@Test
	void testSimpleFforDup() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_for_simple.hrac", SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRAC, SOMFormats.HRAS);
		assertEquals(1 + 6 + 1, hras.getCommandCount());// 1 added by compiler, 6 in loop (upper and lower are included)
														// and 1 at the end
	}
}
