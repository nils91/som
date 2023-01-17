/**
 * 
 */
package de.dralle.som.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.dralle.som.WriteHookManager;
import de.dralle.som.test.util.TestWriteHook;

/**
 * @author Nils Dralle
 *
 */
class WriteHookManagerTests {

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

	private WriteHookManager manager;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		manager = new WriteHookManager();
		manager.registerWriteHook(0, new TestWriteHook());
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testSelectInvalidWriteHookNum() {
		manager.setSelectedWriteHook(-1);
		assertFalse(manager.isLastSwitchSuccess());
	}
	@Test
	void testSwitchToNextButInvalidWriteHook() {
		manager.switchToNextWriteHook();
		assertFalse(manager.isLastSwitchSuccess());
	}
	@Test
	void testSwitchToPrevButInvalidWriteHook() {
		manager.switchToPreviousWriteHook();
		assertFalse(manager.isLastSwitchSuccess());
	}
	@Test
	void testRegisterWriteHookNumIncrease() {
		int whNumBefore = manager.getMaxWhNumber();
		manager.registerWriteHook(new TestWriteHook());
		assertEquals(whNumBefore+1, manager.getMaxWhNumber());
	}
	
	@Test
	void testSwitchToNextButValidWriteHook() {
		TestWriteHook newTestHook = new TestWriteHook();
		manager.registerWriteHook(newTestHook);
		manager.switchToNextWriteHook();
		assertTrue(manager.isLastSwitchSuccess());
	}
	@Test
	void testSwitchToPrevButValidWriteHook() {
		TestWriteHook newTestHook = new TestWriteHook();
		manager.registerWriteHook(newTestHook);
		manager.switchToNextWriteHook();
		manager.switchToPreviousWriteHook();
		assertTrue(manager.isLastSwitchSuccess());
	}
	@Test
	void testRegisterWriteHookGetJustSelectedWriteHookCorrectRef() {
		TestWriteHook newTestHook = new TestWriteHook();
		manager.registerWriteHook(newTestHook);
		manager.switchToNextWriteHook();
		assertTrue(newTestHook==manager.getSelectedWriteHook());
	}
	@Test
	void testRegisterWriteHookGetSelectedWriteHookNoSwitchWrongRef() {
		TestWriteHook newTestHook = new TestWriteHook();
		manager.registerWriteHook(newTestHook);
		assertTrue(newTestHook!=manager.getSelectedWriteHook());
	}
	@Test
	void testRegisterWriteHookGetSelectedWriteHookSwitchBackNForthWrongRef() {
		TestWriteHook newTestHook = new TestWriteHook();
		manager.registerWriteHook(newTestHook);
		manager.switchToNextWriteHook();
		manager.switchToPreviousWriteHook();
		assertTrue(newTestHook!=manager.getSelectedWriteHook());
	}
}
