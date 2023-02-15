/**
 * 
 */
package de.dralle.som.languages.hrbs.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.text.AsyncBoxView.ChildState;

import de.dralle.som.AbstractSomMemspace;
import de.dralle.som.ByteArrayMemspace;
import de.dralle.som.IHeap;
import de.dralle.som.IMemspace;
import de.dralle.som.ISetN;
import de.dralle.som.ISomMemspace;
import de.dralle.som.Opcode;
import de.dralle.som.languages.hrac.model.HRACCommand;
import de.dralle.som.languages.hrac.model.HRACMemoryAddress;
import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hrac.model.HRACSymbol;
import de.dralle.som.languages.hras.model.Command;
import de.dralle.som.languages.hras.model.HRASModel;
import de.dralle.som.languages.hras.model.MemoryAddress;

/**
 * @author Nils
 *
 */
public class HRBSModel implements ISetN, IHeap {

	private String name;
	private List<String> params;
	/**
	 * Maps available commands by their name to their models.
	 */
	private Map<String, HRBSModel> childs;
	private static Map<String, Integer> cmdUsageTracker;
	private int heapSize;
	private int minimumN;

	public boolean addChild(HRBSModel c) {
		return addChild(c.getName(), c);
	}

	public boolean addChild(String name, HRBSModel c) {
		if (childs == null) {
			childs = new HashMap<>();
		}
		if (!childs.containsKey(name)) {
			if (this.name != name) {
				childs.put(name, c);// prevent from adding itsself, prevent recursion
				if (cmdUsageTracker == null) {
					cmdUsageTracker = new HashMap<>();
				}
				cmdUsageTracker.put(name, 0);
				return true;
			}
		}
		return false;
	}

	public void propagateChildList() {
		if (this.childs != null) {
			for (Entry<String, HRBSModel> entry : childs.entrySet()) {
				String key = entry.getKey();
				HRBSModel val = entry.getValue();
				if (val.addChilds(childs) > 0) {
					val.propagateChildList();
				}
			}
		}
	}

	public Collection<HRBSModel> getChildsAsList() {
		if (childs != null) {
			return childs.values();
		} else {
			return new ArrayList<>();
		}
	}

	public Map<String, HRBSModel> getChildsAsMap() {
		if (childs == null) {
			childs = new HashMap<>();
		}
		return childs;
	}

	public int addChilds(Map<String, HRBSModel> childs) {
		int noAdds = 0;
		for (Entry<String, HRBSModel> entry : childs.entrySet()) {
			String key = entry.getKey();
			HRBSModel val = entry.getValue();
			if (addChild(key, val)) {
				noAdds++;
			}
		}
		return noAdds;
	}

	public int addChilds(Collection<HRBSModel> childs) {
		int noAdds = 0;
		for (HRBSModel hrbsModel : childs) {
			if (addChild(hrbsModel)) {
				noAdds++;
			}
		}
		return noAdds;
	}

	private static int incCommandUsage(HRBSCommand c) {
		if (cmdUsageTracker == null) {
			cmdUsageTracker = new HashMap<>();
		}
		int incCmdUsage = getCurrentCommandUsage(c) + 1;
		cmdUsageTracker.put(c.getCmd(), incCmdUsage);
		return incCmdUsage;
	}

	private static int getCurrentCommandUsage(HRBSCommand c) {
		if (cmdUsageTracker == null) {
			cmdUsageTracker = new HashMap<>();
		}
		Integer useCnt = cmdUsageTracker.get(c.getCmd());
		if (useCnt == null) {
			useCnt = 0;
			cmdUsageTracker.put(c.getCmd(), useCnt);
		}
		return useCnt;
	}

	public int getMinimumN(Set<String> checked) {
		if (checked == null) {
			checked = new HashSet<>();
		}
		int rn = minimumN;
		if (!checked.contains(name)) {
			checked.add(name);
			if (childs != null) {
				for (HRBSModel hrbsModel : childs.values()) {
					int childMinimum = hrbsModel.getMinimumN(checked);
					if (childMinimum > rn) {
						rn = childMinimum;
					}
				}
			}
		}
		return rn;
	}

	public int getMinimumN() {
		return getMinimumN(null);
	}

	public void setMinimumN(int minimumN) {
		this.minimumN = minimumN;
	}

