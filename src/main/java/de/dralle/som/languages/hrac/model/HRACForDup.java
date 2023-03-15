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
		HRACForDup clone = null;
		try {
			clone = (HRACForDup) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (cmd != null) {
			clone.cmd = cmd.clone();
		}
		if(model!=null) {
			clone.model=model.clone();
		}
		if(range!=null) {
			clone.range=(HRACForDupRange) range.clone();
		}
		return clone;
	}

	private HRACCommand cmd = null;

	
	public static <V, K> void putNoOverwrite(Map<K, V> src, Map<K, V> tgt) {
		if (src != null) {
			if (tgt != null) {
				for (Entry<K, V> entry : src.entrySet()) {
					K key = entry.getKey();
					V val = entry.getValue();
					tgt.putIfAbsent(key, val);

				}
			}
		}
	}
/**
 * Note: Not Recursive on purpose.
 * @param symbolNameReplacementMap
 */
	public void replaceTargetOnCommand(Map<String, String> symbolNameReplacementMap) {
		if(cmd!=null) {
			HRACMemoryAddress ma = cmd.getTarget();			
			HRACSymbol symbol = ma.getSymbol();
			symbol.setName(symbolNameReplacementMap.getOrDefault(symbol.getName(), symbol.getName()));		
		}
	}
	/**
	 * Note: Not recursive on purpose.
	 * @param symbolNameReplacementMap
	 * @param suffix
	 * @return
	 */
	public Map<String, String> renameLabels(Map<String, String> symbolNameReplacementMap, String suffix) {
		if(symbolNameReplacementMap==null) {
			symbolNameReplacementMap=new HashMap<>();
		}
		if(cmd!=null) {
			HRACSymbol label = cmd.getLabel();
			if(label!=null){
				String newName=label.getName()+suffix;
				symbolNameReplacementMap.put(label.getName(), newName);
				label.setName(newName);
			}				
		}
		return symbolNameReplacementMap;
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
	public List<HRACModel> precompileChilds(String suffix,Map<String, String> symbolNameReplacementList) {
		List<HRACModel> retList=new ArrayList<>();
		if(model!=null) {
			model.setMinimumN(parent.getN());
			if(range!=null) {
				for (int i = 0; i < range.getRange(parent).length; i++) {
					int si = range.getRange(parent)[i];
					HRACModel modelClone = model.clone();
					modelClone.addAddDirective("i", si);
					modelClone.precompile(suffix+"_FD"+id+"_"+i, symbolNameReplacementList);
					retList.add(modelClone);
				}
			}else {
				model.precompile(suffix+"_FD"+id, symbolNameReplacementList);
				retList.add(model);
			}			
		}		
		return retList;
	}

	public int getCommandCountRecursive(int n) {
		int cnt=0;
		if(range==null) {
			if(cmd!=null) {
				cnt+=1;
			}
			if(model!=null) {
				cnt+=model.getCommandCount(n);
			}
		}else {
			int[] rng = range.getRange(parent);			
			if(cmd!=null) {
				cnt+=rng.length;
			}if(model!=null) {
				for (int i = 0; i < rng.length; i++) {
					int j = rng[i];
					model.addAddDirective("i", j);
					cnt+=model.getCommandCount(n);
				}
			}
		}
		return cnt;
	}

	public int getSymbolBitCount(int n) {
		int cnt=0;
		if(range==null) {			
			if(model!=null) {
				cnt+=model.getSymbolBitCnt(n);
			}
		}else {
			int[] rng = range.getRange(parent);
		if(model!=null) {
			for (int i = 0; i < rng.length; i++) {
				int j = rng[i];
				model.addAddDirective("i", j);
				cnt+=model.getSymbolBitCnt(n);
			}
			}
		}
		return cnt;
	}

	

}
