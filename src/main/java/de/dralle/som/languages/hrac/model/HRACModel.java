/**
 * 
 */
package de.dralle.som.languages.hrac.model;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.dralle.som.AbstractSomMemspace;
import de.dralle.som.IHeap;
import de.dralle.som.ISetN;
import de.dralle.som.Opcode;
import de.dralle.som.Util;
import de.dralle.som.languages.hras.model.HRASCommand;
import de.dralle.som.languages.hras.model.HRASModel;
import de.dralle.som.languages.hras.model.HRASMemoryAddress;

/**
 * @author Nils
 *
 */
public class HRACModel implements ISetN, IHeap, Cloneable {

	private static final String HRAC_HEAP_START_MARKER = "HRAC_HEAP_START";

	@Override
	public HRACModel clone() {
		HRACModel clone = null;
		try {
			clone = (HRACModel) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clone.builtins = new HashMap<>(builtins);
		clone.directives = new HashMap<>(directives);
		clone.additionalDirectives = new HashMap<>(additionalDirectives);
		if (symbols != null) {
			clone.symbols = new ArrayList<>();
			for (HRACSymbol hracForDup : symbols) {
				clone.symbols.add(hracForDup.clone());
			}
		}
		if (commands != null) {
			clone.commands = new ArrayList<>();
			for (HRACForDup hracForDup : commands) {
				clone.commands.add(hracForDup.clone());
			}
		}
		return clone;
	}

	private Map<String, Integer> builtins;
	private Map<String, String> directives;// as parsed

	public Map<String, String> getDirectives() {
		return directives;
	}

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
		builtins = new HashMap<>(Util.getBuiltinAdresses());
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
	
	private List<Map.Entry<AbstractHRACMemoryAddress, Boolean>> initOnceAddresses=new ArrayList<Map.Entry<AbstractHRACMemoryAddress,Boolean>>();
	
	public void addInitOnceAdress(AbstractHRACMemoryAddress adr,boolean set) {
		addInitOnceAdress(new AbstractMap.SimpleEntry<AbstractHRACMemoryAddress, Boolean>(adr, set));
	}
	
	public void addInitOnceAdress(Map.Entry<AbstractHRACMemoryAddress, Boolean> ent) {
		initOnceAddresses.add(ent);
	}

	public List<Map.Entry<AbstractHRACMemoryAddress, Boolean>> getInitOnceAddresses() {
		return initOnceAddresses;
	}

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

	int getSymbolBitCnt(int n) {
		addAddDirective("N", n);
		int cnt = 0;
		for (HRACSymbol s : symbols) {
			if (isSymbolNameAllowed(s.getName())) {
				if (s.getTargetSymbol() == null) {
					if (s.isBitCntSpecial()) {
						cnt += getDirectiveAsInt(s.getSpecialName());
					} else {
						cnt += s.getBitCnt();
					}
				}
			}
		}
		for (HRACForDup hracForDup : commands) {
			hracForDup.setParent(this);
			cnt += hracForDup.getSymbolBitCount(n);
		}
		return cnt;
	}

	private boolean isSymbolNameAllowed(String name) {
		return !builtins.containsKey(name) && !"HEAP".equals(name);
	}

	private boolean checkN(int n) {
		additionalDirectives.put("N", n + "");
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
		if (symbol.equals(HRAC_HEAP_START_MARKER)) {
			return getHeapStartAddress(n);
		}
		return -1;
	}

	private int getHeapStartAddress(int n) {
		return getFixedBitCount(n) + getSymbolBitCnt(n);
	}

	/**
	 * Only works for precompiled models (or thos without any for). Update:Should
	 * work for all.
	 * 
	 * @param n
	 * @return
	 */
	private int getCommandBitCount(int n) {
		int commandSize = getCommandSize(n);
		int cmdCnt = 1;// one command will be added during the compile
		cmdCnt += getCommandCount(n);
		return (cmdCnt) * commandSize;
	}

	public int getCommandCount(int n) {
		int cmdCnt = 0;
		addAddDirective("N", n);
		for (HRACForDup hracForDup : commands) {
			hracForDup.setParent(this);
			cmdCnt += hracForDup.getCommandCountRecursive(n);
		}
		return cmdCnt;
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
		for (Entry<AbstractHRACMemoryAddress, Boolean> hracForDup : initOnceAddresses) {
			sb.append((  hracForDup.getValue()?"setonce":"clearonce")+" "+hracForDup.getKey());
		}
		for (String symbolString : getCommandssAsStrings()) {
			sb.append(symbolString);
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}

	public void precompile(String suffix, Map<String, String> symbolNameReplacementList, boolean retainLabels) {
		addAddDirective("N", findN());
		if (suffix == null) {
			suffix = "";
		}
		if (symbolNameReplacementList == null) {
			symbolNameReplacementList = new HashMap<>();
		}
		Map<String, String> localSymbolNameReplacementList = new HashMap<>(symbolNameReplacementList);
		for (HRACSymbol hracForDup : symbols) {// rename symbols
			String oldName = hracForDup.getName();
			String newName = oldName + suffix;
			hracForDup.setName(newName);
			localSymbolNameReplacementList.put(oldName, newName);
		}
		for (HRACForDup hracForDup : commands) {// rename command labels
			String oldLabelName = null;
			if (hracForDup.getCmd() != null) {
				HRACSymbol lbl = hracForDup.getCmd().getLabel();
				if (lbl != null) {
					oldLabelName = lbl.getName();
				}

			}
			hracForDup.renameLabels(localSymbolNameReplacementList, suffix);
			String newLabelName = null;
			if (retainLabels) { // retain original label name by creating a mirror symbol for it
				if (hracForDup.getCmd() != null) {
					HRACSymbol lbl = hracForDup.getCmd().getLabel();
					if (lbl != null) {
						newLabelName = lbl.getName();
						HRACSymbol originalLblMirror = new HRACSymbol(oldLabelName);
						originalLblMirror.setTargetSymbol(new NamedHRACMemoryAddress((newLabelName)));
						symbols.add(originalLblMirror);
					}
				}
			}
		}
		for (HRACSymbol hracForDup : symbols) {// replace target symbols, no need to touch fixed addresses
			if (hracForDup.getTargetSymbol() != null) {
				AbstractHRACMemoryAddress ma = hracForDup.getTargetSymbol();
				if (ma instanceof NamedHRACMemoryAddress) {
					String name = ((NamedHRACMemoryAddress) ma).getName();
					String localName = localSymbolNameReplacementList.getOrDefault(name, name);
					((NamedHRACMemoryAddress) ma).setName(localName);
				}
			}
		}
		for (HRACForDup hracForDup : commands) {// replace command targets
			hracForDup.replaceTargetOnCommand(localSymbolNameReplacementList);
		}
		for (HRACSymbol hracForDup : symbols) {// resolve symbols bitcnt
			if (hracForDup.isBitCntSpecial()) {
				hracForDup.setBitCntSpecial(false);
				hracForDup.setBitCnt(getDirectiveAsInt(hracForDup.getSpecialName()));
			}
		}
		for (HRACSymbol symbol : symbols) {// resolve symbols targets
			if (symbol.getTargetSymbol() != null) {
				AbstractHRACMemoryAddress ma = symbol.getTargetSymbol();
				if (ma.isOffsetSpecial()) {
					ma.setOffset(getDirectiveAsInt(ma.getOffsetSpecialnName()));
					ma.setOffsetSpecial(false);
				}
			}
		}
		for (HRACForDup hracForDup : commands) {// resolve command targets, only on individual commands
			if (hracForDup.getCmd() != null) {
				HRACCommand cmd = hracForDup.getCmd();
				AbstractHRACMemoryAddress ma = cmd.getTarget();
				if (ma.isOffsetSpecial()) {
					ma.setOffset(getDirectiveAsInt(ma.getOffsetSpecialnName()));
					ma.setOffsetSpecial(false);
				}
			}
		}
		List<HRACCommand> newCommandList = new ArrayList<>();
		for (HRACForDup hracForDup : commands) {// precompile childs
			if (hracForDup.getCmd() != null) {
				newCommandList.add(hracForDup.getCmd());
			}
			hracForDup.setParent(this);
			List<HRACModel> precompiledChildModels = hracForDup.precompileChilds(suffix,
					localSymbolNameReplacementList);
			for (HRACModel hracModel : precompiledChildModels) {
				for (HRACSymbol hracModel2 : hracModel.symbols) {
					symbols.add(hracModel2);
				}
				for (HRACForDup hracCommand : hracModel.commands) {
					if (hracCommand.getCmd() != null) {
						newCommandList.add(hracCommand.getCmd());
					}
				}
			}
		}
		commands = new ArrayList<>();
		for (HRACCommand hracCommand : newCommandList) {
			addCommand(hracCommand);
		}

	}

	public HRASModel compileToHRAS() {
		HRACModel toc = clone();
		int n = toc.findN();
		toc.additionalDirectives.put("N", n + "");
		toc.precompile("", new HashMap<>(), true);
		HRASModel m = new HRASModel();
		// add calculate n as directive to be used later on
		m.setN(n);
		m.setStartAddressExplicit(true);
		int startAddress = toc.getStartAdress(n);
		m.setStartAdress(startAddress);
		m.setNextCommandAddress(startAddress);
		for (Entry<String, Integer> entry : toc.builtins.entrySet()) {
			String key = entry.getKey();
			Integer val = entry.getValue();
			m.addSymbol(key, new HRASMemoryAddress(val));
		}
		int nxtSymbolAddress = getFixedBitCount(n);
		// iterate over all symbols and block all directly used addresses
		for (HRACSymbol s : toc.symbols) {
			if (s.getTargetSymbol() != null) {
				AbstractHRACMemoryAddress tgt = s.getTargetSymbol();
				if (tgt instanceof FixedHRACMemoryAddress) {
					int adr = 0;
					Integer ofs = tgt.getOffset();
					if (ofs != null) {
						adr = ofs.intValue();
					}
					// if offset is directive, replace the directive with its value
					if (tgt.isOffsetSpecial()) {
						adr = getDirectiveAsInt(tgt.getOffsetSpecialnName());
					}
					adr += ((FixedHRACMemoryAddress) tgt).getAddress();
					if(adr>nxtSymbolAddress) {
						nxtSymbolAddress=adr+1;
					}
				}
			}
		}
		for (HRACSymbol s : toc.symbols) {
			if (s.getTargetSymbol() == null) {
				int address = nxtSymbolAddress;
				if (s.isBitCntSpecial()) {
					nxtSymbolAddress += getDirectiveAsInt(s.getSpecialName());
				} else {
					nxtSymbolAddress += s.getBitCnt();
				}
				m.addSymbol(s.getName(), new HRASMemoryAddress(address));
			} else {
				AbstractHRACMemoryAddress tgt = s.getTargetSymbol();
				HRASMemoryAddress tgHras = new HRASMemoryAddress();
				if (tgt instanceof NamedHRACMemoryAddress) {
					tgHras.setSymbol(((NamedHRACMemoryAddress) tgt).getName());
				}else if(tgt instanceof FixedHRACMemoryAddress){
					int tgtAdr=((FixedHRACMemoryAddress) tgt).getAddress();
					if(tgtAdr<0) {
						System.out.println("Warning: (HRAC -> HRAS) Symbol "+s.getName()+" points to negative address.");
					}
					tgHras.setSymbol(tgtAdr+"");					
				}
				tgHras.setAddressOffset(tgt.getOffset());
				m.addSymbol(s.getName(), tgHras);
			}
		}
		for (var hracForDup : initOnceAddresses) {
			HRASMemoryAddress newmadr = new HRASMemoryAddress();
			newmadr.setAddressOffset(hracForDup.getKey().getOffset());
			if(hracForDup.getKey() instanceof FixedHRACMemoryAddress) {
				FixedHRACMemoryAddress f = (FixedHRACMemoryAddress)hracForDup.getKey();
				newmadr.setSymbol(f.getAddress());
			}else if(hracForDup.getKey() instanceof NamedHRACMemoryAddress) {
				NamedHRACMemoryAddress na = (NamedHRACMemoryAddress)hracForDup.getKey();
				newmadr.setSymbol(na.getName());
			}
			m.addInitOnceValue(newmadr, hracForDup.getValue());
		}
		m.addSymbol(HRAC_HEAP_START_MARKER, new HRASMemoryAddress(toc.getHeapStartAddress(n)));// place HRAC heap start
																								// marker

		HRASCommand clrAdrEval = new HRASCommand();
		clrAdrEval.setOp(Opcode.NAW);
		clrAdrEval.setAddress(new HRASMemoryAddress("ADR_EVAL"));
		HRASMemoryAddress assignedAddress = m.addCommand(clrAdrEval);
		m.addSymbol("HRAS_PROGRAM_START", assignedAddress);
		int i = 0;
		for (HRACForDup cf : toc.commands) {
			if (cf.getCmd() != null) {
				HRACCommand c = cf.getCmd();
				HRASCommand hrasc = new HRASCommand();
				hrasc.setOp(c.getOp());
				AbstractHRACMemoryAddress hracCmdTgt = c.getTarget();
				HRASMemoryAddress address = null;
				if (hracCmdTgt instanceof NamedHRACMemoryAddress) {
					address = new HRASMemoryAddress(((NamedHRACMemoryAddress) hracCmdTgt).getName());
				}
				if (hracCmdTgt instanceof FixedHRACMemoryAddress) {
					int tgtAdr=((FixedHRACMemoryAddress) hracCmdTgt).getAddress();
					if(tgtAdr<0) {
						System.out.println("Warning: (HRAC -> HRAS) Command "+cf+" points to negative address.");
					}
					address = new HRASMemoryAddress(tgtAdr);
				}
				address.setAddressOffset(c.getTarget().getOffset());
				hrasc.setAddress(address);
				assignedAddress = m.addCommand(hrasc);
				if (i++ == 0) {
					m.addSymbol("HRAC_PROGRAM_START", assignedAddress);// mark start of HRAC
				}
				if (c.getLabel() != null) {
					m.addSymbol(c.getLabel().getName(), assignedAddress);
				}
			}
		}
		return m;
	}

	/**
	 * This method discards the info where a specific HRAS command will be written
	 * to.
	 * 
	 * @param m
	 * @return
	 */
	public static HRACModel compileFromHRAS(HRASModel m) {
		HRACModel newm = new HRACModel();
		newm.setMinimumN(m.getN());
		Map<String, Integer> builtinsToCheck = Util.getBuiltinAdresses();
		for (Entry<String, HRASMemoryAddress> s : m.getSymbols().entrySet()) {
			if (!builtinsToCheck.containsKey(s.getKey())) {
				HRACSymbol news = new HRACSymbol(s.getKey());
				news.setBitCnt(1);
				newm.addSymbol(news);
			}
		}
		List<Entry<HRASMemoryAddress, Boolean>> otiAddresses = m.getInitOnceList();
		for (Entry<HRASMemoryAddress, Boolean> entry : otiAddresses) {
			HRASMemoryAddress hrasAdr = entry.getKey();
			Integer hrasOfs = hrasAdr.getAddressOffset();
			String hrasName = hrasAdr.getSymbol();
			boolean added=false;
			if(hrasOfs==null||hrasOfs.equals(0)) {
				int adr=-1;
				try {
					adr=Util.decodeInt(hrasName);//try if its a reference to a fixed address
				newm.addInitOnceAdress(new FixedHRACMemoryAddress(adr), entry.getValue());	
				added=true;
				}
				catch(Exception e) {
					
				}
			}
			if(!added) {
				NamedHRACMemoryAddress otiadr = new NamedHRACMemoryAddress(hrasName);
				otiadr.setOffset(hrasOfs);
				newm.addInitOnceAdress(otiadr, entry.getValue());
			}
		}
		int i = 0;
		for (Entry<HRASMemoryAddress, HRASCommand> entry : m.getCommands().entrySet()) {
			HRASMemoryAddress key = entry.getKey();
			HRASCommand val = entry.getValue();
			boolean omit = false;
			if (i++ == 0) {// check first command
				if (val.getOp() == Opcode.NAW && val.getAddress().equals(new HRASMemoryAddress("ADR_EVAL"))) {
					omit = true;
				}
			}
			if (!omit) {
				newm.addCommand(new HRACCommand(val));
			}
		}
		return newm;
	}

	@Override
	public String toString() {
		return asCode();
	}

	@Override
	/**
	 * Returns the N calculated for this Model.
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

	/**
	 * Adds commands and symbols from one hrac model to another (from other to
	 * target, returns target)
	 * 
	 * @param target
	 * @param other
	 * @return
	 */
	public void addCommandsAndSymbolsFromOther(HRACModel other) {
		List<HRACSymbol> osymbols = other.getSymbols();
		List<HRACForDup> oCommands = other.getCommands();
		if (osymbols != null) {
			for (HRACSymbol s : osymbols) {
				addSymbol(s);
			}
		}
		if (oCommands != null) {
			for (HRACForDup hracCommand : oCommands) {
				addCommand(hracCommand);
			}
		}
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
		addAddDirective(name, value + "");
	}

	public void addAddDirectives(Map<String, String> additionals) {
		additionalDirectives.putAll(additionals);
	}

	public HRACSymbol getSymbolByName(String name) {
		for (HRACSymbol hracForDup : symbols) {
			if (hracForDup.getName().equals(name)) {
				return hracForDup;
			}

		}
		return null;
	}
}
