/**
 * 
 */
package de.dralle.som.languages.hrac.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.dralle.som.AbstractSomMemspace;
import de.dralle.som.IHeap;
import de.dralle.som.ISetN;
import de.dralle.som.Opcode;
import de.dralle.som.languages.hras.model.Command;
import de.dralle.som.languages.hras.model.HRASModel;
import de.dralle.som.languages.hras.model.MemoryAddress;

/**
 * @author Nils
 *
 */
public class HRACModel implements ISetN, IHeap {

	private Map<String, Integer> builtins;
	private Map<String, String> directives;// as parsed
	private Map<String, String> additionalDirectives;// additionals added at runtime

	public int getMinimumN() {
		return getDirectiveAsInt("n");
	}

	public int getDirectiveAsInt(String name) {
		try {
			String sv = additionalDirectives.get(name);
			return Integer.parseInt(sv);
		} catch (Exception e) {

		}
		try {
			String sv = directives.get(name);
			return Integer.parseInt(sv);
		} catch (Exception e) {
			return 0;
		}
	}

	public Map<String, String> getAllDirectives() {
		Map<String, String> retMap = new HashMap<>();
		retMap.putAll(additionalDirectives);
		retMap.putAll(directives);
		return retMap;
	}

	public void setMinimumN(int minimumN) {
		this.directives.put("n", minimumN + "");
	}

	public int getHeapSize() {
		return getDirectiveAsInt("heap");
	}

	public void setHeapSize(int heapSize) {
		directives.put("heap", heapSize + "");
	}

