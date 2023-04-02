/**
 * 
 */
package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.dralle.som.Util;
import de.dralle.som.writehooks.StdWriteHook;

/**
 * @author Nils
 *
 */
class MissalaniosTest {

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
	void testBitFrByte() {
		int a = 'a';
		for (int i = 0; i < 8; i++) {
			System.out.println(Util.getBit(a, i));
		}
	}

	@Test
	void testStdWH() throws InterruptedException {
		StdWriteHook wh = new StdWriteHook();
		wh.write(false, null);
		wh.write(true, null);
		wh.write(true, null);
		wh.write(false, null);
		wh.write(false, null);
		wh.write(false, null);
		wh.write(false, null);
		wh.write(true, null);
	}
}
