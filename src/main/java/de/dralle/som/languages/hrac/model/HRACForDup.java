package de.dralle.som.languages.hrac.model;

import java.util.ArrayList;
import java.util.List;

import de.dralle.som.IHeap;
import de.dralle.som.ISetN;

/**
 * Holds a single command or an entire HRACChildModel
 * 
 * @author Nils
 *
 */
public class HRACForDup implements ISetN, IHeap, Cloneable {
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
	public List<HRACCommand> getCommands() {
		return getCommands(true);
	}
	public List<HRACCommand> getCommands(boolean retainLabel) {
		List<HRACCommand> returnList = new ArrayList<>();
		if (cmd != null) {
			HRACMemoryAddress ma = cmd.getTarget();
			if (ma != null && ma.isOffsetSpecial()) {
				HRACCommand ncmd = cmd.clone();
				if(!retainLabel) {
					ncmd.setLabel(null);
				}
				ma = ma.clone();
				ma.setOffset(parent.getDirectiveAsInt(ma.getOffsetSpecialnName()));
				ma.setOffsetSpecial(false);
				ncmd.setTarget(ma);	
				returnList.add(ncmd);
			}else {
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
					returnList.addAll(hracForDup.getCommands(i==0));
				}
			}
		}
		return returnList;
	}

	public List<HRACSymbol> getSymbols() {
		List<HRACSymbol> returnList = new ArrayList<>();

		if (model != null) {
			List<HRACSymbol> childModelCommands = model.getSymbolsInclFrCommands();
			for (int i = 0; i < range.getRange(parent).length; i++) {
				int si = range.getRange(parent)[i];
				for (HRACSymbol hracForDup : childModelCommands) {
					if (hracForDup.isBitCntSpecial()) {
						hracForDup = hracForDup.clone();
						hracForDup.setBitCntSpecial(false);
						hracForDup.setBitCnt(si);
					}
				}
			}
		}
		return returnList;
	}

	public HRACForDup(HRACCommand cmd) {
		super();
		this.cmd = cmd;
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
