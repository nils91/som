package de.dralle.som.languages.hrac.model.expressiontree;

import de.dralle.som.languages.hrac.model.HRACModel;
import de.dralle.som.languages.hrac.model.expressiontree.visitors.HRACDirectiveExpressionTreeVisitorInterface;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hras.model.HRASIntegerNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSIntegerNode;

public class HRACIntegerNode extends HRACAbstractDirectiveExpressionTreeNode implements Cloneable {
	private int value;

	public HRACIntegerNode() {
		super();
	}

	public HRACIntegerNode(int value) {
		super();
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	@Override
	public HRACIntegerNode clone() {
		// TODO Auto-generated method stub
		return (HRACIntegerNode) super.clone();
	}

	@Override
	public HRASAbstractExpressionNode compileToHRAS(HRACModel parent) {
		return new HRASIntegerNode(value);
	}

	@Override
	public HRBSAbstractExpressionNode compileToHRBS() {
		return new HRBSIntegerNode(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRACIntegerNode) {
			HRACIntegerNode oth = (HRACIntegerNode) obj;
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
