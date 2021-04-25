/**
 * 
 */
package de.dralle.som.test.meta;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

/**
 * @author Nils Dralle
 *
 */
class MetaTest {

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
	@EnabledIfEnvironmentVariable(named = "TEST_TEST_EXEC_FILE", matches = "*")
	void testTestExecution() { // this test is not to test the som implementation, but instead is used
		// to test if tests are executed at all. It checks for a file created by an
		// github action, and if it exists, creates a new one for which the action can
		// check. The test will only be executed, if the env var 'TEST_TEST_EXEC_FILE'
		// is set.
		String originalToCheckForPath = null;
		try {
			originalToCheckForPath = System.getenv("TEST_TEST_EXEC_FILE");
		} catch (Exception e) {
			fail();
		}
		File originalToCheckFor = new File(originalToCheckForPath);
		File copy = new File(originalToCheckFor.getAbsoluteFile() + ".new");
		try {
			if (!copy.createNewFile()) {
				fail();
			}
		} catch (IOException e) {
			fail();
		}
	}
}
