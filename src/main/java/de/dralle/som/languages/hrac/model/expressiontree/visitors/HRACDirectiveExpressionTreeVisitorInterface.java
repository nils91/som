package de.dralle.som.languages.hrac.model.expressiontree.visitors;

import de.dralle.som.languages.hrac.model.expressiontree.HRACCommutativeDualChildExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACAbsoluteExpressionNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractDirectiveExpressionTreeNode;
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

//Partially generated with GenerateHRACDirectiveTreeVisitorInterface.java
public interface HRACDirectiveExpressionTreeVisitorInterface<T> {
	default T visit(HRACAbsoluteExpressionNode node) {
		return visit((HRACSingleChildExpressionNode) node);
	}

	default T visitSwitch(HRACAbstractDirectiveExpressionTreeNode node) {
		if (node.preAccept(this)) {
			node = preVisit(this, node);
			T returnyValue = switch (node) {
			case HRACDirectiveNode n -> visit(n);
			case HRACAbsoluteExpressionNode n -> visit(n);
			case HRACDivisionExpressionNode n -> visit(n);
			case HRACFactorialExpressionNode n -> visit(n);
			case HRACIntegerNode n -> visit(n);
			case HRACMinusExpressionNode n -> visit(n);
			case HRACModuloExpressionNode n -> visit(n);
			case HRACMultiplicationExpressionNode n -> visit(n);
			case HRACNegationExpressionNode n -> visit(n);
			case HRACPlusExpressionNode n -> visit(n);
			case HRACPowerExpressionNode n -> visit(n);
			case HRACSingleChildExpressionNode n -> visit(n);
			case null -> null;
			default -> node.accept(this); // Fallback: If
											// for whatever
											// reason the
											// node type
											// isnt known
											// here, the default .process is called, which in turn calls the node´s .accept
			};
			returnyValue = postVisit(this, node, returnyValue);
			returnyValue = node.postAccept(this, returnyValue);
			return returnyValue;
		}
		return null;
	}

	default T postVisit(HRACDirectiveExpressionTreeVisitorInterface<T> hracDirectiveExpressionTreeVisitorInterface,
			HRACAbstractDirectiveExpressionTreeNode node, T returnyValue) {
		return returnyValue;
	}

	default HRACAbstractDirectiveExpressionTreeNode preVisit(
			HRACDirectiveExpressionTreeVisitorInterface<T> hracDirectiveExpressionTreeVisitorInterface,
			HRACAbstractDirectiveExpressionTreeNode node) {
		return node;
	}
	
	default T visit(HRACAbstractDirectiveExpressionTreeNode node) {
		return null;
	}


	default T visit(HRACCommutativeDualChildExpressionNode node) {
		return visit((HRACDualChildExpressionNode) node);
	}

	default T visit(HRACDirectiveNode node) {
		return visit((HRACAbstractDirectiveExpressionTreeNode) node);
	}

	default T visit(HRACDivisionExpressionNode node) {
		return visit((HRACDualChildExpressionNode) node);
	}

	default T visit(HRACDualChildExpressionNode node) {
		return visit((HRACAbstractDirectiveExpressionTreeNode) node);
	}

	default T visit(HRACFactorialExpressionNode node) {
		return visit((HRACSingleChildExpressionNode) node);
	}

	default T visit(HRACIntegerNode node) {
		return visit((HRACAbstractDirectiveExpressionTreeNode) node);
	}

	default T visit(HRACMinusExpressionNode node) {
		return visit((HRACDualChildExpressionNode) node);
	}

	default T visit(HRACModuloExpressionNode node) {
		return visit((HRACDualChildExpressionNode) node);
	}

	default T visit(HRACMultiplicationExpressionNode node) {
		return visit((HRACDualChildExpressionNode) node);
	}

	default T visit(HRACNegationExpressionNode node) {
		return visit((HRACSingleChildExpressionNode) node);
	}

	default T visit(HRACPlusExpressionNode node) {
		return visit((HRACCommutativeDualChildExpressionNode) node);
	}

	default T visit(HRACPowerExpressionNode node) {
		return visit((HRACDualChildExpressionNode) node);
	}

	default T visit(HRACSingleChildExpressionNode node) {
		return visit((HRACAbstractDirectiveExpressionTreeNode) node);
	}

}
