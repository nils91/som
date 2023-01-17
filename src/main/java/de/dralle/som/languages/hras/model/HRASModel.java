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

import de.dralle.som.AbstractSomMemspace;
import de.dralle.som.ByteArrayMemspace;
import de.dralle.som.IMemspace;
import de.dralle.som.ISomMemspace;

/**
 * @author Nils
 *
 */
public class HRASModel {
	public HRASModel() {
		setupBuiltins();
		symbols = new LinkedHashMap<>();
	}

	private MemoryAddress nextCommandAddress;

	public MemoryAddress getNextCommandAddress() {
		return nextCommandAddress;
	}

	public void setNextCommandAddress(MemoryAddress nextCommandAddress) {
		this.nextCommandAddress = nextCommandAddress;
	}

	private int n;

	public void setN(int n) {
		this.n = n;
	}

	public void setStartAdress(MemoryAddress startAdress) {
		this.startAdress = startAdress;
	}

	

	private MemoryAddress startAdress;
	public MemoryAddress getStartAdress() {
		return startAdress;
	}

	boolean startAddressExplicit;

	public boolean isStartAddressExplicit() {
		return startAddressExplicit;
	}

	public void setStartAddressExplicit(boolean startAddressExplicit) {
		this.startAddressExplicit = startAddressExplicit;
	}
	
	private Map<String, MemoryAddress> symbols;
	private Map<String, MemoryAddress> builtins;
	private Map<MemoryAddress, Command> commands;

	public void addSymbol(String name, MemoryAddress value) {
		if (symbols == null) {
			symbols = new LinkedHashMap<>();
		}
		symbols.put(name, value);
	}

	public void addCommand(Command c) {
		if (commands == null) {
			commands = new LinkedHashMap<>();
		}
		commands.put(nextCommandAddress, c);
		Integer currentOffset=nextCommandAddress.getAddressOffset();
		if(currentOffset!=null) {
			currentOffset=currentOffset.intValue()+getCommandSize();
		}else {
			currentOffset=getCommandSize();
		}
		nextCommandAddress.setAddressOffset(currentOffset.intValue());
	}

	private void setupBuiltins() {
		builtins = new HashMap<>();
		builtins.put("ACC", new MemoryAddress(AbstractSomMemspace.ACC_ADDRESS));
		builtins.put("ADR_EVAL",new MemoryAddress( AbstractSomMemspace.ADR_EVAL_ADDRESS));
		builtins.put("WH_EN",new MemoryAddress( AbstractSomMemspace.WH_EN));
		builtins.put("N",new MemoryAddress( AbstractSomMemspace.ADDRESS_SIZE_START));
		builtins.put("WH_COM",new MemoryAddress( AbstractSomMemspace.WH_COM));
		builtins.put("WH_DIR",new MemoryAddress( AbstractSomMemspace.WH_DIR));
		builtins.put("WH_SEL",new MemoryAddress( AbstractSomMemspace.WH_SEL));
		builtins.put("ADR",new MemoryAddress( AbstractSomMemspace.START_ADDRESS_START));
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
		for (Entry<MemoryAddress, Command> commandEntry : commands.entrySet()) {
			MemoryAddress commandAddress = commandEntry.getKey();
			Command command = commandEntry.getValue();
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

	private int getCommandTargetAddress(Command c) {
		int tgtAdddress = c.getAddress().resolve(this);
		return tgtAdddress;
	}

	public int resolveSymbolToAddress(String symbol) {
		MemoryAddress targetAddress = builtins.get(symbol);
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

	private String getNDirective() {
		return String.format(";n = %d", n);
	}

	private String getStartDirective() {
		return String.format(";start = %s", getStartAdress().asHRASCode());
	}

	private String getContinueDirective(String string) {
		return String.format(";continue = %s", string);
	}

	private List<String> getSymbolsAsStrings() {
		List<String> tmp = new ArrayList<>();
		for (Entry<String, MemoryAddress> symbol : symbols.entrySet()) {
			tmp.add(String.format("%s %s", symbol.getKey(), symbol.getValue().asHRASCode()));
		}
		return tmp;
	}

	private List<String> getCommandssAsStrings() {
		List<String> tmp = new ArrayList<>();
		for (Entry<MemoryAddress, Command> c : commands.entrySet()) {
			MemoryAddress address = c.getKey();
			Command command = c.getValue();
			MemoryAddress cooamndTgtAddress = command.getAddress();
			tmp.add(String.format("%s%s%s", getContinueDirective(address.asHRASCode()), System.lineSeparator(),
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
		for (Entry<MemoryAddress, Command> c : commands.entrySet()) {
			MemoryAddress address = c.getKey();
			Command command = c.getValue();
			int cTgtAddress = getCommandTargetAddress(command);
			mem.setBit(address.resolve(this), command.getOp().getBitValue());
			mem.setBitsUnsigned(address.resolve(this) + 1, n, cTgtAddress);
		}
		mem.setAccumulatorValue(true);
		mem.setAdrEval(true);
		return mem;
	}
}
