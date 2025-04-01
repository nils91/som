/**
 * 
 */
package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
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
import de.dralle.som.SOMFormats;
import de.dralle.som.languages.hrac.model.AbstractHRACMemoryAddress;
import de.dralle.som.languages.hrac.model.FixedHRACMemoryAddress;
import de.dralle.som.languages.hrac.model.HRACForDup;
import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hrac.model.HRACSymbol;
import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACDirectiveNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACMultiplicationExpressionNode;
import de.dralle.som.languages.hras.model.HRASModel;
import de.dralle.som.languages.hrav.model.HRAVCommand;
import de.dralle.som.languages.hrav.model.HRAVModel;
import de.dralle.som.languages.hrbs.model.HRBSModel;

/**
 * @author Nils
 *
 */
class HRASLanguageFeaturesTests {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	private Compiler c;
	private FileLoader f;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		c = new Compiler();
		f = new FileLoader();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testDirectiveAddress() throws IOException {
		HRASModel model = f.readHRASFile("test/fixtures/hras/test_lf_hras.hras");
		IMemspace memspace = c.compile(model, SOMFormats.HRAS, SOMFormats.BIN);
		// The file sets the address to 104 (1101000 starting at address 11)
		assertTrue(memspace.getBit(11));
		assertTrue(memspace.getBit(12));
		assertFalse(memspace.getBit(13));
		assertTrue(memspace.getBit(14));
		assertFalse(memspace.getBit(15));
		assertFalse(memspace.getBit(16));
		assertFalse(memspace.getBit(17));
	}

	@Test
	void testDirectiveContinue() throws IOException {
		HRASModel model = f.readHRASFile("test/fixtures/hras/test_lf_hras.hras");
		IMemspace memspace = c.compile(model, SOMFormats.HRAS, SOMFormats.BIN);
		// Test if there is the opcode for NAW at 96 and 120
		assertTrue(memspace.getBit(96));
		assertTrue(memspace.getBit(120));
	}

	@Test
	void testDirectiveN() throws IOException {
		HRASModel model = f.readHRASFile("test/fixtures/hras/test_lf_hras.hras");
		IMemspace memspace = c.compile(model, SOMFormats.HRAS, SOMFormats.BIN);
		// The file sets n to 7 (in binary 00011 - there is an offset of 4)
		assertFalse(memspace.getBit(3));
		assertFalse(memspace.getBit(4));
		assertFalse(memspace.getBit(5));
		assertTrue(memspace.getBit(6));
		assertTrue(memspace.getBit(7));
	}

	@Test
	void testSymbols() throws IOException {
		HRASModel model = f.readHRASFile("test/fixtures/hras/test_lf_hras.hras");
		assertEquals(18, model.resolveSymbolToAddress("A"));
		assertEquals(19, model.resolveSymbolToAddress("B"));
		assertEquals(19, model.resolveSymbolToAddress("C"));
		assertEquals(19, model.resolveSymbolToAddress("D"));
	}
	
	@Test
	void testExpressionTreeResolveWhenCompiling2HRAV() throws IOException {
		HRASModel model = f.loadFromFile("test/fixtures/hras/features/expression-tree/test_et_correct_compile.hras", SOMFormats.HRAS);
		HRAVModel hrac = c.compile(model, SOMFormats.HRAS, SOMFormats.HRAV);
		Map<Integer, HRAVCommand> hracC = hrac.getCommands();
		HRAVCommand foundCMD = null;
		//the command this test is looking for has 11*13 as its target address in HRAS, which should get resolved to 143 in HRAV
		for (Entry<Integer, HRAVCommand> entry : hracC.entrySet()) {
			Integer key = entry.getKey();
			HRAVCommand val = entry.getValue();
			if(val.getAddress()==143) {
				foundCMD=val;
			}
		}
		assertNotNull(foundCMD);
		assertEquals(143, foundCMD.getAddress());
	
	}
}
