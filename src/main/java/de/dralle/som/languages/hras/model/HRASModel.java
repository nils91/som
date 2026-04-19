/**
 * 
 */
package de.dralle.som.languages.hras.model;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import de.dralle.som.ISetN;
import de.dralle.som.Util;
import de.dralle.som.languages.hrad.model.AbstractHRADCommand;
import de.dralle.som.languages.hrad.model.HRADAbstractDirectiveStatement;
import de.dralle.som.languages.hrad.model.HRADCommand;
import de.dralle.som.languages.hrad.model.HRADComplexNamedDirectiveStatement;
import de.dralle.som.languages.hrad.model.HRADModel;
import de.dralle.som.languages.hrad.model.HRADOti;
import de.dralle.som.languages.hrad.model.HRADStringNamedDirectiveStatement;
import de.dralle.som.languages.hrad.model.directive.HRADAbstractDirectiveValue;
import de.dralle.som.languages.hrad.model.directive.HRADIntegerDirectiveValue;
import de.dralle.som.languages.hrad.model.directive.HRADStringDirectiveValue;
import de.dralle.som.languages.hrad.model.expressiontree.HRADAbstractDirectiveExpressionTreeNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADIntegerNode;
import de.dralle.som.languages.hrad.model.expressiontree.visitors.HRADDirectiveTreeCalculateValueVisitor;
import de.dralle.som.languages.hrad.model.expressiontree.visitors.HRADResolveDirectiveTreeVisitor;
import de.dralle.som.languages.hrav.model.HRAVCommand;
import de.dralle.som.languages.hrav.model.HRAVModel;

/**
 * @author Nils
 *
 */
public class HRASModel implements ISetN {

	private static final Logger logger = Logger.getLogger(HRASModel.class.getName());

	public static HRASModel compileFromHRAD(HRADModel model) {
		Map<Integer, String> symbols = new HashMap<Integer, String>();
		symbols.putAll(Util.getBuiltinAdressesAddressKey()); //
		HRASModel newm = new HRASModel();
		newm.setN(model.getN());
		newm.setStartAdress(model.getStartAdress());
		newm.setStartAddressExplicit(true);
		newm.setNextCommandAddress(model.getStartAdress());
		AbstractHRASMemoryAddress nca = newm.nextCommandAddress;
		for (AbstractHRADCommand ce : model.getCommands()) {
			if(ce instanceof HRADOti) {
				Integer oAdr = ((HRADOti) ce).getAddress();
				String symbolName = symbols.getOrDefault(oAdr, "MA" + oAdr);
				symbols.put(oAdr, symbolName);
				newm.addInitOnceValue(new SymbolHRASMemoryAddress(symbolName), ((HRADOti)ce).isSet());
			}
			if(ce instanceof HRADCommand) {
				Integer cadr = nca.resolve(newm);
				HRADCommand c = (HRADCommand) ce;
				String symbolName = symbols.getOrDefault(cadr, "MA" + cadr);
				symbols.put(cadr, symbolName);
				newm.setNextCommandAddress(new SymbolHRASMemoryAddress(symbolName));
				symbolName = symbols.getOrDefault(c.getAddress(), "MA" + c.getAddress());
				symbols.put(c.getAddress(), symbolName);
				SymbolHRASMemoryAddress ctgtadr = new SymbolHRASMemoryAddress(symbolName);
				HRASCommand nc = new HRASCommand();
				nc.setOp(c.getOp());
				nc.setAddress(ctgtadr);
				newm.addCommand(nc);
				
				int ncaaInt = cadr+newm.getCommandSize();
				String ncaaSymbolName = symbols.getOrDefault(ncaaInt, "MA"+ncaaInt);
				symbols.put(ncaaInt, ncaaSymbolName);
				newm.setNextCommandAddress(new SymbolHRASMemoryAddress(ncaaSymbolName));
			}
			if(ce instanceof HRADAbstractDirectiveStatement<?>) {
				String dName ="";
				if(ce instanceof HRADStringNamedDirectiveStatement) {
					dName = ((HRADStringNamedDirectiveStatement) ce).getName();
				}if(ce instanceof HRADComplexNamedDirectiveStatement) {
					HRADAbstractDirectiveExpressionTreeNode dNameET = ((HRADComplexNamedDirectiveStatement) ce).getName();
					dName=dNameET.accept(new HRADResolveDirectiveTreeVisitor(model.getDirectives(), null, true, true)).accept(new HRADDirectiveTreeCalculateValueVisitor()).toString();
				}
				if("continue".equals(dName)||"cont".equals(dName)) {
					HRADAbstractDirectiveExpressionTreeNode valueET = ((HRADAbstractDirectiveStatement<?>) ce).getValue();
					HRADAbstractDirectiveValue<?> valueETvalue = valueET.accept(new HRADResolveDirectiveTreeVisitor(model.getDirectives(), null, true, true)).accept(new HRADDirectiveTreeCalculateValueVisitor());
					if(valueETvalue instanceof HRADStringDirectiveValue) {
						int ncaaInt = Util.decodeInt(valueETvalue.toString());
						String ncaaSymbolName = symbols.getOrDefault(ncaaInt, "MA"+ncaaInt);
						symbols.put(ncaaInt, ncaaSymbolName);
						newm.setNextCommandAddress(new SymbolHRASMemoryAddress(ncaaSymbolName));
					}
					if(valueETvalue instanceof HRADIntegerDirectiveValue) {
						int ncaaInt =((HRADIntegerDirectiveValue) valueETvalue).getValue();
						String ncaaSymbolName = symbols.getOrDefault(ncaaInt, "MA"+ncaaInt);
						symbols.put(ncaaInt, ncaaSymbolName);
						newm.setNextCommandAddress(new SymbolHRASMemoryAddress(ncaaSymbolName));
					}
				}
				//HRAS does not support directives (yet), so once it does, all of this can probably go
			}
		}
		for (Entry<Integer, String> entry : symbols.entrySet()) {
			Integer key = entry.getKey();
			String val = entry.getValue();
			newm.addSymbol(val, new SymbolHRASMemoryAddress(key));
		}
		return newm;
	}
	private AbstractHRASMemoryAddress nextCommandAddress;

