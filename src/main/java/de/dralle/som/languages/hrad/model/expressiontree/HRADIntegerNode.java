package de.dralle.som.languages.hrad.model.expressiontree;

import de.dralle.som.languages.hrad.model.HRADModel;
import de.dralle.som.languages.hrad.model.expressiontree.visitors.HRADDirectiveExpressionTreeVisitorInterface;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hras.model.HRASIntegerNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSIntegerNode;

public class HRADIntegerNode extends HRADAbstractDirectiveExpressionTreeNode implements Cloneable {
	private int value;

	public HRADIntegerNode() {
		super();
	}

	public HRADIntegerNode(int value) {
		super();
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	@Override
	public HRADIntegerNode clone() {
		// TODO Auto-generated method stub
		return (HRADIntegerNode) super.clone();
	}

	

	@Override
	public HRBSAbstractExpressionNode compileToHRBS() {
		return new HRBSIntegerNode(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRADIntegerNode) {
			HRADIntegerNode oth = (HRADIntegerNode) obj;
			return value == oth.value;
		}
		if (obj instanceof Integer) {
			Integer oth = (Integer) obj;
			return value == oth.intValue();
		}
		return false;
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
