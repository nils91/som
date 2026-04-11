package de.dralle.som.languages.hrad.model.expressiontree.visitors;

import de.dralle.som.languages.hrad.model.expressiontree.HRADAbsoluteExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADAbstractDirectiveExpressionTreeNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADDirectiveNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADDivisionExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADFactorialExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADIntegerNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADMinusExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADModuloExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADMultiplicationExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADNegationExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADPlusExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADPowerExpressionNode;
import de.dralle.som.languages.hrad.model.expressiontree.HRADSingleChildExpressionNode;

//For abstract nodes, nothing needs to happen (hopefully)
public class HRADDirectiveTreeCalculateIntegerValueVisitor
		implements HRADDirectiveExpressionTreeVisitorInterface<Long> {

	@Override
	public Long visit(HRADSingleChildExpressionNode node) {
		return node.getChild().accept(this);
	}

	@Override
	public Long visit(HRADAbsoluteExpressionNode node) {
		HRADAbstractDirectiveExpressionTreeNode c = node.getChild();
		Long val = c.accept(this);
		return Math.abs(val);
	}
	

	@Override
	public Long visit(HRADDirectiveNode node) {
		String msg = "Unresolved directive node: " + node.getDirectiveName();
		if(node.getSourceLocation()!=null) {
			msg += " at "+node.getSourceLocation();
		}
		throw new RuntimeException(msg);
	}

	@Override
	public Long visit(HRADDivisionExpressionNode node) {
		Long numeratorValue = node.getChilds()[0].accept(this);
		Long denominatorValue = node.getChilds()[1].accept(this);
		return numeratorValue / denominatorValue;
	}

	@Override
	public Long visit(HRADFactorialExpressionNode node) {
		return getFac(node.getChild().accept(this));
	}

	private long getFac(long n) {
		if (n == 1) {
			return n;
		}
		return n * getFac(n - 1);
	}

	@Override
	public Long visit(HRADMinusExpressionNode node) {
		Long v1 = node.getChilds()[0].accept(this);
		Long v2 = node.getChilds()[1].accept(this);
		return v1 - v2;
	}

	@Override
	public Long visit(HRADModuloExpressionNode node) {
		Long numeratorValue = node.getChilds()[0].accept(this);
		Long denominatorValue = node.getChilds()[1].accept(this);
		return numeratorValue % denominatorValue;
	}

	@Override
	public Long visit(HRADMultiplicationExpressionNode node) {
		Long v1 = node.getChilds()[0].accept(this);
		Long v2 = node.getChilds()[1].accept(this);
		return v1 * v2;
	}

	@Override
	public Long visit(HRADNegationExpressionNode node) {
		return -node.getChild().accept(this);
	}

	@Override
	public Long visit(HRADPlusExpressionNode node) {
		Long v1 = node.getChilds()[0].accept(this);
		Long v2 = node.getChilds()[1].accept(this);
		return v1 + v2;
	}

	@Override
	public Long visit(HRADPowerExpressionNode node) {
		Long v1 = node.getChilds()[0].accept(this);
		Long v2 = node.getChilds()[1].accept(this);
		return (long) Math.pow(v1, v2);
	}

	@Override
	public Long visit(HRADIntegerNode node) {
		return (long) node.getValue();
	}

}
