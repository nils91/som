package de.dralle.som.languages.hrac.model;

import java.util.ArrayList;
import java.util.List;

import de.dralle.som.IHeap;
import de.dralle.som.ISetN;

/**
 * Holds a single command or an entire HRACChildModel
 * @author Nils
 *
 */
public class HRACForDup implements ISetN,IHeap, Cloneable{
	private int special;
	public int getSpecial() {
		return special;
	}
	public void setSpecial(int special) {
		this.special = special;
	}
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
	private HRACForDupRange range=null;
	private HRACModel model=null;
	@Override
	protected HRACForDup clone() {
		HRACForDup clone = new HRACForDup();
		if(cmd!=null) {
			clone.cmd=cmd;
		}
		return clone;
	}
	private HRACCommand cmd=null;
	public List<HRACCommand> getCommands(){
		List<HRACCommand> returnList=new ArrayList<>();
		if(cmd!=null) {
			HRACMemoryAddress ma = cmd.getTarget();			
			if(ma!=null&&ma.isOffsetSpecial()) {
				ma=ma.clone();
				ma.setOffset(special);
				ma.setOffsetSpecial(false);
				cmd.setTarget(ma);
			}
			returnList.add(cmd);
		}
		if(model!=null) {
			List<HRACForDup> childModelCommands = model.getCommands();
			for (int i = 0; i < range.getRange(special).length; i++) {
				int si = range.getRange(special)[i];
				for (HRACForDup hracForDup : childModelCommands) {
					hracForDup.setSpecial(si);
					returnList.addAll(hracForDup.getCommands());
				}				
			}
		}
		return returnList;
	}
	public List<HRACSymbol> getSymbols(){
		List<HRACSymbol> returnList=new ArrayList<>();
	
		if(model!=null) {
			List<HRACSymbol> childModelCommands = model.getSymbolsInclFrCommands();
			for (int i = 0; i < range.getRange(special).length; i++) {
				int si = range.getRange(special)[i];
				for (HRACSymbol hracForDup : childModelCommands) {
					if(hracForDup.isBitCntSpecial()) {
						hracForDup=hracForDup.clone();
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
		if(cmd!=null) {
			return cmd.asCode();
		}
		if(model!=null) {
			String returnStr = String.format("for %s dup:\n%s\n", null);
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
		if(model!=null) {
			model.setHeapSize(cnt);
		}
		
	}
	@Override
	public int getHeapSize() {
		if(model!=null) {
			return model.getHeapSize();
		}
		return 0;
	}
	@Override
	public int getN() {
		if(model!=null) {
			return model.getN();
		}
		return special;//assuming special is n
	}
	@Override
	public void setN(int n) {
		if(model!=null) {
			model.setN(n);
		}
	}
}
