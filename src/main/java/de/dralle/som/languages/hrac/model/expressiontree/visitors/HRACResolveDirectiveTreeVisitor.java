package de.dralle.som.languages.hrac.model.expressiontree.visitors;

import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hrac.model.expressiontree.HRACAbsoluteExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractDirectiveExpressionTreeNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACCommutativeDualChildExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACDirectiveNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACDivisionExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACDualChildExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACFactorialExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACIntegerNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACMinusExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACModuloExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACMultiplicationExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACNegationExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACPlusExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACPowerExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACSingleChildExpressionNode;

//For abstract nodes, nothing needs to happen
//This visitor does not avoid modifying the tree it visits by default. Either clone the tree first or use one of the constructors with the clone param
public class HRACResolveDirectiveTreeVisitor
		implements HRACDirectiveExpressionTreeVisitorInterface<HRACAbstractDirectiveExpressionTreeNode> {

	@Override
	public HRACAbstractDirectiveExpressionTreeNode postVisit(
			HRACDirectiveExpressionTreeVisitorInterface<HRACAbstractDirectiveExpressionTreeNode> hracDirectiveExpressionTreeVisitorInterface,
			HRACAbstractDirectiveExpressionTreeNode node, HRACAbstractDirectiveExpressionTreeNode returnyValue) {
		return returnyValue!=null?returnyValue:node;
	}

	@Override
	public HRACAbstractDirectiveExpressionTreeNode preVisit(
			HRACDirectiveExpressionTreeVisitorInterface<HRACAbstractDirectiveExpressionTreeNode> hracDirectiveExpressionTreeVisitorInterface,
			HRACAbstractDirectiveExpressionTreeNode node) {
		return clone?node.clone():node;
	}

	private HRACModel parent;
	private String[] directiveNamesToResolve;
	private boolean clone;

	public HRACResolveDirectiveTreeVisitor(HRACModel parent, String[] directiveNamesToResolve, boolean clone) {
		super();
		this.parent = parent;
		this.directiveNamesToResolve = directiveNamesToResolve;
		this.clone = clone;
	}

	public HRACResolveDirectiveTreeVisitor(HRACModel parent, boolean clone) {
		this(parent, null, clone);
	}

	public HRACResolveDirectiveTreeVisitor(HRACModel parent) {
		this(parent, null, false);
	}

	@Override
	public HRACAbstractDirectiveExpressionTreeNode visit(HRACSingleChildExpressionNode node) {
		if (clone) {
			node = node.clone();
		}
		node.setChild(node.getChild().accept(this));
		return node;
	}

	@Override
	public HRACAbstractDirectiveExpressionTreeNode visit(HRACDualChildExpressionNode node) {
		if (clone) {
			node = node.clone();
		}
		HRACAbstractDirectiveExpressionTreeNode[] nc = node.getChilds();
		for (int i = 0; i < nc.length; i++) {
			nc[i] = nc[i].accept(this);
		}
		return node;
	}

	@Override
	public HRACAbstractDirectiveExpressionTreeNode visit(HRACDirectiveNode node) {
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
