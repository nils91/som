/**
 * 
 */
package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.dralle.som.languages.hra.generated.HRAGrammarBaseVisitor;
import de.dralle.som.languages.hra.generated.HRAGrammarParser;

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
	void testHRAVisitorExistence() {
		assertNotNull(HRAGrammarBaseVisitor.class);
	}
	@Test
	void testHRAParserExistence() {
		assertNotNull(HRAGrammarParser.class);
	}

}
