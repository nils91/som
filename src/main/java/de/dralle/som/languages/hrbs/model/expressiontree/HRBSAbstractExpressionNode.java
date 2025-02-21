package de.dralle.som.languages.hrbs.model.expressiontree;

import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractExpressionNode;

public abstract class HRBSAbstractExpressionNode implements Cloneable {
	public HRBSAbstractExpressionNode() {

	}

	@Override
	public HRBSAbstractExpressionNode clone() {
		// TODO Auto-generated method stub
		try {
			return (HRBSAbstractExpressionNode) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public abstract HRACAbstractExpressionNode compileToHRAC();

}