	public int getHeapSize(Set<String> added) {
		if (added == null) {
			added = new HashSet<>();
		}
		if (!added.contains(name)) {
			added.add(name);
			int rh = heapSize;
			if (childs != null) {
				for (HRBSModel hrbsModel : childs.values()) {
					rh += hrbsModel.getHeapSize(added);
				}
			}
			return rh;
		} else {
			return 0;
		}
	}

	public int getHeapSize() {
		return getHeapSize(null);
	}

	public void setHeapSize(int heapSize) {
		this.heapSize = heapSize;
	}

	public HRBSModel() {
		symbols = new ArrayList<>();
		commands = new ArrayList<>();
	}

	private List<HRBSSymbol> symbols;
	private List<HRBSCommand> commands;

	public void addSymbol(HRBSSymbol symbol) {
		if (symbols == null) {
			symbols = new ArrayList<>();
		}
		symbols.add(symbol);
	}

	public void addCommand(HRBSCommand c) {
		if (commands == null) {
			commands = new ArrayList<>();
		}
		commands.add(c);
	}

	private String getHeapDirective() {
		return ";heap = " + heapSize;
	}

	private String getNDirective() {
		return ";n = " + minimumN;
	}

	private List<String> getSymbolsAsStrings() {
		List<String> tmp = new ArrayList<>();
		for (HRBSSymbol symbol : symbols) {
			tmp.add(String.format("%s", symbol.asCode()));
		}
		return tmp;
	}

	private List<String> getCommandssAsStrings() {
		List<String> tmp = new ArrayList<>();
		for (HRBSCommand c : commands) {

			tmp.add(String.format("%s", c.asCode()));
		}
		return tmp;
	}

	public String asCode(Set<String> printed) {
		if (printed == null) {
			printed = new HashSet<>();
		}
		if (!printed.contains(name)) {
			printed.add(name);
			StringBuilder sb = new StringBuilder();
			sb.append(name);
			if (params != null) {
				sb.append(" ");
				for (String p : params) {
					sb.append(p);
					sb.append(",");
				}
				sb.deleteCharAt(sb.length() - 1);
			}
			sb.append(":");
			sb.append(System.lineSeparator());
			sb.append(getNDirective());
			sb.append(System.lineSeparator());
			sb.append(getHeapDirective());
			sb.append(System.lineSeparator());
			for (String symbolString : getSymbolsAsStrings()) {
				sb.append(symbolString);
				sb.append(System.lineSeparator());
			}
			for (String symbolString : getCommandssAsStrings()) {
				sb.append(symbolString);
				sb.append(System.lineSeparator());
			}
			if (childs != null) {
				for (HRBSModel hrbsModel : childs.values()) {
					sb.append(hrbsModel.asCode(printed));
					sb.append(System.lineSeparator());
				}
			}
			return sb.toString();
		}
		return "";
	}
	public String asCode() {
		return asCode(null);
	}

