/**
 * 
 */
package de.dralle.som;

import java.util.HashMap;
import java.util.Map;

import de.dralle.som.writehooks.IWriteHook;

/**
 * @author Nils Dralle
 *
 */
public class WriteHookManager {
	/**
	 * inclusive
	 */
	private static final int MIN_WH_NUMBER=0;
	/**
	 * inclusive
	 */
	private int maxWhNumber=0;
	private Map<Integer,IWriteHook> registeredWriteHooks=new HashMap<>();
	private int selectedWriteHook=MIN_WH_NUMBER;
}
