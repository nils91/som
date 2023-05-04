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
import de.dralle.som.Util;
import de.dralle.som.languages.hrac.model.HRACSymbol;
import de.dralle.som.languages.hrav.model.HRAVCommand;
import de.dralle.som.languages.hrav.model.HRAVModel;

/**
 * @author Nils
 *
 */
public class HRASModel implements ISetN {
	public HRASModel() {
		symbols = new LinkedHashMap<>();
	}

	private HRASMemoryAddress nextCommandAddress;

	public HRASMemoryAddress getNextCommandAddress() {
		return nextCommandAddress;
	}

	public void setNextCommandAddress(HRASMemoryAddress nextCommandAddress) {
		this.nextCommandAddress = nextCommandAddress;
	}

	private int n;

	public void setN(int n) {
		this.n = n;
	}

	public void setStartAdress(HRASMemoryAddress startAdress) {
		this.startAdress = startAdress;
	}

	private HRASMemoryAddress startAdress;

	public HRASMemoryAddress getStartAdress() {
		return startAdress;
	}

	boolean startAddressExplicit;

	public boolean isStartAddressExplicit() {
		return startAddressExplicit;
	}

	public void setStartAddressExplicit(boolean startAddressExplicit) {
		this.startAddressExplicit = startAddressExplicit;
	}

	private Map<String, HRASMemoryAddress> symbols;

	public Map<String, HRASMemoryAddress> getSymbols() {
		return symbols;
	}

	public Map<HRASMemoryAddress, HRASCommand> getCommands() {
		return commands;
	}

	private Map<HRASMemoryAddress, HRASCommand> commands;

	public int getCommandCount() {
		return commands.size();
	}

	public void addSymbol(String name, HRASMemoryAddress value) {
		if (symbols == null) {
			symbols = new LinkedHashMap<>();
		}
		symbols.put(name, value);
	}

	public HRASMemoryAddress addCommand(HRASCommand c) {
		if (commands == null) {
			commands = new LinkedHashMap<>();
		}
		HRASMemoryAddress assignedCommandAddress = nextCommandAddress.clone();
		commands.put(assignedCommandAddress, c);
		Integer currentOffset = nextCommandAddress.getAddressOffset();
		if (currentOffset != null) {
			currentOffset = currentOffset.intValue() + getCommandSize();
		} else {
			currentOffset = getCommandSize();
		}
		nextCommandAddress.setAddressOffset(currentOffset.intValue());
		return assignedCommandAddress;
	}

	private int getCommandTargetAddress(HRASCommand c) {
		int tgtAdddress =0;
		try {
			tgtAdddress = c.getAddress().resolve(this);
		} catch (Exception e) {
			System.out.println("Command target " + c.getAddress() + " of command " + c + " couldnt be resolved");
			throw e;
		}
		return tgtAdddress;
	}

	public int resolveSymbolToAddress(String symbol) {
		HRASMemoryAddress targetAddress = null;
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
		for (Entry<String, HRASMemoryAddress> symbol : symbols.entrySet()) {
			tmp.add(String.format("symbol %s %s", symbol.getKey(), symbol.getValue().asHRASCode()));
		}
		return tmp;
	}

	private List<String> getCommandssAsStrings() {
		List<String> tmp = new ArrayList<>();
		for (Entry<HRASMemoryAddress, HRASCommand> c : commands.entrySet()) {
			HRASMemoryAddress address = c.getKey();
			HRASCommand command = c.getValue();
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

	@Override
	public String toString() {
		return asCode();
	}

	public void setStartAdress(int startAdress2) {
		setStartAdress(new HRASMemoryAddress(startAdress2));

	}

	public void setNextCommandAddress(int startAdress2) {
		setNextCommandAddress(new HRASMemoryAddress(startAdress2));

	}

	@Override
	public int getN() {
		return n;
	}

	public HRAVModel compileToHRAV() {
		HRAVModel hrav = new HRAVModel();
		hrav.setN(n);
		if (startAdress != null) {
			hrav.setStartAddressExplicit(true);
			hrav.setStartAdress(startAdress.resolve(this));
		}
		for (Entry<HRASMemoryAddress, HRASCommand> c : commands.entrySet()) {
			HRASMemoryAddress address = c.getKey();
			hrav.setNextCommandAddress(address.resolve(this));
			HRASCommand command = c.getValue();
			int cTgtAddress = getCommandTargetAddress(command);
			HRAVCommand hravCommand = new HRAVCommand();
			hravCommand.setOp(command.getOp());
			hravCommand.setAddress(cTgtAddress);
			hrav.addCommand(hravCommand);
		}
		return hrav;
	}

	public static HRASModel compileFromHRAV(HRAVModel model) {
		Map<Integer, String> symbols = new HashMap<Integer, String>();
		symbols.putAll(Util.getBuiltinAdressesAddressKey());
		HRASModel newm = new HRASModel();
		newm.setN(model.getN());
		newm.setStartAdress(model.getStartAdress());
		newm.setStartAddressExplicit(true);
		newm.setNextCommandAddress(model.getStartAdress());
		for (Entry<Integer, HRAVCommand> ce : model.getCommands().entrySet()) {
			Integer cadr = ce.getKey();
			HRAVCommand c = ce.getValue();
			String symbolName = symbols.getOrDefault(cadr, "MA" + cadr);
			symbols.put(cadr, symbolName);
			newm.setNextCommandAddress(new HRASMemoryAddress(symbolName));
			symbolName = symbols.getOrDefault(c.getAddress(), "MA" + c.getAddress());
			symbols.put(c.getAddress(), symbolName);
			HRASMemoryAddress ctgtadr = new HRASMemoryAddress(symbolName);
			HRASCommand nc = new HRASCommand();
			nc.setOp(c.getOp());
			nc.setAddress(ctgtadr);
			newm.addCommand(nc);
		}
		for (Entry<Integer, String> entry : symbols.entrySet()) {
			Integer key = entry.getKey();
			String val = entry.getValue();
			newm.addSymbol(val, new HRASMemoryAddress(key));
		}
		return newm;
	}

	public Integer getSymbolCount() {
		return symbols.size();
	}
}
