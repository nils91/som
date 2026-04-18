package de.dralle.som.languages.hrad.model.expressiontree;

import de.dralle.som.languages.hrad.model.HRADModel;
import de.dralle.som.languages.hrad.model.expressiontree.visitors.HRADDirectiveExpressionTreeVisitorInterface;
import de.dralle.som.languages.hras.model.HRASAbstractExpressionNode;
import de.dralle.som.languages.hras.model.HRASIntegerNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSAbstractExpressionNode;
import de.dralle.som.languages.hrbs.model.expressiontree.HRBSIntegerNode;

public class HRADStringNode extends HRADAbstractDirectiveExpressionTreeNode implements Cloneable {
	private String value;

	public HRADStringNode() {
		super();
	}

	public HRADStringNode(String value) {
		super();
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public HRADStringNode clone() {
		// TODO Auto-generated method stub
		return (HRADStringNode) super.clone();
	}

	


	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HRADStringNode) {
			HRADStringNode oth = (HRADStringNode) obj;
			return value == oth.value;
		}
		if (obj instanceof String) {
			String oth = (String) obj;
			return value == oth;
		}
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return value.hashCode();
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
