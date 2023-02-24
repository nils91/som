/**
 * 
 */
package de.dralle.som.languages.hrav.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.dralle.som.AbstractSomMemspace;
import de.dralle.som.ByteArrayMemspace;
import de.dralle.som.IMemspace;
import de.dralle.som.ISetN;
import de.dralle.som.ISomMemspace;

/**
 * @author Nils
 *
 */
public class HRAVModel implements ISetN{
	public HRAVModel() {
		
	}

	private int nextCommandAddress;

	public int getNextCommandAddress() {
		return nextCommandAddress;
	}

	public void setNextCommandAddress(int nextCommandAddress) {
		this.nextCommandAddress = nextCommandAddress;
	}

	private int n;

	public void setN(int n) {
		this.n = n;
	}

	public void setStartAdress(int startAdress) {
		this.startAdress = startAdress;
	}

	

	private int startAdress;
	public int getStartAdress() {
		return startAdress;
	}

	boolean startAddressExplicit;

	public boolean isStartAddressExplicit() {
		return startAddressExplicit;
	}

	public void setStartAddressExplicit(boolean startAddressExplicit) {
		this.startAddressExplicit = startAddressExplicit;
	}
	private Map<Integer,HRAVCommand> commands;



	public int addCommand(HRAVCommand c) {
		if (commands == null) {
			commands = new LinkedHashMap<>();
		}
		commands.put(nextCommandAddress, c);
		nextCommandAddress+=getCommandSize();
		return nextCommandAddress-getCommandSize();
	}

	

	private int getCommandTargetAddress(HRAVCommand c) {
		int tgtAdddress = c.getAddress();
		return tgtAdddress;
	}

	


	private int getCommandSize() {
		return 1 + n;
	}

	

	private String getNDirective() {
		return String.format(";n = %d", n);
	}

	private String getStartDirective() {
		return String.format(";start = %s", getStartAdress());
	}

	private String getContinueDirective(int string) {
		return String.format(";continue = %d", string);
	}


	private List<String> getCommandssAsStrings() {
		List<String> tmp = new ArrayList<>();
		for (Entry<Integer, HRAVCommand> c : commands.entrySet()) {
			Integer address = c.getKey();
			HRAVCommand command = c.getValue();
			tmp.add(String.format("%s%s%s", getContinueDirective(address), System.lineSeparator(),
					command.asHRAVCode()));
		}
		return tmp;
	}

	public String asCode() {
		StringBuilder sb = new StringBuilder();
		sb.append(getNDirective());
		sb.append(System.lineSeparator());
		sb.append(getStartDirective());
		sb.append(System.lineSeparator());		
		for (String symbolString : getCommandssAsStrings()) {
			sb.append(symbolString);
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}

	public IMemspace compileToMemspace() {
		ISomMemspace mem = new ByteArrayMemspace((int) Math.pow(2, n));
		mem.setN(n);
		mem.setNextAddress(getStartAdress());
		for (Entry<Integer, HRAVCommand> c : commands.entrySet()) {
			Integer address = c.getKey();
			HRAVCommand command = c.getValue();
			int cTgtAddress = getCommandTargetAddress(command);
			mem.setBit(address, command.getOp().getBitValue());
			mem.setBitsUnsigned(address + 1, n, cTgtAddress);
		}
		mem.setAccumulatorValue(true);
		mem.setAdrEval(true);
		return mem;
	}

	@Override
	public String toString() {
		return asCode();
	}

	
	@Override
	public int getN() {
		return n;
	}
}
