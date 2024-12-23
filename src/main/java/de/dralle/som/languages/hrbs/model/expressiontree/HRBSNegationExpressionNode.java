package de.dralle.som.languages.hrbs.model.expressiontree;

import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hras.model.HRASIntegerNode;
import de.dralle.som.languages.hras.model.HRASSingleChildExpressionNode;
import de.dralle.som.languages.hras.model.HRASMultiplicationExpression;

public class HRBSNegationExpressionNode extends HRBSSingleChildExpressionNode implements Cloneable {

	public HRBSNegationExpressionNode(HRBSAbstractExpressionNode child) {
		super(child);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRBSNegationExpressionNode) {
			return super.equals(obj);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "- "+getChild().toString();
	}

	@Override
	public int calculateNumericalValue() {
		// TODO Auto-generated method stub
		return super.calculateNumericalValue()*-1;
	}

	@Override
	public HRASAbstractExpressionNode compileToHRAC(HRACModel parent) {
		return new HRASMultiplicationExpression(getChild().compileToHRAC(parent), new HRASIntegerNode(-1));
	}

}
