/**
 * 
 */
package de.dralle.som.languages.hras.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import de.dralle.som.AbstractSomMemspace;
import de.dralle.som.ByteArrayMemspace;
import de.dralle.som.IMemspace;
import de.dralle.som.ISomMemspace;
import de.dralle.som.SOMBitcodeRunner;

/**
 * @author Nils
 *
 */
public class HRASModel {
	public HRASModel() {
		setupBuiltins();
		symbols=new LinkedHashMap<>();
	}
private int nextCommandAddress;
	public int getNextCommandAddress() {
	return nextCommandAddress;
}

public void setNextCommandAddress(int nextCommandAddress) {
	this.nextCommandAddress = nextCommandAddress;
}
	private int n;
	public void setN(int n) {
		this.n = n;
	}

	public void setStartAdress(int startAdress) {
		this.startAdress = startAdress;
	}

	public void setHeap(int heap) {
		this.heap = heap;
	}
	private int startAdress;
	boolean startAddressExplicit;

	public boolean isStartAddressExplicit() {
		return startAddressExplicit;
	}

	public void setStartAddressExplicit(boolean startAddressExplicit) {
		this.startAddressExplicit = startAddressExplicit;
	}

	public int getStartAddress() {
		if (startAddressExplicit) {
			return startAdress;
		}
		startAdress = getFixedBitCount() + symbols.size();
		return startAdress;
	}

	public int getHeapSize() {
		int minHeap = (int) (Math.pow(2, n)) - getFixedBitCount() - getCommandBits();
		if (heap < minHeap) {
			heap = minHeap;
		}
		return heap;
	}

	private int heap;
	private Map<String, Integer> symbols;
	private Map<String, Integer> builtins;
	private Map<Integer, Command> commands;
	
	public void addSymbol(String name,int value) {
		if(symbols==null) {
			symbols=new LinkedHashMap<>();
		}
		symbols.put(name, value);
	}
	public void addCommand(Command c) {
		if(commands==null) {
			commands=new LinkedHashMap<>();
		}
		commands.put(nextCommandAddress, c);
		nextCommandAddress+=getCommandSize();
	}
	private void setupBuiltins() {
		builtins = new HashMap<>();
		builtins.put("ACC", AbstractSomMemspace.ACC_ADDRESS);
		builtins.put("ADR_EVAL", AbstractSomMemspace.ADR_EVAL_ADDRESS);
		builtins.put("WH_EN", AbstractSomMemspace.WH_EN);
		builtins.put("N", AbstractSomMemspace.ADDRESS_SIZE_START);
		builtins.put("WH_COM", AbstractSomMemspace.WH_COM);
		builtins.put("WH_DIR", AbstractSomMemspace.WH_DIR);
		builtins.put("WH_SEL", AbstractSomMemspace.WH_SEL);
		builtins.put("ADR", AbstractSomMemspace.START_ADDRESS_START);
	}

	private int recalculateN() {
		int minN = Integer.MAX_VALUE;
		int minBitCount = getFixedBitCount() + symbols.size() + getCommandBits() + heap;
		int i = 0;
		while (i < 10000 && minBitCount > Math.pow(2, n)) {
			n++;
			minBitCount = getFixedBitCount() + symbols.size() + getCommandBits() + heap;
			if (minBitCount < getHighestTgtAddress() + 1) {
				minBitCount = getHighestTgtAddress() + 1;
			}
		}
		return n;
	}

	private boolean checkN() {
		int minBitCount = getFixedBitCount() + symbols.size() + getCommandBits() + heap;
		if (minBitCount < getHighestTgtAddress() + 1) {
			minBitCount = getHighestTgtAddress() + 1;
		}
		return Math.pow(2, n) <= minBitCount;
	}

	private int getHighestTgtAddress() {
		int high = 0;
		for (Entry<Integer, Command> commandEntry : commands.entrySet()) {
			int commandAddress = commandEntry.getKey().intValue();
			Command command = commandEntry.getValue();
			int commandTargetAddress = getCommandTargetAddress(command);
			if(commandAddress>high) {
				high=commandAddress;
			}
			if(commandTargetAddress>high) {
				high=commandTargetAddress;
			}
		}
		return high;
	}

	private int getCommandTargetAddress(Command c) {
		int tgtAdddress = c.getAddress().resolve(this);
		return tgtAdddress;
	}

	public int resolveSymbolToAddress(String symbol) {
		Integer target = builtins.get(symbol);
		if (target != null) {
			return target.intValue();
		}
		if(symbols!=null) {
			target = symbols.get(symbol);
		}		
		if (target != null) {
			return target.intValue();
		}
		target = Integer.parseInt(symbol);
		if (target != null) {
			return target.intValue();
		}
		return 0;
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

	private String getNDirective() {
		return String.format(";n = %d", recalculateN());
	}

	private String getHeapDirective() {
		return String.format(";heap = %d", getHeapSize());
	}

	private String getStartDirective() {
		return String.format(";start = %d", getStartAddress());
	}

	private String getContinueDirective(int continueAddress) {
		return String.format(";continue = %d", continueAddress);
	}

	private List<String> getSymbolsAsStrings() {
		List<String> tmp = new ArrayList<>();
		for (Entry<String, Integer> symbol : symbols.entrySet()) {
			tmp.add(String.format("%s %d", symbol.getKey(), symbol.getValue().intValue()));
		}
		return tmp;
	}

	private List<String> getCommandssAsStrings() {
		List<String> tmp = new ArrayList<>();
		for (Entry<Integer, Command> c : commands.entrySet()) {
			Integer address = c.getKey();
			Command command = c.getValue();
			MemoryAddress cooamndTgtAddress = command.getAddress();
			tmp.add(String.format("%s%s%s %s[%d]", getContinueDirective(address.intValue()), System.lineSeparator(),
					command.getOp(), cooamndTgtAddress.getSymbol(), cooamndTgtAddress.getAddressOffset()));
		}
		return tmp;
	}

	public String asCode() {
		StringBuilder sb = new StringBuilder();
		sb.append(getNDirective());
		sb.append(System.lineSeparator());
		sb.append(getHeapDirective());
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
		ISomMemspace mem = new ByteArrayMemspace((int) Math.pow(2, recalculateN()));
		mem.setN(recalculateN());
		mem.setNextAddress(getStartAddress());
		for (Entry<Integer, Command> c : commands.entrySet()) {
			Integer address = c.getKey();
			Command command = c.getValue();
			int cTgtAddress = getCommandTargetAddress(command);
			mem.setBit(address.intValue(), command.getOp().getBitValue());
			mem.setBitsUnsigned(address.intValue() + 1, recalculateN(), cTgtAddress);
		}
		mem.setAccumulatorValue(true);
		mem.setAdrEval(true);
		return mem;
	}
}
