/**
 * 
 */
package de.dralle.som.languages.hrad.model;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import de.dralle.som.BooleanArrayMemspace;
import de.dralle.som.ByteArrayMemspace;
import de.dralle.som.IMemspace;
import de.dralle.som.ISetN;
import de.dralle.som.ISomMemspace;
import de.dralle.som.Opcode;
import de.dralle.som.Util;
import de.dralle.som.languages.hrad.HRADSourceLocation;
import de.dralle.som.languages.hrad.model.directive.HRADAbstractDirectiveValue;
import de.dralle.som.languages.hrad.model.directive.HRADIntegerDirectiveValue;
import de.dralle.som.languages.hrad.model.expressiontree.HRADAbstractDirectiveExpressionTreeNode;
import de.dralle.som.languages.hrad.model.expressiontree.visitors.HRADDirectiveTreeCalculateValueVisitor;
import de.dralle.som.languages.hrad.model.expressiontree.visitors.HRADResolveDirectiveTreeVisitor;
import de.dralle.som.languages.hras.model.AbstractHRASMemoryAddress;
import de.dralle.som.languages.hras.model.HRASCommand;
import de.dralle.som.languages.hras.model.HRASModel;
import de.dralle.som.languages.hrav.model.HRAVCommand;
import de.dralle.som.languages.hrav.model.HRAVModel;

/**
 * @author Nils
 *
 */
public class HRADModel implements ISetN {

	private static final Logger logger = Logger.getLogger(HRADModel.class.getName());

	private List<AbstractHRADCommand> commands2 = new ArrayList<AbstractHRADCommand>();

	public void addCommand2(AbstractHRADCommand c) {
		commands2.add(c);
	}

	private HRADSourceLocation sourceLocation;

	public HRADSourceLocation getSourceLocation() {
		return sourceLocation;
	}

	public void setSourceLocation(HRADSourceLocation sourceLocation) {
		this.sourceLocation = sourceLocation;
	}

	public static HRADModel compileFromMemspace(IMemspace sourceModel) {
		if (sourceModel instanceof ISomMemspace) {
			return compileFromMemspace((ISomMemspace) sourceModel);
		}
		BooleanArrayMemspace newMem = new BooleanArrayMemspace();
		newMem.copy(sourceModel);
		return compileFromMemspace(newMem);
	}

	/**
	 * The resulting model should never be expected to be the same as a HRAD model
	 * which has been compiled to a memspace.
	 * 
	 * @param mem
	 */
	public static HRADModel compileFromMemspace(ISomMemspace mem) {
		HRADModel model = new HRADModel();
		model.n = mem.getN();
		model.setStartAdress(mem.getNextAddress());
		model.setStartAddressExplicit(true);
		model.setNextCommandAddress(mem.getNextAddress());
		for (int i = ISomMemspace.START_ADDRESS_START + mem.getN(); i < mem.getNextAddress(); i++) {
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
			HRADCommand newc = new HRADCommand();
			newc.setOp(op);
			newc.setAddress(cTgtAddress);
			model.addCommand(newc);
		}
		return model;
	}

	private int nextCommandAddress;

	private int n;

	private List<Map.Entry<Integer, Boolean>> initOnceValues = new ArrayList<Map.Entry<Integer, Boolean>>();

	private int startAdress;

	boolean startAddressExplicit;

	private Map<Integer, HRADCommand> commands;

	public HRADModel() {

	}

	public int addCommand(HRADCommand c) {
		if (commands == null) {
			commands = new LinkedHashMap<>();
		}
		commands.put(nextCommandAddress, c);
		nextCommandAddress += getCommandSize();
		return nextCommandAddress - getCommandSize();
	}

	public void addInitOnceAddress(int address, boolean set) {
		initOnceValues.add(new AbstractMap.SimpleEntry<Integer, Boolean>(address, set));
		addCommand2(new HRADOti(set, address, null));
	}

