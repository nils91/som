package de.dralle.som.languages.hrad.model.expressiontree.visitors;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.dralle.som.languages.hrad.model.directive.HRADAbstractCustomDirectiveFunction;
import de.dralle.som.languages.hrad.model.directive.HRADAbstractDirectiveValue;
import de.dralle.som.languages.hrad.model.expressiontree.HRADAbstractDirectiveExpressionTreeNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADAbstractDirectiveNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADComplexNamedDirectiveNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADDualChildExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADIntegerNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADSingleChildExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADStringNamedDirectiveNode;

//For abstract nodes, nothing needs to happen
//This visitor does not avoid modifying the tree it visits by default. Either clone the tree first or use one of the constructors with the clone param
public class HRADResolveDirectiveTreeVisitor
		implements HRADDirectiveExpressionTreeVisitorInterface<HRADAbstractDirectiveExpressionTreeNode> {

	private Map<String, HRADAbstractCustomDirectiveFunction> customFunctions = new LinkedHashMap<String, HRADAbstractCustomDirectiveFunction>(); // custom
																																					// directive
																																					// functions
																																					// defined
																																					// by
																																					// the
																																					// compiler.
																																					// If
																																					// a
																																					// custom
																																					// function
																																					// with
																																					// the
																																					// name
																																					// is
																																					// found,
																																					// the
																																					// compiler
																																					// will
																																					// not
																																					// attempt
																																					// to
																																					// lookup
																																					// the
																																					// name
																																					// in
																																					// the
																																					// resolvables
																																					// directive
																																					// map

	private Map<String, HRADAbstractDirectiveExpressionTreeNode> resolvables = new HashMap<String, HRADAbstractDirectiveExpressionTreeNode>(); // resolvable
																																				// directives

	private boolean clone; // clone node first to avoid modifying original

	private boolean deep; // attempt to resolve subtrees too

	private Map<String, List<String>> resolvableParameter; // lookup map for the parameters of directive functions. will
															// be used to build a parameter map if a directive function
															// is resolved

	private List<HRADAbstractDirectiveExpressionTreeNode> dependables; // if

	public HRADResolveDirectiveTreeVisitor(Map<String, HRADAbstractDirectiveExpressionTreeNode> resolvables,
			Map<String, List<String>> resolvableParameter, List<HRADAbstractDirectiveExpressionTreeNode> dependables,
			boolean clone, boolean deep) {
		super();
		this.resolvableParameter = resolvableParameter;
		this.resolvables = resolvables;
		this.clone = clone;
		this.deep = deep;
		this.dependables = dependables;
	}

	public void addCustomDirectiveFunction(String name, HRADAbstractCustomDirectiveFunction f) {
		customFunctions.put(name, f);
	}

	public Map<String, HRADAbstractDirectiveExpressionTreeNode> getResolvables() {
		return resolvables;
	}

	private Map<String, HRADAbstractDirectiveExpressionTreeNode> getResolvablesWithParameters(
			HRADStringNamedDirectiveNode node) {
		return getResolvablesWithParameters(node.getDirectiveName(), node.getParamValues());
	}

	private Map<String, HRADAbstractDirectiveExpressionTreeNode> getResolvablesWithParameters(String nodeName,
			List<HRADAbstractDirectiveExpressionTreeNode> nodeParamValues) {
		Map<String, HRADAbstractDirectiveExpressionTreeNode> resolvablesCopy = new LinkedHashMap<>(resolvables);

		// go over custom functions
		HRADAbstractCustomDirectiveFunction cf = customFunctions.get(nodeName);
		if (cf != null) {
			HRADAbstractDirectiveExpressionTreeNode cfv = cf.getValue(nodeParamValues);
			if (cfv != null && !nodeName.isEmpty()) {
				resolvablesCopy.put(nodeName, cfv);
			}
		}

		// match parameter names to values
		List<String> parameters = resolvableParameter.get(nodeName);
		if (parameters != null) {
			List<HRADAbstractDirectiveExpressionTreeNode> pValues = nodeParamValues;
			for (int i = 0; i < parameters.size(); i++) {
				String pName = parameters.get(i);
				if (i < pValues.size()) {
					HRADAbstractDirectiveExpressionTreeNode pValue = pValues.get(i).clone()
							.accept(new HRADResolveDirectiveTreeVisitor(resolvables, resolvableParameter, dependables,
									clone, deep));
					if (pValue != null) {
						resolvablesCopy.put(pName, pValue);
					}
				}
			}
		}
		return resolvablesCopy;
	}

	@Override
	public HRADAbstractDirectiveExpressionTreeNode postVisit(
			HRADDirectiveExpressionTreeVisitorInterface<HRADAbstractDirectiveExpressionTreeNode> hradDirectiveExpressionTreeVisitorInterface,
			HRADAbstractDirectiveExpressionTreeNode node, HRADAbstractDirectiveExpressionTreeNode returnyValue) {
		return returnyValue != null ? returnyValue : node;
	}

	@Override
	public HRADAbstractDirectiveExpressionTreeNode preVisit(
			HRADDirectiveExpressionTreeVisitorInterface<HRADAbstractDirectiveExpressionTreeNode> hradDirectiveExpressionTreeVisitorInterface,
			HRADAbstractDirectiveExpressionTreeNode node) {
		return clone ? node.clone() : node;
	}

	public void setResolvables(Map<String, HRADAbstractDirectiveExpressionTreeNode> resolvables) {
		this.resolvables = resolvables;
	}

	@Override
	public HRADAbstractDirectiveExpressionTreeNode visit(HRADComplexNamedDirectiveNode node) {
		if (clone) {
			node = node.clone();
		}
		HRADAbstractDirectiveExpressionTreeNode nameNode = node.getDirectiveName();
		HRADAbstractDirectiveExpressionTreeNode resolvedNameNode = nameNode.accept(
				new HRADResolveDirectiveTreeVisitor(resolvables, resolvableParameter, dependables, clone, deep));
		HRADDirectiveTreeCalculateValueVisitor nameValueCalculateVisitor = new HRADDirectiveTreeCalculateValueVisitor();
		HRADAbstractDirectiveValue<?> rnnv = resolvedNameNode.accept(nameValueCalculateVisitor);
		List<HRADAbstractDirectiveNode<?>> unresolvables = nameValueCalculateVisitor.getUnresolvables();
		if (unresolvables != null && dependables != null) {
			for (HRADAbstractDirectiveNode<?> unresolved : unresolvables) {
				String unresolvedName = "";
				if (unresolved instanceof HRADStringNamedDirectiveNode) {
					unresolvedName = ((HRADStringNamedDirectiveNode) unresolved).getDirectiveName();
				} else if (unresolved instanceof HRADComplexNamedDirectiveNode) {
					unresolvedName = ((HRADComplexNamedDirectiveNode) unresolved)
							.getDirectiveName().clone().accept(new HRADResolveDirectiveTreeVisitor(resolvables,
									resolvableParameter, null, clone, deep))
							.accept(new HRADDirectiveTreeCalculateValueVisitor()).getValue().toString();
				}
				for (HRADAbstractDirectiveExpressionTreeNode hradAbstractNode : dependables) {
					String dependableName = hradAbstractNode
							.accept(new HRADResolveDirectiveTreeVisitor(resolvables, resolvableParameter, dependables,
									clone, deep))
							.accept(new HRADDirectiveTreeCalculateValueVisitor()).getValue().toString();
					if (dependableName == unresolvedName || dependableName.equals(unresolvedName)) {
						return node;
					}
				}
			}
		}
		String nodeName = rnnv.getValue().toString();
		if (nodeName.isEmpty()) {
			return new HRADIntegerNode(0);
		}
		HRADAbstractDirectiveExpressionTreeNode sub = null;
		if (customFunctions != null) {
			HRADAbstractCustomDirectiveFunction cf = customFunctions.get(nodeName);
			if (cf != null) {
				sub = cf.getValue(node.getParamValues());
			}
		}
		if (sub == null && resolvables != null) {
			sub = resolvables.get(nodeName);

		}
		if (sub != null) {
			if (deep) {
				Map<String, HRADAbstractDirectiveExpressionTreeNode> paramResolvables = getResolvablesWithParameters(
						nodeName, node.getParamValues());
				sub = sub.accept(
						new HRADResolveDirectiveTreeVisitor(paramResolvables, resolvableParameter, null, clone, deep));
			}
		}
		if (sub != null) {
			return sub;
		}
		return node;

	}

	@Override
	public HRADAbstractDirectiveExpressionTreeNode visit(HRADDualChildExpressionNode node) {
		if (clone) {
			node = node.clone();
		}
		HRADAbstractDirectiveExpressionTreeNode[] nc = node.getChilds();
		for (int i = 0; i < nc.length; i++) {
			nc[i] = nc[i].accept(this);
		}
		return node;
	}

	@Override
	public HRADAbstractDirectiveExpressionTreeNode visit(HRADSingleChildExpressionNode node) {
		if (clone) {
			node = node.clone();
		}
		node.setChild(node.getChild().accept(this));
		return node;
	}

	@Override
	public HRADAbstractDirectiveExpressionTreeNode visit(HRADStringNamedDirectiveNode node) {
		if (clone) {
			node = node.clone();
		}
		String nodeName = node.getDirectiveName();
		HRADAbstractDirectiveExpressionTreeNode sub = null;
		if (customFunctions != null) {
			HRADAbstractCustomDirectiveFunction cf = customFunctions.get(nodeName);
			if (cf != null) {
				sub = cf.getValue(node.getParamValues());
			}
		}
		if (sub == null && resolvables != null) {
			sub = resolvables.get(node.getDirectiveName());

		}
		if (sub != null) {
			if (deep) {
				Map<String, HRADAbstractDirectiveExpressionTreeNode> paramResolvables = getResolvablesWithParameters(
						node);
				sub = sub.accept(
						new HRADResolveDirectiveTreeVisitor(paramResolvables, resolvableParameter, null, clone, deep));
			}
		}
		if (sub != null) {
			return sub;
		}
		return node;
	}

}
