package de.dralle.som.languages.hrad.model.expressiontree.visitors;

import de.dralle.som.languages.hrad.model.HRADModel;
import de.dralle.som.languages.hrad.model.expressiontree.HRADAbsoluteExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADAbstractDirectiveExpressionTreeNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADCommutativeDualChildExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADDirectiveNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADDivisionExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADDualChildExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADFactorialExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADIntegerNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADMinusExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADModuloExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADMultiplicationExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADNegationExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADPlusExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADPowerExpressionNode;
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

	private HRADModel parent;
	private String[] directiveNamesToResolve;
	private boolean clone;

	public HRADResolveDirectiveTreeVisitor(HRADModel parent, String[] directiveNamesToResolve, boolean clone) {
		super();
		this.parent = parent;
		this.directiveNamesToResolve = directiveNamesToResolve;
		this.clone = clone;
	}

	public HRADResolveDirectiveTreeVisitor(HRADModel parent, boolean clone) {
		this(parent, null, clone);
	}

	public HRADResolveDirectiveTreeVisitor(HRADModel parent) {
		this(parent, null, false);
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
		if (directiveNamesToResolve == null) {
			return parent.getDirectiveAsExpressionTree(node.getDirectiveName());
		} else {
			for (int i = 0; i < directiveNamesToResolve.length; i++) {
				if (directiveNamesToResolve[i].contentEquals(node.getDirectiveName())) {
					return parent.getDirectiveAsExpressionTree(node.getDirectiveName());
				}
			}
		}
		return node;
	}

}