	private HRASAbstractExpressionNode n; // This can either be a integer (wrapped in the IntegerNode class) or an
											// entire
											// expression tree. Either way it would have a definite value, use
											// calculateNumericalValue for that.

	private AbstractHRASMemoryAddress startAdress;

	boolean startAddressExplicit;

	private Map<String, AbstractHRASMemoryAddress> symbols;

	private Map<AbstractHRASMemoryAddress, HRASCommand> commands;

	private List<Map.Entry<AbstractHRASMemoryAddress, Boolean>> initOnceList = new ArrayList<Map.Entry<AbstractHRASMemoryAddress, Boolean>>();

	public HRASModel() {
		symbols = new LinkedHashMap<>();
	}
	
	public HRASCommand getCommandAtAddress(int address) {
		for (Entry<AbstractHRASMemoryAddress, HRASCommand> entry : commands.entrySet()) {
			AbstractHRASMemoryAddress abstractAdrt = entry.getKey();
			if(abstractAdrt.resolve(this)==address) {
				return entry.getValue();
			}			
		}
		return null;
	}

	public AbstractHRASMemoryAddress addCommand(HRASCommand c) {
		if (commands == null) {
			commands = new LinkedHashMap<>();
		}
		AbstractHRASMemoryAddress assignedCommandAddress = nextCommandAddress.clone();
		commands.put(assignedCommandAddress, c);
		HRASAbstractExpressionNode currentOffset = nextCommandAddress.getAddressOffset();
		if (currentOffset != null) {
			currentOffset = new PlusExpressionNode(currentOffset, new HRASIntegerNode(getCommandSize()));
		} else {
			currentOffset = new HRASIntegerNode(getCommandSize());
		}
		nextCommandAddress.setAddressOffset(currentOffset);
		return assignedCommandAddress;
	}

	public void addInitOnceValue(AbstractHRASMemoryAddress abstractHRASMemoryAddress, boolean set) {
		initOnceList
				.add(new AbstractMap.SimpleEntry<AbstractHRASMemoryAddress, Boolean>(abstractHRASMemoryAddress, set));
	}

	public void addSymbol(String name, AbstractHRASMemoryAddress abstractHRASMemoryAddress) {
		if (symbols == null) {
			symbols = new LinkedHashMap<>();
		}
		symbols.put(name, abstractHRASMemoryAddress);
	}