	public HRACModel compileToHRAC(String uniqueUsageId, Map<String, HRBSMemoryAddress> params, String label) {
		//copy symbols n commands in local lists
		List<HRBSSymbol  >lclSymbols=new ArrayList<>();
	List<HRBSCommand> lclCommands=new ArrayList<>();
	for (HRBSSymbol s : symbols) {
		lclSymbols.add(s.clone());
	}
	for (HRBSCommand hrbsCommand : commands) {
		lclCommands.add(hrbsCommand.clone());
	}
		Map<String, HRBSMemoryAddress> modifiedParamMap = new HashMap<String, HRBSMemoryAddress>();
		List<HRBSSymbol> additionalSymbols = new ArrayList<>();
		List<HRBSCommand> additionalCommands = new ArrayList<>();
		if (params != null) { // convert all params that are derefs
			for (Map.Entry<String, HRBSMemoryAddress> entry : params.entrySet()) {
				String key = entry.getKey();
				HRBSMemoryAddress val = entry.getValue();
				HRBSMemoryAddress dedereffed = resolveDeref(additionalSymbols, additionalCommands, val);
				modifiedParamMap.put(key, dedereffed);
			}
		}
		lclSymbols.addAll(additionalSymbols);
		lclCommands.addAll(additionalCommands);
		additionalCommands.clear();
		additionalSymbols.clear();
		Map<String, String> lclSymbolNameMap = new HashMap<>();
		HRACModel m = new HRACModel();
		m.setN(getMinimumN());
		m.setHeapSize(getHeapSize());
		for (HRBSSymbol s : lclSymbols) {// convert all mirror symbols that are derefs
			if (s.getTargetSymbol() != null) {
				s.setTargetSymbol(resolveDeref(additionalSymbols, additionalCommands, s.getTargetSymbol()));
			}
		}
		lclSymbols.addAll(additionalSymbols);
		lclCommands.addAll(additionalCommands);
		additionalCommands.clear();
		additionalSymbols.clear();
		if (modifiedParamMap != null) { // create mirror symbol for each param
			for (Entry<String, HRBSMemoryAddress> entry : modifiedParamMap.entrySet()) {
				String key = entry.getKey();
				HRBSMemoryAddress val = entry.getValue();
				HRBSSymbol s = new HRBSSymbol();
				String convertedName = generateHRACSymbolName(key, HRBSSymbolType.local, name, uniqueUsageId) + "_MS";
				s.setName(convertedName);
				s.setTargetSymbol(val);
				lclSymbols.add(s);
				lclSymbolNameMap.put(key, convertedName);
			}
		}
		for (HRBSSymbol s : lclSymbols) {// assume no target symbol is a deref (but be prepared for it anyway,
										// becuase...)
			String symbolName = generateHRACSymbolName(s, name, uniqueUsageId);
			lclSymbolNameMap.put(s.getName(), symbolName);
			HRACSymbol hracSymbol = getAsHRACSymbol(s, lclSymbolNameMap, name, uniqueUsageId, m, childs);
			m.addSymbol(hracSymbol);

		}
		localizeCommandLabels(lclCommands, lclSymbolNameMap, name, uniqueUsageId);
		for (int i = 0; i < lclCommands.size(); i++) {
			HRBSCommand c = lclCommands.get(i);
			convertAnyCommand(c, name, uniqueUsageId, (i == 0 ? label : null), lclSymbolNameMap, childs, m);
		}

		return m;
	}

	private void addCommands(List<HRBSCommand> additionalCommands) {
		for (HRBSCommand hrbsCommand : additionalCommands) {
			addCommand(hrbsCommand);
		}

	}

	private void addSymbols(List<HRBSSymbol> additionalSymbols) {
		for (HRBSSymbol hrbsSymbol : additionalSymbols) {
			addSymbol(hrbsSymbol);
		}
	}

	/**
	 * Adds commands and symbols from one hrac model to another (from other to
	 * target, returns target)
	 * 
	 * @param target
	 * @param other
	 * @return
	 */
	private static HRACModel addCommandsAndSymbolsFromOther(HRACModel target, HRACModel other) {
		List<HRACSymbol> osymbols = other.getSymbols();
		List<HRACCommand> oCommands = other.getCommands();
		if (osymbols != null) {
			for (HRACSymbol s : osymbols) {
				target.addSymbol(s);
			}
		}
		if (oCommands != null) {
			for (HRACCommand hracCommand : oCommands) {
				target.addCommand(hracCommand);
			}
		}
		return target;
	}

	/**
	 * Converts the HRBS command to HRBS. Returns the final command, does not add it
	 * to the HRAC model.
	 * 
	 * @param c
	 * @param parentCmdName
	 * @param cmdExecId
	 * @param params
	 * @param symbolNameReplacementMap
	 * @param m
	 * @param additionalCommands
	 * @return
	 */
	private static HRACCommand convertNARCommand(HRBSCommand c, String parentCmdName, String cmdExecId,
			Map<String, String> symbolNameReplacementMap, HRACModel m, Map<String, HRBSModel> additionalCommands) {
		return convertStandardCommands(c, Opcode.NAR, parentCmdName, cmdExecId, symbolNameReplacementMap, m,
				additionalCommands);
	}

