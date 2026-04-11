package de.dralle.som.languages.hrad.model.expressiontree;

import de.dralle.som.languages.hrac.model.expressiontree.HRACAbstractDirectiveExpressionTreeNode;
import de.dralle.som.languages.hrac.model.expressiontree.HRACIntegerNode;

public class HRBSIntegerNode extends HRBSAbstractExpressionNode implements Cloneable {
	private int value;

	public HRBSIntegerNode() {
		super();
	}

	public HRBSIntegerNode(int value) {
		super();
		this.value = value;
	}

	@Override
	public HRBSIntegerNode clone() {
		// TODO Auto-generated method stub
		return (HRBSIntegerNode) super.clone();
	}

	@Override
	public HRACAbstractDirectiveExpressionTreeNode compileToHRAC() {
		return new HRACIntegerNode(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRBSIntegerNode) {
			HRBSIntegerNode oth = (HRBSIntegerNode) obj;
			return value == oth.value;
		}
		if (obj instanceof Integer) {
			Integer oth = (Integer) obj;
			return value == oth.intValue();
		}
		return false;
	}

	public int getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value + "";
	}
}
