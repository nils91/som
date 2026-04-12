package de.dralle.som.languages.hrad.model.expressiontree.visitors;

import java.util.HashMap;
import java.util.Map;

import de.dralle.som.languages.hrad.model.HRADModel;
import de.dralle.som.languages.hrad.model.expressiontree.HRADAbstractDirectiveExpressionTreeNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADDirectiveNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADDualChildExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADSingleChildExpressionNode;

//For abstract nodes, nothing needs to happen
//This visitor does not avoid modifying the tree it visits by default. Either clone the tree first or use one of the constructors with the clone param
public class HRADResolveDirectiveTreeVisitor
		implements HRADDirectiveExpressionTreeVisitorInterface<HRADAbstractDirectiveExpressionTreeNode> {

	@Override
	public HRADAbstractDirectiveExpressionTreeNode postVisit(
			HRADDirectiveExpressionTreeVisitorInterface<HRADAbstractDirectiveExpressionTreeNode> hradDirectiveExpressionTreeVisitorInterface,
			HRADAbstractDirectiveExpressionTreeNode node, HRADAbstractDirectiveExpressionTreeNode returnyValue) {
		return returnyValue!=null?returnyValue:node;
	}

	@Override
	public HRADAbstractDirectiveExpressionTreeNode preVisit(
			HRADDirectiveExpressionTreeVisitorInterface<HRADAbstractDirectiveExpressionTreeNode> hradDirectiveExpressionTreeVisitorInterface,
			HRADAbstractDirectiveExpressionTreeNode node) {
		return clone?node.clone():node;
	}

	private Map<String,HRADAbstractDirectiveExpressionTreeNode> resolvables=new HashMap<String, HRADAbstractDirectiveExpressionTreeNode>();
	private boolean clone; //clone node first to avoid modifying original
	private boolean deep; //attempt to resolve subtrees too

	
	public HRADResolveDirectiveTreeVisitor(Map<String, HRADAbstractDirectiveExpressionTreeNode> resolvables,
			boolean clone,boolean deep) {
		super();
		this.resolvables = resolvables;
		this.clone = clone;
		this.deep=deep;
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
	public HRADAbstractDirectiveExpressionTreeNode visit(HRADDirectiveNode node) {
		if (clone) {
			node = node.clone();
		}
		HRADAbstractDirectiveExpressionTreeNode sub = resolvables.get(node.getDirectiveName());
		if(sub!=null) {
			if(deep) {
				sub=sub.accept(this);
			}
		}
		if(sub!=null) {
			return sub;
		}
		return node;
	}

}
