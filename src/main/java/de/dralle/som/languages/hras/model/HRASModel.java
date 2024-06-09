/**
 * 
 */
package de.dralle.som.languages.hras.model;

import java.util.AbstractMap;
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

	private SymbolHRASMemoryAddress nextCommandAddress;

	public SymbolHRASMemoryAddress getNextCommandAddress() {
		return nextCommandAddress;
	}

	public void setNextCommandAddress(SymbolHRASMemoryAddress nextCommandAddress) {
		this.nextCommandAddress = nextCommandAddress;
	}

	private AbstractExpressionNode n; // This can either be a integer (wrapped in the IntegerNode class) or an entire
										// expression tree. Either way it would have a definite value, use
										// calculateNumericalValue for that.

	public void setN(AbstractExpressionNode abstractExpressionNode) {
		this.n = abstractExpressionNode;
	}

	public void setStartAdress(SymbolHRASMemoryAddress startAdress) {
		this.startAdress = startAdress;
	}

	private SymbolHRASMemoryAddress startAdress;

	public SymbolHRASMemoryAddress getStartAdress() {
		return startAdress;
	}

	boolean startAddressExplicit;

	public boolean isStartAddressExplicit() {
		return startAddressExplicit;
	}

	public void setStartAddressExplicit(boolean startAddressExplicit) {
		this.startAddressExplicit = startAddressExplicit;
	}

	private Map<String, SymbolHRASMemoryAddress> symbols;

	public Map<String, SymbolHRASMemoryAddress> getSymbols() {
		return symbols;
	}

	public Map<SymbolHRASMemoryAddress, HRASCommand> getCommands() {
		return commands;
	}

	private Map<SymbolHRASMemoryAddress, HRASCommand> commands;

	private List<Map.Entry<SymbolHRASMemoryAddress, Boolean>> initOnceList = new ArrayList<Map.Entry<SymbolHRASMemoryAddress, Boolean>>();

	public List<Map.Entry<SymbolHRASMemoryAddress, Boolean>> getInitOnceList() {
		return initOnceList;
	}

	public void addInitOnceValue(SymbolHRASMemoryAddress ma, boolean set) {
		initOnceList.add(new AbstractMap.SimpleEntry<SymbolHRASMemoryAddress, Boolean>(ma, set));
	}

	public int getCommandCount() {
		return commands.size();
	}

	public void addSymbol(String name, SymbolHRASMemoryAddress value) {
		if (symbols == null) {
			symbols = new LinkedHashMap<>();
		}
		symbols.put(name, value);
	}

	public SymbolHRASMemoryAddress addCommand(HRASCommand c) {
		if (commands == null) {
			commands = new LinkedHashMap<>();
		}
		SymbolHRASMemoryAddress assignedCommandAddress = nextCommandAddress.clone();
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
		int tgtAdddress = 0;
		try {
			tgtAdddress = c.getAddress().resolve(this);
		} catch (Exception e) {
			System.out.println("Command target " + c.getAddress() + " of command " + c + " couldnt be resolved");
			throw e;
		}
		return tgtAdddress;
	}

	public int resolveSymbolToAddress(String symbol) {
		SymbolHRASMemoryAddress targetAddress = null;
		if (symbols != null) {
			targetAddress = symbols.get(symbol);
		}
		if (targetAddress != null) {
			return targetAddress.resolve(this);
		}
		return Integer.parseInt(symbol);

	}

	private int getCommandSize() {
		return 1 + n.calculateNumericalValue();
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
		for (Entry<String, SymbolHRASMemoryAddress> symbol : symbols.entrySet()) {
			tmp.add(String.format("symbol %s %s", symbol.getKey(), symbol.getValue().asHRASCode()));
		}
		return tmp;
	}

	private List<String> getCommandssAsStrings() {
		List<String> tmp = new ArrayList<>();
		for (Entry<SymbolHRASMemoryAddress, HRASCommand> c : commands.entrySet()) {
			SymbolHRASMemoryAddress address = c.getKey();
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
		for (Entry<SymbolHRASMemoryAddress, Boolean> entry : initOnceList) {
			sb.append((entry.getValue() ? "setonce" : "clearonce") + " " + entry.getKey().asHRASCode());
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
		setStartAdress(new SymbolHRASMemoryAddress(startAdress2));

	}

	public void setNextCommandAddress(int startAdress2) {
		setNextCommandAddress(new SymbolHRASMemoryAddress(startAdress2));

	}

	@Override
	public int getN() {
		return n.calculateNumericalValue();
	}

	public HRAVModel compileToHRAV() {
		HRAVModel hrav = new HRAVModel();
		hrav.setN(n.calculateNumericalValue());
		if (startAdress != null) {
			hrav.setStartAddressExplicit(true);
			hrav.setStartAdress(startAdress.resolve(this));
		}
		for (Entry<SymbolHRASMemoryAddress, Boolean> entry : initOnceList) {
			hrav.addInitOnceAddress(entry.getKey().resolve(this), entry.getValue());
		}
		for (Entry<SymbolHRASMemoryAddress, HRASCommand> c : commands.entrySet()) {
			SymbolHRASMemoryAddress address = c.getKey();
			hrav.setNextCommandAddress(address.resolve(this));
			HRASCommand command = c.getValue();
			int cTgtAddress = getCommandTargetAddress(command);
			HRAVCommand hravCommand = new HRAVCommand();
			hravCommand.setOp(command.getOp());
			if (cTgtAddress < 0) {
				System.out.println(
						"Warning: (HRAS -> HRAV) Negative memory address in command at address " + address + ".");
			}
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
		for (Entry<Integer, Boolean> otiE : model.getInitOnceValues()) {
			Integer oAdr = otiE.getKey();
			String symbolName = symbols.getOrDefault(oAdr, "MA" + oAdr);
			symbols.put(oAdr, symbolName);
			newm.addInitOnceValue(new SymbolHRASMemoryAddress(symbolName), otiE.getValue());
		}
		for (Entry<Integer, HRAVCommand> ce : model.getCommands().entrySet()) {
			Integer cadr = ce.getKey();
			HRAVCommand c = ce.getValue();
			String symbolName = symbols.getOrDefault(cadr, "MA" + cadr);
			symbols.put(cadr, symbolName);
			newm.setNextCommandAddress(new SymbolHRASMemoryAddress(symbolName));
			symbolName = symbols.getOrDefault(c.getAddress(), "MA" + c.getAddress());
			symbols.put(c.getAddress(), symbolName);
			SymbolHRASMemoryAddress ctgtadr = new SymbolHRASMemoryAddress(symbolName);
			HRASCommand nc = new HRASCommand();
			nc.setOp(c.getOp());
			nc.setAddress(ctgtadr);
			newm.addCommand(nc);
		}
		for (Entry<Integer, String> entry : symbols.entrySet()) {
			Integer key = entry.getKey();
			String val = entry.getValue();
			newm.addSymbol(val, new SymbolHRASMemoryAddress(key));
		}
		return newm;
	}

	public Integer getSymbolCount() {
		return symbols.size();
	}

	@Override
	public void setN(int n) {
		this.n = new IntegerNode(n);

	}
}
