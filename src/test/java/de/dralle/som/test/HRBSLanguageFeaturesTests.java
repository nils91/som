/**
 * 
 */
package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

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
import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hras.model.HRASModel;
import de.dralle.som.languages.hrav.model.HRAVModel;
import de.dralle.som.languages.hrbs.model.HRBSModel;

/**
 * @author Nils
 *
 */
class HRBSLanguageFeaturesTests {

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
	void testMinNCalculation() throws IOException {//see file
		HRBSModel model =f.readHRBSFile("test/fixtures/hrbs/commands_single_file/test_minn_and_heap_calc.hrbs");

		HRASModel hrasModel = c.compile(model, SOMFormats.HRBS	, SOMFormats.HRAS);
		
		ISomMemspace space=c.compile(model, SOMFormats.HRBS, SOMFormats.BIN);
		assertTrue(hrasModel.getN()>=11);
		assertTrue(space.getN()>=11);
	}

	@Test
	void testHeapCalculation() throws IOException {
		HRBSModel model =f.readHRBSFile("test/fixtures/hrbs/commands_single_file/test_minn_and_heap_calc.hrbs");

		HRACModel hracModel = c.compile(model, SOMFormats.HRBS	, SOMFormats.HRAC);
		
		assertEquals(37, hracModel.getHeapSize());
	}
	@Test
	void testInstIdCompile() throws IOException {
		HRBSModel model =f.readHRBSFile("test/fixtures/hrbs/test_struct.hrbs");

		HRACModel hracModel = c.compile(model, SOMFormats.HRBS	, SOMFormats.HRAC);
		HRASModel hrasModel = c.compile(model, SOMFormats.HRBS	, SOMFormats.HRAS);	HRAVModel hravModel = c.compile(model, SOMFormats.HRBS	, SOMFormats.HRAV);
		assertNotNull(hravModel);
	}
	
}
