package de.dralle.som.languages.hrac.model.expressiontree;

import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hras.model.HRASFactorialExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSFactorialExpressionNode;

public class HRACFactorialExpressionNode extends HRACSingleChildExpressionNode implements Cloneable {

	public HRACFactorialExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HRACFactorialExpressionNode(HRACAbstractExpressionNode child) {
		super(child);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int calculateNumericalValue() {
		// TODO Auto-generated method stub
		return getFac(getChild().calculateNumericalValue());
	}

	@Override
	public HRASFactorialExpressionNode compileToHRAS(HRACModel parent) {
		return new HRASFactorialExpressionNode(getChild().compileToHRAS(parent));
	}

	@Override
	public HRBSAbstractExpressionNode compileToHRBS() {
		return new HRBSFactorialExpressionNode(getChild().compileToHRBS());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRACFactorialExpressionNode) {
			return super.equals(obj);
		}
		return false;
	}

	private int getFac(int n) {
		if (n == 1) {
			return n;
		}
		return n * getFac(n - 1);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getChild().toString() + "!";
	}

}
