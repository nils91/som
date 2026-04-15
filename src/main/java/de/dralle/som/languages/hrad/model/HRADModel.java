/**
 * 
 */
package de.dralle.som.languages.hrad.model;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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
import de.dralle.som.languages.hrad.model.expressiontree.HRADIntegerNode;
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

	private List<AbstractHRADCommand> commands = new ArrayList<AbstractHRADCommand>();

	public void addCommand2(AbstractHRADCommand c) {
		commands.add(c);
	}

	private HRADSourceLocation sourceLocation;

	public HRADSourceLocation getSourceLocation() {
		return sourceLocation;
	}

	public void setSourceLocation(HRADSourceLocation sourceLocation) {
		this.sourceLocation = sourceLocation;
	}

	/**
	 * The resulting model should never be expected to be the same as a HRAD model
	 * which has been compiled to HRAV. Could be, but unlikely.
	 * 
	 * @param mem
	 */
	public static HRADModel compileFromHRAV(HRAVModel mem) {
		HRADModel model = new HRADModel();
		model.setN(mem.getN());
		model.setStartAdress(mem.getStartAdress());
		for (int i = 0; i < mem.getInitOnceValues().size(); i++) {
			model.addInitOnceAddress(mem.getInitOnceValues().get(i).getKey(),mem.getInitOnceValues().get(i).getValue());
		}
		List<Integer> commandLocs=new ArrayList( mem.getCommands().keySet());
		Collections.sort(commandLocs);
		for (Integer integer : commandLocs) {
			HRAVCommand cHrav = mem.getCommands().get(integer);
			HRADCommand cHrad = new HRADCommand(null);
			cHrad.setOp(cHrav.getOp());
			cHrad.setAddress(cHrav.getAddress());
			model.addCommand2(cHrad);
		}
		return model;
	}

	

	public HRADModel() {

	}

	
	public void addInitOnceAddress(int address, boolean set) {
		addCommand2(new HRADOti(set, address, null));
	}

	public String asCode() {
		StringBuilder sb = new StringBuilder();
		for (AbstractHRADCommand abstractHRADCommand : commands) {
			sb.append(abstractHRADCommand.toString());
			sb.append(System.lineSeparator());
		}

		
		return sb.toString();
	}

	

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRADModel) {
			HRADModel oth = (HRADModel) obj;
			if (oth.commands.size() != commands.size()) {
				return false;
			}
			for (int i = 0; i < commands.size(); i++) {
				AbstractHRADCommand array_element = commands.get(i);
				AbstractHRADCommand othc = oth.commands.get(i);
				if (!array_element.equals(othc))
					return false;
			}
		}
		return super.equals(obj);
	}

	

	private int getProbableCommandSize() {
		return 1 + getN();
	}

	//only static analysis
	public List<Map.Entry<Integer, Boolean>> getInitOnceValues() {
		List<Entry<Integer, Boolean>> initOnceValues=new ArrayList<Map.Entry<Integer,Boolean>>();
		for (AbstractHRADCommand entry : commands) {
			if(entry instanceof HRADOti) {
				initOnceValues.add(new AbstractMap.SimpleEntry<Integer, Boolean>(((HRADOti)entry).getAddress(), ((HRADOti)entry).isSet()));
			}
		}
		return initOnceValues;
	}

	public HRAVModel compileToHRAV() {
		Map<String, HRADAbstractDirectiveExpressionTreeNode> localDirectivesMap = new LinkedHashMap<String, HRADAbstractDirectiveExpressionTreeNode>();
		int n = getN();
		int start = getStartAdress();
		HRAVModel hrav = new HRAVModel();

		hrav.setN(n);

		hrav.setStartAddressExplicit(true);
		hrav.setStartAdress(start);

		for (AbstractHRADCommand entry : commands) {
			if (entry instanceof HRADOti)
				hrav.addInitOnceAddress(((HRADOti) entry).getAddress(), ((HRADOti) entry).isSet());
		}
		int nca = start;
		for (AbstractHRADCommand entry : commands) {
			if (entry instanceof HRADCommand) {
				HRADCommand c = (HRADCommand) entry;
				hrav.setNextCommandAddress(nca);
				HRAVCommand hravCommand = new HRAVCommand();
				hravCommand.setOp(c.getOp());
				if (c.getAddress() < 0) {
					logger.warning("Negative memory address in command at address " + nca + ". "
							+ c.getSourceLocation() != null ? c.getSourceLocation() + "" : "");
				}
				hravCommand.setAddress(c.getAddress());
				hrav.addCommand(hravCommand);
			}
		}
		return hrav;
	}

	private int getN(Map<String, HRADAbstractDirectiveExpressionTreeNode> localDirectivesMap) {
		// Find N directive: N first (which is the one actually set by the compiler), n
		// as fall back (minimum n)
		int n = 0;
		HRADStringNamedDirectiveStatement nDir = null;
		for (AbstractHRADCommand abstractHRADCommand : commands) {
			if (abstractHRADCommand instanceof HRADStringNamedDirectiveStatement) {
				if (((HRADStringNamedDirectiveStatement) abstractHRADCommand).getName().equals("N")) {
					nDir = (HRADStringNamedDirectiveStatement) abstractHRADCommand;
				}
			}
		}
		if (nDir == null) {
			logger.info("No N directive found. This could be OK if this is not compiled from a SOM language >= HRAC. "
					+ sourceLocation != null ? sourceLocation + "" : "");
			for (AbstractHRADCommand abstractHRADCommand : commands) {
				if (abstractHRADCommand instanceof HRADStringNamedDirectiveStatement) {
					if (((HRADStringNamedDirectiveStatement) abstractHRADCommand).getName().equals("n")) {
						nDir = (HRADStringNamedDirectiveStatement) abstractHRADCommand;
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
		}
		return n;
	}

	@Override
	public int getN() {
		return getN(getDirectives());
	}

	private String getNDirective() {
		return String.format(";n = %d", getN());
	}
	public int getStartAdress(Map<String, HRADAbstractDirectiveExpressionTreeNode> localDirectivesMap) {
		int start = 0;
		// Find start directive
		HRADStringNamedDirectiveStatement sDir = null;
		for (AbstractHRADCommand abstractHRADCommand : commands) {
			if (abstractHRADCommand instanceof HRADStringNamedDirectiveStatement) {
				if (((HRADStringNamedDirectiveStatement) abstractHRADCommand).getName().equals("start")) {
					sDir = (HRADStringNamedDirectiveStatement) abstractHRADCommand;
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
		}
		return start;
	}

	public int getStartAdress() {
		return getStartAdress(getDirectives());
	}

	private String getStartDirective() {
		return String.format(";start = %s", getStartAdress());
	}

	public void setCommands(List<AbstractHRADCommand> commands) {
		this.commands = commands;
	}

	public void setN(int n, HRADSourceLocation from) {
		// Check if theres already an n set. If not, set minimum (lowercase n) and
		// actual (uppercase N). If n is set, set N
		boolean nLCSet = false;
		boolean nUCSet = false;
		for (AbstractHRADCommand abstractHRADCommand : commands) {
			if (abstractHRADCommand instanceof HRADStringNamedDirectiveStatement) {
				if (((HRADStringNamedDirectiveStatement) abstractHRADCommand).getName().equals("n")) {
					nLCSet = true;
				}
			}
		}
		if (!nLCSet) {
			commands.add(new HRADStringNamedDirectiveStatement("n", new HRADIntegerNode(n), from));
		}
		for (AbstractHRADCommand abstractHRADCommand : commands) {
			if (abstractHRADCommand instanceof HRADStringNamedDirectiveStatement) {
				if (((HRADStringNamedDirectiveStatement) abstractHRADCommand).getName().equals("N")) {
					nUCSet = true;
				}
			}
		}
		if (nUCSet) {
			logger.warning("Overwrting actual n. " + from != null ? from + "" : "");
		}
		commands.add(new HRADStringNamedDirectiveStatement("N", new HRADIntegerNode(n), from));
	}

	public void setN(int n) {
		setN(n, null);
	}

	public void setStartAdress(int startAdress, HRADSourceLocation from) {
		boolean nUCSet = false;
		for (AbstractHRADCommand abstractHRADCommand : commands) {
			if (abstractHRADCommand instanceof HRADStringNamedDirectiveStatement) {
				if (((HRADStringNamedDirectiveStatement) abstractHRADCommand).getName().equals("start")) {
					nUCSet = true;
				}
			}
		}
		if (nUCSet) {
			logger.warning("Overwriting start address. " + from != null ? from + "" : "");
		}
		commands.add(new HRADStringNamedDirectiveStatement("start", new HRADIntegerNode(startAdress), from));
	}

	public void setStartAdress(int startAdress) {
		setStartAdress(startAdress, null);
	}

	@Override
	public String toString() {
		return asCode();
	}

	//static analysis (before compile)
	public Map<String, HRADAbstractDirectiveExpressionTreeNode> getStringNamedDirectives() {
		Map<String, HRADAbstractDirectiveExpressionTreeNode> r = new LinkedHashMap<String, HRADAbstractDirectiveExpressionTreeNode>();
		for (AbstractHRADCommand abstractHRADCommand : commands) {
			if (abstractHRADCommand instanceof HRADStringNamedDirectiveStatement) {
				String name = ((HRADStringNamedDirectiveStatement) abstractHRADCommand).getName();
				r.put(name, ((HRADStringNamedDirectiveStatement) abstractHRADCommand).getValue());
			}
		}
		return r;
	}
	//static analysis (before compile)
	public Map<String, HRADAbstractDirectiveExpressionTreeNode> getDirectives() {
		Map<String, HRADAbstractDirectiveExpressionTreeNode> r = getStringNamedDirectives();
		for (AbstractHRADCommand abstractHRADCommand : commands) {
			if (abstractHRADCommand instanceof HRADComplexNamedDirectiveStatement) {
				HRADAbstractDirectiveExpressionTreeNode name = ((HRADComplexNamedDirectiveStatement) abstractHRADCommand)
						.getName();
				r.put(name.accept(new HRADResolveDirectiveTreeVisitor(r, true, true))
						.accept(new HRADDirectiveTreeCalculateValueVisitor()).getValue() + "",
						((HRADComplexNamedDirectiveStatement) abstractHRADCommand).getValue());
			}
		}
		return r;
	}
}