	public String asCode() {
		StringBuilder sb = new StringBuilder();
		for (AbstractHRADCommand abstractHRADCommand : commands2) {
			sb.append(abstractHRADCommand.toString());
			sb.append(System.lineSeparator());
		}

		// old below todo remove
		sb.append(getNDirective());
		sb.append(System.lineSeparator());
		sb.append(getStartDirective());
		sb.append(System.lineSeparator());
		for (Entry<Integer, Boolean> entry : initOnceValues) {
			sb.append((entry.getValue() ? "setonce" : "clearonce") + " " + entry.getKey());
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
		for (Entry<Integer, HRADCommand> c : commands.entrySet()) {
			Integer address = c.getKey();
			HRADCommand command = c.getValue();
			int cTgtAddress = getCommandTargetAddress(command);
			if (cTgtAddress < 0) {
				System.out.println(
						"Warning: (HRAD -> Memspace) Negative memory address in command at address " + address + ".");
			}
			mem.setBit(address, command.getOp().getBitValue());
			mem.setBitsUnsigned(address + 1, n, cTgtAddress);
		}
		mem.setAccumulatorValue(true);
		mem.setAdrEval(true);
		return mem;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRADModel) {
			HRADModel oth = (HRADModel) obj;
			if (oth.commands2.size() != commands2.size()) {
				return false;
			}
			for (int i = 0; i < commands2.size(); i++) {
				AbstractHRADCommand array_element = commands2.get(i);
				AbstractHRADCommand othc = oth.commands2.get(i);
				if (!array_element.equals(othc))
					return false;
			}
		}
		return super.equals(obj);
	}

	public Map<Integer, HRADCommand> getCommands() {
		return commands;
	}

	private int getCommandSize() {
		return 1 + n;
	}

	private List<String> getCommandssAsStrings() {
		List<String> tmp = new ArrayList<>();
		for (Entry<Integer, HRADCommand> c : commands.entrySet()) {
			Integer address = c.getKey();
			HRADCommand command = c.getValue();
			tmp.add(String.format("%s%s%s", getContinueDirective(address), System.lineSeparator(),
					command.asHRADCode()));
		}
		return tmp;
	}

	private int getCommandTargetAddress(HRADCommand c) {
		int tgtAdddress = c.getAddress();
		return tgtAdddress;
	}

	private String getContinueDirective(int string) {
		return String.format(";continue = %d", string);
	}

	public List<Map.Entry<Integer, Boolean>> getInitOnceValues() {
		return initOnceValues;
	}

