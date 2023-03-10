package de.dralle.som.languages.hrac.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.dralle.som.IHeap;
import de.dralle.som.ISetN;

/**
 * Holds a single command or an entire HRACChildModel
 * 
 * @author Nils
 *
 */
public class HRACForDup implements ISetN, IHeap, Cloneable {
	private static int runId;
	private int id;
	private HRACModel parent;

	public HRACModel getModel() {
		return model;
	}

	public void setModel(HRACModel model) {
		this.model = model;
	}

	public HRACCommand getCmd() {
		return cmd;
	}

	public void setCmd(HRACCommand cmd) {
		this.cmd = cmd;
	}

	private HRACForDupRange range = null;
	private HRACModel model = null;

	@Override
	protected HRACForDup clone() {
		HRACForDup clone = new HRACForDup();
		if (cmd != null) {
			clone.cmd = cmd.clone();
		}
		return clone;
	}

	private HRACCommand cmd = null;
	private Map<String,String> localSymbolNameReplacementMap=new HashMap<>();

	public List<HRACCommand> getCommands() {
		return getCommands(null,true);
	}
	
	public static <V, K> void putNoOverwrite(Map<K,V> src,Map<K,V> tgt) {
		if(src!=null) {
			if(tgt!=null) {
				for (Entry<K, V> entry : src.entrySet()) {
					K key = entry.getKey();
					V val = entry.getValue();
					tgt.putIfAbsent(key, val);
					
				}
			}
		}
	}

	public List<HRACCommand> getCommands(Map<String, String> symbolNameReplacementMa,boolean retainLabel) {
		List<HRACCommand> returnList = new ArrayList<>();
		putNoOverwrite(symbolNameReplacementMa, localSymbolNameReplacementMap);		
		if (cmd != null) {
			HRACMemoryAddress ma = cmd.getTarget();
			if (ma != null && ma.isOffsetSpecial()) {
				HRACCommand ncmd = cmd.clone();
				if (!retainLabel) {
					ncmd.setLabel(null);
				}
				ma = ma.clone();
				ma.setOffset(parent.getDirectiveAsInt(ma.getOffsetSpecialnName()));
				ma.setOffsetSpecial(false);
				HRACSymbol symbol = ma.getSymbol();
				symbol.setName(localSymbolNameReplacementMap.getOrDefault(symbol.getName(), symbol.getName()));
				ncmd.setTarget(ma);
				returnList.add(ncmd);
			} else {
				returnList.add(cmd);
			}
		}
		if (model != null) {
			model.addAddDirectives(parent.getAllDirectives());
			List<HRACForDup> childModelCommands = model.getCommands();
			for (int i = 0; i < range.getRange(parent).length; i++) {
				int si = range.getRange(parent)[i];
				model.addAddDirective("i", si);
				for (HRACForDup hracForDup : childModelCommands) {
					hracForDup.parent = model;
					returnList.addAll(hracForDup.getCommands(localSymbolNameReplacementMap,i == 0));
				}
			}
		}
		return returnList;
	}
	public List<HRACSymbol> getSymbols() {
		return getSymbols(null);
	}
	public List<HRACSymbol> getSymbols(Map<String, String> symbolNameReplacementMap) {
		List<HRACSymbol> returnList = new ArrayList<>();
		putNoOverwrite(symbolNameReplacementMap, localSymbolNameReplacementMap);
		if (model != null) {
			List<HRACForDup> childModelCommands = model.getCommands();
			List<HRACSymbol> childModelSymbols = model.getSymbolsInclFrCommands();
			for (int i = 0; i < range.getRange(parent).length; i++) {
				int si = range.getRange(parent)[i];
				model.addAddDirective("i", si);
				for (HRACSymbol symbol : childModelSymbols) {
					symbol = symbol.clone();
					String newSymbolName = symbol.getName() + "_FD" + id+"_"+si;
					localSymbolNameReplacementMap.put(symbol.getName(), newSymbolName);
					symbol.setName(newSymbolName);
					if (symbol.isBitCntSpecial()) {
						symbol.setBitCntSpecial(false);
						symbol.setBitCnt(model.getDirectiveAsInt(symbol.getSpecialName()));
					}
					if (symbol.getTargetSymbol() != null) {
						HRACMemoryAddress ma = symbol.getTargetSymbol();
						symbol=ma.getSymbol();
						symbol.setName(localSymbolNameReplacementMap.getOrDefault(symbol.getName(), symbol.getName()));
					}
					returnList.add(symbol);
				}
			}
		}
		return returnList;
	}

	public HRACForDup(HRACCommand cmd) {
		super();
		this.cmd = cmd;
		id = runId++;
	}

	public HRACForDup() {
		// TODO Auto-generated constructor stub
	}

	public String asCode() {
		if (cmd != null) {
			return cmd.asCode();
		}
		if (model != null) {
			return String.format("for %s dup:\n{\n%s\n}\n", range.asCode(), model.asCode());
		}
		return "";
	}

	@Override
	public String toString() {
		return "HRACForDup [asCode()=" + asCode() + "]";
	}

	public HRACForDupRange getRange() {
		return range;
	}

	public void setRange(HRACForDupRange range) {
		this.range = range;
	}

	@Override
	public void setHeapSize(int cnt) {
		if (model != null) {
			model.setHeapSize(cnt);
		}

	}

	@Override
	public int getHeapSize() {
		if (model != null) {
			return model.getHeapSize();
		}
		return 0;
	}

	@Override
	public int getN() {
		if (model != null) {
			return model.getN();
		}
		if (parent != null) {
			parent.getDirectiveAsInt("N");
		}
		return 0;// assuming special is n
	}

	@Override
	public void setN(int n) {
		if (model != null) {
			model.setN(n);
		}
	}

	public HRACModel getParent() {
		return parent;
	}

	public void setParent(HRACModel parent) {
		this.parent = parent;
	}
}