	public String asCode() {
		StringBuilder sb = new StringBuilder();
		sb.append(getNDirective());
		sb.append(System.lineSeparator());
		sb.append(getStartDirective());
		sb.append(System.lineSeparator());
		for (String symbolString : getSymbolsAsStrings()) {
			sb.append(symbolString);
			sb.append(System.lineSeparator());
		}
		for (Entry<AbstractHRASMemoryAddress, Boolean> entry : initOnceList) {
			sb.append((entry.getValue() ? "setonce" : "clearonce") + " " + entry.getKey().asHRASCode());
			sb.append(System.lineSeparator());
		}
		for (String symbolString : getCommandssAsStrings()) {
			sb.append(symbolString);
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}

	public HRADModel compileToHRAD() {
		HRADModel hrav = new HRADModel();
		hrav.setN(n.calculateNumericalValue());
		if (startAdress != null) {
			hrav.setStartAdress(startAdress.resolve(this));
		}
		for (Entry<AbstractHRASMemoryAddress, Boolean> entry : initOnceList) {
			hrav.addInitOnceAddress(entry.getKey().resolve(this), entry.getValue());
		}
		int pca=startAdress.resolve(this)-getCommandSize();
		for (Entry<AbstractHRASMemoryAddress, HRASCommand> c : commands.entrySet()) {			
			AbstractHRASMemoryAddress address = c.getKey();
			if(address.resolve(this)-getCommandSize()!=pca) {
				hrav.addCommand(new HRADStringNamedDirectiveStatement("continue", new HRADIntegerNode(address.resolve(this)), null));
			}
			pca=address.resolve(this);
			HRASCommand command = c.getValue();
			int cTgtAddress = getCommandTargetAddress(command);
			HRADCommand hravCommand = new HRADCommand(null);
			hravCommand.setOp(command.getOp());
			if (cTgtAddress < 0) {
				logger.warning("(HRAS -> HRAD) Negative memory address in command at address " + address + ".");
			}
			hravCommand.setAddress(cTgtAddress);
			hrav.addCommand(hravCommand);
		}
		return hrav;
	}

	public int getCommandCount() {
		return commands.size();
	}

	public Map<AbstractHRASMemoryAddress, HRASCommand> getCommands() {
		return commands;
	}

	private int getCommandSize() {
		return 1 + n.calculateNumericalValue();
	}

	private List<String> getCommandssAsStrings() {
		List<String> tmp = new ArrayList<>();
		for (Entry<AbstractHRASMemoryAddress, HRASCommand> c : commands.entrySet()) {
			AbstractHRASMemoryAddress address = c.getKey();
			HRASCommand command = c.getValue();
			tmp.add(String.format("%s%s%s", getContinueDirective(address.asHRASCode()), System.lineSeparator(),
					command.asHRASCode()));
		}
		return tmp;
	}

	private int getCommandTargetAddress(HRASCommand c) {
		int tgtAdddress = 0;
		try {
			tgtAdddress = c.getAddress().resolve(this);
		} catch (Exception e) {
			System.out.println("Command target " + c.getAddress() + " of command " + c + " couldnt be resolved");
			throw e;
		}
		return tgtAdddress;
	}

	private String getContinueDirective(String string) {
		return String.format(";continue = %s", string);
	}

	public List<Entry<AbstractHRASMemoryAddress, Boolean>> getInitOnceList() {
		return initOnceList;
	}

	@Override
	public int getN() {
		return n.calculateNumericalValue();
	}

	private String getNDirective() {
		return String.format(";n = %s", n);
	}

	public AbstractHRASMemoryAddress getNextCommandAddress() {
		return nextCommandAddress;
	}

	public AbstractHRASMemoryAddress getStartAdress() {
		return startAdress;
	}

	private String getStartDirective() {
		return String.format(";start = %s", getStartAdress().asHRASCode());
	}

	public Integer getSymbolCount() {
		return symbols.size();
	}

	public Map<String, AbstractHRASMemoryAddress> getSymbols() {
		return symbols;
	}

	private List<String> getSymbolsAsStrings() {
		List<String> tmp = new ArrayList<>();
		for (Entry<String, AbstractHRASMemoryAddress> symbol : symbols.entrySet()) {
			tmp.add(String.format("symbol %s %s", symbol.getKey(), symbol.getValue().asHRASCode()));
		}
		return tmp;
	}

	public boolean isStartAddressExplicit() {
		return startAddressExplicit;
	}

	public int resolveSymbolToAddress(String symbol) {
		AbstractHRASMemoryAddress targetAddress = null;
		if (symbols != null) {
			targetAddress = symbols.get(symbol);
		}
		if (targetAddress != null) {
			return targetAddress.resolve(this);
		}
		return Integer.parseInt(symbol);

	}

	public void setN(HRASAbstractExpressionNode abstractExpressionNode) {
		this.n = abstractExpressionNode;
	}

	@Override
	public void setN(int n) {
		this.n = new HRASIntegerNode(n);

	}

	public void setNextCommandAddress(AbstractHRASMemoryAddress address) {
		this.nextCommandAddress = address;
	}

	public void setNextCommandAddress(int startAdress2) {
		setNextCommandAddress(new SymbolHRASMemoryAddress(startAdress2));
	}

	public void setStartAddressExplicit(boolean startAddressExplicit) {
		this.startAddressExplicit = startAddressExplicit;
	}

	public void setStartAdress(AbstractHRASMemoryAddress address) {
		this.startAdress = address;
	}

	public void setStartAdress(int startAdress2) {
		setStartAdress(new SymbolHRASMemoryAddress(startAdress2));

	}

	@Override
	public String toString() {
		return asCode();
	}
}
