package de.dralle.som.languages.hrac.model.expressiontree;

import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hras.model.HRASIntegerNode;
import de.dralle.som.languages.hras.model.HRASMultiplicationExpression;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSNegationExpressionNode;

public class HRACNegationExpressionNode extends HRACSingleChildExpressionNode implements Cloneable {

	public HRACNegationExpressionNode(HRACAbstractExpressionNode child) {
		super(child);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int calculateNumericalValue() {
		// TODO Auto-generated method stub
		return super.calculateNumericalValue() * -1;
	}

	@Override
	public HRASAbstractExpressionNode compileToHRAS(HRACModel parent) {
		return new HRASMultiplicationExpression(getChild().compileToHRAS(parent), new HRASIntegerNode(-1));
	}

	@Override
	public HRBSAbstractExpressionNode compileToHRBS() {
		return new HRBSNegationExpressionNode(getChild().compileToHRBS());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRACNegationExpressionNode) {
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
		return "- " + getChild().toString();
	}

}
