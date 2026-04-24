/**
 * 
 */
package de.dralle.som.languages.hrad.model;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import de.dralle.som.ISetN;
import de.dralle.som.languages.hrad.HRADSourceLocation;
import de.dralle.som.languages.hrad.model.directive.HRADAbstractCustomDirectiveFunction;
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
public class HRADModel implements ISetN {

	private static final Logger logger = Logger.getLogger(HRADModel.class.getName());

	private List<AbstractHRADCommand> commands = new ArrayList<AbstractHRADCommand>();

	public List<AbstractHRADCommand> getCommands() {
		return commands;
	}

	public void addCommand(AbstractHRADCommand c) {
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
			model.addInitOnceAddress(mem.getInitOnceValues().get(i).getKey(),
					mem.getInitOnceValues().get(i).getValue());
		}
		List<Integer> commandLocs = new ArrayList<Integer>(mem.getCommands().keySet());
		Collections.sort(commandLocs);
		for (Integer integer : commandLocs) {
			HRAVCommand cHrav = mem.getCommands().get(integer);
			HRADCommand cHrad = new HRADCommand(null);
			cHrad.setOp(cHrav.getOp());
			cHrad.setAddress(cHrav.getAddress());
			model.addCommand(cHrad);
		}
		return model;
	}

	public HRADModel() {

	}

	public void addInitOnceAddress(int address, boolean set) {
		addCommand(new HRADOti(set, address, null));
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

	// only static analysis
	public List<Map.Entry<Integer, Boolean>> getInitOnceValues() {
		List<Entry<Integer, Boolean>> initOnceValues = new ArrayList<Map.Entry<Integer, Boolean>>();
		for (AbstractHRADCommand entry : commands) {
			if (entry instanceof HRADOti) {
				initOnceValues.add(new AbstractMap.SimpleEntry<Integer, Boolean>(((HRADOti) entry).getAddress(),
						((HRADOti) entry).isSet()));
			}
		}
		return initOnceValues;
	}

	public HRAVModel compileToHRAV() {
		return compileToHRAV(null, null);
	}
	public HRAVModel compileToHRAV(Map<String, HRADAbstractDirectiveExpressionTreeNode> localDirectivesMap, Map<String, List<String>> directiveParameterMap) {
		if(localDirectivesMap==null) localDirectivesMap = new LinkedHashMap<String, HRADAbstractDirectiveExpressionTreeNode>();
		if(directiveParameterMap==null) directiveParameterMap = new HashMap<String, List<String>>();
		int n = getN();
		int start = getStartAdress();
		HRAVModel hrav = new HRAVModel();

		hrav.setN(n);

		hrav.setStartAddressExplicit(true);
		hrav.setStartAdress(start);

		int nca = start;
		hrav.setNextCommandAddress(nca);
		for (int i = 0; i < commands.size(); i++) {
			AbstractHRADCommand entry = commands.get(i);
			if (entry instanceof HRADOti)
				hrav.addInitOnceAddress(((HRADOti) entry).getAddress(), ((HRADOti) entry).isSet());
			if (entry instanceof HRADAbstractDirectiveStatement<?>) {
				HRADAbstractDirectiveExpressionTreeNode value = ((HRADAbstractDirectiveStatement<?>) entry).getValue();
				List<HRADAbstractDirectiveExpressionTreeNode> params = new ArrayList<HRADAbstractDirectiveExpressionTreeNode>();
				params = ((HRADAbstractDirectiveStatement<?>) entry).getParams();
				Map<String, HRADAbstractDirectiveExpressionTreeNode> lldm = new LinkedHashMap<String, HRADAbstractDirectiveExpressionTreeNode>(
						localDirectivesMap);
				HRADResolveDirectiveTreeVisitor directiveResolver = new HRADResolveDirectiveTreeVisitor(lldm,
						directiveParameterMap, true, true);
				int si = i; // Need to be able to use i inside the gci function definition
				HRADAbstractCustomDirectiveFunction gciFunction = new HRADAbstractCustomDirectiveFunction() {

					@Override
					public HRADAbstractDirectiveExpressionTreeNode getValue(
							List<HRADAbstractDirectiveExpressionTreeNode> nodeParamValues) {
						if (nodeParamValues == null) {
							return null;
						}
						List<HRADAbstractDirectiveValue<?>> npvv = new ArrayList<HRADAbstractDirectiveValue<?>>();
						for (HRADAbstractDirectiveExpressionTreeNode string : nodeParamValues) {
							HRADAbstractDirectiveExpressionTreeNode nodeParamValue = string.accept(directiveResolver);
							npvv.add(nodeParamValue.accept(new HRADDirectiveTreeCalculateValueVisitor()));
						}
						String searchedNodeName = null;
						int step = 1;
						int limit = -1;
						int startIndex = si;
						if (npvv.size() >= 4) {
							HRADAbstractDirectiveValue<?> npvvv = npvv.get(3);
							if (npvvv instanceof HRADIntegerDirectiveValue) {
								startIndex = ((HRADIntegerDirectiveValue) npvvv).getValue();
							} else {
								startIndex = Integer.parseInt(npvvv.getValue().toString());
							}
						}
						if (npvv.size() >= 3) {
							HRADAbstractDirectiveValue<?> npvvv = npvv.get(2);
							if (npvvv instanceof HRADIntegerDirectiveValue) {
								limit = ((HRADIntegerDirectiveValue) npvvv).getValue();
							} else {
								limit = Integer.parseInt(npvvv.getValue().toString());
							}
						}
						if (npvv.size() >= 2) {
							HRADAbstractDirectiveValue<?> npvvv = npvv.get(1);
							if (npvvv instanceof HRADIntegerDirectiveValue) {
								step = ((HRADIntegerDirectiveValue) npvvv).getValue();
							} else {
								step = Integer.parseInt(npvvv.getValue().toString());
							}
						}
						if (npvv.size() >= 1) {
							searchedNodeName = npvv.get(0).getValue().toString();
						}
						if (searchedNodeName == null) {
							return new HRADIntegerNode(si);
						}
						if (limit == -1) {
							limit = commands.size();
						}
						for (int j = 0; j < limit; j++) {
							int reqIdx = j * step + startIndex;
							if (reqIdx > 0 && reqIdx < commands.size()) {
								AbstractHRADCommand array_element = commands.get(reqIdx);
								if (array_element instanceof HRADStringNamedDirectiveStatement) {
									String nodeName = ((HRADStringNamedDirectiveStatement) array_element).getName();
									if (searchedNodeName.equals(nodeName)) {
										return new HRADIntegerNode(reqIdx);
									}
								}
							}
						}
						return new HRADIntegerNode(-1);
					}
				};
				directiveResolver.addCustomDirectiveFunction("gci", gciFunction);
				if (params != null) {
					for (HRADAbstractDirectiveExpressionTreeNode string : params) {
						String pName = string.accept(directiveResolver)
								.accept(new HRADDirectiveTreeCalculateValueVisitor()).toString();
						lldm.remove(pName);
					}
				}
				HRADAbstractDirectiveExpressionTreeNode resolvedValueTree = value.accept(directiveResolver);
				String name = null;
				if (entry instanceof HRADStringNamedDirectiveStatement) {
					name = ((HRADStringNamedDirectiveStatement) entry).getName();
				}
				if (entry instanceof HRADComplexNamedDirectiveStatement) {
					HRADAbstractDirectiveExpressionTreeNode nameTree = ((HRADComplexNamedDirectiveStatement) entry)
							.getName();
					directiveResolver.setResolvables(localDirectivesMap);
					HRADDirectiveTreeCalculateValueVisitor vrv = new HRADDirectiveTreeCalculateValueVisitor();
					vrv.setResolver(directiveResolver);
					HRADAbstractDirectiveValue<?> nameTreeValue = nameTree.accept(vrv);
					name = nameTreeValue.toString();
				}
				if (name != null && !name.isBlank()) {
					localDirectivesMap.put(name, resolvedValueTree);
					// Resolve param names
					List<String> pNames = resolveParamNamesToString(localDirectivesMap, directiveParameterMap, params);
					if (pNames != null)
						directiveParameterMap.put(name, pNames);
					// Check for some special directives
					if ("continue".equals(name) || "cont".equals(name)) {
						HRADAbstractDirectiveValue<?> rtv = resolvedValueTree
								.accept(new HRADDirectiveTreeCalculateValueVisitor());
						if (rtv instanceof HRADIntegerDirectiveValue) {
							hrav.setNextCommandAddress(((HRADIntegerDirectiveValue) rtv).getValue());
						}
						if (rtv instanceof HRADStringDirectiveValue) {
							hrav.setNextCommandAddress(Integer.parseInt(rtv.getValue().toString()));
						}
					}
					if ("sci".equals(name)) {
						HRADAbstractDirectiveValue<?> rtv = resolvedValueTree
								.accept(new HRADDirectiveTreeCalculateValueVisitor());
						if (rtv instanceof HRADIntegerDirectiveValue) {
							i = ((HRADIntegerDirectiveValue) rtv).getValue() - 1;
						}
						if (rtv instanceof HRADStringDirectiveValue) {
							i = Integer.parseInt(rtv.getValue().toString()) - 1;
						}
					}
				}
			}

			if (entry instanceof HRADCommand) {
				HRADCommand c = (HRADCommand) entry;

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
					.accept(new HRADResolveDirectiveTreeVisitor(localDirectivesMap, null, true, true));
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
					.accept(new HRADResolveDirectiveTreeVisitor(localDirectivesMap, null, true, true));
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

	// static analysis (before compile)
	public Map<String, HRADAbstractDirectiveExpressionTreeNode> getStringNamedDirectives(
			Map<String, List<String>> directiveParameterMap) {
		Map<String, HRADAbstractDirectiveExpressionTreeNode> r = new LinkedHashMap<String, HRADAbstractDirectiveExpressionTreeNode>();
		if (directiveParameterMap == null)
			directiveParameterMap = new HashMap<String, List<String>>();
		for (AbstractHRADCommand abstractHRADCommand : commands) {
			if (abstractHRADCommand instanceof HRADStringNamedDirectiveStatement) {
				List<String> paramNamesStrings = resolveParamNamesToString(r, directiveParameterMap,
						(HRADAbstractDirectiveStatement<?>) abstractHRADCommand);
				String name = ((HRADStringNamedDirectiveStatement) abstractHRADCommand).getName();
				r.put(name, ((HRADStringNamedDirectiveStatement) abstractHRADCommand).getValue()
						.accept(new HRADResolveDirectiveTreeVisitor(r, directiveParameterMap, true, true)));
				if (paramNamesStrings != null)
					directiveParameterMap.put(name, paramNamesStrings);
			}
		}
		return r;
	}

	// static analysis (before compile)
	public Map<String, HRADAbstractDirectiveExpressionTreeNode> getStringNamedDirectives() {
		return getStringNamedDirectives(null);
	}

	private List<String> resolveParamNamesToString(Map<String, HRADAbstractDirectiveExpressionTreeNode> directiveMap,
			Map<String, List<String>> directiveParameterMap, HRADAbstractDirectiveStatement<?> directiveStatment) {
		return resolveParamNamesToString(directiveMap, directiveParameterMap, directiveStatment.getParams());
	}

	private List<String> resolveParamNamesToString(Map<String, HRADAbstractDirectiveExpressionTreeNode> directiveMap,
			Map<String, List<String>> directiveParameterMap, List<HRADAbstractDirectiveExpressionTreeNode> paramNames) {
		if (paramNames == null) {
			return null;
		}
		List<String> paramNamesStrings = new ArrayList<String>();
		for (HRADAbstractDirectiveExpressionTreeNode paramName : paramNames) {
			String paramNameString = paramName
					.accept(new HRADResolveDirectiveTreeVisitor(directiveMap, directiveParameterMap, true, true))
					.accept(new HRADDirectiveTreeCalculateValueVisitor()).getValue().toString();
			paramNamesStrings.add(paramNameString);
		}
		return paramNamesStrings;
	}

	// static analysis (before compile)
	public Map<String, HRADAbstractDirectiveExpressionTreeNode> getDirectives() {
		Map<String, List<String>> directiveParameterMap = new HashMap<String, List<String>>();
		Map<String, HRADAbstractDirectiveExpressionTreeNode> r = getStringNamedDirectives(directiveParameterMap);
		for (AbstractHRADCommand abstractHRADCommand : commands) {
			if (abstractHRADCommand instanceof HRADComplexNamedDirectiveStatement) {
				List<String> paramNamesStrings = resolveParamNamesToString(r, directiveParameterMap,
						(HRADAbstractDirectiveStatement<?>) abstractHRADCommand);
				HRADAbstractDirectiveExpressionTreeNode name = ((HRADComplexNamedDirectiveStatement) abstractHRADCommand)
						.getName();
				String nameAsString = name
						.accept(new HRADResolveDirectiveTreeVisitor(r, directiveParameterMap, true, true))
						.accept(new HRADDirectiveTreeCalculateValueVisitor()).getValue() + "";
				r.put(nameAsString, ((HRADComplexNamedDirectiveStatement) abstractHRADCommand).getValue()
						.accept(new HRADResolveDirectiveTreeVisitor(r, directiveParameterMap, true, true)));
				if (paramNamesStrings != null)
					directiveParameterMap.put(nameAsString, paramNamesStrings);
			}
		}
		return r;
	}

}
