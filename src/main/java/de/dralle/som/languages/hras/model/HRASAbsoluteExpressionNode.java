package de.dralle.som.languages.hras.model;

import de.dralle.som.languages.hrac.model.expressiontree.HRACAbsoluteExpressionNode;

public class HRASAbsoluteExpressionNode extends HRASSingleChildExpressionNode implements Cloneable {

	public HRASAbsoluteExpressionNode(HRASAbstractExpressionNode child) {
		super(child);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int calculateNumericalValue() {
		return Math.abs(getChild().calculateNumericalValue());
	}

	@Override
	public HRACAbsoluteExpressionNode compileToHRAC() {
		return new HRACAbsoluteExpressionNode(getChild().compileToHRAC());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRASAbsoluteExpressionNode) {
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
		return "| " + getChild().toString() + " |";
	}

}
