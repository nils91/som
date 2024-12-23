package de.dralle.som.languages.hrbs.model.expressiontree;

import de.dralle.som.Util;
import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hras.model.HRASFactorialExpressionNode;

public class HRBSFactorialExpressionNode extends HRBSSingleChildExpressionNode implements Cloneable {

	public HRBSFactorialExpressionNode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HRBSFactorialExpressionNode(HRBSAbstractExpressionNode child) {
		super(child);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRBSFactorialExpressionNode) {
			return super.equals(obj);
		}
		return false;
	}

	@Override
	public HRASFactorialExpressionNode compileToHRAS(HRACModel parent) {
		return new HRASFactorialExpressionNode(getChild().compileToHRAS(parent));
	}

	

	@Override
	public int calculateNumericalValue() {
		// TODO Auto-generated method stub
		return Util.getFac(getChild().calculateNumericalValue());
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getChild().toString()+"!";
	}

}
