package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.HashMap;

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
import de.dralle.som.languages.hrac.model.HRACForDup;
import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hrac.model.HRACSymbol;
import de.dralle.som.languages.hrac.model.NamedHRACMemoryAddress;
import de.dralle.som.languages.hras.model.HRASModel;

class HRACCompileTest {

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
	void testSimpleFforDup() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_for_simple.hrac", SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRAC, SOMFormats.HRAS);
		assertEquals(1 + 6 + 1, hras.getCommandCount());// 1 added by compiler, 6 in loop (upper and lower are included)
														// and 1 at the end
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
			assertEquals(curCommand.getCmd().getTarget().getOffset() + 1, nxtCommand.getCmd().getTarget().getOffset());
		}

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

	private int getSum(int n) {
		if (n == 0) {
			return 0;

		}
		return n + getSum(n - 1);
	}

	@Test
	void testFDSymbolGen() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_fd_smbol_gen.hrac", SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRAC, SOMFormats.HRAS);
		IMemspace m = c.compile(model, SOMFormats.HRAC, SOMFormats.BIN);
		assertNotNull(m);
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
					if(s1CmdTgt instanceof NamedHRACMemoryAddress&&s2CmdTgt instanceof NamedHRACMemoryAddress) {
						assertNotEquals(((NamedHRACMemoryAddress)s1CmdTgt).getOffsetSpecialnName(),((NamedHRACMemoryAddress)s2CmdTgt).getName());
					eval++;
					}else {
						//fail
						assertTrue(false);
					}
					
				}
			}
		}
		assertTrue(eval > 0);
	}

	@Test
	void testFDCorrectAllocNumm() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_fd_smbol_gen2.hrac", SOMFormats.HRAC);
		HRASModel m = c.compile(model, SOMFormats.HRAC, SOMFormats.HRAS);
		assertEquals(11 + 5, m.getSymbolCount());
	}// 11 (8builtins+2 start markers+1 heap marker) symbols should be added by
		// compiler, 5 from input

	@Test
	void testFDIndependentAlloc() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_fd_smbol_gen2.hrac", SOMFormats.HRAC);
		IMemspace m = c.compile(model, SOMFormats.HRAC, SOMFormats.BIN);
		assertNotNull(new SOMBitcodeRunner((ISomMemspace) m).execute());
	}

	@Test
	void testNReplAlloc() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_n_repl_alloc.hrac", SOMFormats.HRAC);
		model.precompile("", null, true);
		assertEquals(model.getN(), model.getSymbolByName("A").getBitCnt());
	}

	@Test
	void testNReplForD() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_n_repl_fordup.hrac", SOMFormats.HRAC);
		model.precompile("", null, true);
		assertEquals(model.getN(), model.getCommandCount(model.getN()));
	}

	@Test
	void testNCorrectCalcNotPrec() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_n_repl_fordup.hrac", SOMFormats.HRAC);
		int nBeforePrec = model.getN();
		model.precompile("", null, true);
		int nAfterPrec = model.getN();
		assertEquals(nAfterPrec, nBeforePrec);
	}
}
