package de.dralle.som.languages.hrad.model.expressiontree.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.dralle.som.languages.hrad.model.HRADModel;
import de.dralle.som.languages.hrad.model.directive.HRADAbstractDirectiveValue;
import de.dralle.som.languages.hrad.model.expressiontree.HRADAbstractDirectiveExpressionTreeNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADComplexNamedDirectiveNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADStringNamedDirectiveNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADDualChildExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADSingleChildExpressionNode;

//For abstract nodes, nothing needs to happen
//This visitor does not avoid modifying the tree it visits by default. Either clone the tree first or use one of the constructors with the clone param
public class HRADResolveDirectiveTreeVisitor
		implements HRADDirectiveExpressionTreeVisitorInterface<HRADAbstractDirectiveExpressionTreeNode> {

	private Map<String, HRADAbstractDirectiveExpressionTreeNode> getResolvablesWithParameters(String nodeName, List<HRADAbstractDirectiveExpressionTreeNode> nodeParamValues){
		Map<String, HRADAbstractDirectiveExpressionTreeNode> resolvablesCopy = new LinkedHashMap<>(resolvables);
		List<String> parameters = resolvableParameter.get(nodeName);
		if(parameters!=null) {
			List<HRADAbstractDirectiveExpressionTreeNode> pValues = nodeParamValues;
			for (int i = 0; i < parameters.size(); i++) {
				String pName = parameters.get(i);
				if(i<pValues.size()) {
					HRADAbstractDirectiveExpressionTreeNode pValue = pValues.get(i);						
					if(pValue!=null) {
						resolvablesCopy.put(pName, pValue);
					}
				}					
			}
		}
		return resolvablesCopy;
	}
	private Map<String, HRADAbstractDirectiveExpressionTreeNode> getResolvablesWithParameters(HRADStringNamedDirectiveNode node){
		return getResolvablesWithParameters(node.getDirectiveName(), node.getParamValues());
	}
	
	@Override
	public HRADAbstractDirectiveExpressionTreeNode visit(HRADComplexNamedDirectiveNode node) {
		if (clone) {
			node = node.clone();
		}
		if (resolvables != null) {
			HRADAbstractDirectiveExpressionTreeNode nameNode = node.getDirectiveName();
			HRADAbstractDirectiveExpressionTreeNode resolvedNameNode = nameNode.accept(this);
			HRADAbstractDirectiveValue<?> rnnv = resolvedNameNode.accept(new HRADDirectiveTreeCalculateValueVisitor());
			HRADAbstractDirectiveExpressionTreeNode sub = resolvables.get(rnnv.toString());
			
			Map<String, HRADAbstractDirectiveExpressionTreeNode> resolvablesCopy = getResolvablesWithParameters(rnnv.toString(), node.getParamValues());
			
			if (sub != null) {
				if (deep) {
					sub = sub.accept(new HRADResolveDirectiveTreeVisitor(resolvablesCopy, resolvableParameter, clone, deep));
				}
			}
			if (sub != null) {
				return sub;
			}
		}
		return node;
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

	private Map<String, HRADAbstractDirectiveExpressionTreeNode> resolvables = new HashMap<String, HRADAbstractDirectiveExpressionTreeNode>();
	
	private boolean clone; // clone node first to avoid modifying original
	private boolean deep; // attempt to resolve subtrees too

	private Map<String, List<String>> resolvableParameter;

	public HRADResolveDirectiveTreeVisitor(Map<String, HRADAbstractDirectiveExpressionTreeNode> resolvables,
			Map<String, List<String>> resolvableParameter, boolean clone, boolean deep) {
		super();
		this.resolvableParameter=resolvableParameter;
		this.resolvables = resolvables;
		this.clone = clone;
		this.deep = deep;
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
	public HRADAbstractDirectiveExpressionTreeNode visit(HRADStringNamedDirectiveNode node) {
		if (clone) {
			node = node.clone();
		}
		if (resolvables != null) {
			HRADAbstractDirectiveExpressionTreeNode sub = resolvables.get(node.getDirectiveName());
			if (sub != null) {
				if (deep) {
					sub = sub.accept(new HRADResolveDirectiveTreeVisitor(getResolvablesWithParameters(node), resolvableParameter, clone, deep));
				}
			}
			if (sub != null) {
				return sub;
			}
		}
		return node;
	}

}