	public HRACModel() {
		setupBuiltins();
		symbols = new ArrayList<>();
		commands = new ArrayList<>();
		directives = new HashMap<>();
		additionalDirectives = new HashMap<>();
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

	public int getStartAdress(int n) {
		return (int) Math.pow(2, n) - getCommandBitCount(n);
	}

	private List<HRACSymbol> symbols;

	public List<HRACSymbol> getSymbols() {
		return symbols;
	}

	public List<HRACForDup> getCommands() {
		return commands;
	}

	private List<HRACForDup> commands;

	public void addSymbol(HRACSymbol symbol) {
		if (symbols == null) {
			symbols = new ArrayList<>();
		}
		symbols.add(symbol);
	}

	public void addCommand(HRACForDup c) {
		if (commands == null) {
			commands = new ArrayList<>();
		}
		commands.add(c);
	}

	public void addCommand(HRACCommand c) {
		addCommand(new HRACForDup(c));
	}

	private int getSymbolBitCnt(int n) {
		int cnt = 0;
		for (HRACSymbol s : getSymbolsInclFrCommands()) {
			if (isSymbolNameAllowed(s.getName())) {
				if (s.getTargetSymbol() == null) {
					if (s.isBitCntSpecial()) {
						cnt += n;
					} else {
						cnt += s.getBitCnt();
					}
				}
			}
		}
		return cnt;
	}

	private boolean isSymbolNameAllowed(String name) {
		return !builtins.containsKey(name) && !"HEAP".equals(name);
	}

	private boolean checkN(int n) {
		additionalDirectives.put("N", n+"");
		int hs = getHeapSize();
		if (n < getMinimumN()) {
			return false;
		}
		for (HRACForDup hracForDup : commands) {
			hracForDup.setParent(this);
			hs += hracForDup.getHeapSize();
			if (n < hracForDup.getN()) {
				return false;
			}
		}
		int minBitCnt = getFixedBitCount(n) + getSymbolBitCnt(n) + hs + getCommandBitCount(n);
		return minBitCnt <= Math.pow(2, n);
	}

	private int findN() {
		int n = 0;
		do {
			n++;
		} while (!checkN(n));
		return n;
	}

	public int resolveBuiltinToAddress(String symbol, int n) {
		Integer targetAddress = builtins.get(symbol);
		if (targetAddress != null) {
			return targetAddress;
		}
		if (symbol.equals("HEAP")) {
			return getHeapStartAddress(n);
		}
		return -1;
	}

	private int getHeapStartAddress(int n) {
		return getFixedBitCount(n) + getSymbolBitCnt(n);
	}

	private int getCommandBitCount(int n) {
		additionalDirectives.put("N", n+"");
		int commandSize = getCommandSize(n);
		int cmdCnt = 1;// one command will be added during the compile
		for (HRACForDup hracForDup : commands) {
			hracForDup.setParent(this);
			cmdCnt += hracForDup.getCommands().size();
		}
		return (cmdCnt) * commandSize;
	}

	private int getCommandSize(int n) {
		return 1 + n;
	}

	private int getFixedBitCount(int n) {
		return 11 + n;
	}

	private List<String> getDirectivesAsStrings() {
		List<String> tmp = new ArrayList<>();
		for (Entry<String, String> symbol : directives.entrySet()) {
			tmp.add(String.format(";%s=\"%s\"", symbol.getKey(), symbol.getValue()));
		}
		return tmp;
	}

	private List<String> getSymbolsAsStrings() {
		List<String> tmp = new ArrayList<>();
		for (HRACSymbol symbol : symbols) {
			tmp.add(String.format("%s", symbol.asCode()));
		}
		return tmp;
	}

	private List<String> getCommandssAsStrings() {
		List<String> tmp = new ArrayList<>();
		for (HRACForDup c : commands) {

			tmp.add(String.format("%s", c.asCode()));
		}
		return tmp;
	}

	public String asCode() {
		StringBuilder sb = new StringBuilder();
		for (String symbolString : getDirectivesAsStrings()) {
			sb.append(symbolString);
			sb.append(System.lineSeparator());
		}
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

	public HRASModel compileToHRAS() {
		HRASModel m = new HRASModel();
		int n = findN();
		// add calculate n as directive to be used later on
		additionalDirectives.put("N", n + "");
		m.setN(n);
		m.setStartAddressExplicit(true);
		int startAddress = getStartAdress(n);
		m.setStartAdress(startAddress);
		m.setNextCommandAddress(startAddress);
		for (Entry<String, Integer> entry : builtins.entrySet()) {
			String key = entry.getKey();
			Integer val = entry.getValue();
			m.addSymbol(key, new MemoryAddress(val));
		}
		int nxtSymbolAddress = getFixedBitCount(n);
		for (HRACSymbol s : getSymbolsInclFrCommands()) {
			if (s.getTargetSymbol() == null) {
				int address = nxtSymbolAddress;
				if (s.isBitCntSpecial()) {
					nxtSymbolAddress += getDirectiveAsInt(s.getSpecialName());
				} else {
					nxtSymbolAddress += s.getBitCnt();
				}
				m.addSymbol(s.getName(), new MemoryAddress(address));
			} else {
				HRACMemoryAddress tgt = s.getTargetSymbol();
				MemoryAddress tgHras = new MemoryAddress();
				tgHras.setSymbol(tgt.getSymbol().getName());
				tgHras.setAddressOffset(tgt.getOffset());
				m.addSymbol(s.getName(), tgHras);
			}
		}
		m.addSymbol("HEAP", new MemoryAddress(getHeapStartAddress(n)));

		Command clrAdrEval = new Command();
		clrAdrEval.setOp(Opcode.NAW);
		clrAdrEval.setAddress(new MemoryAddress("ADR_EVAL"));
		m.addCommand(clrAdrEval);

		for (HRACForDup cf : commands) {
			cf.setParent(this);
			for (HRACCommand c : cf.getCommands()) {
				Command hrasc = new Command();
				hrasc.setOp(c.getOp());
				MemoryAddress address = new MemoryAddress(c.getTarget().getSymbol().getName());
				address.setAddressOffset(c.getTarget().getOffset());
				hrasc.setAddress(address);
				MemoryAddress assignedAddress = m.addCommand(hrasc);
				if (c.getLabel() != null) {
					m.addSymbol(c.getLabel().getName(), assignedAddress);
				}
			}

		}
		return m;
	}

	@Override
	public String toString() {
		return asCode();
	}

	@Override
	/**
	 * Returns the N calculatedx for this Model.
	 */
	public int getN() {
		return findN();
	}

	@Override
	/**
	 * Sets the minimum value for N.
	 */
	public void setN(int n) {
		setMinimumN(n);

	}

	public List<HRACSymbol> getSymbolsInclFrCommands() {
		List<HRACSymbol> allSymbols = new ArrayList<>();
		if (symbols != null) {
			allSymbols.addAll(symbols);
		}
		for (HRACForDup hracSymbol : commands) {
			allSymbols.addAll(hracSymbol.getSymbols());
		}
		return allSymbols;
	}

	public void addDirective(String name, String value) {
		directives.put(name, value);
	}

	public void addDirective(String name, int value) {
		addDirective(name, value + "");
	}

	public void addAddDirective(String name, String value) {
		additionalDirectives.put(name, value);
	}

	public void addAddDirective(String name, int value) {
		addDirective(name, value + "");
	}

	public void addAddDirectives(Map<String, String> additionals) {
		additionalDirectives.putAll(additionals);
	}
}
