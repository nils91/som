package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.dralle.som.Compiler;
import de.dralle.som.FileLoader;
import de.dralle.som.SOMFormats;
import de.dralle.som.languages.hrac.model.HRACForDup;
import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hras.model.HRASModel;

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
		boolean pc=true;
		for (HRACForDup hracForDup : cs) {
			if(hracForDup.getCmd()==null) {
				pc = false;
			}
		}
		assertTrue(pc);
	}
	@Test
	void testIssue62_HRAPCompilationOriginalModelNeedsPrecompilation() throws IOException {
		HRACModel model = f.loadFromFile("test/fixtures/hrac/test_for_simple.hrac", SOMFormats.HRAC);
		List<HRACForDup> cs = model.getCommands();
		boolean pc=true;
		for (HRACForDup hracForDup : cs) {
			if(hracForDup.getCmd()==null) {
				pc = false;
			}
		}
		assertFalse(pc);
	}
}
