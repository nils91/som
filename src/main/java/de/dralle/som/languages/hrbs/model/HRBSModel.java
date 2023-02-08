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

import de.dralle.som.AbstractSomMemspace;
import de.dralle.som.ByteArrayMemspace;
import de.dralle.som.IHeap;
import de.dralle.som.IMemspace;
import de.dralle.som.ISetN;
import de.dralle.som.ISomMemspace;
import de.dralle.som.Opcode;
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
	private List<HRBSModel> childs;

	private int heapSize;
	private int minimumN;

	public void addChild(HRBSModel c) {
		if (childs == null) {
			childs = new ArrayList<>();
		}
		childs.add(c);
	}

	public int getMinimumN() {
		int rn = minimumN;
		if (childs != null) {
			for (HRBSModel hrbsModel : childs) {
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
			for (HRBSModel hrbsModel : childs) {
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
			for (HRBSModel hrbsModel : childs) {
				sb.append(hrbsModel.asCode());
				sb.append(System.lineSeparator());
			}
		}
		return sb.toString();
	}

	public HRASModel compileToHRAS(String uniqueUsageId) {
		Map<String,String> lclSymbolNameMap=new HashMap<>();
		HRACModel m = new HRACModel();
		m.setN(minimumN);
		for (HRBSSymbol s : symbols) {
			String symbolName;
			if (s.getTargetSymbol() == null) {
				switch (s.getType()) {
				case global:
					symbolName=s.getName();
					break;
				case shared:
					symbolName=String.format("%s_%s", name,s.getName());
					break;
				default: //local is default
					symbolName=String.format("%s_%s_%s", name,uniqueUsageId,s.getName());
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
				if(tgt.isDeref()) {
					
				}else {
					HRACMemoryAddress cTGTAddress=new HRACMemoryAddress();
					HRACSymbol cTgtSymbol = new HRACSymbol();
					String cTgtSymbolName = lclSymbolNameMap.getOrDefault( tgt.getSymbol().getName(), tgt.getSymbol().getName());
					
					cTgtSymbol.setName(cTgtSymbolName);
					cTGTAddress.setSymbol(cTgtSymbol);
					cTGTAddress.setOffset(tgt.getOffset());
					
					HRACSymbol hracSymbol = new HRACSymbol();
					hracSymbol.setName(s.getName());
					hracSymbol.setBitCnt(s.getBitCnt());
					hracSymbol.setBitCntISN(s.isBitCntISN());
					hracSymbol.setTargetSymbol(cTGTAddress);
					
					m.addSymbol(hracSymbol);
				}
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

		for (HRBSCommand c : commands) {
			Command hrasc = new Command();
			hrasc.setCmd(c.getOp());
			MemoryAddress address = new MemoryAddress(c.getTarget().getSymbol().getName());
			address.setAddressOffset(c.getTarget().getOffset());
			hrasc.setAddress(address);
			MemoryAddress assignedAddress = m.addCommand(hrasc);
			if (c.getLabel() != null) {
				m.addSymbol(c.getLabel().getName(), assignedAddress);
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

	public void addParam(HRBSSymbolSimple param) {
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