	/**
	 * Converts the HRBS command to HRBS. Does not return the final command(s),
	 * instead adds them to the given model. The label, if given, will be added to
	 * the command (if its a default one), used as a mirror symbol (if a label is
	 * already assigned) or passed down to a child command
	 * 
	 * @param c
	 * @param parentCmdName
	 * @param cmdExecId
	 * @param params
	 * @param symbolNameReplacementMap
	 * @param m
	 * @return
	 */
	private static void convertAnyCommand(HRBSCommand c, String parentCmdName, String cmdExecId, String label,
			Map<String, String> symbolNameReplacementMap, Map<String, HRBSModel> availChildsCommands, HRACModel m) {
		String cmdName = c.getCmd();
		boolean standardCommand = false;
		if (label != null) {
			if (c.getLabel() == null) {
				c.setLabel(label);
			} else {
				HRACSymbol newSymbol = new HRACSymbol(label);
				String lclSmblName = getTargetSymbolName(c.getLabel(), symbolNameReplacementMap);
				newSymbol.setTargetSymbol(new HRACMemoryAddress(new HRACSymbol(lclSmblName)));
				m.addSymbol(newSymbol);
			}
		}
		for (Opcode op : Opcode.values()) {
			if (op.name().equals(cmdName)) {
				HRACCommand converted = convertStandardCommands(c, op, parentCmdName, cmdExecId,
						symbolNameReplacementMap, m, availChildsCommands);
				standardCommand = true;
				m.addCommand(converted);
			}
		}
		if (!standardCommand) {
			HRBSModel cmdModel = availChildsCommands.get(cmdName);
			String lclSmblName = getTargetSymbolName(c.getLabel(), symbolNameReplacementMap);
			HRACModel compiledCmdModel = cmdModel.compileToHRAC(getCurrentCommandUsage(c) + "",
					assembleParamMap(cmdModel, c, symbolNameReplacementMap), lclSmblName);
			m = addCommandsAndSymbolsFromOther(m, compiledCmdModel);
		}
		incCommandUsage(c);
	}

	private static void localizeCommandLabels(List<HRBSCommand> command, Map<String, String> symbolNameReplacementMap,
			String parentCmdName, String cmdExecId) {
		for (HRBSCommand hrbsCommand : command) {
			String cmdLbl = hrbsCommand.getLabel();
			if (cmdLbl != null) {
				String localized = generateHRACSymbolName(cmdLbl, HRBSSymbolType.local, parentCmdName, cmdExecId);

				symbolNameReplacementMap.put(cmdLbl, localized);
			}
		}
	}

	private static Map<String, HRBSMemoryAddress> assembleParamMap(HRBSModel m, HRBSCommand c,
			Map<String, String> lclSymbolReplacementMap) {
		List<HRBSMemoryAddress> cTargets = c.getTarget();
		List<String> modelParams = m.getParams();
		Map<String, HRBSMemoryAddress> retMap = new HashMap<>();
		if (modelParams != null) {
			for (int i = 0; i < modelParams.size(); i++) {
				String p = modelParams.get(i);
				HRBSMemoryAddress cTgt = cTargets.get(i);
				HRBSSymbol cTgtSymbol = cTgt.getSymbol();
				String convertedName = getTargetSymbolName(cTgtSymbol.getName(), lclSymbolReplacementMap);
				cTgtSymbol.setName(convertedName);
				retMap.put(p, cTgt);
			}
		}
		return retMap;
	}

	/**
	 * Converts the HRBS command to HRBS. Returns the final command, does not add it
	 * to the HRAC model.
	 * 
	 * @param c
	 * @param parentCmdName
	 * @param cmdExecId
	 * @param params
	 * @param symbolNameReplacementMap
	 * @param m
	 * @param additionalCommands
	 * @return
	 */
	private static HRACCommand convertNAWCommand(HRBSCommand c, String parentCmdName, String cmdExecId,
			Map<String, String> symbolNameReplacementMap, HRACModel m, Map<String, HRBSModel> additionalCommands) {
		return convertStandardCommands(c, Opcode.NAW, parentCmdName, cmdExecId, symbolNameReplacementMap, m,
				additionalCommands);
	}

