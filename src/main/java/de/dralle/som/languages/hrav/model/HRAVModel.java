/**
 * 
 */
package de.dralle.som.languages.hrav.model;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.dralle.som.AbstractSomMemspace;
import de.dralle.som.BooleanArrayMemspace;
import de.dralle.som.ByteArrayMemspace;
import de.dralle.som.IMemspace;
import de.dralle.som.ISetN;
import de.dralle.som.ISomMemspace;
import de.dralle.som.Opcode;
import de.dralle.som.Util;

/**
 * @author Nils
 *
 */
public class HRAVModel implements ISetN {
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
	
	private List<Map.Entry<Integer, Boolean>> initOnceValues=new ArrayList<Map.Entry<Integer,Boolean>>();
	
	public List<Map.Entry<Integer, Boolean>> getInitOnceValues() {
		return initOnceValues;
	}

	public void addInitOnceAddress(int address, boolean set) {
		initOnceValues.add(new AbstractMap.SimpleEntry<Integer, Boolean>(address, set));
	}

	@Override
	public boolean equals(Object obj) {
	if(obj instanceof HRAVModel) {
		HRAVModel other = (HRAVModel)(obj);
		boolean equal = n==other.n;
		equal=equal&&startAdress==other.startAdress;
		for (Entry<Integer, HRAVCommand> entry : commands.entrySet()) {
			Integer key = entry.getKey();
			HRAVCommand val = entry.getValue();
			HRAVCommand othercommand = other.commands.get(key);
			equal=equal&&val.equals(othercommand);
		}
		return equal;
	}
	return super.equals(obj);
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

	private Map<Integer, HRAVCommand> commands;

	public Map<Integer, HRAVCommand> getCommands() {
		return commands;
	}

	public void setCommands(Map<Integer, HRAVCommand> commands) {
		this.commands = commands;
	}

	public int addCommand(HRAVCommand c) {
		if (commands == null) {
			commands = new LinkedHashMap<>();
		}
		commands.put(nextCommandAddress, c);
		nextCommandAddress += getCommandSize();
		return nextCommandAddress - getCommandSize();
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
		for (Entry<Integer, Boolean> entry : initOnceValues) {
			sb.append((entry.getValue()?"setonce":"clearonce")+" "+entry.getKey());
			sb.append(System.lineSeparator());
		}
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
		for (Entry<Integer, Boolean> entry : initOnceValues) {
			mem.setBit(entry.getKey(), entry.getValue());
		}
		for (Entry<Integer, HRAVCommand> c : commands.entrySet()) {
			Integer address = c.getKey();
			HRAVCommand command = c.getValue();
			int cTgtAddress = getCommandTargetAddress(command);
			if(cTgtAddress<0) {
				System.out.println("Warning: (HRAV -> Memspace) Negative memory address in command at address "+address+".");
			}
			mem.setBit(address, command.getOp().getBitValue());
			mem.setBitsUnsigned(address + 1, n, cTgtAddress);
		}
		mem.setAccumulatorValue(true);
		mem.setAdrEval(true);
		return mem;
	}

	/**
	 * The resulting model should never be expected to be the same as a HRAV model
	 * which has been compiled to a memspace.
	 * 
	 * @param mem
	 */
	public static HRAVModel compileFromMemspace(ISomMemspace mem) {
		HRAVModel model = new HRAVModel();
		model.n = mem.getN();
		model.setStartAdress(mem.getNextAddress());
		model.setStartAddressExplicit(true);
		model.setNextCommandAddress(mem.getNextAddress());
		for (int i = ISomMemspace.START_ADDRESS_START+mem.getN(); i < mem.getNextAddress(); i++) {
			model.addInitOnceAddress(i, mem.getBit(i));
		}
		int commandSize = model.getCommandSize();
		for (int i = mem.getNextAddress(); i < mem.getSize(); i += commandSize) {
			boolean[] nxtCommand = new boolean[commandSize];
			for (int j = 0; j < nxtCommand.length; j++) {
				nxtCommand[j] = mem.getBit(i + j);
			}
			boolean[] ctgtAddressBit = new boolean[nxtCommand.length - 1];
			for (int j = 0; j < ctgtAddressBit.length; j++) {
				ctgtAddressBit[j] = nxtCommand[j + 1];
			}
			int cTgtAddress = Util.getAsUnsignedInt(ctgtAddressBit);
			Opcode op = null;
			if (nxtCommand[0] == false) {
				op = Opcode.NAR;
			} else {
				op = Opcode.NAW;
			}
			HRAVCommand newc = new HRAVCommand();
			newc.setOp(op);
			newc.setAddress(cTgtAddress);
			model.addCommand(newc);
		}
		return model;
	}

	@Override
	public String toString() {
		return asCode();
	}

	@Override
	public int getN() {
		return n;
	}

	public static HRAVModel compileFromMemspace(IMemspace sourceModel) {
		if(sourceModel instanceof ISomMemspace) {
			return compileFromMemspace((ISomMemspace)sourceModel);
		}
		BooleanArrayMemspace newMem = new BooleanArrayMemspace();
		newMem.copy(sourceModel);
		return compileFromMemspace(newMem);
	}
}
