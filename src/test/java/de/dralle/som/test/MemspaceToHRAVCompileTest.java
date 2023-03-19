package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import de.dralle.som.Compiler;
import de.dralle.som.FileLoader;
import de.dralle.som.IMemspace;
import de.dralle.som.ISomMemspace;
import de.dralle.som.SOMBitcodeRunner;
import de.dralle.som.SOMFormats;
import de.dralle.som.languages.hrav.model.HRAVModel;
import de.dralle.som.test.util.TestUtil;

class MemspaceToHRAVCompileTest {

	private Compiler c;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		c = new Compiler();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testSimple() throws IOException {
	String code=";n = 6\r\n"
			+ ";start = 43\r\n"
			+ ";continue = 43\r\n"
			+ "NAW 1\r\n"
			+ ";continue = 50\r\n"
			+ "NAR 0\r\n"
			+ ";continue = 57\r\n"
			+ "NAR 0\r\n";
	HRAVModel model=(HRAVModel) new FileLoader().loadFromString(code, SOMFormats.HRAV);
	IMemspace mem = model.compileToMemspace();
	HRAVModel model2 = HRAVModel.compileFromMemspace((ISomMemspace) mem);
	assertEquals(model, model2);
	}

}

