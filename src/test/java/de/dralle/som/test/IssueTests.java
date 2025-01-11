package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BooleanSupplier;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import de.dralle.som.Compiler;
import de.dralle.som.FileLoader;
import de.dralle.som.IMemspace;
import de.dralle.som.Opcode;
import de.dralle.som.SOMFormats;
import de.dralle.som.languages.hrac.HRACParser;
import de.dralle.som.languages.hrac.model.HRACCommand;
import de.dralle.som.languages.hrac.model.HRACForDup;
import de.dralle.som.languages.hrac.model.HRACForDupBoundingRangeProvider;
import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hrac.model.HRACSymbol;
import de.dralle.som.languages.hrac.model.NamedHRACMemoryAddress;
import de.dralle.som.languages.hras.model.AbstractHRASMemoryAddress;
import de.dralle.som.languages.hras.model.HRASCommand;
import de.dralle.som.languages.hras.model.SymbolHRASMemoryAddress;
import de.dralle.som.languages.hras.model.HRASModel;
import de.dralle.som.languages.hrav.model.HRAVModel;
import de.dralle.som.languages.hrbs.model.HRBSBoundsRange;
import de.dralle.som.languages.hrbs.model.HRBSModel;

class IssueTests {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
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
	void testIssue62_HRAPCompilation() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_for_simple.hrac", SOMFormats.HRAC);
		HRACModel hrap = c.compile(model, SOMFormats.HRAC, SOMFormats.HRAP);
		assertNotNull(hrap);
	}

	@Test
	void testIssue62_HRAPCompilationIsPrecompiled() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_for_simple.hrac", SOMFormats.HRAC);
		HRACModel hrap = c.compile(model, SOMFormats.HRAC, SOMFormats.HRAP);
		List<HRACForDup> cs = hrap.getCommands();
		boolean pc = true;
		for (HRACForDup hracForDup : cs) {
			if (hracForDup.getCmd() == null) {
				pc = false;
			}
		}
		assertTrue(pc);
	}

	@Test
	void testIssue62_HRAPCompilationOriginalModelNeedsPrecompilation() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_for_simple.hrac", SOMFormats.HRAC);
		List<HRACForDup> cs = model.getCommands();
		boolean pc = true;
		for (HRACForDup hracForDup : cs) {
			if (hracForDup.getCmd() == null) {
				pc = false;
			}
		}
		assertFalse(pc);
	}

	@Test
	void testIssue85_HRACCompileToHRAS() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_directive_use_in_offsets.hrac", SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRAC, SOMFormats.HRAS);
		assertNotNull(hras);
	}

	@Test
	void testIssue85_HRACCompileAllocDirectiveReplace() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_directive_use_in_offsets.hrac", SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRAC, SOMFormats.HRAS);
		Map<String, AbstractHRASMemoryAddress> symbols = hras.getSymbols();
		boolean repl = false;
		int aAdr = 0;
		int cAdr = 0;
		for (Entry<String, AbstractHRASMemoryAddress> iterable_element : symbols.entrySet()) {
			if (iterable_element.getKey().equals("A")) {
				aAdr = Integer.parseInt(((SymbolHRASMemoryAddress) iterable_element.getValue()).getSymbol());
			}
			if (iterable_element.getKey().equals("C")) {
				cAdr = Integer.parseInt(((SymbolHRASMemoryAddress) iterable_element.getValue()).getSymbol());
			}
		}
		repl = cAdr - aAdr == 5;
		assertTrue(repl);
	}

	@Test
	void testIssue85_HRACCompileMSDirectiveReplace() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_directive_use_in_offsets.hrac", SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRAC, SOMFormats.HRAS);
		Map<String, AbstractHRASMemoryAddress> symbols = hras.getSymbols();
		boolean repl = false;
		for (Entry<String, AbstractHRASMemoryAddress> iterable_element : symbols.entrySet()) {
			if (iterable_element.getKey().equals("B")) {
				repl = iterable_element.getValue().getAddressOffset().equals(5);
			}
		}
		assertTrue(repl);
	}

	@Test
	void testIssue85_HRACCompileOffsetAfCommandDirectiveReplace() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_directive_use_in_offsets.hrac", SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRAC, SOMFormats.HRAS);
		Map<AbstractHRASMemoryAddress, HRASCommand> symbols = hras.getCommands();
		boolean repl = false;
		for (Entry<AbstractHRASMemoryAddress, HRASCommand> iterable_element : symbols.entrySet()) {
			if (iterable_element.getValue().getOp().equals(Opcode.NAR)
					&& ((SymbolHRASMemoryAddress) iterable_element.getValue().getAddress()).getSymbol().equals("B")) {
				repl = iterable_element.getValue().getAddress().getAddressOffset().equals(5);
			}
		}
		assertTrue(repl);
	}

	@Test
	void testIssue89_DerefLabelGenLocStandardCommands() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_issue89_deref_label_gen_loc_sc.hrbs",
				SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		List<HRACForDup> coms = hrac.getCommands();
		assertEquals("HRBS_START", coms.get(0).getCmd().getLabel().getName());
	}

	static Stream<String> issue90FileNameProvider() {
		return Stream.of("test/fixtures/hrbs/test_issue90_duplicate_deref_mixed.hrbs",
				"test/fixtures/hrbs/test_issue90_duplicate_deref_on_command.hrbs",
				"test/fixtures/hrbs/test_issue90_duplicate_deref.hrbs",
				"test/fixtures/hrbs/test_issue90_duplicate_symbol.hrbs");
	}

	@Test
	void testIssue89_DerefLabelGenLocChildCommands() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_issue89_deref_label_gen_loc_cc.hrbs",
				SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		List<HRACForDup> coms = hrac.getCommands();
		assertEquals("HRBS_START", coms.get(0).getCmd().getLabel().getName());
	}

	@ParameterizedTest
	@MethodSource("issue90FileNameProvider")
	void testIssue90_NoDuplicateSymbolsInHRAC(String filename) throws IOException {
		HRBSModel model = f.loadFromFile(filename, SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		List<HRACSymbol> coms = hrac.getSymbols();
		int duplicates = 0;
		for (HRACSymbol hracSymbol : coms) {
			for (HRACSymbol hracSymbol2 : coms) {
				if (hracSymbol != hracSymbol2 && hracSymbol.getName().equals(hracSymbol2.getName())) {
					duplicates++;
				}
			}
		}
		assertEquals(0, duplicates);
	}

	@ParameterizedTest
	@MethodSource("issue90FileNameProvider")
	void testIssue90_NoDuplicateSymbolsInHRAS(String filename) throws IOException {
		HRBSModel model = f.loadFromFile(filename, SOMFormats.HRBS);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		Map<String, AbstractHRASMemoryAddress> coms = hras.getSymbols();
		int duplicates = 0;
		for (int i = 0; i < coms.keySet().size(); i++) {
			String s1 = new ArrayList<String>(coms.keySet()).get(i);
			for (int j = 0; j < coms.keySet().size(); j++) {
				String s2 = new ArrayList<String>(coms.keySet()).get(j);
				if (s1.equals(s2) && i != j) {
					duplicates++;
				}
			}
		}
		assertEquals(0, duplicates);
	}

	@Test
	void testIssue94() throws IOException {
		// Replicates test
		// FormatHRACFileWriteTest.testCompileFromModelOutputContentEqual for file
		// test/fixtures/hrac/test_directive_use_in_offsets.hrac
		HRACModel hc0 = f.loadFromFile("test/fixtures/hrac/test_directive_use_in_offsets.hrac", SOMFormats.HRAC);

		String hracCode = hc0.asCode();
		HRACParser p = new HRACParser();
		HRACModel hc1 = p.parse(hracCode);
		HRACModel hp0 = c.compile(hc0, SOMFormats.HRAC, SOMFormats.HRAP);
		HRACModel hp1 = c.compile(hc1, SOMFormats.HRAC, SOMFormats.HRAP);
		HRASModel hs0 = c.compile(hc0, SOMFormats.HRAC, SOMFormats.HRAS);
		HRASModel hs1 = c.compile(hc1, SOMFormats.HRAC, SOMFormats.HRAS);
		HRAVModel hv0 = c.compile(hc0, SOMFormats.HRAC, SOMFormats.HRAV);
		HRAVModel hv1 = c.compile(hc1, SOMFormats.HRAC, SOMFormats.HRAV);
		IMemspace nm = c.compile(hc0, SOMFormats.HRAC, SOMFormats.BIN);
		IMemspace nm2 = c.compile(hc1, SOMFormats.HRAC, SOMFormats.BIN);	
		assertTrue(nm.equalContent(nm2));
	}
	
	@Test
	void testIssue103_setonceHRAVEmpty() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/issue/test103.hrbs",
				SOMFormats.HRBS);
		HRAVModel hrav = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAV);
		String hravCode = hrav.asCode();
		Pattern regex = Pattern.compile("setonce \\d+"); //search for setonce with a number
		assertTrue(regex.matcher(hravCode).find());
	}
	@Test
	void testIssue134_HRACtoString() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_for_running_var_repl.hrac", SOMFormats.HRAC);
		String stringRep = model.toString();
		assertNotNull(stringRep);

	}
	@Test
	void testIssue134_HRACtoString2() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_for_running_nested.hrac", SOMFormats.HRAC);
		String stringRep = model.toString();
		assertNotNull(stringRep);
	}
	@Test
	void testIssue136_NegativeOffsetMirrorSymbol() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_for_running_nested.hrac", SOMFormats.HRAC);
		HRACSymbol amir = model.getSymbolByName("A_MIR");
		assertEquals("A", ((NamedHRACMemoryAddress)amir.getTargetSymbol()).getName());
	}
	@Test
	void testIssue140_HRBSBoundsRangeToString() throws IOException {
		HRBSBoundsRange tr = new HRBSBoundsRange();
		tr.setStart(2);tr.setEnd(3);tr.setStep(1);
		String str = tr.toString();
		assertTrue(str.contains("2"));assertTrue(str.contains("3"));assertTrue(str.contains("1"));
		
	}
	@Test
	void testIssue141_HRACForDupToString() throws IOException {
		HRACCommand cmd = new HRACCommand();
		cmd.setOp(Opcode.NAR);
		cmd.setLabel(new HRACSymbol("LBL"));
		cmd.setTarget(new NamedHRACMemoryAddress("A"));
		HRACForDupBoundingRangeProvider rng = new HRACForDupBoundingRangeProvider();
		rng.setRangeStart(2);
		rng.setRangeEnd(3);
		rng.setStepSize(1);
		HRACForDup fdr = new HRACForDup(cmd);
		fdr.setRange(rng);
		
		String str = fdr.toString();
		assertTrue(str.contains("2"));assertTrue(str.contains("3"));assertTrue(str.contains("1"));
		
	}
	@Test
	void testIssue141_HRACForDupBoundingRangeProviderToString() throws IOException {
		HRACForDupBoundingRangeProvider rng = new HRACForDupBoundingRangeProvider();
		rng.setRangeStart(2);
		rng.setRangeEnd(3);
		rng.setStepSize(1);
		
		String str = rng.toString();
		assertTrue(str.contains("2"));assertTrue(str.contains("3"));assertTrue(str.contains("1"));
		
	}
	@Test
	void testIssue142_HRACForDupCompile() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_rng_compile.hrac", SOMFormats.HRAC);
		HRASModel hrasModel = c.compile(model, SOMFormats.HRAC, SOMFormats.HRAS);
		assertEquals(3, hrasModel.getCommandCount());
	}
}
