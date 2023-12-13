package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BooleanSupplier;
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
import de.dralle.som.languages.hrac.model.HRACForDup;
import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hrac.model.HRACSymbol;
import de.dralle.som.languages.hras.model.HRASCommand;
import de.dralle.som.languages.hras.model.HRASMemoryAddress;
import de.dralle.som.languages.hras.model.HRASModel;
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
		Map<String, HRASMemoryAddress> symbols = hras.getSymbols();
		boolean repl = false;
		int aAdr = 0;
		int cAdr = 0;
		for (Entry<String, HRASMemoryAddress> iterable_element : symbols.entrySet()) {
			if (iterable_element.getKey().equals("A")) {
				aAdr = Integer.parseInt(iterable_element.getValue().getSymbol());
			}
			if (iterable_element.getKey().equals("C")) {
				cAdr = Integer.parseInt(iterable_element.getValue().getSymbol());
			}
		}
		repl = cAdr - aAdr == 5;
		assertTrue(repl);
	}

	@Test
	void testIssue85_HRACCompileMSDirectiveReplace() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_directive_use_in_offsets.hrac", SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRAC, SOMFormats.HRAS);
		Map<String, HRASMemoryAddress> symbols = hras.getSymbols();
		boolean repl = false;
		for (Entry<String, HRASMemoryAddress> iterable_element : symbols.entrySet()) {
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
		Map<HRASMemoryAddress, HRASCommand> symbols = hras.getCommands();
		boolean repl = false;
		for (Entry<HRASMemoryAddress, HRASCommand> iterable_element : symbols.entrySet()) {
			if (iterable_element.getValue().getOp().equals(Opcode.NAR)
					&& iterable_element.getValue().getAddress().getSymbol().equals("B")) {
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
		Map<String, HRASMemoryAddress> coms = hras.getSymbols();
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
		HRACModel m = f.loadFromFile("test/fixtures/hrac/test_directive_use_in_offsets.hrac", SOMFormats.HRAC);

		String hracCode = m.asCode();
		HRACParser p = new HRACParser();
		HRACModel m2 = p.parse(hracCode);
		IMemspace nm = c.compile(m, SOMFormats.HRAC, SOMFormats.BIN);
		IMemspace nm2 = c.compile(m2, SOMFormats.HRAC, SOMFormats.BIN);
		assertTrue(nm.equalContent(nm2));
	}
}
