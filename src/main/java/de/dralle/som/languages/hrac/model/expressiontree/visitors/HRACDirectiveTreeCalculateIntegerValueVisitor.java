package de.dralle.som.languages.hrac.model.expressiontree.visitors;

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

//For abstract nodes, nothing needs to happen (hopefully)
public class HRACDirectiveTreeCalculateIntegerValueVisitor
		implements HRACDirectiveExpressionTreeVisitorInterface<Long> {

	@Override
	public Long visit(HRACSingleChildExpressionNode node) {
		return node.getChild().accept(this);
	}

	@Override
	public Long visit(HRACAbsoluteExpressionNode node) {
		HRACAbstractDirectiveExpressionTreeNode c = node.getChild();
		Long val = c.accept(this);
		return Math.abs(val);
	}
	

	@Override
	public Long visit(HRACDirectiveNode node) {
		throw new RuntimeException("Unresolved directive node: " + node.getDirectiveName());
	}

	@Override
	public Long visit(HRACDivisionExpressionNode node) {
		Long numeratorValue = node.getChilds()[0].accept(this);
		Long denominatorValue = node.getChilds()[1].accept(this);
		return numeratorValue / denominatorValue;
	}

	@Override
	public Long visit(HRACFactorialExpressionNode node) {
		return getFac(node.getChild().accept(this));
	}

	private long getFac(long n) {
		if (n == 1) {
			return n;
		}
		return n * getFac(n - 1);
	}

	@Override
	public Long visit(HRACMinusExpressionNode node) {
		Long v1 = node.getChilds()[0].accept(this);
		Long v2 = node.getChilds()[1].accept(this);
		return v1 - v2;
	}

	@Override
	public Long visit(HRACModuloExpressionNode node) {
		Long numeratorValue = node.getChilds()[0].accept(this);
		Long denominatorValue = node.getChilds()[1].accept(this);
		return numeratorValue % denominatorValue;
	}

	@Override
	public Long visit(HRACMultiplicationExpressionNode node) {
		Long v1 = node.getChilds()[0].accept(this);
		Long v2 = node.getChilds()[1].accept(this);
		return v1 - v2;
	}

	@Override
	public Long visit(HRACNegationExpressionNode node) {
		return -node.getChild().accept(this);
	}

	@Override
	public Long visit(HRACPlusExpressionNode node) {
		Long v1 = node.getChilds()[0].accept(this);
		Long v2 = node.getChilds()[1].accept(this);
		return v1 + v2;
	}

	@Override
	public Long visit(HRACPowerExpressionNode node) {
		Long v1 = node.getChilds()[0].accept(this);
		Long v2 = node.getChilds()[1].accept(this);
		return (long) Math.pow(v1, v2);
	}

	@Override
	public Long visit(HRACIntegerNode node) {
		return (long) node.getValue();
	}

}
