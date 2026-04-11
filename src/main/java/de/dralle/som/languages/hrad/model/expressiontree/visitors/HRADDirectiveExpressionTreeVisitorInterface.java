package de.dralle.som.languages.hrad.model.expressiontree.visitors;

import de.dralle.som.languages.hrad.model.expressiontree.HRADAbsoluteExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADAbstractDirectiveExpressionTreeNode;
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
import de.dralle.som.languages.hrad.model.expressiontree.HRADStringNode;

//Partially generated with GenerateHRADDirectiveTreeVisitorInterface.java
public interface HRADDirectiveExpressionTreeVisitorInterface<T> {
	default T visit(HRADAbsoluteExpressionNode node) {
		return visit((HRADSingleChildExpressionNode) node);
	}

	default T visitSwitch(HRADAbstractDirectiveExpressionTreeNode node) {
		if (node.preAccept(this)) {
			node = preVisit(this, node);
			T returnyValue = switch (node) {
			case HRADDirectiveNode n -> visit(n);
			case HRADAbsoluteExpressionNode n -> visit(n);
			case HRADDivisionExpressionNode n -> visit(n);
			case HRADFactorialExpressionNode n -> visit(n);
			case HRADIntegerNode n -> visit(n);
			case HRADStringNode n -> visit(n);
			case HRADMinusExpressionNode n -> visit(n);
			case HRADModuloExpressionNode n -> visit(n);
			case HRADMultiplicationExpressionNode n -> visit(n);
			case HRADNegationExpressionNode n -> visit(n);
			case HRADPlusExpressionNode n -> visit(n);
			case HRADPowerExpressionNode n -> visit(n);
			case HRADSingleChildExpressionNode n -> visit(n);
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

	default T postVisit(HRADDirectiveExpressionTreeVisitorInterface<T> hradDirectiveExpressionTreeVisitorInterface,
			HRADAbstractDirectiveExpressionTreeNode node, T returnyValue) {
		return returnyValue;
	}

	default HRADAbstractDirectiveExpressionTreeNode preVisit(
			HRADDirectiveExpressionTreeVisitorInterface<T> hradDirectiveExpressionTreeVisitorInterface,
			HRADAbstractDirectiveExpressionTreeNode node) {
		return node;
	}
	
	default T visit(HRADAbstractDirectiveExpressionTreeNode node) {
		return null;
	}

	default T visit(HRADDirectiveNode node) {
		return visit((HRADAbstractDirectiveExpressionTreeNode) node);
	}

	default T visit(HRADDivisionExpressionNode node) {
		return visit((HRADDualChildExpressionNode) node);
	}

	default T visit(HRADDualChildExpressionNode node) {
		return visit((HRADAbstractDirectiveExpressionTreeNode) node);
	}

	default T visit(HRADFactorialExpressionNode node) {
		return visit((HRADSingleChildExpressionNode) node);
	}

	default T visit(HRADIntegerNode node) {
		return visit((HRADAbstractDirectiveExpressionTreeNode) node);
	}
	default T visit(HRADStringNode node) {
		return visit((HRADAbstractDirectiveExpressionTreeNode) node);
	}

	default T visit(HRADMinusExpressionNode node) {
		return visit((HRADDualChildExpressionNode) node);
	}

	default T visit(HRADModuloExpressionNode node) {
		return visit((HRADDualChildExpressionNode) node);
	}

	default T visit(HRADMultiplicationExpressionNode node) {
		return visit((HRADDualChildExpressionNode) node);
	}

	default T visit(HRADNegationExpressionNode node) {
		return visit((HRADSingleChildExpressionNode) node);
	}

	default T visit(HRADPlusExpressionNode node) {
		return visit((HRADDualChildExpressionNode) node);
	}

	default T visit(HRADPowerExpressionNode node) {
		return visit((HRADDualChildExpressionNode) node);
	}

	default T visit(HRADSingleChildExpressionNode node) {
		return visit((HRADAbstractDirectiveExpressionTreeNode) node);
	}

}
