/**
 * 
 */
package de.dralle.som.languages.hrbs.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

	public void addChild(HRBSModel c) {
		if (childs == null) {
			childs = new HashMap<>();
		}
		childs.put(c.getName(), c);
		if (cmdUsageTracker == null) {
			cmdUsageTracker = new HashMap<>();
		}
		cmdUsageTracker.put(c.getName(), 0);
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

	public int getMinimumN() {
		int rn = minimumN;
		if (childs != null) {
			for (HRBSModel hrbsModel : childs.values()) {
				if (rn < hrbsModel.getMinimumN()) {
					rn = hrbsModel.getMinimumN();
				}
			}
		}
		return rn;
	}

	public void setMinimumN(int minimumN) {
		this.minimumN = minimumN;
	}

	public int getHeapSize() {
		int rh = heapSize;
		if (childs != null) {
			for (HRBSModel hrbsModel : childs.values()) {
				rh += hrbsModel.getHeapSize();
			}
		}
		return rh;
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
		return String.format(";heap = %d", heapSize);
	}

	private String getNDirective() {
		return String.format(";n = %d", minimumN);
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

	public String asCode() {
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
				sb.append(hrbsModel.asCode());
				sb.append(System.lineSeparator());
			}
		}
		return sb.toString();
	}

	public HRACModel compileToHRAC(String uniqueUsageId, Map<String, HRBSMemoryAddress> params, String label) {
		Map<String, String> lclSymbolNameMap = new HashMap<>();
		HRACModel m = new HRACModel();
		m.setN(minimumN);
		for (HRBSSymbol s : symbols) {
			String symbolName=generateHRACSymbolName(s, name, uniqueUsageId);
			lclSymbolNameMap.put(s.getName(), symbolName);
			HRACSymbol hracSymbol = getAsHRACSymbol(s, params, lclSymbolNameMap);
			if (s.getTargetSymbol() == null) {
				switch (s.getType()) {
				case global:
					symbolName = s.getName();
					break;
				case shared:
					symbolName = String.format("%s_%s", name, s.getName());
					break;
				default: // local is default
					symbolName = String.format("%s_%s_%s", name, uniqueUsageId, s.getName());
					break;

				}
				lclSymbolNameMap.put(s.getName(), symbolName);
				HRACSymbol hracSymbol = new HRACSymbol();
				hracSymbol.setName(symbolName);
				hracSymbol.setBitCnt(s.getBitCnt());
				hracSymbol.setBitCntISN(s.isBitCntISN());

				m.addSymbol(hracSymbol);
			} else {
				HRBSMemoryAddress tgt = s.getTargetSymbol();
				if (tgt.isDeref()) {
					HRACMemoryAddress cTGTAddressForS = new HRACMemoryAddress();
					HRACSymbol cTgtSymbol = new HRACSymbol();

					String cTgtSymbolName = null;// init is null, because the target name will be searched for in the
													// params and locally renamed symbols
					// test if target symbol is a parameter
					HRBSMemoryAddress paramTarget = null;
					if (params != null) {
						paramTarget = params.get(tgt.getSymbol().getName());
						if (paramTarget != null) {
							cTgtSymbolName = paramTarget.getSymbol().getName();
						}
					}

					if (cTgtSymbolName == null) {
						cTgtSymbolName = lclSymbolNameMap.getOrDefault(tgt.getSymbol().getName(),
								tgt.getSymbol().getName());
					}
					cTgtSymbol.setName(cTgtSymbolName);

					String odCommandLabelName = "CL_" + cTgtSymbolName;
					HRACSymbol odCLSymbol = new HRACSymbol();
					odCLSymbol.setName(odCommandLabelName);

					cTGTAddressForS.setSymbol(odCLSymbol);
					cTGTAddressForS.setOffset(1 + tgt.getDerefOffset());

					HRACSymbol hracSymbol = new HRACSymbol();
					hracSymbol.setName(s.getName());
					hracSymbol.setBitCnt(s.getBitCnt());
					hracSymbol.setBitCntISN(s.isBitCntISN());
					hracSymbol.setTargetSymbol(cTGTAddressForS);

					m.addSymbol(hracSymbol);

					HRACMemoryAddress cTGTAddressForC = new HRACMemoryAddress();
					cTGTAddressForC.setSymbol(cTgtSymbol);
					cTGTAddressForC.setOffset(tgt.getOffset());
					if (paramTarget != null) {
						if (cTGTAddressForC.getOffset() == null) {
							cTGTAddressForC.setOffset(paramTarget.getOffset());
						} else {
							if (paramTarget.getOffset() != null) {
								paramTarget.setOffset(paramTarget.getOffset() + cTGTAddressForC.getOffset());
							}
						}
					}

					HRACCommand odCommand = new HRACCommand();
					odCommand.setLabel(odCLSymbol);
					odCommand.setOp(Opcode.NAR);
					odCommand.setTarget(cTGTAddressForC);

					m.addCommand(odCommand);
				} else {
					HRACMemoryAddress cTGTAddress = new HRACMemoryAddress();
					HRACSymbol cTgtSymbol = new HRACSymbol();
					String cTgtSymbolName = null;// init is null, because the target name will be searched for in the
													// params and locally renamed symbols
					// test if target symbol is a parameter
					HRBSMemoryAddress paramTarget = null;
					if (params != null) {
						paramTarget = params.get(tgt.getSymbol().getName());
						if (paramTarget != null) {
							cTgtSymbolName = paramTarget.getSymbol().getName();
						}
					}

					if (cTgtSymbolName == null) {
						cTgtSymbolName = lclSymbolNameMap.getOrDefault(tgt.getSymbol().getName(),
								tgt.getSymbol().getName());
					}
					cTgtSymbol.setName(cTgtSymbolName);
					cTGTAddress.setSymbol(cTgtSymbol);
					cTGTAddress.setOffset(tgt.getOffset());

					if (paramTarget != null) {
						if (cTGTAddress.getOffset() == null) {
							cTGTAddress.setOffset(paramTarget.getOffset());
						} else {
							if (paramTarget.getOffset() != null) {
								paramTarget.setOffset(paramTarget.getOffset() + cTGTAddress.getOffset());
							}
						}
					}

					HRACSymbol hracSymbol = new HRACSymbol();
					hracSymbol.setName(s.getName());
					hracSymbol.setBitCnt(s.getBitCnt());
					hracSymbol.setBitCntISN(s.isBitCntISN());
					hracSymbol.setTargetSymbol(cTGTAddress);

					m.addSymbol(hracSymbol);
				}
			}
		}
		localizeCommandLabels(commands, lclSymbolNameMap, name, uniqueUsageId);
		for (int i = 0; i < commands.size(); i++) {
			HRBSCommand c = commands.get(i);
			convertAnyCommand(c, name, uniqueUsageId, (i == 0 ? label : null), params, lclSymbolNameMap, childs, m);
		}
		
		return m;
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
	 * @return
	 */
	private static HRACCommand convertNARCommand(HRBSCommand c, String parentCmdName, String cmdExecId,
			Map<String, HRBSMemoryAddress> params, Map<String, String> symbolNameReplacementMap, HRACModel m) {
		return convertStandardCommands(c, Opcode.NAR, parentCmdName, cmdExecId, params, symbolNameReplacementMap, m);
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
			Map<String, HRBSMemoryAddress> params, Map<String, String> symbolNameReplacementMap,
			Map<String, HRBSModel> availChildsCommands, HRACModel m) {
		String cmdName = c.getCmd();
		boolean standardCommand = false;
		for (Opcode op : Opcode.values()) {
			if (op.name().equals(cmdName)) {
				HRACCommand converted = convertStandardCommands(c, op, parentCmdName, cmdExecId, params,
						symbolNameReplacementMap, m);
				standardCommand = true;
				if (label != null) {
					if (converted.getLabel() == null) {
						converted.setLabel(new HRACSymbol(label));
					} else {
						HRACSymbol newSymbol = new HRACSymbol(label);
						newSymbol.setTargetSymbol(new HRACMemoryAddress(converted.getLabel()));
						m.addSymbol(newSymbol);
					}
				}

				m.addCommand(converted);
			}
		}
		if (!standardCommand) {
			HRBSModel cmdModel = availChildsCommands.get(cmdName);
			String lclSmblName = getTargetSymbolName(c.getLabel(), params, symbolNameReplacementMap);
			HRACModel compiledCmdModel = cmdModel.compileToHRAC(getCurrentCommandUsage(c) + "",
					assembleParamMap(cmdModel, c), lclSmblName);
			m = addCommandsAndSymbolsFromOther(m, compiledCmdModel);
		}
		incCommandUsage(c);
	}

	private static void localizeCommandLabels(List<HRBSCommand> command, Map<String, String> symbolNameReplacementMap,
			String parentCmdName, String cmdExecId) {
		for (HRBSCommand hrbsCommand : command) {
			String cmdLbl = hrbsCommand.getLabel();
			String localized = generateHRACSymbolName(cmdLbl, HRBSSymbolType.local, parentCmdName, cmdExecId);
			symbolNameReplacementMap.put(cmdLbl, localized);
		}
	}

	private static Map<String, HRBSMemoryAddress> assembleParamMap(HRBSModel m, HRBSCommand c) {
		List<HRBSMemoryAddress> cTargets = c.getTarget();
		List<String> modelParams = m.getParams();
		Map<String, HRBSMemoryAddress> retMap = new HashMap<>();
		;
		for (int i = 0; i < modelParams.size(); i++) {
			String p = modelParams.get(i);
			retMap.put(p, cTargets.get(i));
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
	 * @return
	 */
	private static HRACCommand convertNAWCommand(HRBSCommand c, String parentCmdName, String cmdExecId,
			Map<String, HRBSMemoryAddress> params, Map<String, String> symbolNameReplacementMap, HRACModel m) {
		return convertStandardCommands(c, Opcode.NAW, parentCmdName, cmdExecId, params, symbolNameReplacementMap, m);
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
	 * @return
	 */
	private static HRACCommand convertStandardCommands(HRBSCommand c, Opcode opcode, String parentCmdName,
			String cmdExecId, Map<String, HRBSMemoryAddress> params, Map<String, String> symbolNameReplacementMap,
			HRACModel m) {
		HRACCommand tgtC = new HRACCommand();
		if (c.getLabel() != null) {
			String lclSmblName = getTargetSymbolName(c.getLabel(), params, symbolNameReplacementMap);
			tgtC.setLabel(new HRACSymbol(lclSmblName));
		}
		tgtC.setOp(opcode);
		HRACMemoryAddress tgtAdr = calculateHRACMemoryAddress(c.getTarget().get(0), parentCmdName, cmdExecId, params,
				symbolNameReplacementMap, m);
		tgtC.setTarget(tgtAdr);

		return tgtC;
	}

	/**
	 * Convert the HRBS Address to a HRAC address (includinng required name and
	 * offset changes). if the hrbs address is a deref, this will also generate the
	 * required commands and add them to the hrac model.
	 * 
	 * @param originalMemoryAddress
	 * @param parentCmdName
	 * @param cmdExecId
	 * @param params
	 * @param localSymbolNames
	 * @param m
	 * @return
	 */
	private static HRACMemoryAddress calculateHRACMemoryAddress(HRBSMemoryAddress originalMemoryAddress,
			String parentCmdName, String cmdExecId, Map<String, HRBSMemoryAddress> params,
			Map<String, String> localSymbolNames, HRACModel m) {
		HRACSymbol newTargetSymbol = getAsHRACSymbol(originalMemoryAddress.getSymbol(), params, localSymbolNames, parentCmdName, cmdExecId, m);
		Integer newOffset = null;
		if (params != null) {
			HRBSMemoryAddress passedDownSymbol = params.get(originalMemoryAddress.getSymbol().getName());
			if (passedDownSymbol != null) {
				newOffset = passedDownSymbol.getOffset();
			}
		}
		if (newOffset != null) {
			if (originalMemoryAddress.getOffset() != null) {
				newOffset = newOffset + originalMemoryAddress.getOffset();
			}
		} else {
			newOffset = originalMemoryAddress.getOffset();
		}
		if (originalMemoryAddress.isDeref()) {
			String odCommandLabelName = "CL_"
					+ generateHRACSymbolName(originalMemoryAddress.getSymbol(), parentCmdName, cmdExecId);
			HRACSymbol odCLSymbol = new HRACSymbol();
			odCLSymbol.setName(odCommandLabelName);

			HRACMemoryAddress cTGTAddressForS = new HRACMemoryAddress();
			cTGTAddressForS.setSymbol(odCLSymbol);
			if(originalMemoryAddress.getDerefOffset()==null) {
				cTGTAddressForS.setOffset(1);
			}else {
				cTGTAddressForS.setOffset(1 + originalMemoryAddress.getDerefOffset());
			}
			

			HRACMemoryAddress cTGTAddressForC = new HRACMemoryAddress();
			cTGTAddressForC.setSymbol(getAsHRACSymbol(originalMemoryAddress.getSymbol(), params, localSymbolNames, parentCmdName, cmdExecId, m));
			cTGTAddressForC.setOffset(newOffset);

			HRACCommand odCommand = new HRACCommand();
			odCommand.setLabel(odCLSymbol);
			odCommand.setOp(Opcode.NAR);
			odCommand.setTarget(cTGTAddressForC);

			m.addCommand(odCommand);

			return cTGTAddressForS;
		} else {
			HRACMemoryAddress newTgtAddress = new HRACMemoryAddress();
			newTgtAddress.setSymbol(newTargetSymbol);
			newTgtAddress.setOffset(newOffset);
			return newTgtAddress;
		}

	}

	private static HRACSymbol getAsHRACSymbol(HRBSSymbol symbol, Map<String, HRBSMemoryAddress> params,
			Map<String, String> localSymbolNames,String parentCmdName,String execId,HRACModel m) {
		HRACSymbol s = new HRACSymbol();
		s.setName(getTargetSymbolName(symbol.getName(), params, localSymbolNames));
		s.setBitCnt(symbol.getBitCnt());
		s.setBitCntISN(symbol.isBitCntISN());
		if(symbol.getTargetSymbol()!=null) {
			s.setTargetSymbol(calculateHRACMemoryAddress(symbol.getTargetSymbol(), parentCmdName	, execId, params, localSymbolNames, m));
		}
		return s;
	}

	private static String getTargetSymbolName(String originalSymbolName, Map<String, HRBSMemoryAddress> params,
			Map<String, String> localSymbolNames) {
		String targetName = null;
		if (params != null) {
			HRBSMemoryAddress passedDownSymbol = params.get(originalSymbolName);
			if (passedDownSymbol != null) {
				targetName = passedDownSymbol.getSymbol().getName();
			}
		}
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
	 * Returns the N calculatedx for this Model.
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
		if (params != null) {
			params = new ArrayList<>();
		}
		params.add(param);
	}

	public List<HRASModel> getChilds() {
		return childs;
	}

	public void setChilds(List<HRASModel> childs) {
		this.childs = childs;
	}

}
