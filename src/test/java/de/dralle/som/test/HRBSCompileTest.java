package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import de.dralle.som.AbstractUnconditionalDebugPoint;
import de.dralle.som.Compiler;
import de.dralle.som.FileLoader;
import de.dralle.som.IMemspace;
import de.dralle.som.ISomMemspace;
import de.dralle.som.Opcode;
import de.dralle.som.SOMBitcodeRunner;
import de.dralle.som.SOMFormats;
import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hrac.model.HRACSymbol;
import de.dralle.som.languages.hras.model.HRASModel;
import de.dralle.som.languages.hrav.model.HRAVModel;
import de.dralle.som.languages.hrbs.model.HRBSModel;
import de.dralle.som.languages.hrbs.model.HRBSSymbol;

class HRBSCompileTest {

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
	void testLblOnRngCompile() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_lbl_on_rng.hrbs", SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		HRAVModel hrav = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAV);
		IMemspace bin = c.compile(model, SOMFormats.HRBS, SOMFormats.BIN);
		assertNotNull(bin);
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
		assertEquals(hrbsSA.isBitCntISSpecial() != null, hracSA.isBitCntSpecial());
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
		assertEquals("N", hrbsSA.isBitCntISSpecial());
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
		assertTrue(hrbsSA.isBitCntISSpecial() != null);
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
		assertEquals(hrbsSA.isBitCntISSpecial(), hracSA.getSpecialName());
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
}
