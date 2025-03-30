package de.dralle.som.languages.hras.model;

import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractExpressionNode;

public abstract class HRASAbstractExpressionNode implements Cloneable {
	public HRASAbstractExpressionNode() {

	}

	public abstract int calculateNumericalValue();

	@Override
	public HRASAbstractExpressionNode clone() {
		// TODO Auto-generated method stub
		try {
			return (HRASAbstractExpressionNode) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public abstract HRACAbstractExpressionNode compileToHRAC();

}
