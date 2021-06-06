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
	private boolean lastSwitchSuccess;
	public boolean isLastSwitchSuccess() {
		return lastSwitchSuccess;
	}
	public int getMaxWhNumber() {
		return maxWhNumber;
	}
	public int getSelectedWriteHookNumber() {
		return selectedWriteHook;
	}
	
	public void setSelectedWriteHook(int selectedWriteHook) {
		if(selectedWriteHook>=MIN_WH_NUMBER&&selectedWriteHook<=maxWhNumber) {
			this.selectedWriteHook=selectedWriteHook;
			lastSwitchSuccess=true;
		}else {
			lastSwitchSuccess=false;
		}
	}
	public void switchToPreviousWriteHook() {
		setSelectedWriteHook(getSelectedWriteHookNumber()-1);
	}
	public void switchToNextWriteHook() {
		setSelectedWriteHook(getSelectedWriteHookNumber()+1);
	}
	public void registerWriteHook(int num,IWriteHook writeHook) {
		if(num>maxWhNumber) {
			maxWhNumber=num;
		}
		if(num>=MIN_WH_NUMBER) {
			registeredWriteHooks.put(num, writeHook);
		}
	}
	public void registerWriteHook(IWriteHook writeHook) {
		registerWriteHook(selectedWriteHook+1, writeHook);
	}
	public IWriteHook getSelectedWriteHook() {
		return registeredWriteHooks.get(selectedWriteHook);
	}
}
