package de.dralle.som.languages.hrad.model.expressiontree;

import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractDirectiveExpressionTreeNode;

public abstract class HRADAbstractExpressionNode implements Cloneable {
	public HRADAbstractExpressionNode() {

	}

	@Override
	public HRADAbstractExpressionNode clone() {
		// TODO Auto-generated method stub
		try {
			return (HRADAbstractExpressionNode) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public abstract HRACAbstractDirectiveExpressionTreeNode compileToHRAC();

}
