/**
 * 
 */
package de.dralle.som.languages.hrac.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import de.dralle.som.IHeap;
import de.dralle.som.ISetN;
import de.dralle.som.ISomMemspace;
import de.dralle.som.Opcode;
import de.dralle.som.Util;
import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACIntegerNode;
import de.dralle.som.languages.hras.model.AbstractHRASMemoryAddress;
import de.dralle.som.languages.hras.model.ExpressionHRASMemoryAddress;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hras.model.HRASCommand;
import de.dralle.som.languages.hras.model.HRASIntegerNode;
import de.dralle.som.languages.hras.model.HRASModel;
import de.dralle.som.languages.hras.model.SymbolHRASMemoryAddress;

/**
 * @author Nils
 *
 */
public class HRACModel implements ISetN, IHeap, Cloneable {

	private static final Logger log = Logger.getLogger(HRACModel.class.getName());
	private static final String HRAC_HEAP_START_MARKER = "HRAC_HEAP_START";

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
		int lastHeaderBit = ISomMemspace.START_ADDRESS_START + m.getN() - 1;// everything up to this is assumed to be
																			// fixed
		for (Entry<String, AbstractHRASMemoryAddress> s : m.getSymbols().entrySet()) {
			HRACSymbol news = new HRACSymbol(s.getKey());
			AbstractHRASMemoryAddress adr = s.getValue();
			if (adr instanceof ExpressionHRASMemoryAddress) {
				HRASAbstractExpressionNode adrv = ((ExpressionHRASMemoryAddress) adr).getExpression();
				int adrvi = adrv.calculateNumericalValue();
				if (adrvi <= lastHeaderBit) {

					news.setTargetSymbol(new FixedHRACMemoryAddress(adrv.compileToHRAC()));

				}
			}
			news.setBitCnt(1);
			newm.addSymbol(news);

		}
		List<Entry<AbstractHRASMemoryAddress, Boolean>> otiAddresses = m.getInitOnceList();
		for (Entry<AbstractHRASMemoryAddress, Boolean> entry : otiAddresses) {
			AbstractHRASMemoryAddress hrasAdr = entry.getKey();
			HRACAbstractExpressionNode hrasOfs = hrasAdr.getAddressOffset().compileToHRAC();
			String hrasName = null;
			if (hrasAdr instanceof SymbolHRASMemoryAddress) {
				hrasName = ((SymbolHRASMemoryAddress) hrasAdr).getSymbol();
			} else if (hrasAdr instanceof ExpressionHRASMemoryAddress) {
				hrasName = ((ExpressionHRASMemoryAddress) hrasAdr).getExpression().calculateNumericalValue() + "";// TODO:
																													// //
																													// now
			}
			boolean added = false;
			if (hrasOfs == null || hrasOfs.equals(0)) {
				int adr = -1;
				try {
					adr = Util.decodeInt(hrasName);// try if its a reference to a fixed address
					newm.addInitOnceAdress(new FixedHRACMemoryAddress(adr), entry.getValue());
					added = true;
				} catch (Exception e) {

				}
			}
			if (!added) {
				NamedHRACMemoryAddress otiadr = new NamedHRACMemoryAddress(hrasName);
				otiadr.setOffset(hrasOfs);
				newm.addInitOnceAdress(otiadr, entry.getValue());
			}
		}
		int i = 0;
		for (Entry<AbstractHRASMemoryAddress, HRASCommand> entry : m.getCommands().entrySet()) {
			AbstractHRASMemoryAddress key = entry.getKey();
			HRASCommand val = entry.getValue();
			boolean omit = false;
			if (i++ == 0) {// check first command
				if (val.getOp() == Opcode.NAW && val.getAddress().equals(new SymbolHRASMemoryAddress("ADR_EVAL"))) {
					omit = true;
				}
			}
			if (!omit) {
				newm.addCommand(new HRACCommand(val));
			}
		}
		return newm;
	}

	private Map<String, Object> globalDirectives;// global directives. During precompile, only global directives from
													// child models/commands will be passed on.

	private Map<String, Object> directives;// Directives can either be String or an expression (for int IntegerNode
											// shall be used. But Integer should also be checked, just in case). Making
											// it Object is only a workaround however, the long-term solutiopn would be
											// an additional directive class to handle this.

	private Map<String, Object> additionalDirectives;// additionals added at runtime

	private List<HRACSymbol> symbols;

	private List<HRACForDup> commands;

	private List<Map.Entry<AbstractHRACMemoryAddress, Boolean>> initOnceAddresses = new ArrayList<Map.Entry<AbstractHRACMemoryAddress, Boolean>>();

	public HRACModel() {
		symbols = new ArrayList<>();
		commands = new ArrayList<>();
		globalDirectives = new HashMap<String, Object>();
		directives = new HashMap<>();
		additionalDirectives = new HashMap<>();
	}

	public void addAddDirective(String name, HRACAbstractExpressionNode value) {
		additionalDirectives.put(name, value);
	}

	public void addAddDirective(String name, int value) {
		addAddDirective(name, new HRACIntegerNode(value));
	}

	public void addAddDirective(String name, String value) {
		additionalDirectives.put(name, value);
	}

	public void addAddDirectives(Map<String, String> additionals) {
		additionalDirectives.putAll(additionals);
	}

	public void addGlobalDirective(String name, HRACAbstractExpressionNode value) {
		globalDirectives.put(name, value);
	}

	public void addGlobalDirective(String name, int value) {
		addGlobalDirective(name, new HRACIntegerNode(value));
	}

	public void addGlobalDirective(String name, String value) {
		globalDirectives.put(name, value);
	}

	public void addGlobalDirectives(Map<String, String> globals) {
		globalDirectives.putAll(globals);
	}

	public void addCommand(HRACCommand c) {
		addCommand(new HRACForDup(c));
	}

	public void addCommand(HRACForDup c) {
		if (commands == null) {
			commands = new ArrayList<>();
		}
		commands.add(c);
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

	public void addDirective(String name, int value) {
		addDirective(name, new HRACIntegerNode(value));
	}

	public void addDirective(String name, Object value) {
		directives.put(name, value);

	}

	public void addDirective(String name, String value) {
		directives.put(name, value);
	}

	public void addInitOnceAdress(AbstractHRACMemoryAddress adr, boolean set) {
		addInitOnceAdress(new AbstractMap.SimpleEntry<AbstractHRACMemoryAddress, Boolean>(adr, set));
	}

	public void addInitOnceAdress(Map.Entry<AbstractHRACMemoryAddress, Boolean> ent) {
		initOnceAddresses.add(ent);
	}

	public void addSymbol(HRACSymbol symbol) {
		if (symbols == null) {
			symbols = new ArrayList<>();
		}
		symbols.add(symbol);
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
			sb.append((hracForDup.getValue() ? "setonce" : "clearonce") + " " + hracForDup.getKey());
		}
		for (String symbolString : getCommandssAsStrings()) {
			sb.append(symbolString);
			sb.append(System.lineSeparator());
		}
		return sb.toString();
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

	@Override
	public HRACModel clone() {
		HRACModel clone = null;
		try {
			clone = (HRACModel) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clone.globalDirectives = new HashMap<>(cloneDirectiveMap(globalDirectives, true));
		clone.directives = new HashMap<>(cloneDirectiveMap(directives, true));
		clone.additionalDirectives = new HashMap<>(cloneDirectiveMap(additionalDirectives, true));
		if (initOnceAddresses != null) {
			clone.initOnceAddresses = new ArrayList<Map.Entry<AbstractHRACMemoryAddress, Boolean>>();
			for (Entry<AbstractHRACMemoryAddress, Boolean> hracForDup : initOnceAddresses) {
				clone.initOnceAddresses.add(new AbstractMap.SimpleEntry<AbstractHRACMemoryAddress, Boolean>(
						hracForDup.getKey().clone(), hracForDup.getValue()));
			}
		}

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

	private <T1, T2> Map<T1, T2> cloneDirectiveMap(Map<T1, T2> directives, boolean addNonClonables) { // since clone is
																										// protected on
																										// Object, a
																										// generic
																										// approach
																										// needs this
																										// rather ugly
																										// reflective
																										// call
		Map<T1, T2> retMap = new HashMap<T1, T2>();
		if (directives != null) {
			for (Entry<T1, T2> entry : directives.entrySet()) {
				T1 key = entry.getKey();
				T2 val = entry.getValue();
				T1 keyClone = null;
				T2 valClone = null;
				if (key instanceof Cloneable) {
					// Getting the clone() method using reflection
					Method cloneMethod = null;
					try {
						cloneMethod = key.getClass().getDeclaredMethod("clone");
					} catch (NoSuchMethodException | SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (cloneMethod != null) {
						cloneMethod.setAccessible(true); // Necessary if clone() is protected or private

						// Invoking the clone() method reflectively
						try {
							keyClone = (T1) cloneMethod.invoke(key);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				if (val instanceof Cloneable) {
					// Getting the clone() method using reflection
					Method cloneMethod = null;
					try {
						cloneMethod = key.getClass().getDeclaredMethod("clone");
					} catch (NoSuchMethodException | SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (cloneMethod != null) {
						cloneMethod.setAccessible(true); // Necessary if clone() is protected or private

						// Invoking the clone() method reflectively
						try {
							valClone = (T2) cloneMethod.invoke(val);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				if (addNonClonables) {
					if (keyClone == null) {
						keyClone = key;
					}
					if (valClone == null) {
						valClone = val;
					}
				}
				if (keyClone != null) {
					if (valClone != null || addNonClonables) {
						retMap.put(keyClone, valClone);
					}
				}
			}
		}
		return retMap;
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
		int nxtSymbolAddress = getFixedBitCount(n);
		// iterate over all symbols and block all directly used addresses
		for (HRACSymbol s : toc.symbols) {
			if (s.getTargetSymbol() != null) {
				AbstractHRACMemoryAddress tgt = s.getTargetSymbol();
				if (tgt instanceof FixedHRACMemoryAddress) {
					int adr = 0;
					HRACAbstractExpressionNode ofsET = tgt.getOffset();
					if (ofsET != null) {// shóuld offset be directive, replace the directive with its value
						adr = ofsET.getResolvedExpressionTree(this).calculateNumericalValue();
					}

					adr += ((FixedHRACMemoryAddress) tgt).getAddress().getResolvedExpressionTree(this)
							.calculateNumericalValue();
					if (adr > nxtSymbolAddress) {
						nxtSymbolAddress = adr + 1;
					}
				}
			}
		}
		for (HRACSymbol s : toc.symbols) {
			if (s.getTargetSymbol() == null) {
				int address = nxtSymbolAddress;

				HRACAbstractExpressionNode et = s.getBitCnt();
				if (et != null) {
					et = et.getResolvedExpressionTree(this);
					nxtSymbolAddress += et.calculateNumericalValue();
				} else {
					nxtSymbolAddress++;
				}
				m.addSymbol(s.getName(), new SymbolHRASMemoryAddress(address));
			} else {
				AbstractHRACMemoryAddress tgt = s.getTargetSymbol();
				AbstractHRASMemoryAddress tgtHras = null;
				if (tgt instanceof NamedHRACMemoryAddress) {
					tgtHras = new SymbolHRASMemoryAddress(((NamedHRACMemoryAddress) tgt).getName());
				} else if (tgt instanceof FixedHRACMemoryAddress) {
					HRASAbstractExpressionNode tgtAdr = ((FixedHRACMemoryAddress) tgt).getAddress().compileToHRAS(this);
					tgtHras = new ExpressionHRASMemoryAddress(tgtAdr);
					if (tgtAdr.calculateNumericalValue() < 0) {
						log.warning("Warning: (HRAC -> HRAS) Symbol " + s.getName() + " points to negative address.");

					}
				}
				if (tgt.getOffset() != null) {
					tgtHras.setAddressOffset(tgt.getOffset().compileToHRAS(this));
				}
				m.addSymbol(s.getName(), tgtHras);
			}
		}
		for (var hracForDup : initOnceAddresses) {
			HRACAbstractExpressionNode hracOfs = hracForDup.getKey().getOffset();
			SymbolHRASMemoryAddress newmadr = new SymbolHRASMemoryAddress();
			if (hracOfs != null) {
				newmadr.setAddressOffset(hracForDup.getKey().getOffset().compileToHRAS(this));
			} else {
				newmadr.setAddressOffset(new HRASIntegerNode(0));
			}

			if (hracForDup.getKey() instanceof FixedHRACMemoryAddress) {
				FixedHRACMemoryAddress f = (FixedHRACMemoryAddress) hracForDup.getKey();
				newmadr.setSymbol(f.getAddress().toString());
			} else if (hracForDup.getKey() instanceof NamedHRACMemoryAddress) {
				NamedHRACMemoryAddress na = (NamedHRACMemoryAddress) hracForDup.getKey();
				newmadr.setSymbol(na.getName());
			}
			m.addInitOnceValue(newmadr, hracForDup.getValue());
		}
		m.addSymbol(HRAC_HEAP_START_MARKER, new SymbolHRASMemoryAddress(toc.getHeapStartAddress(n)));// place HRAC heap
																										// start
		// marker

		HRASCommand clrAdrEval = new HRASCommand();
		// Add NAW ADR_EVAL
		clrAdrEval.setOp(Opcode.NAW);
		clrAdrEval.setAddress(new SymbolHRASMemoryAddress("ADR_EVAL"));
		m.addSymbol("ADR_EVAL", new ExpressionHRASMemoryAddress(ISomMemspace.ADR_EVAL_ADDRESS));
		AbstractHRASMemoryAddress assignedAddress = m.addCommand(clrAdrEval);
		m.addSymbol("HRAS_PROGRAM_START", assignedAddress);
		int i = 0;
		for (HRACForDup cf : toc.commands) {
			if (cf.getCmd() != null) {
				HRACCommand c = cf.getCmd();
				HRASCommand hrasc = new HRASCommand();
				hrasc.setOp(c.getOp());
				AbstractHRACMemoryAddress hracCmdTgt = c.getTarget();
				AbstractHRASMemoryAddress address = null;
				if (hracCmdTgt instanceof NamedHRACMemoryAddress) {
					address = new SymbolHRASMemoryAddress(((NamedHRACMemoryAddress) hracCmdTgt).getName());
				}
				if (hracCmdTgt instanceof FixedHRACMemoryAddress) {
					HRASAbstractExpressionNode tgtAdr = ((FixedHRACMemoryAddress) hracCmdTgt).getAddress()
							.compileToHRAS(this);
					if (tgtAdr.calculateNumericalValue() < 0) {
						System.out.println("Warning: (HRAC -> HRAS) Command " + cf + " points to negative address.");
					}
					address = new ExpressionHRASMemoryAddress(tgtAdr);
				}
				if (c.getTarget().getOffset() != null) {
					address.setAddressOffset(c.getTarget().getOffset().compileToHRAS(this));
				}
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

	private int findN() {
		int n = 0;
		do {
			n++;
		} while (!checkN(n));
		return n;
	}

	public Map<String, Object> getAllDirectives() {
		Map<String, Object> retMap = new HashMap<>();
		retMap.putAll(additionalDirectives);
		retMap.putAll(directives);
		return retMap;
	}

	public List<String> getAllLabelsRecursive() {
		List<String> labels = new ArrayList<String>();
		for (HRACForDup string : commands) {
			HRACCommand cmd = string.getCmd();
			if (cmd != null) {
				HRACSymbol lbl = cmd.getLabel();
				if (lbl != null) {
					labels.add(lbl.getName());
				}
			}
			HRACModel model = string.getModel();
			if (model != null) {
				labels.addAll(model.getAllLabelsRecursive());
			}
		}
		return labels;

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

	public List<HRACForDup> getCommands() {
		return commands;
	}

	private int getCommandSize(int n) {
		return 1 + n;
	}

	private List<String> getCommandssAsStrings() {
		List<String> tmp = new ArrayList<>();
		for (HRACForDup c : commands) {

			tmp.add(String.format("%s", c.asCode()));
		}
		return tmp;
	}

	public HRACAbstractExpressionNode getDirectiveAsExpressionTree(String name) {
		Object sv = additionalDirectives.get(name);
		if (sv == null) {
			sv = directives.get(name);
		}
		if (sv == null) {
			return new HRACIntegerNode(0);
		}
		if (sv instanceof HRACAbstractExpressionNode) {
			return (HRACAbstractExpressionNode) sv;
		}
		if (sv instanceof Integer) {
			return new HRACIntegerNode((Integer) sv);
		}
		String svStr = sv.toString();
		try {
			int svI = Util.decodeInt(svStr);
			return new HRACIntegerNode(svI);
		} catch (Exception e) {
			log.warning("Directive " + name + " not a number: " + sv);
		}

		return new HRACIntegerNode(0);
	}

	public Map<String, Object> getDirectives() {
		return directives;
	}

	private List<String> getDirectivesAsStrings() {
		List<String> tmp = new ArrayList<>();
		for (Entry<String, Object> symbol : directives.entrySet()) {
			tmp.add(String.format(";%s=\"%s\"", symbol.getKey(), symbol.getValue()));
		}
		return tmp;
	}

	private int getFixedBitCount(int n) {
		return 11 + n;
	}

	public int getHeapSize() {
		return getDirectiveAsExpressionTree("heap").calculateNumericalValue();
	}

	private int getHeapStartAddress(int n) {
		return getFixedBitCount(n) + getSymbolBitCnt(n);
	}

	public List<Map.Entry<AbstractHRACMemoryAddress, Boolean>> getInitOnceAddresses() {
		return initOnceAddresses;
	}

	public int getMinimumN() {
		return getDirectiveAsExpressionTree("n").calculateNumericalValue();
	}

	@Override
	/**
	 * Returns the N calculated for this Model.
	 */
	public int getN() {
		return findN();
	}

	public int getStartAdress(int n) {
		return (int) Math.pow(2, n) - getCommandBitCount(n);
	}

	int getSymbolBitCnt(int n) {
		addAddDirective("N", n);
		int cnt = 0;
		for (HRACSymbol s : symbols) {
			if (isSymbolNameAllowed(s.getName())) {
				if (s.getTargetSymbol() == null) {
					HRACAbstractExpressionNode et = s.getBitCnt();
					if (et != null) {
						et = et.getResolvedExpressionTree(this);
						cnt += et.calculateNumericalValue();
					} else {
						cnt++;
					}
				}
			} else {
				log.warning("Symbol name " + s.getName() + " is not allowed here");
			}
		}
		for (HRACForDup hracForDup : commands) {
			hracForDup.setParent(this);
			cnt += hracForDup.getSymbolBitCount(n);
		}
		return cnt;
	}

	public HRACSymbol getSymbolByName(String name) {
		for (HRACSymbol hracForDup : symbols) {
			if (hracForDup.getName().equals(name)) {
				return hracForDup;
			}

		}
		return null;
	}

	public List<HRACSymbol> getSymbols() {
		return symbols;
	}

	private List<String> getSymbolsAsStrings() {
		List<String> tmp = new ArrayList<>();
		for (HRACSymbol symbol : symbols) {
			tmp.add(String.format("%s", symbol.asCode()));
		}
		return tmp;
	}

	/**
	 * Checks wether the name for a symbol is allowed or not. Sine the generation of
	 * built-in symbols will not be a thing anymore, the currently only disallowed
	 * name is HRAC_HEAP_START_MARKER
	 * 
	 * @param name
	 * @return
	 */
	private boolean isSymbolNameAllowed(String name) {
		return !HRAC_HEAP_START_MARKER.equals(name);
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
		for (HRACSymbol symbl : symbols) {// resolve directives (and et´s) to a value if used to specify bitcnt on
											// symbols
			if (symbl.getBitCnt() == null) {
				symbl.setBitCnt(1);
			} else {
				symbl.setBitCnt(symbl.getBitCnt().resolve(this));
			}

		}
		for (HRACSymbol symbol : symbols) {// resolve symbols targets offsets
			if (symbol.getTargetSymbol() != null) {
				AbstractHRACMemoryAddress ma = symbol.getTargetSymbol();
				ma.resolve(this);
			}
		}
		for (HRACForDup hracForDup : commands) {// resolve command targets, only on individual commands
			if (hracForDup.getCmd() != null) {
				HRACCommand cmd = hracForDup.getCmd();
				AbstractHRACMemoryAddress ma = cmd.getTarget();
				ma.resolve(this);
			}
		}
		List<HRACCommand> newCommandList = new ArrayList<>();
		for (HRACForDup hracForDup : commands) {// precompile childs
			if (hracForDup.getCmd() != null) {
				newCommandList.addAll(hracForDup.getPrecompiledCmds());
			}
			hracForDup.setParent(this);
			List<HRACModel> precompiledChildModels = hracForDup.precompileChilds(suffix,
					localSymbolNameReplacementList);// precompile/resolve/expand loops
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

	public void setHeapSize(int heapSize) {
		directives.put("heap", heapSize + "");
	}

	public void setMinimumN(int minimumN) {
		this.directives.put("n", minimumN + "");
	}

	@Override
	/**
	 * Sets the minimum value for N.
	 */
	public void setN(int n) {
		setMinimumN(n);

	}

	@Override
	public String toString() {
		return asCode();
	}
}
