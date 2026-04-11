package de.dralle.som.languages.hrad.model.expressiontree.visitors;

import de.dralle.som.languages.hrad.HRADSourceLocation;
import de.dralle.som.languages.hrad.model.directive.HRADAbstractDirectiveValue;
import de.dralle.som.languages.hrad.model.directive.HRADIntegerDirectiveValue;
import de.dralle.som.languages.hrad.model.directive.HRADStringDirectiveValue;
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

//For abstract nodes, nothing needs to happen (hopefully)
public class HRADDirectiveTreeCalculateValueVisitor
		implements HRADDirectiveExpressionTreeVisitorInterface<HRADAbstractDirectiveValue<?>> {

	@Override
	public HRADAbstractDirectiveValue<?> visit(HRADAbsoluteExpressionNode node) {
		// TODO Auto-generated method stub
		return HRADDirectiveExpressionTreeVisitorInterface.super.visit(node);
	}

	@Override
	public HRADAbstractDirectiveValue<?> visit(HRADAbstractDirectiveExpressionTreeNode node) {
		// TODO Auto-generated method stub
		return HRADDirectiveExpressionTreeVisitorInterface.super.visit(node);
	}

	@Override
	public HRADAbstractDirectiveValue<?> visit(HRADDirectiveNode node) {
		// TODO Auto-generated method stub
		return HRADDirectiveExpressionTreeVisitorInterface.super.visit(node);
	}

	@Override
	public HRADAbstractDirectiveValue<?> visit(HRADDivisionExpressionNode node) {
		// TODO Auto-generated method stub
		return HRADDirectiveExpressionTreeVisitorInterface.super.visit(node);
	}

	@Override
	public HRADAbstractDirectiveValue<?> visit(HRADDualChildExpressionNode node) {
		// TODO Auto-generated method stub
		return HRADDirectiveExpressionTreeVisitorInterface.super.visit(node);
	}

	@Override
	public HRADAbstractDirectiveValue<?> visit(HRADFactorialExpressionNode node) {
		// TODO Auto-generated method stub
		return HRADDirectiveExpressionTreeVisitorInterface.super.visit(node);
	}

	@Override
	public HRADAbstractDirectiveValue<?> visit(HRADIntegerNode node) {
		return new HRADIntegerDirectiveValue(node.getValue(), node.getSourceLocation());
	}

	@Override
	public HRADAbstractDirectiveValue<?> visit(HRADStringNode node) {
		return new HRADStringDirectiveValue(node.getValue(), node.getSourceLocation());

	}

	@Override
	public HRADAbstractDirectiveValue<?> visit(HRADMinusExpressionNode node) {
		// TODO Auto-generated method stub
		return HRADDirectiveExpressionTreeVisitorInterface.super.visit(node);
	}

	@Override
	public HRADAbstractDirectiveValue<?> visit(HRADModuloExpressionNode node) {
		// TODO Auto-generated method stub
		return HRADDirectiveExpressionTreeVisitorInterface.super.visit(node);
	}

	@Override
	public HRADAbstractDirectiveValue<?> visit(HRADMultiplicationExpressionNode node) {
		HRADAbstractDirectiveValue<?> v1 = node.getChilds()[0].accept(this);
		HRADAbstractDirectiveValue<?> v2 = node.getChilds()[1].accept(this);
		if (v1 instanceof HRADStringDirectiveValue) {
			if (v2 instanceof HRADStringDirectiveValue) {
				return new HRADStringDirectiveValue(v1.getValue().toString() + v2.getValue().toString(),
						node.getSourceLocation());
			}
			if (v2 instanceof HRADIntegerDirectiveValue) {
				return new HRADStringDirectiveValue(v1.getValue().toString() + v2.getValue().toString(),
						node.getSourceLocation());
			}
		}
		if (v1 instanceof HRADIntegerDirectiveValue) {
			HRADIntegerDirectiveValue v1Int = (HRADIntegerDirectiveValue) v1;
			if (v2 instanceof HRADStringDirectiveValue) {
				return new HRADIntegerDirectiveValue(v1Int.getValue() * Integer.parseInt(v2.getValue().toString()),
						node.getSourceLocation());
			}
			if (v2 instanceof HRADIntegerDirectiveValue) {
				HRADIntegerDirectiveValue v2Int = (HRADIntegerDirectiveValue) v2;
				return new HRADIntegerDirectiveValue(v1Int.getValue() * v2Int.getValue(), node.getSourceLocation());
			}
		}
		throw new RuntimeException("Could not determine return type " + node.getSourceLocation());
	}

	@Override
	public HRADAbstractDirectiveValue<?> visit(HRADNegationExpressionNode node) {
		// TODO Auto-generated method stub
		return HRADDirectiveExpressionTreeVisitorInterface.super.visit(node);
	}

	@Override
	public HRADAbstractDirectiveValue<?> visit(HRADPlusExpressionNode node) {
		HRADAbstractDirectiveValue<?> v1 = node.getChilds()[0].accept(this);
		HRADAbstractDirectiveValue<?> v2 = node.getChilds()[1].accept(this);
		if (v1 instanceof HRADStringDirectiveValue) {
			if (v2 instanceof HRADStringDirectiveValue) {
				return new HRADStringDirectiveValue(v1.getValue().toString() + v2.getValue().toString(),
						node.getSourceLocation());
			}
			if (v2 instanceof HRADIntegerDirectiveValue) {
				return new HRADStringDirectiveValue(v1.getValue().toString() + v2.getValue().toString(),
						node.getSourceLocation());
			}
		}
		if (v1 instanceof HRADIntegerDirectiveValue) {
			HRADIntegerDirectiveValue v1Int = (HRADIntegerDirectiveValue) v1;
			if (v2 instanceof HRADStringDirectiveValue) {
				return new HRADIntegerDirectiveValue(v1Int.getValue() + Integer.parseInt(v2.getValue().toString()),
						node.getSourceLocation());
			}
			if (v2 instanceof HRADIntegerDirectiveValue) {
				HRADIntegerDirectiveValue v2Int = (HRADIntegerDirectiveValue) v2;
				return new HRADIntegerDirectiveValue(v1Int.getValue() + v2Int.getValue(), node.getSourceLocation());
			}
		}
		throw new RuntimeException("Could not determine return type " + node.getSourceLocation());
	}

	@Override
	public HRADAbstractDirectiveValue<?> visit(HRADPowerExpressionNode node) {
		// TODO Auto-generated method stub
		return HRADDirectiveExpressionTreeVisitorInterface.super.visit(node);
	}

	@Override
	public HRADAbstractDirectiveValue<?> visit(HRADSingleChildExpressionNode node) {
		// TODO Auto-generated method stub
		return HRADDirectiveExpressionTreeVisitorInterface.super.visit(node);
	}

}
