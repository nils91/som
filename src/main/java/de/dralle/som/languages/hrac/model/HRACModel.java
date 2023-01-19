/**
 * 
 */
package de.dralle.som.languages.hrac.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.dralle.som.AbstractSomMemspace;
import de.dralle.som.ByteArrayMemspace;
import de.dralle.som.IMemspace;
import de.dralle.som.ISomMemspace;

/**
 * @author Nils
 *
 */
public class HRACModel {
	public HRACModel() {
		setupBuiltins();
		symbols = new LinkedHashMap<>();
	}

	private HRACMemoryAddress nextCommandAddress;

	public HRACMemoryAddress getNextCommandAddress() {
		return nextCommandAddress;
	}

	public void setNextCommandAddress(HRACMemoryAddress nextCommandAddress) {
		this.nextCommandAddress = nextCommandAddress;
	}

	private int n;

	public void setN(int n) {
		this.n = n;
	}

	public void setStartAdress(HRACMemoryAddress startAdress) {
		this.startAdress = startAdress;
	}

	

	private HRACMemoryAddress startAdress;
	public HRACMemoryAddress getStartAdress() {
		return startAdress;
	}

	boolean startAddressExplicit;

	public boolean isStartAddressExplicit() {
		return startAddressExplicit;
	}

	public void setStartAddressExplicit(boolean startAddressExplicit) {
		this.startAddressExplicit = startAddressExplicit;
	}
	
	private List<HRACSymbol> symbols;
	private List<HRACCommand> commands;

	public void addSymbol(HRACSymbol symbol) {
		if (symbols == null) {
			symbols = new ArrayList<>();
		}
		symbols.add(symbol);
	}

	public void addCommand(HRACCommand c) {
		if (commands == null) {
			commands = new ArrayList<>();
		}
		commands.add(c);
	}

	private boolean checkN() {
		int minBitCount = getFixedBitCount() + symbols.size() + getCommandBits();
		if (minBitCount < getHighestTgtAddress() + 1) {
			minBitCount = getHighestTgtAddress() + 1;
		}
		return Math.pow(2, n) <= minBitCount;
	}

	private int getHighestTgtAddress() {
		int high = 0;
		for (Entry<HRACMemoryAddress, HRACCommand> commandEntry : commands.entrySet()) {
			HRACMemoryAddress commandAddress = commandEntry.getKey();
			HRACCommand command = commandEntry.getValue();
			int commandAddressValue = commandAddress.resolve(this);
			int commandTargetAddress = getCommandTargetAddress(command);
			if (commandAddressValue > high) {
				high = commandAddressValue;
			}
			if (commandTargetAddress > high) {
				high = commandTargetAddress;
			}
		}
		return high;
	}

	private int getCommandTargetAddress(HRACCommand c) {
		int tgtAdddress = c.getAddress().resolve(this);
		return tgtAdddress;
	}

	public int resolveSymbolToAddress(String symbol) {
		HRACMemoryAddress targetAddress = builtins.get(symbol);
		if (targetAddress != null) {
			return targetAddress.resolve(this);
		}
		if (symbols != null) {
			targetAddress = symbols.get(symbol);
		}
		if (targetAddress != null) {
			return targetAddress.resolve(this);
		}
		return Integer.parseInt(symbol);
		
	}

	private int getCommandBits() {
		int commandSize = getCommandSize();
		return commands.size() * commandSize;
	}

	private int getCommandSize() {
		return 1 + n;
	}

	private int getFixedBitCount() {
		return 11 + n;
	}

	private int getExitAddress() {
		return (int) (Math.pow(2, n) - getCommandSize());
	}

	private String getHeapDirective() {
		return String.format(";heap = %d", n);
	}

	private List<String> getSymbolsAsStrings() {
		List<String> tmp = new ArrayList<>();
		for (Entry<String, HRACMemoryAddress> symbol : symbols.entrySet()) {
			tmp.add(String.format("%s %s", symbol.getKey(), symbol.getValue().asHRACCode()));
		}
		return tmp;
	}

	private List<String> getCommandssAsStrings() {
		List<String> tmp = new ArrayList<>();
		for (Entry<HRACMemoryAddress, HRACCommand> c : commands.entrySet()) {
			HRACMemoryAddress address = c.getKey();
			HRACCommand command = c.getValue();
			HRACMemoryAddress cooamndTgtAddress = command.getAddress();
			tmp.add(String.format("%s%s%s", getContinueDirective(address.asHRACCode()), System.lineSeparator(),
					command.asHRASCode()));
		}
		return tmp;
	}

	public String asCode() {
		StringBuilder sb = new StringBuilder();
		sb.append(getNDirective());
		sb.append(System.lineSeparator());
		sb.append(getStartDirective());
		sb.append(System.lineSeparator());
		for (String symbolString : getSymbolsAsStrings()) {
			sb.append(symbolString);
			sb.append(System.lineSeparator());
		}
		for (String symbolString : getCommandssAsStrings()) {
			sb.append(symbolString);
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}

	public IMemspace compileToMemspace() {
		ISomMemspace mem = new ByteArrayMemspace((int) Math.pow(2, n));
		mem.setN(n);
		mem.setNextAddress(getStartAdress().resolve(this));
		for (Entry<HRACMemoryAddress, HRACCommand> c : commands.entrySet()) {
			HRACMemoryAddress address = c.getKey();
			HRACCommand command = c.getValue();
			int cTgtAddress = getCommandTargetAddress(command);
			mem.setBit(address.resolve(this), command.getOp().getBitValue());
			mem.setBitsUnsigned(address.resolve(this) + 1, n, cTgtAddress);
		}
		mem.setAccumulatorValue(true);
		mem.setAdrEval(true);
		return mem;
	}

	@Override
	public String toString() {
		return asCode();
	}
}
