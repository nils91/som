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
	private static final int MIN_WH_NUMBER=0;
	private int maxWhNumber=0;
	private Map<Integer,IWriteHook> registeredWriteHooks=new HashMap<>();
}
