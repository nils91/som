/**
 * 
 */
package de.dralle.som.languages.hrac.model;

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
import de.dralle.som.languages.hras.model.Command;
import de.dralle.som.languages.hras.model.HRASModel;
import de.dralle.som.languages.hras.model.MemoryAddress;

/**
 * @author Nils
 *
 */
public class HRACModel implements ISetN,IHeap
{
	
	private Map<String, Integer> builtins;
	
	private int heapSize;
	private int minimumN;
	public int getMinimumN() {
		return minimumN;
	}

	public void setMinimumN(int minimumN) {
		this.minimumN = minimumN;
	}

	public int getHeapSize() {
		return heapSize;
	}

	public void setHeapSize(int heapSize) {
		this.heapSize = heapSize;
	}

	public HRACModel() {
		setupBuiltins();
		symbols = new ArrayList<>();
		commands=new ArrayList<>();
	}
	
	private void setupBuiltins() {
		builtins = new HashMap<>();
		builtins.put("ACC", AbstractSomMemspace.ACC_ADDRESS);
		builtins.put("ADR_EVAL", AbstractSomMemspace.ADR_EVAL_ADDRESS);
		builtins.put("WH_EN", AbstractSomMemspace.WH_EN);
		builtins.put("N",AbstractSomMemspace.ADDRESS_SIZE_START);
		builtins.put("WH_COM",AbstractSomMemspace.WH_COM);
		builtins.put("WH_DIR",AbstractSomMemspace.WH_DIR);
		builtins.put("WH_SEL",AbstractSomMemspace.WH_SEL);
		builtins.put("ADR",AbstractSomMemspace.START_ADDRESS_START);
	}

	public int getStartAdress(int n) {
		return (int) Math.pow(2, n)-getCommandBitCount(n);
	}

	
	private List<HRACSymbol> symbols;
	private List<HRACCommand> commands;

	public void addSymbol(HRACSymbol symbol) {
		if (symbols == null) {
			symbols = new ArrayList<>();
		}
		symbols.add(symbol);
	}

	public void addCommand(HRACCommand c) {
		if (commands == null) {
			commands = new ArrayList<>();
		}
		commands.add(c);
	}
	
	private int getSymbolBitCnt(int n) {
		int cnt = 0;
		for (HRACSymbol s : symbols) {
			if(isSymbolNameAllowed(s.getName())) {
				if(s.getTargetSymbol()==null) {
					if(s.isBitCntISN()) {
						cnt+=n;
					}else {
						cnt+=s.getBitCnt();
					}
				}					
			}
		}
			return cnt;
	}
	private boolean isSymbolNameAllowed(String name) {
		return !builtins.containsKey(name)&&!"HEAP".equals(name);
	}

	private boolean checkN(int n) {
		if(n<minimumN) {
			return false;
		}
		int minBitCnt=getFixedBitCount(n)+getSymbolBitCnt(n)+heapSize+getCommandBitCount(n);
		return minBitCnt<= Math.pow(2, n);
	}
	private int findN() {
		int n=0;
		do {
			n++;
		}while(!checkN(n));
		return n;
	}
	

	
	public int resolveBuiltinToAddress(String symbol, int n) {
		Integer targetAddress = builtins.get(symbol);
		if (targetAddress != null) {
			return targetAddress;
		}
		if(symbol.equals("HEAP")) {
			return getHeapStartAddress(n);
		}
		return -1;
	}
	
	private int getHeapStartAddress(int n) {
		return getFixedBitCount(n)+getSymbolBitCnt(n);
	}

	private int getCommandBitCount(int n) {
		int commandSize = getCommandSize(n);
		return commands.size() * commandSize;
	}

	private int getCommandSize(int n) {
		return 1 + n;
	}

	private int getFixedBitCount(int n) {
		return 11 + n;
	}

	private String getHeapDirective() {
		return String.format(";heap = %d", heapSize);
	}
	private String getNDirective() {
		return String.format(";n = %d", minimumN);
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
		for (HRACCommand c : commands) {
			
			tmp.add(String.format("%s", c.asCode()));
		}
		return tmp;
	}

	public String asCode() {
		StringBuilder sb = new StringBuilder();
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
		return sb.toString();
	}

	public HRASModel compileToHRAS() {
		HRASModel m = new HRASModel();
		int n=findN();
		m.setN(n);
		m.setStartAddressExplicit(true);
		int startAddress=getStartAdress(n);
		m.setStartAdress(startAddress);
		m.setNextCommandAddress(startAddress);
		int nxtSymbolAddress=getFixedBitCount(n);
		for (HRACSymbol s : symbols) {
			if(s.getTargetSymbol()==null) {
				int address = nxtSymbolAddress;
				if(s.isBitCntISN()) {
					nxtSymbolAddress+=n;
				}else {
					nxtSymbolAddress+=s.getBitCnt();
				}
				m.addSymbol(s.getName(), new MemoryAddress(address));
			}else {
				HRACMemoryAddress tgt = s.getTargetSymbol();
				MemoryAddress tgHras=new MemoryAddress();
				tgHras.setSymbol(tgt.getSymbol().getName());
				tgHras.setAddressOffset(tgt.getOffset());
				m.addSymbol(s.getName(), tgHras);
			}
		}
		m.addSymbol("HEAP",new MemoryAddress(getHeapStartAddress(n)));
		for (HRACCommand c : commands) {
			Command hrasc = new Command();
			hrasc.setOp(c.getOp());
			MemoryAddress address = new MemoryAddress(c.getTarget().getSymbol().getName());
			address.setAddressOffset(c.getTarget().getOffset());
			hrasc.setAddress(address);
			MemoryAddress assignedAddress=m.addCommand(hrasc);
			if(c.getLabel()!=null) {
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
		return findN();
	}

	@Override
	/**
	 * Sets the minimum value for N.
	 */
	public void setN(int n) {
		setMinimumN(n);
		
	}

}
