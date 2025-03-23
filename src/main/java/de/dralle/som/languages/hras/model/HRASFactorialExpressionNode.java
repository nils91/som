package de.dralle.som.languages.hras.model;

import de.dralle.som.languages.hrac.model.expressiontree.HRACFactorialExpressionNode;

public class HRASFactorialExpressionNode extends HRASSingleChildExpressionNode implements Cloneable {

	public HRASFactorialExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HRASFactorialExpressionNode(HRASAbstractExpressionNode child) {
		super(child);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int calculateNumericalValue() {
		// TODO Auto-generated method stub
		return getFac(getChild().calculateNumericalValue());
	}

	@Override
	public HRACFactorialExpressionNode compileToHRAC() {
		return new HRACFactorialExpressionNode(getChild().compileToHRAC());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRASFactorialExpressionNode) {
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