	public HRAVModel compileToHRAV() {
		Map<String, HRADAbstractDirectiveExpressionTreeNode> localDirectivesMap = new LinkedHashMap<String, HRADAbstractDirectiveExpressionTreeNode>();
		int n=0;
		int start = 0;
		HRAVModel hrav = new HRAVModel();
		// Find N directive: N first (which is the one actually set by the compiler), n
		// as fall back (minimum n)
		HRADDirectiveStatement nDir = null;
		for (AbstractHRADCommand abstractHRADCommand : commands2) {
			if (abstractHRADCommand instanceof HRADDirectiveStatement) {
				if (((HRADDirectiveStatement) abstractHRADCommand).getName().equals("N")) {
					nDir = (HRADDirectiveStatement) abstractHRADCommand;
				}
			}
		}
		if (nDir == null) {
			logger.info("No N directive found. This could be OK if this is not compiled from a SOM language >= HRAC. "
					+ sourceLocation != null ? sourceLocation + "" : "");
			for (AbstractHRADCommand abstractHRADCommand : commands2) {
				if (abstractHRADCommand instanceof HRADDirectiveStatement) {
					if (((HRADDirectiveStatement) abstractHRADCommand).getName().equals("n")) {
						nDir = (HRADDirectiveStatement) abstractHRADCommand;
					}
				}
			}
		}
		if (nDir == null) {
			logger.warning(
					"No n directive found either (No N was found previously). This is bad. " + sourceLocation != null
							? sourceLocation + ""
							: "");
		} else {
			HRADAbstractDirectiveExpressionTreeNode nTree = nDir.getValue();

			HRADAbstractDirectiveExpressionTreeNode ntresolved = nTree
					.accept(new HRADResolveDirectiveTreeVisitor(localDirectivesMap, true, true));
			HRADAbstractDirectiveValue<?> nval = ntresolved.accept(new HRADDirectiveTreeCalculateValueVisitor());
			if (nval instanceof HRADIntegerDirectiveValue) {
				n = ((HRADIntegerDirectiveValue) nval).getValue();
			} else {
				n = Integer.parseInt(nval.getValue().toString());
			}
			hrav.setN(n);
		}
		// Find start directive
		HRADDirectiveStatement sDir = null;
		for (AbstractHRADCommand abstractHRADCommand : commands2) {
			if (abstractHRADCommand instanceof HRADDirectiveStatement) {
				if (((HRADDirectiveStatement) abstractHRADCommand).getName().equals("start")) {
					sDir = (HRADDirectiveStatement) abstractHRADCommand;
				}
			}
		}
		if (sDir == null) {
			logger.warning("No start directive found. There really should be one here, or the compiled code wont work. "
					+ sourceLocation != null ? sourceLocation + "" : "");
		} else {
			HRADAbstractDirectiveExpressionTreeNode nTree = sDir.getValue();
			HRADAbstractDirectiveExpressionTreeNode ntresolved = nTree
					.accept(new HRADResolveDirectiveTreeVisitor(localDirectivesMap, true, true));
			HRADAbstractDirectiveValue<?> nval = ntresolved.accept(new HRADDirectiveTreeCalculateValueVisitor());
			if (nval instanceof HRADIntegerDirectiveValue) {
				start = ((HRADIntegerDirectiveValue) nval).getValue();
			} else {
				start = Integer.parseInt(nval.getValue().toString());
			}
			hrav.setStartAddressExplicit(true);
			hrav.setStartAdress(start);
		}
		for (AbstractHRADCommand entry : commands2) {
			if(entry instanceof HRADOti)		hrav.addInitOnceAddress(((HRADOti) entry).getAddress(), ((HRADOti) entry).isSet());
		}
		int nca=start;
		for (AbstractHRADCommand entry : commands2) {
			if(entry instanceof HRADCommand)		{
				HRADCommand c = (HRADCommand)entry;
				hrav.setNextCommandAddress(nca);
				HRAVCommand hravCommand = new HRAVCommand();
				hravCommand.setOp(c.getOp());
				if (c.getAddress() < 0) {
					logger.warning("Negative memory address in command at address " + nca + ". "+c.getSourceLocation() != null ? c.getSourceLocation() + "" : "");
				}
				hravCommand.setAddress(c.getAddress());
				hrav.addCommand(hravCommand);
			}
		}		
		return hrav;
	}

	@Override
	public int getN() {
		return n;
	}

	private String getNDirective() {
		return String.format(";n = %d", n);
	}

	public int getNextCommandAddress() {
		return nextCommandAddress;
	}

	public int getStartAdress() {
		return startAdress;
	}

	private String getStartDirective() {
		return String.format(";start = %s", getStartAdress());
	}

	public boolean isStartAddressExplicit() {
		return startAddressExplicit;
	}

	public void setCommands(Map<Integer, HRADCommand> commands) {
		this.commands = commands;
	}

	public void setN(int n) {
		this.n = n;
	}

	public void setNextCommandAddress(int nextCommandAddress) {
		this.nextCommandAddress = nextCommandAddress;
	}

	public void setStartAddressExplicit(boolean startAddressExplicit) {
		this.startAddressExplicit = startAddressExplicit;
	}

	public void setStartAdress(int startAdress) {
		this.startAdress = startAdress;
	}

	@Override
	public String toString() {
		return asCode();
	}

	public Map<String, HRADAbstractDirectiveExpressionTreeNode> getDirectives() {
		return null;
	}
}
