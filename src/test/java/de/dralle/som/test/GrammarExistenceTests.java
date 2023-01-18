/**
 * 
 */
package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.dralle.som.languages.hrac.generated.HRACGrammarBaseVisitor;
import de.dralle.som.languages.hrac.generated.HRACGrammarParser;
import de.dralle.som.languages.hras.generated.HRASGrammarBaseVisitor;
import de.dralle.som.languages.hras.generated.HRASGrammarParser;

/**
 * @author Nils
 *
 */
class GrammarExistenceTests {

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

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testHRASVisitorExistence() {
		assertNotNull(HRASGrammarBaseVisitor.class);
	}
	@Test
	void testHRASParserExistence() {
		assertNotNull(HRASGrammarParser.class);
	}@Test
	void testHRACVisitorExistence() {
		assertNotNull(HRACGrammarBaseVisitor.class);
	}
	@Test
	void testHRACParserExistence() {
		assertNotNull(HRACGrammarParser.class);
	}

}
