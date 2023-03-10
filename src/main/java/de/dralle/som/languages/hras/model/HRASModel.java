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
import de.dralle.som.ISetN;
import de.dralle.som.ISomMemspace;
import de.dralle.som.languages.hrav.model.HRAVCommand;
import de.dralle.som.languages.hrav.model.HRAVModel;

/**
 * @author Nils
 *
 */
public class HRASModel implements ISetN{
	public HRASModel() {
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
	private Map<MemoryAddress, Command> commands;

	public int getCommandCount() {
		return commands.size();
	}
	public void addSymbol(String name, MemoryAddress value) {
		if (symbols == null) {
			symbols = new LinkedHashMap<>();
		}
		symbols.put(name, value);
	}

	public MemoryAddress addCommand(Command c) {
		if (commands == null) {
			commands = new LinkedHashMap<>();
		}
		MemoryAddress assignedCommandAddress = nextCommandAddress.clone();
		commands.put(assignedCommandAddress, c);
		Integer currentOffset=nextCommandAddress.getAddressOffset();
		if(currentOffset!=null) {
			currentOffset=currentOffset.intValue()+getCommandSize();
		}else {
			currentOffset=getCommandSize();
		}
		nextCommandAddress.setAddressOffset(currentOffset.intValue());
		return assignedCommandAddress;
	}






	private int getCommandTargetAddress(Command c) {
		int tgtAdddress = c.getAddress().resolve(this);
		return tgtAdddress;
	}

	public int resolveSymbolToAddress(String symbol) {
		MemoryAddress targetAddress = null;
		if (symbols != null) {
			targetAddress = symbols.get(symbol);
		}
		if (targetAddress != null) {
			return targetAddress.resolve(this);
		}
		return Integer.parseInt(symbol);
		
	}

	
	private int getCommandSize() {
		return 1 + n;
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
			tmp.add(String.format("symbol %s %s", symbol.getKey(), symbol.getValue().asHRASCode()));
		}
		return tmp;
	}

	private List<String> getCommandssAsStrings() {
		List<String> tmp = new ArrayList<>();
		for (Entry<MemoryAddress, Command> c : commands.entrySet()) {
			MemoryAddress address = c.getKey();
			Command command = c.getValue();
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

	@Override
	public String toString() {
		return asCode();
	}

	public void setStartAdress(int startAdress2) {
		setStartAdress(new MemoryAddress(startAdress2));
		
	}

	public void setNextCommandAddress(int startAdress2) {
		setNextCommandAddress(new MemoryAddress(startAdress2));
		
	}

	@Override
	public int getN() {
		return n;
	}

	public HRAVModel compileToHRAV() {
		HRAVModel hrav = new HRAVModel();
		hrav.setN(n);
		if(startAdress!=null) {
			hrav.setStartAddressExplicit(true);
			hrav.setStartAdress(startAdress.resolve(this));
		}
		for (Entry<MemoryAddress, Command> c : commands.entrySet()) {
			MemoryAddress address = c.getKey();
			hrav.setNextCommandAddress(address.resolve(this));
			Command command = c.getValue();
			int cTgtAddress = getCommandTargetAddress(command);
			HRAVCommand hravCommand = new HRAVCommand();
			hravCommand.setOp(command.getOp());
			hravCommand.setAddress(cTgtAddress);
			hrav.addCommand(hravCommand);
		}
		return hrav;
	}

	public Integer getSymbolCount() {
		return symbols.size();
	}
}