	/**
	 * Converts the HRBS command to HRBS. Returns the final command, does not add it
	 * to the HRAC model.
	 * 
	 * @param c
	 * @param parentCmdName
	 * @param cmdExecId
	 * @param params
	 * @param symbolNameReplacementMap
	 * @param m
	 * @param additionalCommands
	 * @return
	 */
	private static HRACCommand convertStandardCommands(HRBSCommand c, Opcode opcode, String parentCmdName,
			String cmdExecId, Map<String, String> symbolNameReplacementMap, HRACModel m,
			Map<String, HRBSModel> additionalCommands) {
		HRACCommand tgtC = new HRACCommand();
		if (c.getLabel() != null) {
			String lclSmblName = getTargetSymbolName(c.getLabel(), symbolNameReplacementMap);
			tgtC.setLabel(new HRACSymbol(lclSmblName));
		}
		tgtC.setOp(opcode);
		HRACMemoryAddress tgtAdr = calculateHRACMemoryAddress(c.getTarget().get(0), parentCmdName, cmdExecId,
				symbolNameReplacementMap, m, additionalCommands);
		tgtC.setTarget(tgtAdr);

		return tgtC;
	}

	/**
	 * Method to turn a HRBS address with deref into one without. If the given
	 * address has no deref, it is returned without change (also true for null). If
	 * it has, the required commands/symbols are generated, added to the provided
	 * lists if applicable, and an address without deref is returned.
	 * 
	 * @param m
	 * @param originalMemoryAddress
	 * @return
	 */
	private static HRBSMemoryAddress resolveDeref(List<HRBSSymbol> addSymbols, List<HRBSCommand> addCommands,
			HRBSMemoryAddress originalMemoryAddress) {
		if (originalMemoryAddress != null && originalMemoryAddress.isDeref()) {
			String odCommandLabelName = "DRFL_" + originalMemoryAddress.getSymbol().getName();
			String odSymbolName = "DRFS_" + originalMemoryAddress.getSymbol().getName();

			HRBSSymbol odSymbol = new HRBSSymbol();
			odSymbol.setType(HRBSSymbolType.local);
			odSymbol.setName(odSymbolName);

			HRBSSymbol odCommandLblSymbol = new HRBSSymbol();
			odCommandLblSymbol.setName(odCommandLabelName);
			odCommandLblSymbol.setType(HRBSSymbolType.local);
			HRBSMemoryAddress odCommandLblTargetSymbol = new HRBSMemoryAddress(odCommandLblSymbol);
			if (originalMemoryAddress.getDerefOffset() == null) {
				odCommandLblTargetSymbol.setOffset(1);
			} else {
				odCommandLblTargetSymbol.setOffset(originalMemoryAddress.getDerefOffset() + 1);
			}
			odSymbol.setTargetSymbol(odCommandLblTargetSymbol);
			addSymbols.add(odSymbol);

			HRBSCommand odCommand = new HRBSCommand();
			odCommand.setLabel(odCommandLabelName);
			odCommand.setCmd(Opcode.NAR.name());
			HRBSMemoryAddress odCommandTarget = new HRBSMemoryAddress(originalMemoryAddress.getSymbol());
			odCommandTarget.setOffset(originalMemoryAddress.getOffset());
			odCommand.addTarget(odCommandTarget);
			addCommands.add(odCommand);

			return new HRBSMemoryAddress(odSymbol);
		} else {
			return originalMemoryAddress;
		}
	}

	/**
	 * Convert the HRBS Address to a HRAC address (includinng required name and
	 * offset changes). if the hrbs address is a deref, this will also generate the
	 * required commands and symbols and add them to the hrac model.
	 * 
	 * @param originalMemoryAddress
	 * @param parentCmdName
	 * @param cmdExecId
	 * @param params
	 * @param localSymbolNames
	 * @param m
	 * @param additionalAvailableCommands
	 * @return
	 */
	private static HRACMemoryAddress calculateHRACMemoryAddress(HRBSMemoryAddress originalMemoryAddress,
			String parentCmdName, String cmdExecId, Map<String, String> localSymbolNames, HRACModel m,
			Map<String, HRBSModel> additionalAvailableCommands) {

		if (originalMemoryAddress.isDeref()) {
			List<HRBSSymbol> additionalSymbols = new ArrayList<>();
			List<HRBSCommand> additionalCommands = new ArrayList<>();
			originalMemoryAddress = resolveDeref(additionalSymbols, additionalCommands, originalMemoryAddress);
			for (HRBSSymbol s : additionalSymbols) {
				HRACSymbol hracS = getAsHRACSymbol(s, localSymbolNames, parentCmdName, cmdExecId, m,
						additionalAvailableCommands);
				m.addSymbol(hracS);
			}
			for (HRBSCommand hrbsCommand : additionalCommands) {
				convertAnyCommand(hrbsCommand, parentCmdName, cmdExecId, cmdExecId, localSymbolNames,
						additionalAvailableCommands, m);
			}

		}
		return calculateHRACMemoryAddressNoDeref(originalMemoryAddress, localSymbolNames);
	}

