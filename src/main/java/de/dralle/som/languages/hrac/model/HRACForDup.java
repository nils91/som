package de.dralle.som.languages.hrac.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.dralle.som.IHeap;
import de.dralle.som.ISetN;
import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractDirectiveExpressionTreeNode;
import de.dralle.som.languages.hrac.model.expressiontree.visitors.HRACDirectiveTreeCalculateIntegerValueVisitor;
import de.dralle.som.languages.hrac.model.expressiontree.visitors.HRACResolveDirectiveTreeVisitor;

/**
 * Holds a single command or an entire HRACChildModel
 * 
 * @author Nils
 *
 */
public class HRACForDup implements ISetN, IHeap, Cloneable {
	private static final Logger log = Logger.getLogger(HRACForDup.class.getName());
	private static int runId;
	private static int getn_cnt = 0;

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

	private Integer cachedN;

	private int id;

	private HRACModel parent;

	private IHRACRangeProvider range = null;

	private HRACModel model = null;

	private HRACCommand cmd = null;

	public HRACForDup() {
		// TODO Auto-generated constructor stub
	}

	public HRACForDup(HRACCommand cmd) {
		super();
		this.cmd = cmd;
		id = runId++;
	}

	public String asCode() {
		if (cmd != null) {
			if (range != null) {
				return String.format("for %s dup:\n{\n%s\n}\n", range.asCode(), cmd.asCode());
			}
			return cmd.asCode();
		}
		if (model != null) {
			if (range != null) {
				return String.format("for %s dup:\n{\n%s\n}\n", range.asCode(), model.asCode());
			} else {
				return "{" + System.lineSeparator() + "\t" + model.asCode() + System.lineSeparator() + "}"
						+ System.lineSeparator();
			}
		}
		return "";
	}

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
		if (model != null) {
			clone.model = model.clone();
		}
		if (range != null) {
			clone.range = range.clone();
		}
		return clone;
	}

	/**
	 * Only returns the contained command. use getPrecompile to get all resolved
	 * 
	 * @return
	 */
	public HRACCommand getCmd() {
		return cmd;
	}

	public int getCommandCountRecursive(int n) {
		int cnt = 0;
		if (range == null) {
			if (cmd != null) {
				cnt += 1;
			}
			if (model != null) {
				cnt += model.getCommandCount(n);
			}
		} else {
			HRACAbstractDirectiveExpressionTreeNode[] rng = range.getRange(parent);
			if (cmd != null) {
				cnt += rng.length;
			}
			if (model != null) {
				for (int i = 0; i < rng.length; i++) {
					HRACAbstractDirectiveExpressionTreeNode j = rng[i];
					model.addAddDirective(range.getRunningDirectiveName(), j);
					cnt += model.getCommandCount(n);
				}
			}
		}
		return cnt;
	}

	@Override
	public int getHeapSize() {
		if (model != null) {
			return model.getHeapSize();
		}
		return 0;
	}

	public int getId() {
		return id;
	}

	public HRACModel getModel() {
		return model;
	}

	@Override
	public int getN() {
		getn_cnt++;
		if (getn_cnt % 1000000000 == 0) {
			System.out.println(getn_cnt);
		}
		if (cachedN == null) {
			int n = 0;
			if (model != null) {
				if (model == parent) {
					log.log(Level.SEVERE, "Child and parent are the same object");
					throw new RuntimeException("Child and parent are the same object");
				}
				if (range != null) {
					HRACAbstractDirectiveExpressionTreeNode[] rng = range.getRange(parent);
					for (HRACAbstractDirectiveExpressionTreeNode hracAbstractExpressionNode : rng) {
						String runDir = range.getRunningDirectiveName();
						HRACModel mc = model.clone();
						mc.addAddDirective(runDir, hracAbstractExpressionNode);
						int ln = mc.getN();
						if (ln > n) {
							n = ln;
						}
					}
					for (int i = rng.length; i <= 0; i /= 2) {
						n++;
					}
				} else {
					n = model.getN();
				}
				cachedN = n;
				return n;
			}
			if (parent != null) {
				HRACAbstractDirectiveExpressionTreeNode et = parent.getDirectiveAsExpressionTree("N").clone()
						.accept(new HRACResolveDirectiveTreeVisitor(parent));
				cachedN = et.accept(new HRACDirectiveTreeCalculateIntegerValueVisitor()).intValue();
				return cachedN;
			}
			cachedN = 0;
			return cachedN;// assuming special is n
		}
		return cachedN;
	}

	public HRACModel getParent() {
		return parent;
	}

	public List<HRACCommand> getPrecompiledCmds() {
		List<HRACCommand> cmds = new ArrayList<HRACCommand>();
		if (range == null || parent == null) {
			cmds.add(cmd);
		} else {
			AbstractHRACMemoryAddress cmdTgt = cmd.getTarget();
			if (cmdTgt != null) {
				HRACAbstractDirectiveExpressionTreeNode cmdTOfs = cmdTgt.getOffset();
				for (int i = 0; i < range.getRange(parent).length; i++) {
					HRACAbstractDirectiveExpressionTreeNode si = range.getRange(parent)[i];
					String rangeVar = range.getRunningDirectiveName();
					if (rangeVar == null) {
						rangeVar = "i";
					}
					HRACModel parentClone = parent.clone();
					parentClone.addAddDirective(rangeVar, si);
					HRACAbstractDirectiveExpressionTreeNode cmdOfsRes = null;
					if (cmdTOfs != null) {
						cmdOfsRes = cmdTOfs.accept(
								new HRACResolveDirectiveTreeVisitor(parentClone, new String[] { rangeVar }, true));
					}
					HRACCommand cmdClone = cmd.clone();
					// prevent label duplication #153
					if (i > 0 && cmdClone.getLabel() != null) {
						cmdClone.setLabel(null);
					}
					cmdClone.getTarget().setOffset(cmdOfsRes);
					cmds.add(cmdClone);
				}
			}
		}
		return cmds;
	}

	public IHRACRangeProvider getRange() {
		return range;
	}

	public int getSymbolBitCount(int n) {
		int cnt = 0;
		if (range == null) {
			if (model != null) {
				cnt += model.getSymbolBitCnt(n);
			}
		} else {
			HRACAbstractDirectiveExpressionTreeNode[] rng = range.getRange(parent);
			if (model != null) {
				for (int i = 0; i < rng.length; i++) {
					HRACAbstractDirectiveExpressionTreeNode j = rng[i];
					model.addAddDirective("i", j);
					cnt += model.getSymbolBitCnt(n);
				}
			}
		}
		return cnt;
	}

	public List<HRACModel> precompileChilds(String suffix, Map<String, String> symbolNameReplacementList) {
		List<HRACModel> retList = new ArrayList<>();
		if (model != null) {
			model.setMinimumN(parent.getN());
			if (range != null) {
				for (int i = 0; i < range.getRange(parent).length; i++) {
					HRACAbstractDirectiveExpressionTreeNode si = range.getRange(parent)[i];
					HRACModel modelClone = model.clone();
					modelClone.addAddDirective(range.getRunningDirectiveName(), si);
					modelClone.precompile(suffix + "_FD" + id + "_" + i, symbolNameReplacementList, i == 0);
					retList.add(modelClone);
				}
			} else {
				model.precompile(suffix + "_FD" + id, symbolNameReplacementList, true);
				retList.add(model);
			}
		}
		return retList;
	}

	/**
	 * Note: Not recursive on purpose.
	 * 
	 * @param symbolNameReplacementMap
	 * @param suffix
	 * @return
	 */
	public Map<String, String> renameLabels(Map<String, String> symbolNameReplacementMap, String suffix) {
		if (symbolNameReplacementMap == null) {
			symbolNameReplacementMap = new HashMap<>();
		}
		if (cmd != null) {
			HRACSymbol label = cmd.getLabel();
			if (label != null) {
				String newName = label.getName() + suffix;
				symbolNameReplacementMap.put(label.getName(), newName);
				label.setName(newName);
			}
		}
		return symbolNameReplacementMap;
	}

	/**
	 * Note: Not Recursive on purpose. Change Commad targets during compile.
	 * FixedMemoryAddresses do not need to be changed.
	 * 
	 * @param symbolNameReplacementMap
	 */
	public void replaceTargetOnCommand(Map<String, String> symbolNameReplacementMap) {
		if (cmd != null) {
			AbstractHRACMemoryAddress ma = cmd.getTarget();
			if (ma instanceof NamedHRACMemoryAddress) {
				String name = ((NamedHRACMemoryAddress) ma).getName();
				String resolvedNamed = symbolNameReplacementMap.getOrDefault(name, name);
				((NamedHRACMemoryAddress) ma).setName(resolvedNamed);
			}
		}
	}

	public void setCmd(HRACCommand cmd) {
		this.cmd = cmd;
		cachedN = null;
	}

	@Override
	public void setHeapSize(int cnt) {
		if (model != null) {
			model.setHeapSize(cnt);
		}
		cachedN = null;
	}

	public void setModel(HRACModel model) {
		this.model = model;
		cachedN = null;
	}

	@Override
	public void setN(int n) {
		if (model != null) {
			model.setN(n);
		}
		cachedN = null;
	}

	public void setParent(HRACModel parent) {
		if (this.parent != parent) {
			this.parent = parent;
			cachedN = null;
		}
	}

	public void setRange(IHRACRangeProvider range) {
		this.range = range;
		cachedN = null;
	}

	@Override
	public String toString() {
		return "" + asCode() + "";
	}

}
