package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import de.dralle.som.Compiler;
import de.dralle.som.FileLoader;
import de.dralle.som.IMemspace;
import de.dralle.som.ISomMemspace;
import de.dralle.som.SOMBitcodeRunner;
import de.dralle.som.SOMFormats;
import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hras.model.HRASModel;
import de.dralle.som.languages.hrav.model.HRAVModel;
import de.dralle.som.languages.hrbs.model.HRBSModel;

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
	void testAdrSetToLabelAfterExec() throws IOException {
		HRBSModel model = f.loadFromFile("test/fixtures/hrbs/test_jump.hrbs", SOMFormats.HRBS);
		HRACModel hrac = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAC);
		HRASModel hras = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAS);
		int labelArd=hras.resolveSymbolToAddress("LABEL");
		HRAVModel hrav = c.compile(model, SOMFormats.HRBS, SOMFormats.HRAV);
		IMemspace bin = c.compile(model, SOMFormats.HRBS, SOMFormats.BIN);
		assertNotEquals(((ISomMemspace)bin).getNextAddress(), labelArd);//no change before exec
		SOMBitcodeRunner runner = new SOMBitcodeRunner((ISomMemspace) bin);
		runner.execute();
		bin=runner.getMemspace();
		assertEquals(labelArd,((ISomMemspace)bin).getNextAddress());//written to label expectesd after exec
	}
}