	/**
	 * Convert the HRBS Address to a HRAC address (includinng required name and
	 * offset changes). Assumes no deref.
	 * 
	 * @param originalMemoryAddress
	 * @param parentCmdName
	 * @param cmdExecId
	 * @param params
	 * @param localSymbolNames
	 * @param m
	 * @param additionalAvailableCommands
	 * @return
	 */
	private static HRACMemoryAddress calculateHRACMemoryAddressNoDeref(HRBSMemoryAddress originalMemoryAddress,
			Map<String, String> localSymbolNames) {
		HRACSymbol newTargetSymbol = getAsHRACSymbolNoTgt(originalMemoryAddress.getSymbol(), localSymbolNames);
		Integer newOffset = null;

		if (originalMemoryAddress.getOffset() != null) {
			newOffset = originalMemoryAddress.getOffset();
		}
		HRACMemoryAddress newTgtAddress = new HRACMemoryAddress();
		newTgtAddress.setSymbol(newTargetSymbol);
		newTgtAddress.setOffset(newOffset);
		return newTgtAddress;
	}

	private static HRACSymbol getAsHRACSymbol(HRBSSymbol symbol, Map<String, String> localSymbolNames,
			String parentCmdName, String execId, HRACModel m, Map<String, HRBSModel> additionalCommands) {
		HRACSymbol s = new HRACSymbol();
		s = getAsHRACSymbolNoTgt(symbol, localSymbolNames);
		if (symbol.getTargetSymbol() != null) {
			s.setTargetSymbol(calculateHRACMemoryAddress(symbol.getTargetSymbol(), parentCmdName, execId,
					localSymbolNames, m, additionalCommands));
		}
		return s;
	}

	private static HRACSymbol getAsHRACSymbolNoTgt(HRBSSymbol symbol, Map<String, String> localSymbolNames) {
		HRACSymbol s = new HRACSymbol();
		s.setName(getTargetSymbolName(symbol.getName(), localSymbolNames));
		s.setBitCnt(symbol.getBitCnt());
		s.setBitCntISN(symbol.isBitCntISN());
		return s;
	}

	private static String getTargetSymbolName(String originalSymbolName, Map<String, String> localSymbolNames) {
		String targetName = null;
		if (targetName == null) {
			if (localSymbolNames != null) {
				targetName = localSymbolNames.getOrDefault(originalSymbolName, originalSymbolName);
			}
		}
		return targetName;
	}

	public static String generateHRACSymbolName(String hrbsSymbolName, HRBSSymbolType hrbsSymbolType, String cmdName,
			String cmdExecId) {
		String rString = hrbsSymbolName;
		if (rString == null) {
			rString = "NULL";
		}
		if (hrbsSymbolType == null) {
			hrbsSymbolType = HRBSSymbolType.global;
		}
		switch (hrbsSymbolType) {
		case global:
			return rString;
		case shared:
			return String.format("%s_%s", cmdName, rString);
		default: // local is default
			return String.format("%s_%s_%s", cmdName, cmdExecId, rString);

		}
	}

	public static String generateHRACSymbolName(HRBSSymbol hrbsSymbol, String cmdName, String cmdExecId) {
		return generateHRACSymbolName(hrbsSymbol.getName(), hrbsSymbol.getType(), cmdName, cmdExecId);
	}

	@Override
	public String toString() {
		return asCode();
	}

	@Override
	/**
	 * Returns the N minimum for this Model.
	 */
	public int getN() {
		return getMinimumN();
	}

	@Override
	/**
	 * Sets the minimum value for N.
	 */
	public void setN(int n) {
		setMinimumN(n);

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getParams() {
		return params;
	}

	public void setParams(List<String> params) {
		this.params = params;
	}

	public void addParam(String param) {
		if (params == null) {
			params = new ArrayList<>();
		}
		params.add(param);
	}

	public Map<String, HRBSModel> getChilds() {
		return childs;
	}

	public void setChilds(Map<String, HRBSModel> childs) {
		this.childs = childs;
	}

}
